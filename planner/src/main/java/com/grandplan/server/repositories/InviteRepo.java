package com.grandplan.server.repositories;

import com.grandplan.util.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteRepo extends JpaRepository<Invite, Long> {
    public Invite findInviteById(Long id);

    @Query("SELECT i " +
            "FROM User as u, Invite as i " +
            "WHERE u.email = :email " +
                "AND i.user.id = u.id")
    public Set<Invite> findInvitesByUserEmail(@Param("email") String email);

    @Query("SELECT i " +
            "FROM Event as e, Invite as i " +
            "WHERE e.id = :id " +
                "AND i.event.id = e.id")
    public Set<Invite> findInvitesByEvent(@Param("id") Long id);

    @Query("SELECT i " +
            "FROM Event as e, Invite as i, User as u " +
            "WHERE e.id = :eventId " +
                "AND u.email = :email" +
                "AND i.event.id = e.id" +
                "AND i.user.id = u.id")
    public Invite findInviteByEventUser(@Param("email") String email, @Param("eventId") Long eventId);

    @Modifying
    @Query("UPDATE Invite i " +
            "SET " +
            "i.accepted = :accepted, " +
            "WHERE i.id = :id")
    public void update(
            @Param("accepted") Boolean accepted,
            @Param("id") Long id);
}
