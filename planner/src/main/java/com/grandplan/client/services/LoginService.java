package com.grandplan.client.services;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.grandplan.server.models.User;
import org.springframework.ui.Model;
import org.springframework.stereotype.Service;

@Service
public class LoginService{
    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN = "^[\\w.+\\-]+@bbd\\.co\\.za$";

    public boolean validateLogin(User user, Model model){
      boolean email = validateEmail(user.getEmail(), model);
      boolean password = validateInput(user.getPassword(), model, "passwordError", "Please provide your password");
      return (email && password);
    }

    public boolean validateSignup(User user, Model model){
      boolean name = validateInput(user.getFirstName(), model, "firstNameError", "Please provide your first name");
      boolean surname = validateInput(user.getLastName(), model, "lastNameError", "Please provide your last name"); 
      boolean phone = validateInput(user.getPhone(), model, "phoneError", "Please provide your phone number");
      boolean password = validateInput(user.getPassword(), model, "passwordError", "Please provide your password");
      boolean email = validateEmail(user.getEmail(), model);
      if(name && surname && email && phone && password){
        return true;
      }
      return false;
    }

    private boolean validateInput(String input, Model model, String error, String errorMessage){
      if(input.isEmpty()){
        model.addAttribute(error, errorMessage);
        return false;
      }
      return true;
    }

    private boolean validateEmail(String email, Model model){
      if(email.isEmpty()){
        model.addAttribute("emailError", "Please provide your email");
        return false;
      }
      return validateEmailRegex(email, model);
    }

    private boolean validateEmailRegex(String email, Model model){
      pattern = Pattern.compile(EMAIL_PATTERN);
      matcher = pattern.matcher(email);
      if(!matcher.matches()){
        model.addAttribute("emailError", "Please provide a valid email");
        return false;
      }
      return true;
    }

}