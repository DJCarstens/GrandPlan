package com.grandplan.server.repositories;

import com.grandplan.util.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteRepo extends JpaRepository<Invite, Long> {

}
