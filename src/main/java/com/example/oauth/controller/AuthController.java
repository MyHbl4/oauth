package com.example.oauth.controller;

import com.example.oauth.controller.dto.AuthRequest;
import com.example.oauth.controller.dto.AuthResponse;
import com.example.oauth.controller.dto.MessageResponse;
import com.example.oauth.controller.dto.UserNewDTO;
import com.example.oauth.controller.dto.UserShortDTO;
import com.example.oauth.mapper.UserMapper;
import com.example.oauth.model.User;
import com.example.oauth.service.AuthService;
import io.swagger.annotations.Api;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@Api(tags = "Authentication")
public class AuthController {

    @Autowired
    AuthService authService;

    @PutMapping("/register")
    public UserShortDTO createUser(@RequestBody @Valid UserNewDTO user) {
        log.info("createUser - User created with email: {}", user.getEmail());
        User createdUser = authService.userRegistration(UserMapper.INSTANCE.toUserFromUserNewDto(user));
        return UserMapper.INSTANCE.toUserShortDtoFromUser(createdUser);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest requestDto) {
        log.info("login - login user with email: {}", requestDto.getEmail());
        return authService.login(requestDto);
    }

    @GetMapping(path = "/activate/{email}")
    public ResponseEntity<?> activate(@PathVariable String email) {
        log.info("activate - activate user account with email: {}", email);
        String response = authService.activateUser(email);
        return ResponseEntity.ok().body(new MessageResponse(response));
    }
}
