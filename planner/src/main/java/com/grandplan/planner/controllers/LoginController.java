package com.grandplan.planner.controllers;

import com.grandplan.planner.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
  public String validate(User user, Model model){
    return this.validateLogin(user) ? "home/home" : "login/login";
  }

  public boolean validateLogin(User user){
    return true;
  }

}