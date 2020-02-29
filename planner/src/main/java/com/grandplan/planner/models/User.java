package com.grandplan.planner.models;

public class User{
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String pass){
        this.password = pass;
    }

    public String getConfirmPassword(){
        return this.confirmPassword;
    }

    public void setConfirmPassword(String pass){
        this.confirmPassword = pass;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public void setFirstName(String name){
        this.firstName = name;
    }

    public String getLastName(){
        return this.lastName;
    }

    public void setLastName(String surname){
        this.lastName = surname;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPhone(){
        return this.phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }
}