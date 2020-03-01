package com.grandplan.server;

import com.grandplan.util.User;
import com.grandplan.util.UserBuilder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {

    @GetMapping("/getUser")
    public User getUser() {
        return new UserBuilder()
                .firstName("Homey")
                .email("Homey@home.ru")
                .lastName("McHome")
                .build();
    }
}
