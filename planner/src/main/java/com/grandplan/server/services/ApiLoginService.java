package com.grandplan.server.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grandplan.server.repositories.UserRepo;
import com.grandplan.util.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Component
@Slf4j
public class ApiLoginService {

    @Autowired
    private UserRepo userRepo;

    public User validateUserCredentials(User user) {
        User fetchedUser = userRepo.getUserByEmail(user.getEmail());
        if (fetchedUser == null || !fetchedUser.getPassword().equals(user.getPassword())) {
            return null;
        }
            return fetchedUser;
    }

    public User save(User user) {
        userRepo.save(user);
        writeListOfUsers(getUsers()); //overwrites current list of users in Users.json
        return user;
    }

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public User getUser(User user) {
        return userRepo.getUserByEmail(user.getEmail());
    }

    public User getUserByEmail(String email)
    {
        return userRepo.getUserByEmail(email);
    }

    private void writeListOfUsers(List<User> users) {
        ObjectMapper mapper = new ObjectMapper();
        JSONArray jsonObjects = new JSONArray();
        for (User user : users) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", user.getId());
            jsonObject.put("email", user.getEmail());
            jsonObject.put("password", user.getPassword());
            jsonObject.put("firstName", user.getFirstName());
            jsonObject.put("lastName", user.getLastName());
            jsonObject.put("phone", user.getPhone());
            jsonObjects.add(jsonObject);
        }
        try (FileWriter file = new FileWriter("src/main/resources/data/Users.json", false)) {
            //false indicates that Users.json will get overridden with the current user data
            String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObjects);
            file.write(indented);
            log.info("JSON file updated: " + jsonObjects);
        } catch (IOException e) {
            log.info("Unable to write Users to Users.json");
            log.error(e.getMessage());
        }
    }
}