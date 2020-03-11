package com.grandplan.client.services;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.grandplan.client.util.LoginUser;
import com.grandplan.client.util.SignupUser;
import com.grandplan.util.User;

import org.apache.commons.codec.digest.DigestUtils;
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
    private CloseableHttpResponse response;

    private static final String LOGIN = "login";
    private static final String SIGNUP = "signup";
    private static final String HOME = "home";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String LOGIN_ERROR = "Something went wrong with the login process. Please try again.";
    private static final String SIGNUP_ERROR = "Something went wrong with the signup process. Please try again.";
    
    public String validateLogin(LoginUser loginUser, Model model, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return LOGIN;
        }

        try{
            response = httpRequestService.sendHttpPost(generateLoginObject(loginUser), "http://localhost:8080/api/validateLogin");
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 404){
                showModal(model, "Your account was not found. Please check your login details and try again, or signup if you do not have an account.", SIGNUP);
                return LOGIN;
            }

            if(statusCode == 200){
                currentUser = getUserInfo(EntityUtils.toString(response.getEntity()));
                model.addAttribute("user", currentUser);
                return HOME;
            }

            showModal(model, LOGIN_ERROR, LOGIN);
            return LOGIN;
        }
        catch(Exception exception){
            showModal(model, LOGIN_ERROR, LOGIN);
            return LOGIN;
        }
    }

    private JSONObject generateLoginObject(LoginUser loginUser){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put(EMAIL, loginUser.getEmail());
        hashMap.put(PASSWORD, loginUser.getPassword());
        return new JSONObject(hashMap); 
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
        if(!button.equals("")){
            model.addAttribute("button", button);
        }
    }

    public String validateSignup(SignupUser signupUser, Model model, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return SIGNUP;
        }

        if (!signupUser.getPassword().equals(signupUser.getConfirmPassword())) {
            model.addAttribute("matchingPasswordError", "The passwords don't match");
            return SIGNUP;
        }

        try{
            response = httpRequestService.sendHttpPost(generateSignupObject(signupUser), "http://localhost:8080/api/addUser");
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

            showModal(model, SIGNUP_ERROR, "");
            return SIGNUP;
        }
        catch(IOException exception){
            showModal(model, SIGNUP_ERROR, "");
            return SIGNUP;
        }
    }

    private JSONObject generateSignupObject(SignupUser signupUser){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put(EMAIL, signupUser.getEmail());
        hashMap.put(PASSWORD, generatePasswordHash(signupUser.getPassword()));
        hashMap.put("firstName", signupUser.getFirstName());
        hashMap.put("lastName", signupUser.getLastName());
        hashMap.put("phone", signupUser.getPhone());
        return new JSONObject(hashMap);
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public String logout(){
        currentUser = null;
        return LOGIN;
    }

    private String generatePasswordHash(String password){
        String salt = DigestUtils.sha256Hex(password);
        return DigestUtils.sha256Hex(password.concat(salt));
    }
}