package com.grandplan.planner.controllers;

import com.grandplan.planner.models.Login;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

  @GetMapping("/")
  public String main() {
    return "login/login";
  }

  @GetMapping("/login")
  public String login() {
    return "login/login";
  }

  @PostMapping("/validate")
  public String validate(@ModelAttribute("user") Login user){
    System.out.println(user);
    return this.validateLogin(user) ? "home/home" : "login/login";
  }

  public boolean validateLogin(Login user){
    return true;
  }

}