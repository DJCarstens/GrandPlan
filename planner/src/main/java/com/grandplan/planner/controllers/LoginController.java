package com.grandplan.planner.controllers;

import com.grandplan.planner.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

  @Autowired
  // private LoginService loginService; 

  @GetMapping("/")
  public String login() {
    return "login/login";
  }

  @PostMapping("/validate")
  public String validate(User user){
    System.out.println(user);
    return "login/login";
  }

}