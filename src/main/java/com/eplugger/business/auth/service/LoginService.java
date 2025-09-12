package com.eplugger.business.auth.service;

import com.eplugger.business.auth.dto.LoginRequest;
import com.eplugger.business.auth.dto.LoginResponse;
import com.eplugger.business.user.model.User;
import com.eplugger.business.user.service.UserService;
import com.eplugger.common.utils.Result;
import com.eplugger.config.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    public Result<LoginResponse> login(LoginRequest loginRequest) {
        try {
            // 1. 认证用户
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            if (!authentication.isAuthenticated()) {
                return Result.error("认证失败");
            }

            // 2. 获取用户基本信息
            User user = userService.findByUsername(loginRequest.getUsername());
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 3. 清除敏感信息
            user.setPassword(null);

            // 4. 生成JWT令牌
            String token = jwtUtil.generateToken(authentication);

            // 5. 构建登录响应
            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setUser(user);

            return Result.success(response);

        } catch (DisabledException e) {
            return Result.error("用户被禁用");
        } catch (AuthenticationException e) {
            return Result.error("用户名或密码错误");
        } catch (Exception e) {
            return Result.error("登录失败：" + e.getMessage());
        }
    }
}
