package com.manager;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class ManagerAuthenticationFailureHandler {
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		// 构建重定向URL，URL中
		
        String errorMessage = URLEncoder.encode(exception.getMessage(), "UTF-8");

		System.out.println(exception.getMessage());
        String redirectUrl = "/manager/login?error=" + errorMessage;
		
		// 将用户重定向到登录页面，并传递错误信息
		response.sendRedirect(redirectUrl);
	}
}