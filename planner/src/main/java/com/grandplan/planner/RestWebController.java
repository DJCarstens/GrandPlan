package com.grandplan.planner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grandplan.util.Event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.grandplan.util.User;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.grandplan.client.services.ClientLoginService;
import com.grandplan.server.services.ApiEventService;
import com.grandplan.server.services.ApiLoginService;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/event")
public class RestWebController {
  private Set<Event> events;
  @Autowired
  private ApiLoginService loginService;

  @Autowired
  private ClientLoginService clientLoginService;

  @Autowired
  private ApiEventService apiEventService;

  @GetMapping(value = "/all")
  public String mapCurrentUserEvents() {
    User user = clientLoginService.getCurrentUser();
    events = apiEventService.getUserEvents(user.getEmail());
    String eventsStr = events.toString();
        try {
            ObjectMapper mapper = new ObjectMapper();
            eventsStr =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return eventsStr;
  }

  @GetMapping(value = "/userlist")
    public List<String> userlist(HttpServletRequest request) {
        //TODO : remove the user creating the event from the prompt list
        List<String> userPrompts = new ArrayList<>();
        List<User> allUsers = loginService.getUsers();
        for (User user : allUsers) {
            userPrompts.add(user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        }
        return userPrompts;
    }

    @PostMapping("/addUser")
    public void addUser(@RequestBody JSONObject user) {
        String userEmail = user.get("username").toString().split("[\\(\\)]")[1];
        // TODO: add to list of users that will be invited to the event when the event is created
    }

    @PostMapping("/deleteUser")
    public void deleteUser(@RequestBody JSONObject user) {
        //Gets the user's email
        String userEmail = user.get("username").toString().split("[\\(\\)]")[1];
        // TODO: remove from list of users that will be invited to the event when the event is created
    }
}
