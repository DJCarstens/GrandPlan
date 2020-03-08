package com.grandplan.server;

import com.grandplan.server.services.ApiEventService;
import com.grandplan.server.services.ApiLoginService;
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

import java.util.List;
import java.util.Set;

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
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(apiLoginService.getUsers());
    }

    @GetMapping("/listEvents")
    public ResponseEntity<List<Event>> listEvents() {
        return ResponseEntity.ok(apiEventService.getEvents());
    }

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        if (apiLoginService.getUser(user) != null) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("x-error-code", "User already exists");
            return new ResponseEntity<>(httpHeaders, HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(apiLoginService.save(user));
    }

    @PostMapping("/getUserEvents")
    public ResponseEntity<Set<Event>> getUserEvents(@RequestBody String email) {
        return ResponseEntity.ok(apiEventService.getUserEvents(email));
    }

    @PostMapping("/createEvent")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(apiEventService.createEvent(event));
    }

    @PostMapping("/deleteEvent")
    public ResponseEntity<Boolean> deleteEvent(@RequestBody Event event) {
        return ResponseEntity.ok(apiEventService.deleteEvent(event));
    }

    @PostMapping("/updateEvent")
    public ResponseEntity<Event> updateEvent(@RequestBody Event event) {
        return ResponseEntity.ok(apiEventService.updateEvent(event));
    }
}
