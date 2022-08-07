package com.example.oauth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthException extends RuntimeException {

    private final String message;
}