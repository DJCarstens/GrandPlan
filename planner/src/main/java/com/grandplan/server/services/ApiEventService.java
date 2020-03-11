package com.grandplan.server.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grandplan.server.repositories.EventRepo;
import com.grandplan.util.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Component
@Slf4j
public class ApiEventService {

    private final EventRepo eventRepo;

    public Event getEventById(Long id) {
        return eventRepo.findEventById(id);
    }

    public List<Event> getEvents() {
        return eventRepo.findAll();
    }

    public Set<Event> getUserEvents(String email) {
        return eventRepo.findEventsByUserEmail(email);
    }

    public Event createEvent(Event event) {
        Event e = eventRepo.save(event);
        writeListOfEvents(getEvents());
        return e;
    }

    public boolean deleteEvent(Long eventId) {
        Event e = eventRepo.findEventById(eventId);
        if (e == null) {
            return false;
        }
        eventRepo.delete(e);
        return true;
    }

    public Event updateEvent(Event event) {
        eventRepo.update(
            event.getTitle(),
            event.getStart(),
            event.getEnd(),
            event.getAllDay(),
            event.getColor(),
            event.getTag(),
            event.getDescription(),
            event.getId(),
            event.getHostUsername()
        );
        writeListOfEvents(getEvents());
        return eventRepo.findEventById(event.getId());
    }
    private void writeListOfEvents(List<Event> events) {
        ObjectMapper mapper = new ObjectMapper();
        JSONArray jsonObjects = new JSONArray();
        for (Event event : events) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", event.getId());
            jsonObject.put("title", event.getTitle());
            jsonObject.put("start", event.getStart());
            jsonObject.put("end", event.getEnd());
            jsonObject.put("allDay", event.getAllDay());
            jsonObject.put("color", event.getColor());
            jsonObject.put("tag", event.getTag());
            jsonObject.put("description", event.getDescription());
            jsonObject.put("hostUsername", event.getHostUsername());
            jsonObjects.add(jsonObject);
        }
        try (FileWriter file = new FileWriter("src/main/resources/data/Events.json", false)) {
            //false indicates that Users.json will get overridden with the current user data
            String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObjects);
            file.write(indented);
            log.info("JSON file updated: " + jsonObjects);
        } catch (IOException e) {
            log.info("Unable to write Events to Events.json");
            log.error(e.getMessage());
        }
    }

}
