package com.config;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@Component("customAccessDeniedHandler")
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

   
	   @Override
	    public void handle(HttpServletRequest request, HttpServletResponse response,
	                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

		   
		   // 处理 AccessDeniedException 的逻辑
	        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 设置HTTP状态码为403
	        // 处理 AccessDeniedException 的逻辑
	        request.setAttribute("error", accessDeniedException.getMessage());

	    }
}