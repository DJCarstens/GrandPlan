package com.grandplan.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
@Builder
@AllArgsConstructor

public class User implements Serializable { //serializable allows to read and write user objects

    private @Id String email;
    private String firstName;
    private String lastName;
    private String password;
    private String phone;
}

