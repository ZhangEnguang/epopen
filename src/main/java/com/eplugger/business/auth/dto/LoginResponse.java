package com.eplugger.business.auth.dto;

import com.eplugger.business.user.model.User;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private User user;
} 