package com.grandplan.server.repositories;

import com.grandplan.util.Event;
import com.grandplan.util.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepo extends JpaRepository<Event, Integer> {

    public Event getEventById(Long Id);

    public List<Event> getByInviteUser(User user);

}
