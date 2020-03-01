package com.grandplan.util;

import java.util.Optional;

public class UserBuilder{
    protected Optional<String> firstName = Optional.empty();
    protected Optional<String> lastName = Optional.empty();
    protected String email;
    protected Optional<String> phone = Optional.empty();
    protected String password;
    protected Optional<String> confirmPassword = Optional.empty();

    public User build() {
        User user =  new User(this);
        validateUserObject(user);
        return user;
    }

    public UserBuilder firstName(String name){
        this.firstName = Optional.ofNullable(name);
        return this;
    }

    public UserBuilder lastName(String surname){
        this.lastName = Optional.ofNullable(surname);
        return this;
    }

    public UserBuilder email(String email){
        this.email = email;
        return this;
    }

    public UserBuilder phone(String phone){
        this.phone = Optional.ofNullable(phone);
        return this;
    }

    public UserBuilder password(String pass){
        this.password = pass;
        return this;
    }

    public UserBuilder confirmPassword(String pass){
        this.confirmPassword = Optional.ofNullable(pass);
        return this;
    }

    private void validateUserObject(User user) {
        //Do some basic validations to check 
        //if user object does not break any assumption of system
    }
}