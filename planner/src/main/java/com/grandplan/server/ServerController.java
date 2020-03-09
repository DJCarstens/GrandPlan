package com.grandplan.server;

import com.grandplan.server.services.ApiEventService;
import com.grandplan.server.services.ApiLoginService;
import com.grandplan.server.services.ApiInviteService;
import com.grandplan.util.Event;
import com.grandplan.util.User;
import com.grandplan.util.Invite;
import com.grandplan.client.util.NewInvite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;


import org.springframework.boot.CommandLineRunner;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api")  //prevents conflict with client mapping (a.k.a. grandPlanController)
public class ServerController {

    @Autowired
    private ApiLoginService apiLoginService;

    @Autowired
    private ApiEventService apiEventService;

    @Autowired
    private ApiInviteService apiInviteService;

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
    public ResponseEntity<Set<Event>> getUserEvents(String email) {
        return ResponseEntity.ok(apiEventService.getUserEvents(email));
    }

    @PostMapping("/createEvent")
    public ResponseEntity<Event> createEvent(Event event) {
        return ResponseEntity.ok(apiEventService.createEvent(event));
    }

    @PostMapping("/deleteEvent")
    public ResponseEntity<Boolean> deleteEvent(Event event) {
        return ResponseEntity.ok(apiEventService.deleteEvent(event));
    }

    @PostMapping("/updateEvent")
    public ResponseEntity<Event> updateEvent(Event event) {
        return ResponseEntity.ok(apiEventService.updateEvent(event));
    }

    @PostMapping("/acceptInvite")
    public ResponseEntity<Invite> acceptInvite(Invite invite)
    {
        return ResponseEntity.ok(apiInviteService.updateInvite(invite, true));
    }

    @PostMapping("/declineInvite")
    public ResponseEntity<Invite> declineInvite(Invite invite)
    {
        return ResponseEntity.ok(apiInviteService.updateInvite(invite, false));
    }

    @PostMapping("/createInvite")
    public ResponseEntity<Invite> createInvite(@RequestBody NewInvite newInvite)
    {
        log.info("eventId: " + newInvite.getEventId());
        log.info("userEmail: " + newInvite.getUserEmail());
        Event eventObj = apiEventService.getEventById(newInvite.getEventId());
        User userObj = apiLoginService.getUserByEmail(newInvite.getUserEmail());
        Invite inv = Invite.builder()
        .user(userObj)
        .event(eventObj)
        .accepted(newInvite.getIsAccepted())
        .build();

      

        return ResponseEntity.ok(apiInviteService.createInvite(inv));
    }

    @PostMapping("/getUserInvites")
    public ResponseEntity<Set<Invite>> getUserInvites(String email)
    {
        
        log.info("userEmail: " + email);
       

        return ResponseEntity.ok(apiInviteService.getUserInvites(email));
    }

    @GetMapping("/listInvites")
    public ResponseEntity<List<Invite>> listInvites()
    {

        return ResponseEntity.ok(apiInviteService.getInvites());
    } 

    @PostMapping("/deleteInvite")
    public ResponseEntity<Boolean> deleteInvite(@RequestBody Invite inv)
    {      

        return ResponseEntity.ok(apiInviteService.deleteInvite(inv));
    }


    
}
