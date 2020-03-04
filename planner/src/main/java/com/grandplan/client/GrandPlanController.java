package com.grandplan.client;

import com.grandplan.client.util.LoginUser;
import com.grandplan.client.util.SignupUser;
import com.grandplan.server.services.ApiLoginService;
import com.grandplan.util.Event;
import com.grandplan.util.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
public class GrandPlanController {
    public Model mainModel;
    private User currentUser;
    private List<Event> events;
    private List<Event> invites;
    private String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    @Autowired
    private ApiLoginService loginService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginUser", new LoginUser());
        return "login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signupUser", new SignupUser());
        return "signup";
    }

    @PostMapping(value = "/validateLogin")
    public String validateLogin(@Valid @ModelAttribute("loginUser") LoginUser loginUser, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        User user = loginUser.convertUser();
        if (loginService.validateUserCredentials(user) == null) {
            model.addAttribute("messageModal", "Your account was not found. Please check your login details and try again, or signup if you do not have an account.");
            model.addAttribute("button", "signup");
            return "login";
        } else {
            model.addAttribute("user", user);
            return "home";
        }
    }

    @PostMapping(value = "/validateSignup")
    public String validateSignup(@Valid @ModelAttribute("signupUser") SignupUser signupUser, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        if (!signupUser.getPassword().equals(signupUser.getConfirmPassword())) {
            model.addAttribute("matchingPasswordError", "The passwords don't match");
            return "signup";
        }

        User user = signupUser.convertUser();
        if (loginService.validateUserCredentials(user) == null) {
            model.addAttribute("messageModal", "An account for " + signupUser.getEmail() + ". Please check your signup details and try again, or login if you have an account.");
            model.addAttribute("button", "login");
            return "signup";
        }

        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/")
    public String home(Model model) {
        //Temporary user assignment until the login has been completed
        if (currentUser == null) {
            //currentUser = new User();
            currentUser.setFirstName("Testy McTestface");
        }
        model.addAttribute("user", currentUser);
        return "home";
    }

    @GetMapping("/events")
    public String events(Model model) {
        //For testing purposes. Need to remove
        events = new ArrayList<Event>();

        Event event2 = Event.builder().title("second event")
                .start("2020-03-02T10:00")
                .end("2020-03-02T10:30")
                .allDay(false)
                .color("")
                .type("test")
                .description("")
                .build();
        events.add(event2);

        Event event3 = Event.builder().title("third event: call")
                .start("2020-02-15T11:00")
                .end("2020-02-15T12:00")
                .allDay(false)
                .color("")
                .type("test")
                .description("")
                .build();
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
        return "events";
    }

    @GetMapping("/error")
    public String error(Model model) {
        return "error";
    }

    @GetMapping("/invites")
    public String invites(Model model) {
        invites = new ArrayList<Event>();

        Event event2 = Event.builder().title("second event")
                .start("2020-03-02T10:00")
                .end("2020-03-02T10:30")
                .allDay(false)
                .color("")
                .type("test")
                .description("")
                .build();
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