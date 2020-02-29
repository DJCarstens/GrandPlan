package com.grandplan.planner;

import com.grandplan.planner.models.Login;
import com.grandplan.planner.models.User;
import com.grandplan.planner.services.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GrandPlanController {
  public Model mainModel;
  private User currentUser;

  @Autowired
  private LoginService loginService;

  @GetMapping("/login")
  public String login(Model model) {
    model.addAttribute("user", new Login());
    return "login";
  }

  @GetMapping("/signup")
  public String signup(Model model) {
    model.addAttribute("user", new User());
    return "signup";
  }

  @RequestMapping(value = "/validateLogin", method = RequestMethod.POST)
  public String validate(@ModelAttribute("user") Login user, BindingResult bindingResult, Model model){
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

  @GetMapping("/")
  public String home(Model model) {
    //Temporary user assignment until the login has been completed
    if (currentUser == null) {
      currentUser = new User();
      currentUser.setFirstName("Testy McTestface");

    }
    model.addAttribute("user", currentUser);
    //
    return "home";
  }

  @GetMapping("/events")
  public String events(Model model) {
    //Temporary user assignment until the login has been completed
    if (currentUser == null) {
      currentUser = new User();
      currentUser.setFirstName("Testy McTestface");

    }
    model.addAttribute("user", currentUser);
    //
    return "events";
  }

  @RequestMapping(value = "/validateSignup", method = RequestMethod.POST)
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

  @GetMapping("/error")
  public String error(Model model) {
    return "error";
  }

}