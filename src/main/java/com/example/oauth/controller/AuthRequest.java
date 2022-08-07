package com.example.oauth.controller;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    @NotNull
    @Email
    @Length(min = 5, max = 50)
    private String email;

    @NotNull
    @Length(min = 8, max = 20)
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}