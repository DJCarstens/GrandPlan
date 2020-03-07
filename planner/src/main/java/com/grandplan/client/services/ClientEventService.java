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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Component
public class ClientEventService {

    @Autowired
    private HttpRequestService httpRequestService;

    public String getUserEvents(User user, Model model) throws IOException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", user.getEmail());

        CloseableHttpResponse response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/getUserEvents");
        String responseBody = EntityUtils.toString(response.getEntity());

        if(responseBody.equals("[]")){
            model.addAttribute("noEvents", "You currently have no events");
        }

        return "events";
    }

}