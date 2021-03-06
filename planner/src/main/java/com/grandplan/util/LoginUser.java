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
public class LoginUser{
    @NotNull(message="Please provide your email")
    @NotEmpty(message="Please provide your email")
    @Pattern(regexp="^[\\w.+\\-]+@bbd\\.co\\.za$", message="Please provide a valid BBD email")
    private @Id String email;

    @NotNull(message="Please provide your password")
    @NotEmpty(message="Please provide your password")
    private String password;

    public User convertUser(){
        User validUser = new User();
        validUser.setEmail(this.getEmail());
        validUser.setPassword(this.getPassword());
        return validUser;
    }
}