package com.grandplan.server;


import com.grandplan.server.repositories.UserRepo;
import com.grandplan.util.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(UserRepo repository) {
        return args -> {
            log.info("Preloading " + repository.save(new User("grad@bbd.com", "password","grad","","0814568825","")));
        };
    }
}
