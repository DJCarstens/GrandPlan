package com.grandplan.server;

import com.grandplan.server.repositories.UserRepo;
import com.grandplan.util.User;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
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
                       ((JSONObject) object).get("email").toString(),
                        ((JSONObject) object).get("password").toString(),
                        ((JSONObject) object).get("firstName").toString(),
                        ((JSONObject) object).get("lastName").toString(),
                        ((JSONObject) object).get("phone").toString());
                users.add(user);
            }
        } catch (Exception e) {
           log.info("Unable to load Users to repository");
            e.printStackTrace();
        }
        return users;
    }
}