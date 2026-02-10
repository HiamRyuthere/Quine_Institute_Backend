package com.quine.backend.controller;

import com.quine.backend.dto.LoginRequest;
import com.quine.backend.dto.LoginResponse;
import com.quine.backend.model.User;
import com.quine.backend.repository.UserRepository;
import com.quine.backend.security.JwtService;
import com.quine.backend.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwtService jwtService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = new LoginResponse();

        // Step 1: User dhoondho
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        // Case A: User mila hi nahi
        if (userOptional.isEmpty()) {
            response.setMessage("User nahi mila! ID check karo.");
            return response;
        }

        User user = userOptional.get();

        // Case B: Validating password
       if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){

           response.setMessage("Password doesn't match ! Try again later ");
           return response;
       }

        // Case C: Sab sahi hai -> Login Success
        response.setMessage("Login Successful!");
        response.setRole(user.getRole().toString());
        response.setUsername(user.getUsername());

        //Implementing JWT here
            //finding username
        var userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());

        // Step 4: JwtService se token banwao
        String jwtToken = jwtService.generateToken(userDetails);
            //generating JWT token


        // Naam set karo (Cleaner logic)
        String name = "Unknown User";
        if (user.getStudent() != null) {
            name = user.getStudent().getName();
        } else if (user.getEmployee() != null) {
            name = user.getEmployee().getName();
        }
        response.setName(name);
        response.setToken(jwtToken);


        return response;
    }
}