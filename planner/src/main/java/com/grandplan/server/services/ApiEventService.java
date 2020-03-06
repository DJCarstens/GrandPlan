package com.grandplan.server.services;

import com.grandplan.server.repositories.EventRepo;
import com.grandplan.util.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
@Component
public class ApiEventService {

    private final EventRepo eventRepo;

    public Set<Event> getUserEvents(String email) {
        return eventRepo.findEventsByUserEmail(email);
    }

    public Event createEvent(Event event) {
        eventRepo.save(event);
        return event;
    }

    public boolean deleteEvent(Event event) {
        if (eventRepo.findEventById(event.getId()) == null) {
            return false;
        }
        eventRepo.delete(event);
        return true;
    }

}
