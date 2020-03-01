package com.grandplan.planner.services;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.grandplan.planner.models.User;
import org.springframework.ui.Model;
import org.springframework.stereotype.Service;

@Service
public class LoginService{
    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN = "^[\\w.+\\-]+@bbd\\.co\\.za$";

    public boolean validateLogin(User user, Model model){
      boolean email = validateEmail(user. getEmail(), model);
      boolean password = validatePassword(user.getPassword(), model);
      return (email && password);
    }

    public boolean validateSignup(User user, Model model){
      boolean name = validateInput(user.getFirstName(), model, "firstNameError", "Please provide your first name");
      boolean surname = validateInput(user.getLastName(), model, "lastNameError", "Please provide your last name"); 
      boolean email = validateEmail(user.getEmail(), model);
      boolean phone = validatePhone(user.getPhone(), model);
      boolean password = validatePassword(user.getPassword(), model);
      boolean confirmPassword = validateInput(user.getConfirmPassword(), model, "confirmPasswordError", "Please provide your password");
      if(name && surname && email && phone && password && confirmPassword){
        if(user.getPassword() != user.getConfirmPassword()){
          model.addAttribute("matchingPasswordError", "These passwords do not match. Please try again.");
          return false;
        }
        return true;
      }
      return false;
    }

    private boolean validateInput(String input, Model model, String error, String errorMessage){
      if(input.equals("")){
        model.addAttribute(error, errorMessage);
        return false;
      }
      return true;
    }

    private boolean validatePassword(String pass, Model model){
      if(pass.equals("")){
        model.addAttribute("passwordError", "Please provide your password");
        return false;
      }
      return true;
    }

    private boolean validateEmail(String email, Model model){
      if(email.equals("")){
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

    private boolean validatePhone(String phoneNumber, Model model){
      if(phoneNumber.equals("")){
        model.addAttribute("phoneError", "Please provide your number");
        return false;
      }
      return true;
    }
}