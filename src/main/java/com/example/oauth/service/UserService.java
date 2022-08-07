package com.example.oauth.service;

import com.example.oauth.exception.ValidationException;
import com.example.oauth.model.Provider;
import com.example.oauth.model.Role;
import com.example.oauth.model.User;
import com.example.oauth.repository.RoleRepository;
import com.example.oauth.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public void processOAuthPostLogin(Map<String, Object> attributes) {
        User existUser = userRepository.findUserByEmail((String) attributes.get("email"))
            .orElseGet(() -> {
                User newUser = new User();

                Role role = roleRepository.findByName("ROLE_USER");
                Set<Role> roles = new HashSet<>();
                roles.add(role);
                newUser.setRoles(roles);
                newUser.setUsername((String) attributes.get("name"));
                newUser.setEmail((String) attributes.get("email"));
                newUser.setProvider(Provider.GOOGLE);
                newUser.setRoles(roles);
                newUser.setEnabled(true);

                System.out.println("Created new user: " + (String) attributes.get("name"));

                return userRepository.save(newUser);
            });
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User with id " + id + " does not exist."));
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

}
