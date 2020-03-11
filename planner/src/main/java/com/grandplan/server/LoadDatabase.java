package com.grandplan.server;

import com.grandplan.server.repositories.EventRepo;
import com.grandplan.server.repositories.InviteRepo;
import com.grandplan.server.repositories.UserRepo;
import com.grandplan.util.Event;
import com.grandplan.util.Invite;
import com.grandplan.util.User;
import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.io.FileReader;

@Configuration
@Slf4j
public class LoadDatabase {
    private static final String PRELOADING = "Preloading ";
    @Bean
    CommandLineRunner initDatabase(UserRepo userRepo, EventRepo eventRepo, InviteRepo inviteRepo) {
        return args -> {
            List<User> users = getListOfUsers();
            for (User user : users) {
                log.info(PRELOADING + userRepo.save(user));
            }
            List<Event> events = getListOfEvents();
            for (Event event : events) {
                log.info(PRELOADING + eventRepo.save(event));
            }
            // List<Invite> invites = getListOfInvites();
            // for (Invite invite : invites) {
            //     inviteRepo.save(invite);
            //     log.info(PRELOADING + invite.toString());
            // }
        };
    }
        private List<Event> getListOfEvents() {
            JSONParser parser = new JSONParser();
            List<Event> events = new ArrayList<>();
            try {
                Object obj = parser.parse(new FileReader("src/main/resources/data/Events.json"));
                JSONArray jsonObjects = (JSONArray) obj;
                jsonObjects.forEach(item -> {
                    JSONObject jsonObject = (JSONObject) item;
                    Event event = Event.builder()
                            .id(Long.valueOf((jsonObject.get("id")).toString()))
                            .title(jsonObject.get("title").toString())
                            .start(jsonObject.get("start").toString())
                            .end(jsonObject.get("end").toString())
                            .allDay((Boolean) (jsonObject.get("allDay")))
                            .color(jsonObject.get("color").toString())
                            .tag(jsonObject.get("tag").toString())
                            .description(jsonObject.get("description").toString())
                            .hostUsername(jsonObject.get("hostUsername").toString())
                            .invites(new HashSet<>())
                            .build();
                    events.add(event);
                });
            } catch (Exception e) {
                log.info("Unable to load events to repository");
                e.printStackTrace();
            }
            return events;
    }

    private List<User> getListOfUsers() {
        JSONParser parser = new JSONParser();
        List<User> users = new ArrayList<>();
        try {
            Object obj = parser.parse(new FileReader("src/main/resources/data/Users.json"));
            JSONArray jsonObjects = (JSONArray) obj;
            jsonObjects.forEach(item -> {
                JSONObject jsonObject = (JSONObject) item;
                User user = User.builder()
                        .id(Long.valueOf((jsonObject.get("id")).toString()))
                        .email(jsonObject.get("email").toString())
                        .password(jsonObject.get("password").toString())
                        .firstName(jsonObject.get("firstName").toString())
                        .lastName(jsonObject.get("lastName").toString())
                        .phone(jsonObject.get("phone").toString())
                        .invites(new HashSet<>())
                        .build();
                users.add(user);
            });
        } catch (Exception e) {
           log.info("Unable to load Users to repository");
            e.printStackTrace();
        }
        return users;
    }

    // private List<Invite> getListOfInvites() {
    //     JSONParser parser = new JSONParser();
    //     List<Invite> invites = new ArrayList<>();
    //     try {
    //         Object obj = parser.parse(new FileReader("src/main/resources/data/Invites.json"));
    //         JSONArray jsonObjects = (JSONArray) obj;

    //         jsonObjects.forEach(item -> {
    //             JSONObject jsonObject = (JSONObject) item;
    //             Invite invite = Invite.builder()
    //                     .event(getEventById(Long.valueOf((jsonObject.get("eventId")).toString())))
    //                     .user(getUserById(Long.valueOf((jsonObject.get("userId")).toString())))
    //                     .id(Long.valueOf((jsonObject.get("inviteId")).toString()))
    //                     .accepted((Boolean) (jsonObject.get("accepted")))
    //                     .build();
    //             invites.add(invite);
    //         });
    //     } catch (Exception e) {
    //         log.info("Unable to load Invites to repository");
    //         e.printStackTrace();
    //     }
    //     return invites;
    // }

    // private Event getEventById(Long eventId){
    //     List<Event> events = getListOfEvents();
    //     for (Event event : events) {
    //         if(eventId.equals(event.getId()))
    //             return event;
    //     }
    //    return null;
    // }

    // private User getUserById(Long userId){
    //     List<User> users = getListOfUsers();
    //     for (User user : users) {
    //         if(userId.equals(user.getId()))
    //             return user;
    //     }
    //     return null;
    // }
}