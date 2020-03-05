package com.grandplan.server.repositories;

import com.grandplan.util.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface EventRepo extends JpaRepository<Event, Long> {

    public Event findEventById(Long Id);

    @Query("SELECT e " +
            "FROM Event as e, User as u, Invite as i " +
            "WHERE u.email = ?1 " +
                "AND i.event.id = e.id " +
                "AND i.user.id = u.id")
    public Set<Event> findEventsByUserEmail(String email);

}
