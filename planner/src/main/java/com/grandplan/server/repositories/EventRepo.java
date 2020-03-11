package com.grandplan.server.repositories;

import com.grandplan.util.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Modifying
    @Query("UPDATE Event e " +
            "SET " +
            "e.title = :title, " +
            "e.start = :start, " +
            "e.end = :end, " +
            "e.allDay = :allDay, " +
            "e.color = :color, " +
            "e.tag = :tag, " +
            "e.description = :description, " +
            "e.hostUsername = :hostUsername " +
            "WHERE e.id = :id")
    public void update(
            @Param("title") String title,
            @Param("start") String start,
            @Param("end") String end,
            @Param("allDay") Boolean allDay,
            @Param("color") String color,
            @Param("tag") String tag,
            @Param("description") String description,
            @Param("id") Long id,
            @Param("hostUsername") String hostUsername);
}
