package com.grandplan.planner.models;

import java.util.Optional;

public class User{
    private Optional<String> firstName = Optional.empty();
    private Optional<String> lastName = Optional.empty();;
    private String email;
    private Optional<String> phone = Optional.empty();;
    private String password;
    private Optional<String> confirmPassword = Optional.empty();;

    public String getFirstName(){
        return this.firstName.isPresent()
        ? this.firstName.get()
        : "";
    }

    public void setFirstName(String name){
        this.firstName = Optional.ofNullable(name);
    }

    public String getLastName(){
        return this.lastName.isPresent()
        ? this.lastName.get()
        : "";
    }

    public void setLastName(String surname){
        this.lastName = Optional.ofNullable(surname);
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPhone(){
        return this.phone.isPresent()
        ? this.phone.get()
        : "";
    }

    public void setPhone(String phone){
        this.phone = Optional.ofNullable(phone);
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String pass){
        this.password = pass;
    }

    public String getConfirmPassword(){
        return this.confirmPassword.isPresent()
        ? this.confirmPassword.get()
        : "";
    }

    public void setConfirmPassword(String pass){
        this.confirmPassword = Optional.ofNullable(pass);
    }
}