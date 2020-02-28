package com.grandplan.planner.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    model.addAttribute("events", mapEvents());
    return "home";
  }

  
  private String mapEvents() {
    String jsonEvents = null;
        try {
            EventTemp event = new EventTemp();
            event.setTitle("first event");
            event.setStart("2020-02-02");
            events.add(event);

            event = new EventTemp();
            event.setTitle("second event");
            event.setStart("2020-03-02");
            event.setEnd("2020-03-06");

            ObjectMapper mapper = new ObjectMapper();
            jsonEvents =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        }
        return jsonEvents;

  }

}