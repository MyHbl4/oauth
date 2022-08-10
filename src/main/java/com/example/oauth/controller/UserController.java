package com.example.oauth.controller;

import com.example.oauth.model.User;
import com.example.oauth.service.UserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping()
    public List<User> getUsers() {
        log.info("getUsers - get all users");
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id) {
        log.info("findUserById - get user with id: {}", id);
        return userService.getUser(id);
    }
}
