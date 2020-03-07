package com.grandplan.client.services;

import java.io.IOException;

import com.grandplan.util.User;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import antlr.debug.Event;
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

    public String getUserEvents(User user, Model model) throws IOException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", user.getEmail());

        CloseableHttpResponse response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/getUserEvents");
        String responseBody = EntityUtils.toString(response.getEntity());

        if(responseBody.equals("[]")){
            model.addAttribute("noEvents", "You currently have no events");
        }

        return EVENTS;
    }

    public String deleteEvent(String eventId, Model model) throws IOException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", eventId);

        CloseableHttpResponse response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/deleteEvent");
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

    public void showModal(Model model, String message, String button) {
        model.addAttribute("messageModal", message);
        model.addAttribute("button", button);
    }

}