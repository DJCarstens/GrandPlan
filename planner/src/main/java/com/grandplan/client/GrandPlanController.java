package com.grandplan.client;

import com.grandplan.server.models.User;
import com.grandplan.client.services.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GrandPlanController {
  public Model mainModel;

  @Autowired
  private LoginService loginService;

  @GetMapping({"/login", "/"})
  public String login(Model model) {
    model.addAttribute("user", new User());
    return "login";
  }

  @GetMapping("/signup")
  public String signup(Model model) {
    model.addAttribute("user", new User());
    return "signup";
  }

  @PostMapping(value = "/validateLogin")
  public String validate(@ModelAttribute("user") User user, BindingResult bindingResult, Model model){
    this.mainModel = model;
    mainModel.addAttribute("user", user);
    if(loginService.validateLogin(user, mainModel)){
      //TODO: validate whether user exists or not before navigation
      //If user exists and info matches, navigate to "home" else navigate to "signup"
      return "home";
    }
    else{
      return "login";
    }
  }

  @PostMapping(value = "/validateSignup")
  public String validateSignup(@ModelAttribute("user") User user, BindingResult bindingResult, Model model){
    this.mainModel = model;
    mainModel.addAttribute("user", user);
    if(loginService.validateSignup(user, mainModel)){
      //TODO: validate whether user exists or not before navigation
      //If user exists and info matches, navigate to "login" else create the user and nagivate to "home"
      return "home";
    }
    else{
      return "signup";
    }
  }

}