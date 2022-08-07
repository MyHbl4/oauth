package com.example.oauth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotFoundException extends RuntimeException {

    private final String message;
}