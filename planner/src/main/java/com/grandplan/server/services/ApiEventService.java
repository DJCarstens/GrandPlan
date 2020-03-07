package com.grandplan.server.services;

import com.grandplan.server.repositories.EventRepo;
import com.grandplan.util.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Component
public class ApiEventService {

    private final EventRepo eventRepo;

    public List<Event> getEvents() {
        return eventRepo.findAll();
    }

    public Set<Event> getUserEvents(String email) {
        return eventRepo.findEventsByUserEmail(email);
    }

    public Event createEvent(Event event) {
        eventRepo.save(event);
        return event;
    }

    public boolean deleteEvent(Long id) {
        Event event = eventRepo.findEventById(id);
        if (event == null) {
            return false;
        }
        eventRepo.delete(event);
        return true;
    }

    public Event updateEvent(Event event) {
        eventRepo.update(
                event.getTitle(),
                event.getStart(),
                event.getEnd(),
                event.getAllDay(),
                event.getColor(),
                event.getType(),
                event.getDescription(),
                event.getId()
        );
        return eventRepo.findEventById(event.getId());
    }

}
