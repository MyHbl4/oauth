package com.example.oauth.controller;

import com.example.oauth.service.AuthService;
import com.example.oauth.service.UserService;
import com.example.oauth.model.User;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping()
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id) {
        return userService.getUser(id);
    }

//    @PutMapping("/register")
//    public ResponseEntity<?> createUser(@RequestBody @Valid User user) {
//        User createdUser = AuthService.save(user);
//        URI uri = URI.create("/users/" + createdUser.getId());
//
//        return ResponseEntity.created(uri)
//            .body(new User(createdUser.getId(), createdUser.getEmail()));
//    }

}
