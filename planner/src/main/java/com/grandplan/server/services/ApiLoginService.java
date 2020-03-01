package com.grandplan.server.services;

import com.grandplan.server.repositories.UserRepo;
import com.grandplan.util.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class ApiLoginService {
    private final UserRepo userRepo;

    @Autowired
    public ApiLoginService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User validateUserCredentials(User user)
    {
        User fetchedUser = userRepo.getUserByEmail(user.getEmail());
        if(fetchedUser == null || !fetchedUser.getPassword().equals(user.getPassword()))
            return null;
        else
            return user;
    }
}

