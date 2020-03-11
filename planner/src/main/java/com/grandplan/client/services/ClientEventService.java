package com.grandplan.client.services;

import java.io.IOException;
import java.util.HashMap;

import com.grandplan.client.util.EventStatus;
import com.grandplan.client.util.NewEvent;
import com.grandplan.util.User;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Component
public class ClientEventService {
    private static final String EVENTS ="events";

    @Autowired
    private HttpRequestService httpRequestService;

    @Autowired 
    private ClientLoginService clientLoginService;

    @Autowired
    private ClientInviteService clientInviteService;

    public String getUserEvents(User user, Model model){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("email", user.getEmail());
        JSONObject jsonObject = new JSONObject(hashMap);

        CloseableHttpResponse response;
        try{
            response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/getUserEvents");
            String responseBody = EntityUtils.toString(response.getEntity());
            if(responseBody.equals("[]")){
                model.addAttribute("noEvents", "You currently have no events");
                return EVENTS;
            }
           
            model.addAttribute(EVENTS, responseBody);
            return EVENTS;
        }
        catch(IOException exception){
            showModal(model, "Something went wrong when getting your events. Please try again later.", "");
            return EVENTS;
        }
    }

    public HashMap<String,String> getEventById(String eventId){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("eventId", eventId);
        JSONObject jsonObject = new JSONObject(hashMap);

        CloseableHttpResponse response;
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

    public String createEvent(NewEvent newEvent, Model model) throws ParseException{
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("title", newEvent.getTitle());
        hashMap.put("description", newEvent.getDescription());
        hashMap.put("start", newEvent.getStart());
        hashMap.put("end", newEvent.getEnd());
        hashMap.put("allDay", newEvent.getAllDay().toString());
        hashMap.put("color", newEvent.getColor());
        hashMap.put("hostUsername", clientLoginService.getCurrentUser().getEmail());
        hashMap.put("tag", newEvent.getTag());
        JSONObject jsonObject = new JSONObject(hashMap);

        CloseableHttpResponse response;
        try{
            response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/createEvent");
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
        }
        catch(IOException exception){
            showModal(model, "Something went wrong creating your event. Please try again later.", "");
            return getUserEvents(clientLoginService.getCurrentUser(), model);
        }

        showModal(model, "Something went wrong creating your event. Please try again later.", "");
        return getUserEvents(clientLoginService.getCurrentUser(), model);
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
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("id", eventStatus.getEventId());
        hashMap.put("userEmail", eventStatus.getHostUsername());
        JSONObject jsonObject = new JSONObject(hashMap);

        CloseableHttpResponse response;
        try{
            response = (eventStatus.getHostUsername().equals(clientLoginService.getCurrentUser().getEmail()))
                ? httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/deleteEvent")
                : httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/deleteEvent"); //TODO: remove invite from user
            
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200){
                showModal(model, "Successfully deleted event", "");
            }             
            else{
                showModal(model, "Could not delete event. Please try again later.", "");
            }                

            model.addAttribute("user", clientLoginService.getCurrentUser());
            return getUserEvents(clientLoginService.getCurrentUser(), model);
        }
        catch(IOException exception){
            showModal(model, "Something went wrong deleting this event. Please try again later.", "");
            return getUserEvents(clientLoginService.getCurrentUser(), model);
        }
    }

    public String transferEvent(EventStatus eventStatus, Model model){
        HashMap<String,String> hashMap = getEventById(eventStatus.getEventId());
        hashMap.put("hostUsername", eventStatus.getHostUsername());
        JSONObject jsonObject = new JSONObject(hashMap);

        CloseableHttpResponse response;
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

            model.addAttribute("user", clientLoginService.getCurrentUser());
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