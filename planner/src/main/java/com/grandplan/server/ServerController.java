package com.grandplan.server;

import com.grandplan.server.services.ApiEventService;
import com.grandplan.server.services.ApiLoginService;
import com.grandplan.server.services.ApiInviteService;
import com.grandplan.util.Event;
import com.grandplan.util.User;
import com.grandplan.util.Invite;
import com.grandplan.client.services.ClientLoginService;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@RestController
@RequestMapping("/api") // prevents conflict with client mapping (a.k.a. grandPlanController)
public class ServerController {

    @Autowired
    private ApiLoginService apiLoginService;

    @Autowired
    private ApiEventService apiEventService;

    @Autowired
    private ApiInviteService apiInviteService;

    @Autowired
    ClientLoginService clientLoginService;

    @PostMapping("/validateLogin")
    public ResponseEntity<User> validate(@RequestBody User user) {
        if (user != null) {
            User validUser = apiLoginService.validateUserCredentials(user);
            if (validUser != null) {
                return ResponseEntity.ok(validUser);
            }
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("x-error-code", "Username and password combination does not match");
        return new ResponseEntity<>(httpHeaders, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/listUsers") // view users that are currently stored in the repository
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
        return ResponseEntity.ok(apiEventService.deleteEvent(event.getId()));
    }

    @PostMapping("/updateEvent")
    public ResponseEntity<Event> updateEvent(@RequestBody Event event) {
        return ResponseEntity.ok(apiEventService.updateEvent(event));
    }

    // ----------------- INVITES --------------------
    @PostMapping("/acceptInvite")
    public ResponseEntity<Invite> acceptInvite(@RequestBody Invite invite) {
        log.info("Invite id: " + invite.getId());
        return ResponseEntity.ok(apiInviteService.updateInvite(invite, true));
    }

    @PostMapping("/declineInvite")
    public ResponseEntity<Boolean> declineInvite(@RequestBody Invite invite) {

        return ResponseEntity.ok(apiInviteService.deleteInvite(invite));
    }

    @PostMapping("/createInvite")
    public ResponseEntity<Invite> createInvite(@RequestBody NewInvite newInvite) {

        Event eventObj = apiEventService.getEventById(newInvite.getEventId());
        User userObj = apiLoginService.getUserByEmail(newInvite.getUserEmail());

        log.info("eventId: " + eventObj.getHostUsername());
        log.info("userEmail: " + userObj.getEmail());

        if (eventObj == null || userObj == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Boolean isHost = eventObj.getHostUsername().equals(userObj.getEmail());
        Invite inv = Invite.builder().user(userObj).event(eventObj).accepted(isHost).build();

        return ResponseEntity.ok(apiInviteService.createInvite(inv));
    }

    @PostMapping("/getUserInvites")
    public ResponseEntity<Set<Invite>> getUserInvites(@RequestBody User u) {
        log.info("userEmail: " + u.getEmail());

        return ResponseEntity.ok(apiInviteService.getUserInvites(u.getEmail()));
    }

    @PostMapping("/getEventInvites")
    public ResponseEntity<Set<Invite>> getEventInvites(@RequestBody Event e) {

        log.info("event Id: " + e.getId());

        return ResponseEntity.ok(apiInviteService.getEventInvites(e.getId()));
    }

    @PostMapping("/getInviteByUserAndEvent")
    public ResponseEntity<Invite> getInviteByUserAndEvent(@RequestBody UserEventQuery ue) {
        log.info("event Id: " + ue.getEvent().getId());
        log.info("user email: " + ue.getUser().getEmail());

        return ResponseEntity.ok(apiInviteService.getUserEventInvite(ue.getUser().getEmail(), ue.getEvent().getId()));
    }

    @GetMapping("/listInvites")
    public ResponseEntity<List<Invite>> listInvites() {
        return ResponseEntity.ok(apiInviteService.getInvites());
    }

    @PostMapping("/deleteInvite")
    public ResponseEntity<Boolean> deleteInvite(@RequestBody Invite inv) {
        return ResponseEntity.ok(apiInviteService.deleteInvite(inv));
    }

    @PostMapping("/getUnacceptedUserInvites")
    public ResponseEntity<Set<Invite>> getUnacceptedUserInvites(@RequestBody User u) {
        return ResponseEntity.ok(apiInviteService.getUnacceptedUserInvites(u.getEmail()));
    }

    @PostMapping("/getAcceptedUserInvites")
    public ResponseEntity<Set<Invite>> getAcceptedUserInvites(@RequestBody User u) {
        return ResponseEntity.ok(apiInviteService.getAcceptedUserInvites(u.getEmail()));
    }

    @PostMapping("/getUnacceptedEventInvites")
    public ResponseEntity<Set<Invite>> getUnacceptedEventInvites(@RequestBody Event e) {
        return ResponseEntity.ok(apiInviteService.getUnacceptedEventInvites(e.getId()));
    }

    @PostMapping("/getAcceptedEventInvites")
    public ResponseEntity<Set<Invite>> getAcceptedEventInvites(@RequestBody Event e) {
        return ResponseEntity.ok(apiInviteService.getAcceptedEventInvites(e.getId()));
    }

    // ----------------- NEW EVENTS --------------------

    @GetMapping("/allUsersList")
    public ResponseEntity<List<String>> getAllUsersList() {
        // TODO : remove the user creating the event from the prompt list
        System.out.println("GETTING USERS");
        User currentUser = clientLoginService.getCurrentUser();
        List<String> userPrompts = new ArrayList<>();
        List<User> allUsers = apiLoginService.getUsers();
        for (User user : allUsers) {
            if (!user.equals(currentUser)) {
                userPrompts.add(user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
                System.out.println(user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
            }
        }
        return ResponseEntity.ok(userPrompts);
    }

    @PostMapping("/addUserToEvent")
    public ResponseEntity<String> addUserToEvent(@RequestBody JSONObject user) {
        System.out.println("ADDING USER:");
        System.out.println(user.get("username"));
        // Gets the user's email
        String userEmail = user.get("username").toString().split("[\\(\\)]")[1];
        System.out.println(userEmail);
        // TODO: add to list of users that will be invited to the event when the event
        // is created
        return ResponseEntity.ok("{\"msg\":\"success\", \"email\":\"" + userEmail + "\"}");
    }

    @PostMapping("/deleteUserFromEvent")
    public ResponseEntity<String> deleteUserFromEvent(@RequestBody JSONObject user) {
        System.out.println("REMOVING USER:");
        System.out.println(user.get("username"));
        // Gets the user's email
        String userEmail = user.get("username").toString().split("[\\(\\)]")[1];
        System.out.println(userEmail);
        // TODO: remove from list of users that will be invited to the event when the
        // event is created
        return ResponseEntity.ok("{\"msg\":\"success\", \"email\":\"" + userEmail + "\"}");
    }

    @PostMapping("/getCurrentUserEvents")
    public ResponseEntity<String> getCurrentUserEvents() {
        String jsonEvents = null;
        try {
            List<Event> events = new ArrayList<>();
            Set<Event> setEvents = apiEventService.getUserEvents(clientLoginService.getCurrentUser().getEmail());
            for (Event event : setEvents) {
                events.add(event);
            }
            ObjectMapper mapper = new ObjectMapper();
            jsonEvents = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(jsonEvents);
    }

    @PostMapping("/getUserEventsByEmail")
    public ResponseEntity<String> getUserEventsByEmail(@RequestBody String email) {
        String jsonEvents = null;
        try {
            List<Event> events = new ArrayList<>();
            Set<Event> setEvents = apiEventService.getUserEvents(email);
            for (Event event : setEvents) {
                events.add(event);
            }
            ObjectMapper mapper = new ObjectMapper();
            jsonEvents = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);
            System.out.println(jsonEvents);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(jsonEvents);
    }

}
