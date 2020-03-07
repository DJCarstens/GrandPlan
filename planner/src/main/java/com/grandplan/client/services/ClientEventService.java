package com.grandplan.client.services;

import java.io.IOException;

import com.grandplan.util.User;

import org.apache.http.client.methods.CloseableHttpResponse;
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
        CloseableHttpResponse response = httpRequestService.sendHttpGet("http://localhost:8080/api/getUserEvents");

        return "events";
    }

}