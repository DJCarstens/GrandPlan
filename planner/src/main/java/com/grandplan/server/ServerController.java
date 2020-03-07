package com.grandplan.server;

import com.grandplan.server.services.ApiLoginService;
import com.grandplan.server.services.ApiEventService;
import com.grandplan.util.Event;
import com.grandplan.util.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import java.util.List;

@RestController
@RequestMapping("/api")  //prevents conflict with client mapping (a.k.a. grandPlanController)
public class ServerController {

    @Autowired
    private ApiLoginService apiLoginService;

    @Autowired
    private ApiEventService apiEventService;

    @PostMapping("/validateLogin")
    public ResponseEntity<User> validate(@RequestBody User user) {
        if (user != null && apiLoginService.validateUserCredentials(user) != null) {
            return ResponseEntity.ok(user);
        } else {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("x-error-code", "Username and password combination does not match");
            return new ResponseEntity<>(httpHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listUsers") //view users that are currently stored in the repository
    public ResponseEntity<List<User>> list() {
        return ResponseEntity.ok(apiLoginService.getUsers());
    }

    @PostMapping("/addUser")
    public void addUser(@RequestBody User user) {
        apiLoginService.save(user);
    }

    @PostMapping("/getUserEvents")
    public Set<Event> getUserEvents(String email) {
        return apiEventService.getUserEvents(email);
    }

    @PostMapping("/createEvent")
    public Event createEvent(Event event) {
        return apiEventService.createEvent(event);
    }

    @PostMapping("/deleteEvent")
    public boolean deleteEvent(Event event) {
        return apiEventService.deleteEvent(event);
    }

}
