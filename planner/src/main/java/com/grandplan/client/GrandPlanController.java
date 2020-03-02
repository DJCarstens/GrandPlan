package com.grandplan.client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.grandplan.util.Event;
import com.grandplan.util.User;
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
  private User currentUser;
  private List<Event> events;
  private List<Event> invites;
  private String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

  @Autowired
  private LoginService loginService;

  @GetMapping("/login")
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
    if(loginService.validateLogin(user, mainModel)){
      //TODO: validate whether user exists or not before navigation
      //If user exists and info matches, navigate to "home" else navigate to "signup"
      mainModel.addAttribute("user", user);
      return "home";
    }
    else{
      return "login";
    }
  }

  @GetMapping("/")
  public String home(Model model) {
    //Temporary user assignment until the login has been completed
    if (currentUser == null) {
      currentUser = new User();
      currentUser.setFirstName("Testy McTestface");

    }
    model.addAttribute("user", currentUser);
    return "home";
  }

  @GetMapping("/events")
  public String events(Model model) {
    //For testing purposes. Need to remove
    events = new ArrayList<Event>();

    Event event2 = new Event();
    event2.setTitle("second event");
    event2.setDate("2020-03-02");
    event2.setStart("10:00");
    event2.setEnd("10:30");
    events.add(event2);

    Event event3 = new Event();
    event3.setTitle("third event: call");
    event3.setDate("2020-02-15");
    event3.setStart("11:00");
    event3.setEnd("12:00");
    events.add(event3);
    events.add(event3);
    events.add(event3);
    events.add(event3);

    //Temporary user assignment until the login has been completed
    if (currentUser == null) {
      currentUser = new User();
      currentUser.setFirstName("Testy McTestface");
    }

    model.addAttribute("user", currentUser);
    model.addAttribute("heading", months[Calendar.getInstance().get(Calendar.MONTH)] + " " + Calendar.getInstance().get(Calendar.YEAR));
    model.addAttribute("events", events);
    //
    return "events";
  }

  @PostMapping(value = "/validateSignup")
  public String validateSignup(@ModelAttribute("user") User user, BindingResult bindingResult, Model model){
    this.mainModel = model;
    if(loginService.validateSignup(user, mainModel)){
      //TODO: validate whether user exists or not before navigation
      //If user exists and info matches, navigate to "login" else create the user and nagivate to "home"
      mainModel.addAttribute("user", user);
      return "home";
    }
    else{
      return "signup";
    }
  }

  @GetMapping("/error")
  public String error(Model model) {
    return "error";
  }

  @GetMapping("/invites")
  public String invites(Model model) {
    invites = new ArrayList<Event>();

    Event event2 = new Event();
    event2.setTitle("second event");
    event2.setDate("2020-03-02");
    event2.setStart("10:00");
    event2.setEnd("10:30");
    invites.add(event2);
    invites.add(event2);

    if (currentUser == null) {
      currentUser = new User();
      currentUser.setFirstName("Testy McTestface");
    }

    model.addAttribute("user", currentUser);
    model.addAttribute("heading", "Your current event invites");
    model.addAttribute("invites", invites);
    return "invites";
  }

}