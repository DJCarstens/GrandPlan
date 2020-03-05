package com.grandplan.server;


import com.grandplan.server.repositories.EventRepo;
import com.grandplan.server.repositories.InviteRepo;
import com.grandplan.server.repositories.UserRepo;
import com.grandplan.util.Event;
import com.grandplan.util.Invite;
import com.grandplan.util.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Configuration
@Slf4j
public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(UserRepo userRepo, EventRepo eventRepo) {
        return args -> {
            User user = User.builder()
                    .email("grad@bbd.co.za")
                    .password("password")
                    .firstName("grad")
                    .lastName("McGrad")
                    .phone("911")
                    .invites(new HashSet<>())
                    .build();

            Event event = Event.builder()
                    .eventName("Test event")
                    .invites(new HashSet<>())
                    .build();

            log.info("Preloading " + userRepo.save(user));
            log.info("Preloading " + eventRepo.save(event));
        };
    }

}
