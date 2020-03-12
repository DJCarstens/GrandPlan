package com.grandplan.client.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.grandplan.util.Constants;
import com.grandplan.util.NewEvent;
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

    private CloseableHttpResponse response;

    public String getUserEvents(User user, Model model){
        JSONObject jsonObject = generateJsonObject(
            new ArrayList<String>(){{add(Constants.EMAIL);}},
            new ArrayList<String>(){{add(user.getEmail());}}
        );

        try{
            response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/getUserEvents");
            String responseBody = EntityUtils.toString(response.getEntity());
            if(responseBody.equals("[]")){
                model.addAttribute("noEvents", "You currently have no events");
                return addModelAttributes(model);
            }
            
            model.addAttribute("delete", new Event());
            model.addAttribute("transfer", new Event());
            model.addAttribute(Constants.EVENTS, generateResponse(responseBody));
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

    public Event generateEventObject(JSONObject jsonObject){
        return Event.builder()
        .id(Long.parseLong(jsonObject.get(Constants.ID).toString()))
        .title(jsonObject.get(Constants.TITLE).toString())
        .description(jsonObject.get(Constants.DESCRIPTION).toString())
        .start(jsonObject.get(Constants.START).toString())
        .end(jsonObject.get(Constants.END).toString())
        .tag(jsonObject.get(Constants.TAG).toString())
        .color(jsonObject.get(Constants.COLOR).toString())
        .hostUsername(jsonObject.get(Constants.HOSTUSERNAME).toString())
        .allDay(Boolean.parseBoolean(jsonObject.get(Constants.ALLDAY).toString()))
        .build();
    }

    private String addModelAttributes(Model model){
        model.addAttribute("user", clientLoginService.getCurrentUser());
        model.addAttribute("delete", new Event());
        model.addAttribute("transfer", new Event());
        return Constants.EVENTS;
    }

    private HashMap<String, String> getEventHashMap(String eventId){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("eventId", eventId);
        JSONObject jsonObject = new JSONObject(hashMap);

        try{
            response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/getEventById");
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(responseBody);
            hashMap.clear();
            hashMap.put(Constants.ID, obj.get(Constants.ID).toString());
            hashMap.put(Constants.TITLE, obj.get(Constants.TITLE).toString());
            hashMap.put(Constants.DESCRIPTION, obj.get(Constants.DESCRIPTION).toString());
            hashMap.put(Constants.START, obj.get(Constants.START).toString());
            hashMap.put(Constants.END, obj.get(Constants.END).toString());
            hashMap.put(Constants.TAG, obj.get(Constants.TAG).toString());
            hashMap.put(Constants.COLOR, obj.get(Constants.COLOR).toString());
            hashMap.put(Constants.HOSTUSERNAME, obj.get(Constants.HOSTUSERNAME).toString());
            hashMap.put(Constants.ALLDAY, obj.get(Constants.ALLDAY).toString());
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
        return generateJsonObject(
            new ArrayList<String>(){{
                add(Constants.TITLE);
                add(Constants.DESCRIPTION);
                add(Constants.START);
                add(Constants.END);
                add(Constants.ALLDAY);
                add(Constants.COLOR);
                add(Constants.HOSTUSERNAME);
                add(Constants.TAG);
            }},
            new ArrayList<String>(){{
                add(newEvent.getTitle());
                add(newEvent.getDescription());
                add(newEvent.getStart());
                add(newEvent.getEnd());
                add(newEvent.getAllDay().toString());
                add(newEvent.getColor());
                add(clientLoginService.getCurrentUser().getEmail());
                add(newEvent.getTag());
            }}
        );
    }

    private Boolean createEventInvites(CloseableHttpResponse response, NewEvent newEvent) throws ParseException, IOException{
        String responseBody = EntityUtils.toString(response.getEntity());
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(responseBody);
        Long eventId = (Long) json.get(Constants.ID);

        String[] members = newEvent.getMembers().split(",");
        for(int i = 1; i < members.length; i++){
            if(!clientInviteService.createInvites(members[i], eventId)){
                return false;
            }
        }
        return true;
    }

    public String deleteEvent(Event event, Model model){
        try{
            if(event.getHostUsername().equals(clientLoginService.getCurrentUser().getEmail())){
                response = httpRequestService.sendHttpPost(generateDeleteEventObject(event), "http://localhost:8080/api/deleteEvent");
            }
            else{
                response = httpRequestService.sendHttpPost(generateDeleteEventObject(event), "http://localhost:8080/api/deleteInvite");
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

    private JSONObject generateDeleteEventObject(Event event){
        return generateJsonObject(
            new ArrayList<String>(){{add(Constants.ID); add("userEmail");}},
            new ArrayList<String>(){{add(event.getId().toString()); add(event.getHostUsername());}}
        );
    }

    public String transferEvent(Event event, Model model){
        HashMap<String, String> hashMap = getEventHashMap(event.getId().toString());
        hashMap.put(Constants.HOSTUSERNAME, event.getHostUsername());
        JSONObject jsonObject = new JSONObject(hashMap);

        try{
            response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/updateEvent");
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200){
                showModal(model, "Successfully transferred event", "");
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

    private JSONObject generateJsonObject(List<String> keys, List<String> values){
        HashMap<String,String> hashMap = new HashMap<>();
        for(int i = 0; i < keys.size(); i++){
            hashMap.put(keys.get(i), values.get(i));
        }
        return new JSONObject(hashMap);
    }

    public void showModal(Model model, String message, String button) {
        model.addAttribute("messageModal", message);
        if(!button.equals("")){
            model.addAttribute("button", button);
        }
    }

}