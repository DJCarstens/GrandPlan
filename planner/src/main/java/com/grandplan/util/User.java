package com.grandplan.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User{
    private @Id
    String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String confirmPassword;
}
