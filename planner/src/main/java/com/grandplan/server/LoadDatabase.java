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


@Configuration
@Slf4j
public class LoadDatabase {
    @Bean
    CommandLineRunner initUserDatabase(UserRepo repository) {
        return args -> {
            User user = new User().builder()
                    .email("grab@bbd.co.za")
                    .password("password")
                    .firstName("grad")
                    .lastName("McGrad")
                    .phone("911")
                    .build()

            log.info("Preloading " + repository.save(
                    new User().builder()
                            .email("grab@bbd.co.za")
                            .password("password")
                            .firstName("grad")
                            .lastName("McGrad")
                            .phone("911")
                            .build()
            ));
        };
    }

    @Bean
    CommandLineRunner initEventDatabase(EventRepo repository) {
        return args -> {
            log.info("Preloading " + repository.save(
                    new Event().builder()
                            .hostUsername("grad")
                            .eventName("exampleEvent")
                            .date("20/04/2020")
                            .startTime("4:20")
                            .endTime("16:20")
                            .allDay(false)
                            .build()));
        };
    }

//    @Bean
//    CommandLineRunner initInviteDatabase(InviteRepo repository) {
//        return args -> {
//            log.info("Preloading " + repository.save(
//                    new Invite().builder()
//                            .user(new User().builder()
//                                    .email("grab@bbd.co.za")
//                                    .password("password")
//                                    .firstName("grad")
//                                    .lastName("McGrad")
//                                    .phone("911").build())
//                            .event(new Event().builder()
//                                    .hostUsername("grad")
//                                    .eventName("exampleEvent")
//                                    .date("20/04/2020")
//                                    .startTime("4:20")
//                                    .endTime("16:20")
//                                    .allDay(false)
//                                    .build())
//                            .build()));
//        };
//    }
}
