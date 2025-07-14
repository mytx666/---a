package com.data.agricultural_material_management_system.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.data.agricultural_material_management_system.utill.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头中获取token
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        token = token.substring(7); // "Bearer " 后面的部分

        // 2. 验证token
        DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
        if (decodedJWT == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 可以选择将用户信息放入request中，方便后续controller使用
        // request.setAttribute("userId", decodedJWT.getClaim("userId").asString());

        return true;
    }
} 