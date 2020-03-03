package com.grandplan.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User{
    // @NotNull(message="Please provide your email")
    // @Pattern(regexp="^[\\w.+\\-]+@bbd\\.co\\.za$", message="Please provide a valid email")
    private @Id String email;

    // @NotEmpty(message="Please provide your first name")
    private String firstName;

    // @NotEmpty(message="Please provide your last name")
    private String lastName;

    // @NotEmpty(message="Please provide your number")
    // @Pattern(regexp="^[0-9]$", message="Please provide a valid number")
    private String phone;

    // @NotEmpty(message="Please provide your password")
    private String password;

    // @NotEmpty(message="Please re-enter your password")
    private String confirmPassword;
}