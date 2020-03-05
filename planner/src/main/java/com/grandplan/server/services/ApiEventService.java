package com.grandplan.server.services;

import com.grandplan.server.repositories.EventRepo;
import com.grandplan.server.repositories.UserRepo;
import com.grandplan.util.Event;
import com.grandplan.util.Invite;
import com.grandplan.util.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Component
public class ApiEventService {

    private final EventRepo eventRepo;

    private final UserRepo userRepo;

    public List<Event> getUserEvents(User user) {
        return eventRepo.getByInviteUser(user);
    }
}
