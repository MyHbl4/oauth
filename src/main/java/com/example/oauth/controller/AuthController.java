package com.example.oauth.controller;

import com.example.oauth.model.User;
import com.example.oauth.service.AuthService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public User registerUser(@RequestBody @Valid User user) {
        return authService.save(user);
    }
//
//    @Autowired
//    UserService userService;
//    @Autowired
//    AuthenticationManager authManager;
//    @Autowired
//    JwtTokenUtil jwtTokenUtil;
//
//    /**
//     * POST /auth/login: get token request for any user. need do this method accessible for not
//     * authenticated users
//     *
//     * @return ResponseEntity with status 200 (OK) and email and token
//     */
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
//        if (!userService.checkUserByEmail(request.getEmail())) {
//            return ResponseEntity.ok().body(new MessageResponse("User not found"));
//        }
//        if (!userService.checkAuthentication(request.getEmail())) {
//            return ResponseEntity.ok().body(new MessageResponse("User not activated"));
//        }
//        try {
//            Authentication authentication = authManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                    request.getEmail(), request.getPassword())
//            );
//
//            User user = (User) authentication.getPrincipal();
//            String accessToken = jwtTokenUtil.generateAccessToken(user);
//            AuthResponse response = new AuthResponse(user.getEmail(), accessToken);
//
//            return ResponseEntity.ok().body(response);
//
//        } catch (BadCredentialsException ex) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }
//
//    /**
//     * PUT /users/ : create user request for any user. need do this method accessible for not
//     * authenticated users
//     *
//     * @return ResponseEntity with status 201 (Created) and body {@link UserDTO}
//     */
//    @PutMapping("/register")
//    public ResponseEntity<?> createUser(@RequestBody @Valid User user) {
//        User createdUser = userService.save(user);
//        URI uri = URI.create("/users/" + createdUser.getId());
//
//        return ResponseEntity.created(uri)
//            .body(new UserDTO(createdUser.getId(), createdUser.getEmail()));
//    }
//
//    @GetMapping(path = "/activate/{code}")
//    public ResponseEntity<?> activate(@PathVariable String code) {
//        boolean isActivated = userService.activateUser(code);
//        if (isActivated) {
//            return ResponseEntity.ok().body(new MessageResponse("User successfully activated"));
//        } else {
//            return ResponseEntity.ok().body(new MessageResponse("Activation failed"));
//        }
//    }
}
