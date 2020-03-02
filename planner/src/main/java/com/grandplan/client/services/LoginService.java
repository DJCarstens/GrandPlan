package com.grandplan.client.services;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.grandplan.util.User;
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
      boolean email = validateEmail(user.getEmail(), model);
      boolean phone = validateInput(user.getPhone(), model, "phoneError", "Please provide your number");
      boolean password = validatePassword(user.getPassword(), model);
      boolean confirmPassword = validateInput(user.getConfirmPassword(), model, "confirmPasswordError", "Please provide your password");
      if(name && surname && email && phone && password && confirmPassword){
        if(user.getPassword() != user.getConfirmPassword()){
          model.addAttribute("matchingPasswordError", "These passwords do not match. Please try again.");
          return false;
        }
      }
      return true;
    }

    private boolean validateInput(String input, Model model, String error, String errorMessage){
      if(input.equals(null)){
        model.addAttribute(error, errorMessage);
        return false;
      }
      return true;
    }

    private boolean validatePassword(String pass, Model model){
      if(pass.equals(null)){
        model.addAttribute("passwordError", "Please provide your password");
        return false;
      }
      return true;
    }

    private boolean validateEmail(String email, Model model){
      if(email.equals(null)){
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