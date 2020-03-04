package com.grandplan.server.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grandplan.server.repositories.UserRepo;
import com.grandplan.util.User;
import lombok.RequiredArgsConstructor;
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
public class ApiLoginService {

    @Autowired
    private UserRepo userRepo;

    public User validateUserCredentials(User user) {
        User fetchedUser = userRepo.getUserByEmail(user.getEmail());
        if (fetchedUser == null || !fetchedUser.getPassword().equals(user.getPassword())) {
            return null;
        } else {
            return fetchedUser;
        }
    }

    public User save(User user) {
        User response = userRepo.save(user);
        writeListOfUsers(getUsers()); //overwrites current list of users in Users.json
        return response;
    }

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    private void writeListOfUsers(List<User> users) {
        ObjectMapper mapper = new ObjectMapper();
        JSONArray jsonObjects = new JSONArray();
        for (Object object : users) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", ((User) object).getEmail());
            jsonObject.put("password", ((User) object).getPassword());
            jsonObject.put("firstName", ((User) object).getFirstName());
            jsonObject.put("lastName", ((User) object).getLastName());
            jsonObject.put("phone", ((User) object).getPhone());
            jsonObjects.add(jsonObject);
        }
        try {
            FileWriter file = new FileWriter("src/main/resources/data/Users.json", false);//false indicates that Users.json will get overridden with the current user data
            String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObjects);
            file.write(indented);
            file.write(String.valueOf(new User("test@bbd.com", "", "", "", "")));
            System.out.println("JSON file updated: " + jsonObjects);
            file.close();
        } catch (IOException e) {
            System.out.println("Unable to write Users to Users.json");
            e.printStackTrace();
        }
    }
}