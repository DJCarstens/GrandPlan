package com.grandplan.server.repositories;

import com.grandplan.util.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

     User getUserByEmail(String username);

}
