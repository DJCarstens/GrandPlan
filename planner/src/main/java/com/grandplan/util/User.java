package com.grandplan.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User{
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String confirmPassword;
}
