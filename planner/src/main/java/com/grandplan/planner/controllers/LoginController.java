package com.grandplan.planner.controllers;

import com.grandplan.planner.models.Login;
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
public class LoginController {
  public Model mainModel;

  @Autowired
  private LoginService loginService;

  @GetMapping({"/", "/login"})
  public String login(Model model) {
    model.addAttribute("user", new Login());
    return "login/login";
  }

  @RequestMapping(value = "/validate", method = RequestMethod.POST)
  public String validate(@ModelAttribute("user") Login user, BindingResult bindingResult, Model model){
    this.mainModel = model;
    mainModel.addAttribute("login", user);
    return loginService.validateLogin(user, mainModel) ? "home/home" : "login/login";
  }

}