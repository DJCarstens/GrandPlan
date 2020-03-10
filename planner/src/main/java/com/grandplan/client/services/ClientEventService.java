package com.grandplan.client.services;

import java.io.IOException;
import java.util.HashMap;

import com.grandplan.client.util.NewEvent;
import com.grandplan.util.Event;
import com.grandplan.util.User;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
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
                model.addAttribute("user", user);
                return EVENTS;
            }
            showModal(model, "Successfully created event!", "");
            model.addAttribute(EVENTS, responseBody);
            model.addAttribute("user", user);
            return EVENTS;
        }
        catch(IOException exception){
            showModal(model, "Something went wrong when getting your events. Please try again later.", "");
            model.addAttribute("user", user);
            return EVENTS;
        }
    }

    public String createEvent(NewEvent newEvent, Model model){
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

        try{
            httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/createEvent");
            showModal(model, "Successfully created event!", "");
            return getUserEvents(clientLoginService.getCurrentUser(), model);
        }
        catch(IOException exception){
            showModal(model, "Something went wrong creating your event. Please try again later.", "");
            return getUserEvents(clientLoginService.getCurrentUser(), model);
        }
    }

    public String deleteEvent(String eventId, String userEmail, Model model){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("id", eventId);
        hashMap.put("userEmail", userEmail);
        JSONObject jsonObject = new JSONObject(hashMap);

        CloseableHttpResponse response;
        try{
            response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/deleteEvent");
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200){
                showModal(model, "Successfully deleted event", EVENTS);
            }             
            else{
                showModal(model, "Could not delete event. Please try again later.", EVENTS);
            }                

            model.addAttribute("user", clientLoginService.getCurrentUser());
            return getUserEvents(clientLoginService.getCurrentUser(), model);
        }
        catch(IOException exception){
            showModal(model, "Something went wrong deleting this event. Please try again later.", "");
            return getUserEvents(clientLoginService.getCurrentUser(), model);
        }
    }

    // public String transferEvent(String eventId, String userEmail, Model model){
    //     HashMap<String,String> hashMap = new HashMap<>();
    //     hashMap.put("id", eventId);
    //     hashMap.put("userEmail", userEmail);
    //     JSONObject jsonObject = new JSONObject(hashMap);

    //     CloseableHttpResponse response;
    //     try{
    //         response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/transferEvent");
    //         int statusCode = response.getStatusLine().getStatusCode();

    //         if (statusCode == 200){
    //             showModal(model, "Successfully transferred event", EVENTS);
    //         }             
    //         else{
    //             showModal(model, "Could not transfer event. Please try again later.", EVENTS);
    //         }                

    //         model.addAttribute("user", clientLoginService.getCurrentUser());
    //         return getUserEvents(clientLoginService.getCurrentUser(), model);
    //     }
    //     catch(IOException exception){
    //         showModal(model, "Something went wrong transferring this event. Please try again later.");
    //         return getUserEvents(clientLoginService.getCurrentUser(), model);
    //     }
    // }

    public void showModal(Model model, String message, String button) {
        model.addAttribute("messageModal", message);
        if(!button.equals("")){
            model.addAttribute("button", button);
        }
    }

}