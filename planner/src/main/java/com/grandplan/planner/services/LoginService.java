package com.grandplan.planner.services;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.grandplan.planner.models.Login;
import com.grandplan.planner.models.User;
import org.springframework.ui.Model;
import org.springframework.stereotype.Service;

@Service
public class LoginService{
    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public boolean validateLogin(Login user, Model model){
      return (validatePassword(user.getPassword(), model))
        ? validateEmail(user. getEmail(), model)
        : validatePassword(user.getPassword(), model);
    }

    public boolean validateSignup(User user, Model model){
      boolean name = validateName(user.getFirstName(), model);
      boolean surname = validateSurname(user.getLastName(), model); 
      boolean email = validateEmail(user.getEmail(), model);
      boolean phone = validatePhone(user.getPhone(), model);
      boolean password = validatePassword(user.getPassword(), model);
      boolean confirmPassword = validateConfirmPassword(user.getConfirmPassword(), model);
      if(name && surname && email && phone && password && confirmPassword){
        return user.getPassword() == user.getConfirmPassword();
      }
      return false;
    }

    private boolean validateName(String name, Model model){
      if(name == ""){
        model.addAttribute("firstNameError", "Please provide your first name");
        return false;
      }
      return true;
    }

    private boolean validateSurname(String surname, Model model){
      if(surname == ""){
        model.addAttribute("lastNameError", "Please provide your last name");
        return false;
      }
      return true;
    }

    private boolean validateEmail(String email, Model model){
      if(email == ""){
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
      if(phoneNumber == ""){
        model.addAttribute("phoneError", "Please provide your number");
        return false;
      }
      return true;
    }

    private boolean validatePassword(String password, Model model){
      if(password == "") {
        model.addAttribute("passwordError", "Please provide your password");
        return false;
      }
      return true;
    }

    private boolean validateConfirmPassword(String password, Model model){
      if(password == "") {
        model.addAttribute("confirmPasswordError", "Please provide your password");
        return false;
      }
      return true;
    }
}