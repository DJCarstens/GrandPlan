package com.grandplan.client.services;

import java.io.IOException;
import java.util.HashMap;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.grandplan.util.LoginUser;
import com.grandplan.util.SignupUser;
import com.grandplan.util.User;
import com.grandplan.client.util.Constants;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Service
@Component
public class ClientLoginService {
    @Autowired
    private HttpRequestService httpRequestService;  

    private User currentUser = null;
    private CloseableHttpResponse response;

    private static final String LOGIN_ERROR = "Something went wrong with the login process. Please try again.";
    private static final String SIGNUP_ERROR = "Something went wrong with the signup process. Please try again.";
    
    public String validateLogin(LoginUser loginUser, Model model, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return Constants.LOGIN;
        }

        try{
            response = httpRequestService.sendHttpPost(generateLoginObject(loginUser), "http://localhost:8080/api/validateLogin");
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 404){
                showModal(model, "Your account was not found. Please check your login details and try again, or signup if you do not have an account.", Constants.SIGNUP);
                return Constants.LOGIN;
            }

            if(statusCode == 200){
                currentUser = getUserInfo(EntityUtils.toString(response.getEntity()));
                model.addAttribute("user", currentUser);
                return Constants.HOME;
            }

            showModal(model, LOGIN_ERROR, Constants.LOGIN);
            return Constants.LOGIN;
        }
        catch(Exception exception){
            showModal(model, LOGIN_ERROR, Constants.LOGIN);
            return Constants.LOGIN;
        }
    }

    private JSONObject generateLoginObject(LoginUser loginUser){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put(Constants.EMAIL, loginUser.getEmail());
        hashMap.put(Constants.PASSWORD, loginUser.getPassword());
        return new JSONObject(hashMap); 
    }

    private User getUserInfo(String response) throws ParseException{
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(response);
        JSONObject jsonBody = (JSONObject) obj;

        return User.builder()
            .id(Long.parseLong(jsonBody.get(Constants.ID).toString()))
            .email(jsonBody.get(Constants.EMAIL).toString())
            .password(jsonBody.get(Constants.PASSWORD).toString())
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
            return Constants.SIGNUP;
        }

        if (!signupUser.getPassword().equals(signupUser.getConfirmPassword())) {
            model.addAttribute("matchingPasswordError", "The passwords don't match");
            return Constants.SIGNUP;
        }

        try{
            response = httpRequestService.sendHttpPost(generateSignupObject(signupUser), "http://localhost:8080/api/addUser");
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 409){
                showModal(model, "An account for " + signupUser.getEmail() + " already exists. Please check your signup details and try again, or login if you have an account.", Constants.LOGIN);
                return Constants.SIGNUP;
            }

            if(statusCode == 200){
                currentUser = signupUser.convertUser();
                model.addAttribute("user", currentUser);
                return Constants.HOME;
            }

            showModal(model, SIGNUP_ERROR, "");
            return Constants.SIGNUP;
        }
        catch(IOException exception){
            showModal(model, SIGNUP_ERROR, "");
            return Constants.SIGNUP;
        }
    }

    public User getUserByEmail(String email){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("email", email);
        JSONObject jsonObject = new JSONObject(hashMap);

        try{
            response = httpRequestService.sendHttpPost(jsonObject, "http://localhost:8080/api/getUserByEmail");
            String responseBody = EntityUtils.toString(response.getEntity());
            return getUserInfo(responseBody);
        }
        catch(Exception exception){
            return null;
        }
    }

    private JSONObject generateSignupObject(SignupUser signupUser){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put(Constants.EMAIL, signupUser.getEmail());
        hashMap.put(Constants.PASSWORD, generatePasswordHash(signupUser.getPassword()));
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
        return Constants.LOGIN;
    }

    private String generatePasswordHash(String password){
        String salt = DigestUtils.sha256Hex(password);
        return DigestUtils.sha256Hex(password.concat(salt));
    }
}