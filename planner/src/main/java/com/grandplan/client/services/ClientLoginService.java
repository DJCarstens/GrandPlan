package com.grandplan.client.services;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.HashMap;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.grandplan.client.util.LoginUser;
import com.grandplan.client.util.SignupUser;
import com.grandplan.util.User;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@RequiredArgsConstructor
@Service
@Component
public class ClientLoginService {
    @Autowired
    private HttpRequestService httpRequestService;

    private User currentUser;

    private static final String LOGIN = "login";
    private static final String SIGNUP = "signup";
    private static final String HOME = "home";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    
    public String validateLogin(LoginUser loginUser, Model model, BindingResult bindingResult) throws IOException{
        if(bindingResult.hasErrors()){
            return LOGIN;
        }

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put(EMAIL, loginUser.getEmail());
        hashMap.put(PASSWORD, loginUser.getPassword());
        JSONObject jsonObject = new JSONObject(hashMap);

        CloseableHttpResponse response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/validateLogin");
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode == 404){
            showModal(model, "Your account was not found. Please check your login details and try again, or signup if you do not have an account.", SIGNUP);
            return LOGIN;
        }

        if(statusCode == 200){
            try{
                currentUser = getUserInfo(EntityUtils.toString(response.getEntity()));
                model.addAttribute("user", currentUser);
                return HOME;
            }
            catch(Exception e){
                showModal(model, "Something went wrong with the login process. Please try again.", LOGIN);
                return LOGIN;
            }
        }

        showModal(model, "Something went wrong with the login process. Please try again.", LOGIN);
        return LOGIN;
    }

    private User getUserInfo(String response) throws ParseException{
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(response);
        JSONObject jsonBody = (JSONObject) obj;
        return User.builder()
            .email(jsonBody.get(EMAIL).toString())
            .password(jsonBody.get(PASSWORD).toString())
            .lastName(jsonBody.get("lastName").toString())
            .firstName(jsonBody.get("firstName").toString())
            .phone(jsonBody.get("phone").toString())
            .build();
    }

    public void showModal(Model model, String message, String button) {
        model.addAttribute("messageModal", message);
        model.addAttribute("button", button);
    }

    public String validateSignup(SignupUser signupUser, Model model, BindingResult bindingResult) throws IOException{
        if (bindingResult.hasErrors()) {
            return SIGNUP;
        }

        if (!signupUser.getPassword().equals(signupUser.getConfirmPassword())) {
            model.addAttribute("matchingPasswordError", "The passwords don't match");
            return SIGNUP;
        }

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put(EMAIL, signupUser.getEmail());
        hashMap.put(PASSWORD, signupUser.getPassword());
        hashMap.put("firstName", signupUser.getFirstName());
        hashMap.put("lastName", signupUser.getLastName());
        hashMap.put("phone", signupUser.getPhone());
        JSONObject jsonObject = new JSONObject(hashMap);

        CloseableHttpResponse response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/addUser");
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode == 409){
            showModal(model, "An account for " + signupUser.getEmail() + " already exists. Please check your signup details and try again, or login if you have an account.", LOGIN);
            return SIGNUP;
        }

        if(statusCode == 200){
            currentUser = signupUser.convertUser();
            model.addAttribute("user", currentUser);
            return HOME;
        }

        showModal(model, "Something went wrong with the signup process. Please try again.", LOGIN);
        return SIGNUP;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public String logout(){
        currentUser = null;
        return LOGIN;
    }
}