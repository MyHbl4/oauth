package com.example.oauth.service;

import com.example.oauth.model.User;
import com.example.oauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow(
            () -> new UsernameNotFoundException("Could not find user with username " + username));

        return new MyUserDetails(user);
    }

}
