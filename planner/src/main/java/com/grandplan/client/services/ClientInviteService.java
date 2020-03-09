package com.grandplan.client.services;

import com.grandplan.util.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Component
public class ClientInviteService {
    @Autowired
    private HttpRequestService httpRequestService;

    private static final String INVITES = "invites";

    public String getInvites(User user, Model model){
        return INVITES;
    }
}