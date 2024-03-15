package com.config;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 获取请求的路径
        String requestURI = request.getRequestURI();
        
        if (requestURI.startsWith("/front/seller")) {
            response.sendRedirect("/seller/login");
        } else if (requestURI.startsWith("/front/buyer")) {
            response.sendRedirect("/buyer/login");
        } else if (requestURI.startsWith("/back/**")){
            response.sendRedirect("/backlogin/K4312378E9394834");
        }
    }
}
