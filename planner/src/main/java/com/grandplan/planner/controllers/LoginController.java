package com.grandplan.planner.controllers;

import com.grandplan.planner.models.Login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

  private Pattern pattern;
  private Matcher matcher;

  public Model mainModel;

  private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

  @GetMapping({"/", "/login"})
  public String login(Model model) {
    model.addAttribute("user", new Login());
    return "login/login";
  }

  @RequestMapping(value = "/validate", method = RequestMethod.POST)
  public String validate(@ModelAttribute("user") Login user, BindingResult bindingResult, Model model){
    this.mainModel = model;
    mainModel.addAttribute("login", user);
    return this.validateLogin(user, mainModel) ? "home/home" : "login/login";
  }

  public boolean validateLogin(Login user, Model model){
    if(user.getEmail() == "" || user.getPassword() == ""){
      model.addAttribute("emailError", "Please provide your email");
      model.addAttribute("passwordError", "Please provide your password");
      return false;
    }

    if(!validateEmail(user.getEmail())){
      model.addAttribute("emailError", "Please provide a valid email");
      return false;
    }

    return true;
  }

  public boolean validateEmail(String email){
    if(email == "") return false;
    pattern = Pattern.compile(EMAIL_PATTERN);
    matcher = pattern.matcher(email);
    return matcher.matches();
  }

  public boolean findUser(Login user){
    return true;
  }

}