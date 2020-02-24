package com.grandplan.planner.controllers;

import com.grandplan.planner.models.Login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

  @GetMapping({"/", "/login"})
  public String login(Model model) {
    model.addAttribute("user", new Login());
    return "login/login";
  }

  @RequestMapping(value = "/validate", method = RequestMethod.POST)
  public String validate(@ModelAttribute Login user, BindingResult bindingResult, Model model){
    model.addAttribute("emailError", "Please enter a valid email");
    // if(bindingResult.hasErrors()) return "login/login";
    return this.validateLogin(user) ? "home/home" : "login/login";
  }

  public boolean validateLogin(Login user){
    return true;
  }

}