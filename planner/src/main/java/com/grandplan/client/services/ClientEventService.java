package com.grandplan.client.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.grandplan.client.util.EventStatus;
import com.grandplan.client.util.NewEvent;
import com.grandplan.util.Event;
import com.grandplan.util.User;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@Component
public class ClientEventService {
    @Autowired
    private HttpRequestService httpRequestService;

    @Autowired 
    private ClientLoginService clientLoginService;

    @Autowired
    private ClientInviteService clientInviteService;

    private static final String EVENTS ="events";
    private CloseableHttpResponse response;

    public String getUserEvents(User user, Model model){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("email", user.getEmail());
        JSONObject jsonObject = new JSONObject(hashMap);

        try{
            response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/getUserEvents");
            String responseBody = EntityUtils.toString(response.getEntity());
            if(responseBody.equals("[]")){
                model.addAttribute("noEvents", "You currently have no events");
                return addModelAttributes(model);
            }
            
            model.addAttribute(EVENTS, generateResponse(responseBody));
            return addModelAttributes(model);
        }
        catch(Exception exception){
            model.addAttribute("noEvents", "Your events could not be loaded");
            showModal(model, "Something went wrong when getting your events. Please try again later.", "");
            return addModelAttributes(model);
        }
    }

    private List<Event> generateResponse(String responseBody){
        List<Event> events = new ArrayList<>();
        try{
            JSONParser parser = new JSONParser();
            Object object = parser.parse(responseBody);
            JSONArray jsonArray = (JSONArray) object;
            jsonArray.forEach(item -> {
                JSONObject obj = (JSONObject) item;
                events.add(generateEventObject(obj));
            });
            return events;
        }
        catch(Exception exception){
            return events;
        }
    }

    private Event generateEventObject(JSONObject jsonObject){
        return Event.builder()
        .id(Long.parseLong(jsonObject.get("id").toString()))
        .title(jsonObject.get("title").toString())
        .description(jsonObject.get("description").toString())
        .start(jsonObject.get("start").toString())
        .end(jsonObject.get("end").toString())
        .tag(jsonObject.get("tag").toString())
        .color(jsonObject.get("color").toString())
        .hostUsername(jsonObject.get("hostUsername").toString())
        .allDay(Boolean.parseBoolean(jsonObject.get("allDay").toString()))
        .build();
    }

    private String addModelAttributes(Model model){
        model.addAttribute("user", clientLoginService.getCurrentUser());
        model.addAttribute("delete", new EventStatus());
        model.addAttribute("transfer", new EventStatus());
        return EVENTS;
    }

    private HashMap<String, String> getEventById(String eventId){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("eventId", eventId);
        JSONObject jsonObject = new JSONObject(hashMap);

        try{
            response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/getEventById");
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(responseBody);
            hashMap.clear();
            hashMap.put("id", obj.get("id").toString());
            hashMap.put("title", obj.get("title").toString());
            hashMap.put("description", obj.get("description").toString());
            hashMap.put("start", obj.get("start").toString());
            hashMap.put("end", obj.get("end").toString());
            hashMap.put("tag", obj.get("tag").toString());
            hashMap.put("color", obj.get("color").toString());
            hashMap.put("hostUsername", obj.get("hostUsername").toString());
            hashMap.put("allDay", obj.get("allDay").toString());
            return hashMap;
        }
        catch(Exception exception){
            return null;
        }
    }

    public String createEvent(NewEvent newEvent, Model model){
        try{
            response = httpRequestService.sendHttpPost(generateEventHashMapObject(newEvent), "http://localhost:8080/api/createEvent");
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 200){
                if(createEventInvites(response, newEvent)){
                    showModal(model, "Successfully created event!", "");
                }
                else{
                    showModal(model, "There was an error creating your event. Please try again later.", "");
                }
                return getUserEvents(clientLoginService.getCurrentUser(), model);
            }

            showModal(model, "Something went wrong creating your event. Please try again later.", "");
            return getUserEvents(clientLoginService.getCurrentUser(), model);
        }
        catch(Exception exception){
            showModal(model, "Something went wrong creating your event. Please try again later.", "");
            return getUserEvents(clientLoginService.getCurrentUser(), model);
        }
    }

    private JSONObject generateEventHashMapObject(NewEvent newEvent){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("title", newEvent.getTitle());
        hashMap.put("description", newEvent.getDescription());
        hashMap.put("start", newEvent.getStart());
        hashMap.put("end", newEvent.getEnd());
        hashMap.put("allDay", newEvent.getAllDay().toString());
        hashMap.put("color", newEvent.getColor());
        hashMap.put("hostUsername", clientLoginService.getCurrentUser().getEmail());
        hashMap.put("tag", newEvent.getTag());
        return new JSONObject(hashMap);
    }

    private Boolean createEventInvites(CloseableHttpResponse response, NewEvent newEvent) throws ParseException, IOException{
        String responseBody = EntityUtils.toString(response.getEntity());
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(responseBody);
        Long eventId = (Long) json.get("id");

        String[] members = newEvent.getMembers().split(",");
        for(int i = 1; i < members.length; i++){
            if(!clientInviteService.createInvites(members[i], eventId)){
                return false;
            }
        }

        return true;
    }

    public String deleteEvent(EventStatus eventStatus, Model model){
        try{
            if(eventStatus.getHostUsername().equals(clientLoginService.getCurrentUser().getEmail())){
                response = httpRequestService.sendHttpPost(generateDeleteEventObject(eventStatus), "http://localhost:8080/api/deleteEvent");
            }
            else{
                response = httpRequestService.sendHttpPost(generateDeleteEventObject(eventStatus), "http://localhost:8080/api/deleteInvite"); //TODO: remove invite from user
            }
            
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200){
                showModal(model, "Successfully deleted event", "");
            }             
            else{
                showModal(model, "Could not delete event. Please try again later.", "");
            }                

            return getUserEvents(clientLoginService.getCurrentUser(), model);
        }
        catch(IOException exception){
            showModal(model, "Something went wrong deleting this event. Please try again later.", "");
            return getUserEvents(clientLoginService.getCurrentUser(), model);
        }
    }

    private JSONObject generateDeleteEventObject(EventStatus eventStatus){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("id", eventStatus.getEventId());
        hashMap.put("userEmail", eventStatus.getHostUsername());
        return new JSONObject(hashMap);
    }

    public String transferEvent(EventStatus eventStatus, Model model){
        HashMap<String,String> hashMap = getEventById(eventStatus.getEventId());
        hashMap.put("hostUsername", eventStatus.getHostUsername());
        JSONObject jsonObject = new JSONObject(hashMap);

        try{
            response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/updateEvent");
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200){
                showModal(model, "Successfully transferred event", "");
                //TODO: delete invite from user;
            }             
            else{
                showModal(model, "Could not transfer event. Please try again later.", "");
            }                

            return getUserEvents(clientLoginService.getCurrentUser(), model);
        }
        catch(IOException exception){
            showModal(model, "Something went wrong transferring this event. Please try again later.", "");
            return getUserEvents(clientLoginService.getCurrentUser(), model);
        }
    }

    public void showModal(Model model, String message, String button) {
        model.addAttribute("messageModal", message);
        if(!button.equals("")){
            model.addAttribute("button", button);
        }
    }

}