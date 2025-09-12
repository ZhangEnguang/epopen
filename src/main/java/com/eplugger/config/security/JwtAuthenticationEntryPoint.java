package com.eplugger.config.security;

import com.alibaba.fastjson.JSONObject;
import com.eplugger.common.utils.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 认证失败处理
 * <p>当用户尝试访问安全的资源而不提供任何凭据时,根据请求类型返回认证失败结果
 *
 * @author zeg
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @SneakyThrows
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("401 身份认证失败。uri:[{}]", request.getRequestURI());
        response.setCharacterEncoding("UTF-8");
        response.setStatus(401);
        response.getWriter().write(JSONObject.toJSONString(Result.error("401 身份认证失败")));
        response.addHeader("Content-type", "application/json");
        response.flushBuffer();
    }
}
