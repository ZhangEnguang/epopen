package com.eplugger.business.auth.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
} 