package com.example.oauth.service;

import com.example.oauth.controller.AuthRequest;
import com.example.oauth.controller.AuthResponse;
import com.example.oauth.exception.AuthException;
import com.example.oauth.exception.NotFoundException;
import com.example.oauth.exception.ValidationException;
import com.example.oauth.model.Provider;
import com.example.oauth.model.Role;
import com.example.oauth.model.User;
import com.example.oauth.repository.RoleRepository;
import com.example.oauth.repository.UserRepository;
import com.example.oauth.security.JwtTokenUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;
import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailSender mailSender;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    AuthenticationManager authManager;

    public AuthResponse login(@RequestBody @Valid AuthRequest request) {
        String email = request.getEmail();
        if (!checkUserByEmail(email)) {
            throw new NotFoundException("User not found");
        }
        if (!checkAuthentication(email)) {
            throw new AuthException("User not activated");
        }
        try {
            Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword())
            );

            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();
            String accessToken = jwtTokenUtil.generateAccessToken(user);

            AuthResponse response = new AuthResponse();
            response.setEmail(email);
            response.setAccessToken(accessToken);
            return response;

        } catch (Exception e) {
            throw new ValidationException("Invalid username or password");
        }
    }

    public User userRegistration(User user) {
        User userFromDb = userRepository.findUserByEmail(user.getEmail()).orElse(null);
        if (userFromDb != null) {
            throw new ValidationException("User already registered");
        }
        Role role = roleRepository.findByName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setUsername(user.getEmail());
        user.setPassword(encodedPassword);
        user.setEnabled(false);
        user.setProvider(Provider.LOCAL);

        userRepository.save(user);

        if (StringUtils.isNotEmpty(user.getEmail())) {
            String message = String.format(
                "Hello, you have registered on the Shire website \n" +
                    "Please visit next link: http://localhost:9000/auth/activate/%s",
                user.getEmail()
            );
            mailSender.send(user.getEmail(), "Activation account", message);
        }

        return user;
    }

    public String activateUser(String email) {
        User user = userRepository.findUserByEmail(email).orElse(null);
        if (user == null || user.isEnabled()) {
            return "Activation failed";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "User successfully activated";
    }

    public boolean checkAuthentication(String email) {
        User userFromDB = userRepository.findUserByEmail(email).orElse(null);
        return userFromDB != null && userFromDB.isEnabled();
    }

    public boolean checkUserByEmail(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }
}