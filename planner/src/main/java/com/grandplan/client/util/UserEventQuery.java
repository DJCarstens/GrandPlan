package com.grandplan.client.util;

import com.grandplan.util.User;
import com.grandplan.util.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEventQuery {

    private Event event;
    private User user;
}