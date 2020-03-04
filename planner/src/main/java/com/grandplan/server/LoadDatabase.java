package com.grandplan.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grandplan.server.repositories.UserRepo;
import com.grandplan.util.User;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContextListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(UserRepo repository) {
        return args -> {
            List<User> users = getListOfUsers();
            for (User user : users) {
                log.info("Preloading " + repository.save(user));
            }
            //users = repository.findAll();
            //writeListOfUsers(users);
        };
    }
    private List<User> getListOfUsers() {

        JSONParser parser = new JSONParser();
        List<User> users = new ArrayList<>();
        try {
            Object obj = parser.parse(new FileReader("src/main/resources/data/Users.json"));
            JSONArray jsonObjects = (JSONArray) obj;
            for (Object object : jsonObjects) {
                User user = new User(
                        (String) ((JSONObject) object).get("email"),
                        (String) ((JSONObject) object).get("password"),
                        (String) ((JSONObject) object).get("firstName"),
                        (String) ((JSONObject) object).get("lastName"),
                        (String) ((JSONObject) object).get("phone"));
                users.add(user);
            }
        } catch (Exception e) {
            System.out.println("Unable to load Users to repository");
            e.printStackTrace();
        }
        return users;
    }


}