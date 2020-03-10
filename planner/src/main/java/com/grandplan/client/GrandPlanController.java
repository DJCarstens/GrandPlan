package com.grandplan.client;

import com.grandplan.util.User;
import com.grandplan.client.services.ClientEventService;
import com.grandplan.client.services.ClientInviteService;
import com.grandplan.client.services.ClientLoginService;
import com.grandplan.client.util.LoginUser;
import com.grandplan.client.util.SignupUser;
import com.grandplan.client.util.NewEvent;

import org.json.simple.JSONObject;
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
    private static final String LOGIN = "login";
    private static final String SIGNUP = "signup";
    private static final String HOME = "home";
    private static final String EVENTS = "events";

    @Autowired
    private ClientLoginService clientLoginService;

    @Autowired
    private ClientEventService clientEventService;

    @Autowired
    private ClientInviteService clientInviteService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginUser", new LoginUser());
        return LOGIN;
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signupUser", new SignupUser());
        return SIGNUP;
    }

    @GetMapping("/logout")
    public String logout(Model model){
        model.addAttribute("loginUser", new LoginUser());
        return clientLoginService.logout();
    }

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("user", clientLoginService.getCurrentUser());
        return HOME;
    }

    @PostMapping(value = "/validateLogin")
    public String validateLogin(@Valid @ModelAttribute("loginUser") LoginUser loginUser, BindingResult bindingResult, Model model) {
        return clientLoginService.validateLogin(loginUser, model, bindingResult);
    }

    @PostMapping(value = "/validateSignup")
    public String validateSignup(@Valid @ModelAttribute("signupUser") SignupUser signupUser, BindingResult bindingResult, Model model) {
        return clientLoginService.validateSignup(signupUser, model, bindingResult);
    }

    public void showModal(Model model, String message, String button) {
        model.addAttribute("messageModal", message);
        model.addAttribute("button", button);
    }

    @GetMapping("/events")
    public String events(Model model) {
        model.addAttribute("user", clientLoginService.getCurrentUser());
        return clientEventService.getUserEvents(clientLoginService.getCurrentUser(), model);
    }

  @GetMapping("/invites")
    public String invites(Model model){
        model.addAttribute("user", clientLoginService.getCurrentUser());
        return clientInviteService.getInvites(clientLoginService.getCurrentUser(), model);
    }

    @GetMapping("/error")
    public String error(Model model) {
        return "error";
    }

    @PostMapping("/deleteEvent")
    public String deleteEvent(@RequestBody JSONObject event, Model model) throws IOException{
        return clientEventService.deleteEvent(event.get("id").toString(), model);
    public String deleteEvent(@RequestBody JSONObject deleteEvent, Model model){
        return clientEventService.deleteEvent(deleteEvent.get("id").toString(), deleteEvent.get("hostUsername").toString(), model);
    }

    @PostMapping("/transferEvent")
    public String transferEvent(@RequestBody JSONObject tranferEvent, Model model){
        // return clientEventService.transferEvent(tranferEvent.get("id").toString(), tranferEvent.get("hostUsername").toString(), model);
        return EVENTS;
    }

    @PostMapping("/createEvent")
    public String createEvent(@RequestBody NewEvent newEvent, Model model) {
        showModal(model, "Event Successfully created.", "Ok");
        model.addAttribute("user", clientLoginService.getCurrentUser());

        //TODO add NewEvent modal to events to update events

        return EVENTS;
    }

}