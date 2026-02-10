package com.quine.backend.service;

import com.quine.backend.model.User;
import com.quine.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = Logger.getLogger(CustomUserDetailsService.class.getName());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Checking DB for user: " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Bhai, user nahi mila: " + username));

        // Explicitly ROLE_ prefix add kar rahe hain taaki koi doubt na rahe
        String roleWithPrefix = "ROLE_" + user.getRole().name();

        logger.info("User found! Assigning Authority: " + roleWithPrefix);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(new SimpleGrantedAuthority(roleWithPrefix)) // .roles() ki jagah explicit authority use kar rahe hain
                .build();
    }
}