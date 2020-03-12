package com.grandplan.client;

import com.grandplan.client.services.ClientEventService;
import com.grandplan.client.services.ClientInviteService;
import com.grandplan.client.services.ClientLoginService;
import com.grandplan.util.LoginUser;
import com.grandplan.util.SignupUser;
import com.grandplan.util.NewEvent;
import com.grandplan.util.Constants;

import com.grandplan.util.User;
import com.grandplan.util.Event;
import com.grandplan.util.Invite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Controller
public class GrandPlanController {
    @Autowired
    private ClientLoginService clientLoginService;

    @Autowired
    private ClientEventService clientEventService;

    @Autowired
    private ClientInviteService clientInviteService;


    @GetMapping("/redirect")
    public String redirect(Model model) {
        User user = clientLoginService.getCurrentUser();
        if(user == null){
            model.addAttribute("loginUser", new LoginUser());
            return Constants.LOGIN;
        }
        else{
            model.addAttribute("user", clientLoginService.getCurrentUser());
            return Constants.HOME;
        }
    }

    @GetMapping("/login")
    public String login(Model model) {
        if (clientLoginService.getCurrentUser() != null) {
            model.addAttribute("user", clientLoginService.getCurrentUser());
            return Constants.HOME;
        }
        model.addAttribute("loginUser", new LoginUser());
        return Constants.LOGIN;
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signupUser", new SignupUser());
        return Constants.SIGNUP;
    }

    @GetMapping("/logout")
    public String logout(Model model){
        model.addAttribute("loginUser", new LoginUser());
        return clientLoginService.logout();
    }

    @GetMapping("/")
    public String home(Model model){
        if(clientLoginService.getCurrentUser() == null){
            model.addAttribute("loginUser", new LoginUser());
            return Constants.LOGIN;
        }

        model.addAttribute("user", clientLoginService.getCurrentUser());
        return Constants.HOME;
    }

    @PostMapping(value = "/validateLogin")
    public String validateLogin(@Valid @ModelAttribute("loginUser") LoginUser loginUser, BindingResult bindingResult, Model model) {
        return clientLoginService.validateLogin(loginUser, model, bindingResult);
    }

    @PostMapping(value = "/validateSignup")
    public String validateSignup(@Valid @ModelAttribute("signupUser") SignupUser signupUser, BindingResult bindingResult, Model model) {
        return clientLoginService.validateSignup(signupUser, model, bindingResult);
    }

    @GetMapping("/events")
    public String events(Model model) {
        return clientEventService.getUserEvents(clientLoginService.getCurrentUser(), model);
    }

  @GetMapping("/invites")
    public String invites(Model model){
        return clientInviteService.getInvites(clientLoginService.getCurrentUser(), model);
    }

    @PostMapping("/deleteEvent")
    public String deleteEvent(@ModelAttribute("delete") Event event, Model model){
        return clientEventService.deleteEvent(event, model);
    }

    @PostMapping("/transferEvent")
    public String transferEvent(@ModelAttribute("transfer") Event event, Model model){
        return clientEventService.transferEvent(event, model);
    }

    @PostMapping("/createEvent")
    public String createEvent(@RequestBody NewEvent newEvent, Model model){
        return clientEventService.createEvent(newEvent, model);
    }

    @PostMapping("/acceptInvite")
    public String acceptInvite(@ModelAttribute("accept") Invite acceptInvite, Model model){
        return clientInviteService.acceptInvite(acceptInvite, model);
    }

    @PostMapping("/declineInvite")
    public String declineInvite(@ModelAttribute("decline") Invite declineInvite, Model model){
        return clientInviteService.declineInvite(declineInvite, model);
    }

}