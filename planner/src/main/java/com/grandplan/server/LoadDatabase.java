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
            User testUser = User.builder()
                    .email("grad@bbd.co.za")
                    .password("password")
                    .firstName("grad")
                    .lastName("McGrad")
                    .phone("911")
                    .invites(new HashSet<>())
                    .build();

            Event event = Event.builder()
                    .title("Test event")
                    .invites(new HashSet<>())
                    .build();

            log.info("Preloading " + userRepo.save(testUser));
            log.info("Preloading " + eventRepo.save(event));

            List<User> users = getListOfUsers();
            for (User user : users) {
                log.info("Preloading " + userRepo.save(user));
            }
        };
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