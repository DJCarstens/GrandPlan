package com.grandplan.server;

import com.grandplan.client.services.ClientLoginService;
import com.grandplan.server.services.ApiEventService;
import com.grandplan.server.services.ApiLoginService;
import com.grandplan.server.services.ApiInviteService;
import com.grandplan.util.Event;
import com.grandplan.util.User;
import com.grandplan.util.Invite;
import com.grandplan.util.NewInvite;
import com.grandplan.util.UserEventQuery;

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

    @Autowired
    private ClientLoginService clientLoginService;

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

    @PostMapping("/getUserByEmail")
    public ResponseEntity<User> getUserByEmail(@RequestBody User userStatus){
        return ResponseEntity.ok(apiLoginService.getUserByEmail(userStatus.getEmail()));
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

    @PostMapping("/getEventById")
    public ResponseEntity<Event> getEventById(@RequestBody Event event) {
        return ResponseEntity.ok(apiEventService.getEventById(event.getId()));
    }

    @PostMapping("/createEvent")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
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
    
    @PostMapping("/acceptInvite")
    public ResponseEntity<Invite> acceptInvite(@RequestBody Invite invite){
        return ResponseEntity.ok(apiInviteService.updateInvite(invite.getId(), true));
    }

    @PostMapping("/declineInvite")
    public ResponseEntity<Boolean> declineInvite(@RequestBody Invite invite){
        return ResponseEntity.ok(apiInviteService.deleteInvite(invite.getId()));
    }

    @PostMapping("/createInvite")
    public ResponseEntity<Invite> createInvite(@RequestBody NewInvite newInvite){
        Event eventObj = apiEventService.getEventById(newInvite.getEventId());
        User userObj = apiLoginService.getUserByEmail(newInvite.getUserEmail());

        if (eventObj == null || userObj == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Boolean isHost = eventObj.getHostUsername().equals(userObj.getEmail());
        Invite invite = Invite.builder()
            .user(userObj)
            .event(eventObj)
            .accepted(isHost)
            .build();

        return ResponseEntity.ok(apiInviteService.createInvite(invite));
    }

    @PostMapping("/getUserInvites")
    public ResponseEntity<Set<Invite>> getUserInvites(@RequestBody User user){
        return ResponseEntity.ok(apiInviteService.getUserInvites(user.getEmail()));
    }

    @PostMapping("/getEventInvites")
    public ResponseEntity<Set<Invite>> getEventInvites(@RequestBody Event event){
        return ResponseEntity.ok(apiInviteService.getEventInvites(event.getId()));
    }

    @PostMapping("/getEventByInviteId")
    public ResponseEntity<Event> getEventByInviteId(@RequestBody Invite invite) {
        return ResponseEntity.ok(apiEventService.getEventByInviteId(invite.getId()));
    }

    @PostMapping("/getInviteByUserAndEvent")
    public ResponseEntity<Invite> getInviteByUserAndEvent(@RequestBody UserEventQuery userEventQuery){
        return ResponseEntity.ok(apiInviteService.getUserEventInvite(userEventQuery.getEmail(), Long.parseLong(userEventQuery.getEventId())));
    }

    @PostMapping("/deleteInvite")
    public ResponseEntity<Boolean> deleteInvite(@RequestBody Invite invite){
        return ResponseEntity.ok(apiInviteService.deleteInvite(invite.getId()));
    }

    @PostMapping("/getUnacceptedUserInvites")
    public ResponseEntity<Set<Invite>> getUnacceptedUserInvites(@RequestBody User user){
        return ResponseEntity.ok(apiInviteService.getUnacceptedUserInvites(user.getEmail()));
    }

    @PostMapping("/getAcceptedUserInvites")
    public ResponseEntity<Set<Invite>> getAcceptedUserInvites(@RequestBody User user){
        return ResponseEntity.ok(apiInviteService.getAcceptedUserInvites(user.getEmail()));
    }

    @PostMapping("/getUnacceptedEventInvites")
    public ResponseEntity<Set<Invite>> getUnacceptedEventInvites(@RequestBody Event event){
        return ResponseEntity.ok(apiInviteService.getUnacceptedEventInvites(event.getId()));
    }

    @PostMapping("/getAcceptedEventInvites")
    public ResponseEntity<Set<Invite>> getAcceptedEventInvites(@RequestBody Event event){
        return ResponseEntity.ok(apiInviteService.getAcceptedEventInvites(event.getId()));
    }

    @GetMapping("/allUsersList")
    public ResponseEntity<List<User>> getAllUsersList() {
        System.out.println("GETTING USERS");
        return ResponseEntity.ok(apiLoginService.getUsers());
    }

    @PostMapping("/addUserToEvent")
    public ResponseEntity<String> addUserToEvent(@RequestBody JSONObject user) {
        String userEmail = user.get("username").toString().split("[\\(\\)]")[1];
        return ResponseEntity.ok("{\"msg\":\"success\", \"email\":\"" + userEmail + "\"}");
    }

    @PostMapping("/deleteUserFromEvent")
    public ResponseEntity<String> deleteUserFromEvent(@RequestBody JSONObject user) {
        String userEmail = user.get("username").toString().split("[\\(\\)]")[1];
        return ResponseEntity.ok("{\"msg\":\"success\", \"email\":\"" + userEmail + "\"}");
    }

    @GetMapping("/getCurrentUserEvents")
    public ResponseEntity<Set<Event>> getCurrentUserEvents() {
        return ResponseEntity.ok(apiEventService.getUserEvents(clientLoginService.getCurrentUser().getEmail()));
    }

    @PostMapping("/getUserEventsByEmail")
    public ResponseEntity<Set<Event>> getUserEventsByEmail(@RequestBody JSONObject data) {
        return ResponseEntity.ok(apiEventService.getUserEvents(data.get("email").toString()));
    }
}
