package com.grandplan.server;

import com.grandplan.server.repositories.EventRepo;
import com.grandplan.server.repositories.UserRepo;
import com.grandplan.util.Event;
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
    @Bean
    CommandLineRunner initDatabase(UserRepo userRepo, EventRepo eventRepo) {
        return args -> {

            List<User> users = getListOfUsers();
            for (User user : users) {
                log.info("Preloading " + userRepo.save(user));
            }
            List<Event> events = getListOfEvents();
            for (Event e : events) {
                log.info("Preloading " + eventRepo.save(e));
            }
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
                            .id((Long)jsonObject.get("id"))
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
                        .id((long)jsonObject.get("id"))
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
}