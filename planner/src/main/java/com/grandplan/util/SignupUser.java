package com.grandplan.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupUser{
    @NotNull(message="Please provide your email")
    @NotEmpty(message="Please provide your email")
    @Pattern(regexp="^[\\w.+\\-]+@bbd\\.co\\.za$", message="Please provide a valid BBD email")
    private @Id String email;

    @NotNull(message="Please provide your first name")
    @NotEmpty(message="Please provide your first name")
    private String firstName = "";

    @NotNull(message="Please provide your last name")
    @NotEmpty(message="Please provide your last name")
    private String lastName = "";

    @NotNull(message="Please provide your number")
    @NotEmpty(message="Please provide your number")
    @Pattern(regexp="^[0-9]*$", message="Please provide a valid number")
    private String phone = "";

    @NotNull(message="Please provide your password")
    @NotEmpty(message="Please provide your password")
    private String password;

    @NotNull(message="Please re-enter your password")
    @NotEmpty(message="Please re-enter your password")
    private String confirmPassword = "";

    public User convertUser(){
        User validUser = new User();
        validUser.setEmail(this.getEmail());
        validUser.setPassword(this.getPassword());
        validUser.setFirstName(this.getFirstName());
        validUser.setLastName(this.getLastName());
        validUser.setPhone(this.getPhone());
        return validUser;
    }
}