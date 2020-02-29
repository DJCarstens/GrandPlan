package com.grandplan.planner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grandplan.planner.models.Event;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/event")
public class RestWebController {
  private List<Event> events;

  @GetMapping(value = "/all")
  public String mapCurrentUserEvents() {
    String jsonEvents = null;
        try {
            events = new ArrayList<Event>();

            Event event1 = new Event();
            event1.setTitle("first event");
            event1.setStart("2020-02-02");
            events.add(event1);

            Event event2 = new Event();
            event2.setTitle("second event");
            event2.setStart("2020-03-02");
            event2.setEnd("2020-03-06");
            events.add(event2);

            Event event3 = new Event();
            event3.setTitle("third event: call");
            event3.setStart("2020-02-29T11:00");
            event3.setEnd("2020-02-29T12:00");
            events.add(event3);

            ObjectMapper mapper = new ObjectMapper();
            jsonEvents =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        }
        return jsonEvents;
  }

}