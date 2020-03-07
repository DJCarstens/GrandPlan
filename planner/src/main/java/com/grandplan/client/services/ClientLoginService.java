package com.grandplan.client.services;

import lombok.RequiredArgsConstructor;
import lombok.var;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.grandplan.client.util.LoginUser;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Component
public class ClientLoginService {
    @Autowired
    private HttpRequestService httpRequestService;
    
    public Integer validateLogin(LoginUser loginUser) throws IOException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", loginUser.getEmail());
        jsonObject.put("password", loginUser.getPassword());

        return httpRequestService.sendHttpRequest(jsonObject, "http://localhost:8080/api/validateLogin");
    }
}