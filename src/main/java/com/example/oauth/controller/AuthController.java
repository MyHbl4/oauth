package com.example.oauth.controller;

import com.example.oauth.dto.UserDTO;
import com.example.oauth.model.User;
import com.example.oauth.security.JwtTokenUtil;
import com.example.oauth.service.AuthService;
import com.example.oauth.service.MyUserDetails;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @PutMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody @Valid User user) {
        User createdUser = authService.userRegistration(user);
        URI uri = URI.create("/users/" + createdUser.getId());

        return ResponseEntity.created(uri)
            .body(new UserDTO(createdUser.getId(), createdUser.getEmail()));
    }

    // TODO: 09.08.2022 не выдаёт ошибку при не верном пароле
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        if (!authService.checkUserByEmail(request.getEmail())) {
            return ResponseEntity.ok().body(new MessageResponse("User not found"));
        }
        if (!authService.checkAuthentication(request.getEmail())) {
            return ResponseEntity.ok().body(new MessageResponse("User not activated"));
        }
        try {
            Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword())
            );

            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();
            String accessToken = jwtTokenUtil.generateAccessToken(user);
            AuthResponse response = new AuthResponse(user.getEmail(), accessToken);

            return ResponseEntity.ok().body(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping(path = "/activate/{email}")
    public ResponseEntity<?> activate(@PathVariable String email) {
        boolean isActivated = authService.activateUser(email);
        if (isActivated) {
            return ResponseEntity.ok().body(new MessageResponse("Activation failed"));
        } else {
            return ResponseEntity.ok().body(new MessageResponse("User successfully activated"));
        }
    }
}
