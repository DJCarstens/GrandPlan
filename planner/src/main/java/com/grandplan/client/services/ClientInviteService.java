package com.grandplan.client.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.grandplan.client.util.InviteStatus;
import com.grandplan.util.Invite;
import com.grandplan.util.User;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@Component
public class ClientInviteService {
    @Autowired
    private HttpRequestService httpRequestService;

    @Autowired
    private ClientLoginService clientLoginService;

    private CloseableHttpResponse response;
    private static final String INVITES = "invites";
    private static final String INVITE_ERROR = "Something went wrong when getting your invites. Please try again later.";

    public String getInvites(User user, Model model){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("email", user.getEmail());
        JSONObject jsonObject = new JSONObject(hashMap);

        try{
            response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/getUserInvites");
            String responseBody = EntityUtils.toString(response.getEntity());
            if(responseBody.equals("[]")){
                model.addAttribute("noInvites", "You currently have no events");
                return INVITES;
            }

            JSONParser parser = new JSONParser();
            Object object = parser.parse(responseBody);
            JSONArray jsonArray = (JSONArray) object;
            List<Invite> invites = new ArrayList<>();
            jsonArray.forEach(item -> {
                JSONObject obj = (JSONObject) item;
                // invites.add(getInviteByUserAndEvent(json));
                System.out.println(obj);
                getInvite(Long.parseLong(obj.get("id").toString()));
            });

            // model.addAttribute(INVITES, responseBody);
            // model.addAttribute("user", clientLoginService.getCurrentUser());
            // return INVITES;
        }
        catch(Exception exception){
            showModal(model, INVITE_ERROR, "");
            model.addAttribute("user", clientLoginService.getCurrentUser());
            return INVITES;
        }

        showModal(model, INVITE_ERROR, "");
        model.addAttribute("user", clientLoginService.getCurrentUser());
        return INVITES;
    }

    private void getInvite(Long inviteId){
        try{
           
        }
        catch(Exception exception){
            // return null;
        }
    }

    public Boolean createInvites(String userEmail, Long eventId){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("eventId", eventId.toString());
        hashMap.put("userEmail", userEmail);
        JSONObject jsonObject = new JSONObject(hashMap);

        try{
            response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/createInvite");
            int statusCode = response.getStatusLine().getStatusCode();
            return (statusCode == 200);
        }
        catch(Exception exception){
            return false;
        }
    }

    public String acceptInvite(InviteStatus acceptInvite, Model model){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("inviteId", acceptInvite.getInviteId());
        JSONObject jsonObject = new JSONObject(hashMap);

        try{
            response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/acceptInvite");
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 200){
                showModal(model, "Successfully accepted invite. This event has been added to your profile.", "");
                return getInvites(clientLoginService.getCurrentUser(), model);
            }
        }
        catch(IOException exception){
            showModal(model, "Could not accept invite. Please try again later", "");
            return getInvites(clientLoginService.getCurrentUser(), model);
        }

        showModal(model, "Could not accept invite. Please try again later", "");
        return getInvites(clientLoginService.getCurrentUser(), model);
    }

    public String declineInvite(InviteStatus declineInvite, Model model){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("inviteId", declineInvite.getInviteId());
        JSONObject jsonObject = new JSONObject(hashMap);

        try{
            response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/declineInvite");
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 200){
                showModal(model, "Successfully declined invite.", "");
                return getInvites(clientLoginService.getCurrentUser(), model);
            }
        }
        catch(IOException exception){
            showModal(model, "Could not decline invite. Please try again later", "");
            return getInvites(clientLoginService.getCurrentUser(), model);
        }

        showModal(model, "Could not decline invite. Please try again later", "");
        return getInvites(clientLoginService.getCurrentUser(), model);
    }

    public void showModal(Model model, String message, String button) {
        model.addAttribute("messageModal", message);
        if(!button.equals("")){
            model.addAttribute("button", button);
        }
    }
}