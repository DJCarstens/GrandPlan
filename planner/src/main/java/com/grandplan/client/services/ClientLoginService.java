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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Component
public class ClientLoginService {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public Integer sendHttpRequest(LoginUser loginUser) throws IOException{
        CloseableHttpClient client = HttpClients.createDefault();
        try{
            HttpPost httpPost = new HttpPost("http://localhost:8080/api/validateLogin");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", loginUser.getEmail());
            jsonObject.put("password", loginUser.getPassword());
        
            StringEntity entity = new StringEntity(jsonObject.toJSONString());
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
        
            CloseableHttpResponse response = client.execute(httpPost);
            return response.getStatusLine().getStatusCode();
        }
        finally{
            client.close();
        }
    }
}