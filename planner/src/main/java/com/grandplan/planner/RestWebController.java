package com.grandplan.planner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grandplan.util.Event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/event")
public class RestWebController {
  private List<Event> events;

  @GetMapping(value = "/all")
  public String mapCurrentUserEvents() {
    String jsonEvents = null;
        try {
            events = new ArrayList<>();

            Event event1 = Event.builder().title("first event")
                                  .start("2020-02-02")
                                  .end("")
                                  .allDay(true)
                                  .color("blue")
                                  .tag("work")
                                  .description("This is a very long description. It is purely to test the functionality of the modal and how it will cope with more information. So I will keep talking about stuff that is completely unnecessary and irrelevant. We shall see how this goes.")
                                  .build();
            events.add(event1);

            Event event2 = Event.builder().title("second event")
                                  .start("2020-03-02")
                                  .end("2020-03-06")
                                  .allDay(true)
                                  .color("#ddd")
                                  .tag("Personal")
                                  .description("Some relevant description")
                                  .build();
            events.add(event2);

            Event event3 = Event.builder().title("third event: call")
                                  .start("2020-02-29T11:00")
                                  .end("2020-02-29T12:00")
                                  .allDay(false)
                                  .color("")
                                  .tag("Grad")
                                  .description("Discussing project")
                                  .build();
            events.add(event3);

            ObjectMapper mapper = new ObjectMapper();
            jsonEvents =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return jsonEvents;
  }

}