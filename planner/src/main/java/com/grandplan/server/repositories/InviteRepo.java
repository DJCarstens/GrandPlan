package com.grandplan.server.repositories;

import com.grandplan.util.Event;
import com.grandplan.util.Invite;
import com.grandplan.util.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteRepo extends JpaRepository<Invite, Integer> {

    public Event getEventByUser(User user);

    public User getUserByEvent(Event event);

}
