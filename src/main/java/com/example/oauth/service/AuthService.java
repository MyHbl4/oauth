package com.example.oauth.service;

import com.example.oauth.exception.ValidationException;
import com.example.oauth.model.Provider;
import com.example.oauth.model.Role;
import com.example.oauth.model.User;
import com.example.oauth.repository.RoleRepository;
import com.example.oauth.repository.UserRepository;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
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

//    public Map<String, String> login(AuthenticationRequestDto requestDto) {
//        String username = requestDto.getUsername();
//        if (userRepository.findByUsername(username) == null) {
//            throw new NotFoundException("User with username: " + username + ", not found");
//        }
//        User user = userRepository.findByUsername(username);
//        if (user.getStatus().equals(Status.DELETED)) {
//            throw new CustomException(
//                "You cannot log in with this username, because your account has been deleted");
//        }
//        try {
//            String token = jwtTokenProvider.createToken(username, user.getRoles());
//            Map<String, String> response = new HashMap<>();
//            response.put("username", username);
//            response.put("token", token);
//            return response;
//        } catch (Exception e) {
//            throw new ValidationException("Invalid username or password");
//        }
//    }

    public User save(User user) {
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
        user.setPassword(encodedPassword);
        user.setEnabled(false);
        user.setProvider(Provider.LOCAL);

        userRepository.save(user);
//
//        if (StringUtils.isNotEmpty(user.getEmail())) {
//            String message = String.format(
//                "Hello, you have registered on the Shire website \n" +
//                    "Please visit next link: http://localhost:9000/auth/activate/%s",
//                user.getActivationCode()
//            );
//            mailSender.send(user.getEmail(), "Activation Code", message);
//        }

        return user;
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