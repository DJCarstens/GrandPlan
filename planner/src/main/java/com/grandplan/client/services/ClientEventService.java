package com.grandplan.client.services;

import java.io.IOException;
import java.util.HashMap;

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
            System.out.println(responseBody);
            if(responseBody.equals("[]")){
                model.addAttribute("noEvents", "You currently have no events");
                return EVENTS;
            }

            // model.addAttribute(EVENTS, responseBody);
            // model.addAttribute("user", clientLoginService.getCurrentUser());
        }
        catch(IOException exception){
            showModal(model, "Something went wrong when getting your events. Please try again later.", "");
            return EVENTS;
        }

        showModal(model, "Something went wrong when getting your events. Please try again later.", "");
        return EVENTS;
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
            return EVENTS;
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
    //         return EVENTS;
    //     }
    // }

    public void showModal(Model model, String message, String button) {
        model.addAttribute("messageModal", message);
        if(!button.equals("")){
            model.addAttribute("button", button);
        }
    }

}