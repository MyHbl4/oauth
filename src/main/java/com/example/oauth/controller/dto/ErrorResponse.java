package com.example.oauth.controller.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ErrorResponse {

    private String message;
    private HttpStatus status;
    private LocalDateTime timestamp;
}
