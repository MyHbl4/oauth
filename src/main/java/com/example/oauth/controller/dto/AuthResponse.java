package com.example.oauth.controller.dto;

import lombok.Data;

@Data
public class AuthResponse {

    private String email;
    private String accessToken;
}
