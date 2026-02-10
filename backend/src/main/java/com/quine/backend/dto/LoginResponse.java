package com.quine.backend.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String message;
    private String role;
    private String username;
    private String name;
    private String token;
}
