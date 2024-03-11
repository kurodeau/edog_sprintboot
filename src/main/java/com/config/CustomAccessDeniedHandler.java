package com.config;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.util.HttpResult;


@Component("customAccessDeniedHandler")
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

   
	   @Override
	    public void handle(HttpServletRequest req, HttpServletResponse res,
	                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

	        res.setContentType("application/json;charset=UTF-8");
		   	res.setStatus(403);
	        String msg = "請考慮升級";
	        String data = "賣家中心>賣家等級，導向賣家等級";
	        HttpResult httpResult = new HttpResult(403, data, msg);
	        ObjectMapper objectMapper = new ObjectMapper();
	        String json = objectMapper.writeValueAsString(httpResult);
	        res.getWriter().write(json);
	        
	        
	        // 使用 JavaScript 处理重定向和倒计时
	        String scheme = req.getScheme();
	        String serverName = req.getServerName();
	        int port = req.getServerPort();
	        String contextPath = req.getContextPath();
	        String path = "/seller/error403";

	        String fullPath = scheme + "://" + serverName + ":" + port + contextPath + path;
	        res.sendRedirect(fullPath);
	   }
}