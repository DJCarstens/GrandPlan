package com.grandplan.server;

import com.grandplan.server.services.ApiEventService;
import com.grandplan.server.services.ApiLoginService;
import com.grandplan.server.services.ApiInviteService;
import com.grandplan.util.Event;
import com.grandplan.util.User;
import com.grandplan.util.Invite;
import com.grandplan.client.util.NewInvite;
import com.grandplan.client.util.UserEventQuery;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

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
        if (user != null){
            User validUser = apiLoginService.validateUserCredentials(user);
            if(validUser != null){
                return ResponseEntity.ok(validUser);
            }      
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("x-error-code", "Username and password combination does not match");
        return new ResponseEntity<>(httpHeaders, HttpStatus.NOT_FOUND);
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
    public ResponseEntity<Set<Event>> getUserEvents(@RequestBody User user) {
        return ResponseEntity.ok(apiEventService.getUserEvents(user.getEmail()));
    }

    @PostMapping("/createEvent")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        System.out.println("EVENT CREATING HERE");
        System.out.println(event);
        return ResponseEntity.ok(apiEventService.createEvent(event));
    }

    @PostMapping("/deleteEvent")
    public ResponseEntity<Boolean> deleteEvent(@RequestBody Event event) {
        return ResponseEntity.ok(apiEventService.deleteEvent(event.getId()));
    }

    @PostMapping("/updateEvent")
    public ResponseEntity<Event> updateEvent(@RequestBody Event event) {
        return ResponseEntity.ok(apiEventService.updateEvent(event));
    }

    // ----------------- INVITES --------------------
    @PostMapping("/acceptInvite")
    public ResponseEntity<Invite> acceptInvite(@RequestBody Invite invite){
        log.info("Invite id: " + invite.getId());
        return ResponseEntity.ok(apiInviteService.updateInvite(invite, true));
    }

    @PostMapping("/declineInvite")
    public ResponseEntity<Boolean> declineInvite(@RequestBody Invite invite){
        return ResponseEntity.ok(apiInviteService.deleteInvite(invite));
    }

    @PostMapping("/createInvite")
    public ResponseEntity<Invite> createInvite(@RequestBody NewInvite newInvite){
        Event eventObj = apiEventService.getEventById(newInvite.getEventId());
        User userObj = apiLoginService.getUserByEmail(newInvite.getUserEmail());

        if (eventObj == null || userObj == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Boolean isHost = eventObj.getHostUsername().equals(userObj.getEmail());
        Invite inv = Invite.builder()
                .user(userObj)
                .event(eventObj)
                .accepted(isHost)
                .build();

        return ResponseEntity.ok(apiInviteService.createInvite(inv));
    }

    @PostMapping("/getUserInvites")
    public ResponseEntity<Set<Invite>> getUserInvites(@RequestBody User user){
        return ResponseEntity.ok(apiInviteService.getUserInvites(user.getEmail()));
    }

    @PostMapping("/getEventInvites")
    public ResponseEntity<Set<Invite>> getEventInvites(@RequestBody Event e){
        return ResponseEntity.ok(apiInviteService.getEventInvites(e.getId()));
    }

    @PostMapping("/getInviteByUserAndEvent")
    public ResponseEntity<Invite> getInviteByUserAndEvent(@RequestBody UserEventQuery ue){
        return ResponseEntity.ok(apiInviteService.getUserEventInvite(ue.getUser().getEmail(), ue.getEvent().getId()));
    }

    @GetMapping("/listInvites")
    public ResponseEntity<List<Invite>> listInvites(){
        return ResponseEntity.ok(apiInviteService.getInvites());
    } 

    @PostMapping("/deleteInvite")
    public ResponseEntity<Boolean> deleteInvite(@RequestBody Invite inv){      
        return ResponseEntity.ok(apiInviteService.deleteInvite(inv));
    }

    @PostMapping("/getUnacceptedUserInvites")
    public ResponseEntity<Set<Invite>> getUnacceptedUserInvites(@RequestBody User u){
        return ResponseEntity.ok(apiInviteService.getUnacceptedUserInvites(u.getEmail()));
    }

    @PostMapping("/getAcceptedUserInvites")
    public ResponseEntity<Set<Invite>> getAcceptedUserInvites(@RequestBody User u){
        return ResponseEntity.ok(apiInviteService.getAcceptedUserInvites(u.getEmail()));
    }

    @PostMapping("/getUnacceptedEventInvites")
    public ResponseEntity<Set<Invite>> getUnacceptedEventInvites(@RequestBody Event e){
        return ResponseEntity.ok(apiInviteService.getUnacceptedEventInvites(e.getId()));
    }

    @PostMapping("/getAcceptedEventInvites")
    public ResponseEntity<Set<Invite>> getAcceptedEventInvites(@RequestBody Event e){
        return ResponseEntity.ok(apiInviteService.getAcceptedEventInvites(e.getId()));
    }

}