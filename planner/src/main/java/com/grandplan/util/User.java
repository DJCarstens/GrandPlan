package com.grandplan.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

import java.util.Optional;

@Data
@Entity
@Builder
@AllArgsConstructor
public class User{
    private @Id String email;
    private Optional<String> firstName = Optional.empty();
    private Optional<String> lastName = Optional.empty();
    private String email;
    private Optional<String> phone = Optional.empty();
    private String password;
    private Optional<String> confirmPassword = Optional.empty();
}