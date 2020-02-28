package com.grandplan.planner.controllers;

import java.util.ArrayList;
import java.util.List;

import com.grandplan.planner.models.EventTemp;
import com.grandplan.planner.models.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  private User user;

  private List<EventTemp> events = new ArrayList<EventTemp>();

  @GetMapping("/")
  public String home(Model model) {
    user = new User();
    user.setUsername("Testy McTestface");
    model.addAttribute("username", user.getUsername());
    return "home";
  }

  @GetMapping("/error")
  public String error(Model model) {
    return "error";
  }
}