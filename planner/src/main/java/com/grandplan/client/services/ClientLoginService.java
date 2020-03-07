package com.grandplan.client.services;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

import com.grandplan.client.util.LoginUser;
import com.grandplan.client.util.SignupUser;
import com.grandplan.util.User;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@RequiredArgsConstructor
@Service
@Component
public class ClientLoginService {
    @Autowired
    private HttpRequestService httpRequestService;

    private static final String LOGIN = "login";
    private static final String SIGNUP = "signup";
    private static final String HOME = "home";
    
    public String validateLogin(LoginUser loginUser, Model model) throws IOException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", loginUser.getEmail());
        jsonObject.put("password", loginUser.getPassword());

        CloseableHttpResponse response = httpRequestService.sendHttpRequest(jsonObject, "http://localhost:8080/api/validateLogin");
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode == 404){
            showModal(model, "Your account was not found. Please check your login details and try again, or signup if you do not have an account.", SIGNUP);
            return LOGIN;
        }

        if(statusCode == 200){
            User user = loginUser.convertUser();
            model.addAttribute("user", user);
            return HOME;
        }

        showModal(model, "Something went wrong with the login process. Please try again.", LOGIN);
        return LOGIN;
    }

    public void showModal(Model model, String message, String button) {
        model.addAttribute("messageModal", message);
        model.addAttribute("button", button);
    }

    public String validateSignup(SignupUser signupUser, Model model) throws IOException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", signupUser.getEmail());
        jsonObject.put("password", signupUser.getPassword());
        jsonObject.put("firstName", signupUser.getFirstName());
        jsonObject.put("lastName", signupUser.getLastName());
        jsonObject.put("phone", signupUser.getPhone());

        CloseableHttpResponse response = httpRequestService.sendHttpRequest(jsonObject, "http://localhost:8080/api/addUser");
        int statusCode = response.getStatusLine().getStatusCode();

        if(statusCode == 409){
            showModal(model, "An account for " + signupUser.getEmail() + " already exists. Please check your signup details and try again, or login if you have an account.", LOGIN);
            return SIGNUP;
        }

        if(statusCode == 200){
            User user = signupUser.convertUser();
            model.addAttribute("user", user);
            return HOME;
        }

        showModal(model, "Something went wrong with the signup process. Please try again.", LOGIN);
        return SIGNUP;
    }
}