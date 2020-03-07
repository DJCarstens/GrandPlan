package com.grandplan.client;

import com.grandplan.server.services.ApiLoginService;
import com.grandplan.util.Event;
import com.grandplan.util.User;
import com.grandplan.client.services.ClientLoginService;
import com.grandplan.client.util.LoginUser;
import com.grandplan.client.util.SignupUser;
import com.grandplan.client.util.NewEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

import java.util.Calendar;
import java.util.List;

@Controller
public class GrandPlanController {
    private User currentUser;
    private List<Event> events;
    private List<Event> invites;
    private String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    private static final String LOGIN = "login";
    private static final String SIGNUP = "signup";
    private static final String HOME = "home";

    @Autowired
    private ClientLoginService clientLoginService;

    @Autowired
    private ApiLoginService loginService;

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

    @PostMapping(value = "/validateLogin")
    public String validateLogin(@Valid @ModelAttribute("loginUser") LoginUser loginUser, BindingResult bindingResult, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return LOGIN;
        }

        int responseCode = clientLoginService.sendHttpRequest(loginUser);
        if(responseCode == 404){
            showModal(model, "Your account was not found. Please check your login details and try again, or signup if you do not have an account.", SIGNUP);
            return LOGIN;
        }

        if(responseCode == 200){
            User user = loginUser.convertUser();
            model.addAttribute("user", user);
            return HOME;
        }

        showModal(model, "Something went wrong with the login. Please try again.", LOGIN);
        return LOGIN;
    }

    @PostMapping(value = "/validateSignup")
    public String validateSignup(@Valid @ModelAttribute("signupUser") SignupUser signupUser, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return SIGNUP;
        }

        if (!signupUser.getPassword().equals(signupUser.getConfirmPassword())) {
            model.addAttribute("matchingPasswordError", "The passwords don't match");
            return SIGNUP;
        }

        User user = signupUser.convertUser();
        //TODO Check that if user exists when creating user instead of doing it here
        if (loginService.validateUserCredentials(user) != null) {
            showModal(model, "An account for " + signupUser.getEmail() + ". Please check your signup details and try again, or login if you have an account.", LOGIN);
            return SIGNUP;
        }

        //TODO Create user and save details before navigating (backend functionality)
        model.addAttribute("user", user);
        return HOME;
    }

    public void showModal(Model model, String message, String button) {
        model.addAttribute("messageModal", message);
        model.addAttribute("button", button);
    }

    @GetMapping("/")
    public String home(Model model){
        return HOME;
    }

    @GetMapping("/events")
    public String events(Model model)
    {
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
        currentUser = new User();
        currentUser.setEmail("g@bbd.co.za");
        currentUser.setFirstName("Grad");
        currentUser.setLastName("Person");
        currentUser.setPassword("Password");
        currentUser.setPhone("0718831926");

        model.addAttribute("user", currentUser);
        model.addAttribute("heading", months[Calendar.getInstance().get(Calendar.MONTH)] + " " + Calendar.getInstance().get(Calendar.YEAR));
        model.addAttribute("events", events);
        return "events";
  }

  @GetMapping("/invites")
    public String invites(Model model){
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
        return "invites";
    }

    @GetMapping("/error")
    public String error(Model model) {
        return "error";
    }

  @PostMapping(value="/createEvent")
  public String createEvent(@RequestBody NewEvent newEvent, Model model) {
    //TODO create event based on info passed
    System.out.println(model);
    showModal(model, "Event Successfully created.", "Ok");
    

    //Temporary user assignment until the login has been completed
    currentUser = new User();
    currentUser.setEmail("g@bbd.co.za");
    currentUser.setFirstName("Grad");
    currentUser.setLastName("Person");
    currentUser.setPassword("Password");
    currentUser.setPhone("0718831926");

    model.addAttribute("user", currentUser);

    //TODO add NewEvent modal to events to update events

    return "events";
  }

}