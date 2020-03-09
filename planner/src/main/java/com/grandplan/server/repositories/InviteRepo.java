package com.grandplan.server.repositories;

import com.grandplan.util.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.transaction.annotation.Transactional;


import java.util.Set;

@Repository
public interface InviteRepo extends JpaRepository<Invite, Long> {
    public Invite findInviteById(Long id);

    @Query("SELECT i " +
            "FROM Invite as i, User as u " +
            "WHERE u.email = :email " +
                "AND i.user.id = u.id")
    public Set<Invite> findInvitesByEmail(@Param("email") String email);

    @Query("SELECT i " +
            "FROM Invite as i, User as u " +
            "WHERE u.email = :email " +
                "AND i.user.id = u.id " +
                "AND i.accepted = false")
    public Set<Invite> findUnacceptedInvitesByEmail(@Param("email") String email);


    @Query("SELECT i " +
            "FROM Invite as i, User as u " +
            "WHERE u.email = :email " +
                "AND i.user.id = u.id " +
                "AND i.accepted = true")
    public Set<Invite> findAcceptedInvitesByEmail(@Param("email") String email);

    @Query("SELECT i " +
            "FROM Event as e, Invite as i " +
            "WHERE e.id = :id " +
                "AND i.event.id = e.id")
    public Set<Invite> findInvitesByEvent(@Param("id") Long id);

    @Query("SELECT i " +
            "FROM Event as e, Invite as i " +
            "WHERE e.id = :id " +
                "AND i.event.id = e.id " + 
                "AND i.accepted = false")
    public Set<Invite> findUnacceptedInvitesByEvent(@Param("id") Long id);

    @Query("SELECT i " +
            "FROM Event as e, Invite as i " +
            "WHERE e.id = :id " +
                "AND i.event.id = e.id " + 
                "AND i.accepted = true")
    public Set<Invite> findAcceptedInvitesByEvent(@Param("id") Long id);

    @Query("SELECT i " +
            "FROM Event as e, Invite as i, User as u " +
            "WHERE e.id = :eventId " +
                "AND u.email = :email " +
                "AND i.event.id = e.id " +
                "AND i.user.id = u.id")
    public Invite findInviteByEventAndUser(@Param("email") String email, @Param("eventId") Long eventId);

    @Transactional
    @Modifying
    @Query("UPDATE Invite i " +
            "SET " +
            "i.accepted = :accepted " +
            "WHERE i.id = :id")
    public void update(
            @Param("accepted") Boolean accepted,
            @Param("id") Long id);
}
