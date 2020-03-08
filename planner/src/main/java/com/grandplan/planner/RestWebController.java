package com.grandplan.planner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grandplan.util.Event;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.grandplan.util.User;
import org.springframework.beans.factory.annotation.Autowired;
import com.grandplan.server.services.ApiLoginService;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/event")
public class RestWebController {
  private List<Event> events;
  @Autowired
  private ApiLoginService loginService;

  @GetMapping(value = "/all")
  public String mapCurrentUserEvents() {
    String jsonEvents = null;
        try {
            events = new ArrayList<Event>();

            Event event1 = Event.builder().title("first event")
                                  .start("2020-02-02")
                                  .end("")
                                  .allDay(true)
                                  .color("blue")
                                  .type("work")
                                  .description("This is a very long description. It is purely to test the functionality of the modal and how it will cope with more information. So I will keep talking about stuff that is completely unnecessary and irrelevant. We shall see how this goes.")
                                  .build();
            events.add(event1);

            Event event2 = Event.builder().title("second event")
                                  .start("2020-03-02")
                                  .end("2020-03-06")
                                  .allDay(true)
                                  .color("#ddd")
                                  .type("Personal")
                                  .description("Some relevant description")
                                  .build();
            events.add(event2);

            Event event3 = Event.builder().title("third event: call")
                                  .start("2020-02-29T11:00")
                                  .end("2020-02-29T12:00")
                                  .allDay(false)
                                  .color("")
                                  .type("Grad")
                                  .description("Discussing project")
                                  .build();
            events.add(event3);

            ObjectMapper mapper = new ObjectMapper();
            jsonEvents =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        }
        return jsonEvents;
  }

  @GetMapping(value = "/userlist")
    public List<String> userlist(HttpServletRequest request) {
        // return productService.search(request.getParameter("term"));
        // @Transactional
        // @Service("productService")
        // public class ProductServiceImpl implements ProductService {

        //     @Autowired
        //     private ProductRepository productRepository;

        //     @Override
        //     public List<String> search(String keyword) {
        //         return productRepository.search(keyword);
        //     }

        // }
        System.out.println("GETTING USERS");
        List<String> userPrompts = new ArrayList<>();
        List<User> allUsers = loginService.getUsers();
        for (User user : allUsers) {
            userPrompts.add(user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
            System.out.println(user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        }
        return userPrompts;
    }
}

// http://learningprogramming.net/java/spring-mvc/autocomplete-with-spring-data-jpa-in-spring-mvc//
// https://frontbackend.com/thymeleaf/spring-boot-bootstrap-thymeleaf-autocomplete
// https://stackoverflow.com/questions/46760315/spring-boot-autocomplete-ajax