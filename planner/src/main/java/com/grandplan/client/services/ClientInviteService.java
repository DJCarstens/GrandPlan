package com.grandplan.client.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.grandplan.util.Event;
import com.grandplan.util.Invite;
import com.grandplan.util.User;
import com.grandplan.client.util.Constants;

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

    @Autowired
    private ClientEventService clientEventService;

    private CloseableHttpResponse response;
    private static final String INVITE_ERROR = "Something went wrong when getting your invites. Please try again later.";

    public String getInvites(User user, Model model){
        JSONObject jsonObject = generateJsonObject(
            new ArrayList<String>(){{add(Constants.EMAIL);}},
            new ArrayList<String>(){{add(user.getEmail());}}
        );

        try{
            response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/getUserInvites");
            String responseBody = EntityUtils.toString(response.getEntity());
            if(responseBody.equals("[]")){
                model.addAttribute("noInvites", "You currently have no events");
                return Constants.INVITES;
            }

            JSONParser parser = new JSONParser();
            Object object = parser.parse(responseBody);
            System.out.println(object);
            JSONArray jsonArray = (JSONArray) object;
            List<Invite> invites = new ArrayList<>();
            jsonArray.forEach(item -> {
                JSONObject obj = (JSONObject) item;
                invites.add(getInvite(Long.parseLong(obj.get(Constants.ID).toString()), Boolean.parseBoolean(obj.get("accepted").toString())));
            });

            model.addAttribute(Constants.INVITES, invites);
            model.addAttribute("user", clientLoginService.getCurrentUser());
            return Constants.INVITES;
        }
        catch(Exception exception){
            showModal(model, INVITE_ERROR, "");
            model.addAttribute("user", clientLoginService.getCurrentUser());
            return Constants.INVITES;
        }
    }

    private Invite getInvite(Long inviteId, Boolean accepted){
        JSONObject jsonObject = generateJsonObject(
            new ArrayList<String>(){{add("eventId");}},
            new ArrayList<String>(){{add(inviteId.toString());}}
        );

        try{
            response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/getUnacceptedUserInvites");
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONParser parser = new JSONParser();
            Object object = parser.parse(responseBody);
            JSONObject json = (JSONObject) object;
            Event event = clientEventService.generateEventObject(json);
            User user = clientLoginService.getUserByEmail(event.getHostUsername());
            return Invite.builder()
                .id(inviteId)
                .accepted(accepted)
                .event(event)
                .user(user)
                .build();
        }
        catch(Exception exception){
            return null;
        }
    }

    public Boolean createInvites(String userEmail, Long eventId){
        JSONObject jsonObject = generateJsonObject(
            new ArrayList<String>(){{add("eventId"); add("userEmail");}},
            new ArrayList<String>(){{add(eventId.toString()); add(userEmail);}}
        );

        try{
            response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/createInvite");
            int statusCode = response.getStatusLine().getStatusCode();
            return (statusCode == 200);
        }
        catch(Exception exception){
            return false;
        }
    }

    public String acceptInvite(Invite acceptInvite, Model model){
        JSONObject jsonObject = generateJsonObject(
            new ArrayList<String>(){{add("inviteId");}},
            new ArrayList<String>(){{add(acceptInvite.getId().toString());}}
        );

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

    public String declineInvite(Invite declineInvite, Model model){
        JSONObject jsonObject = generateJsonObject(
            new ArrayList<String>(){{add("inviteId");}},
            new ArrayList<String>(){{add(declineInvite.getId().toString());}}
        );

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

    private JSONObject generateJsonObject(List<String> keys, List<String> values){
        HashMap<String,String> hashMap = new HashMap<>();
        for(int i = 0; i < keys.size(); i++){
            hashMap.put(keys.get(i), values.get(i));
        }
        return new JSONObject(hashMap);
    }
}