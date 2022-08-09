package com.example.oauth.service;

import com.example.oauth.controller.AuthRequest;
import com.example.oauth.controller.MessageResponse;
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
import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public Map<String, String> login(AuthRequest requestDto) {
        String email = requestDto.getEmail();
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException("User with username: " + email + ", not found"));
        if (!checkAuthentication(email)) {
            throw new AuthException("User not activated");
        }
//        if (user.getStatus().equals(Status.DELETED)) {
//            throw new CustomException(
//                "You cannot log in with this username, because your account has been deleted");
//        }
        try {
            String token = jwtTokenUtil.generateAccessToken(user);
            Map<String, String> response = new HashMap<>();
            response.put("email", email);
            response.put("token", token);
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

    public boolean activateUser(String email) {
        User user = userRepository.findUserByEmail(email).orElse(null);
        if(user == null || user.isEnabled()) {
            return true;
        }
        user.setEnabled(true);
        userRepository.save(user);
        return false;
    }

    public boolean checkAuthentication(String email) {
        User userFromDB = userRepository.findUserByEmail(email).orElse(null);
        return userFromDB != null && userFromDB.isEnabled();
    }

    public boolean checkUserByEmail(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

//    public User update(Principal principal, User user) {
//        try {
//            User oldUser = userRepository.findByUsername(principal.getName());
//            oldUser.setEmail(user.getEmail());
//            oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
//            oldUser.setFirstName(user.getFirstName());
//            oldUser.setLastName(user.getLastName());
//            oldUser.setStatus(user.getStatus());
//            User updatedUser = userRepository.save(oldUser);
//            log.info("update - update user id: {} successfully updated", updatedUser.getId());
//
//            return updatedUser;
//        } catch (Exception e) {
//            throw new ValidationException(COULD_NOT_UPDATED.value);
//        }
//    }
}