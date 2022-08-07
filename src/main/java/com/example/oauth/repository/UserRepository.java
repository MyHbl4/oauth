package com.example.oauth.repository;

import com.example.oauth.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findUserByEmail(String email);
    public Optional<User> findUserByUsername(String username);
}
