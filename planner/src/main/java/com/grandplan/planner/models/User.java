package com.grandplan.planner.models;

public class User{
    private String username;
    private String password;

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String name){
        this.username = name;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String pass){
        this.password = pass;
    }
}