package com.grandplan.server.repositories;

import com.grandplan.util.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface EventRepo extends JpaRepository<Event, Long> {

    public Event findEventById(Long id);

    @Query("SELECT e " +
            "FROM Event as e, User as u, Invite as i " +
            "WHERE u.email = ?1 " +
                "AND i.event.id = e.id " +
                "AND i.user.id = u.id")
    public Set<Event> findEventsByUserEmail(String email);

    @Query("UPDATE Event " +
            "SET " +
            "title = ?2, " +
            "start = ?3, " +
            "end = ?4, " +
            "allDay = ?5, " +
            "color = ?6, " +
            "type = ?7 " +
/*
            "description = ?8 " +
*/
            "WHERE id = ?1")
    public void update(Long id, String title, String start, String end, Boolean allDay, String color, String type/*, String description*/);
}
