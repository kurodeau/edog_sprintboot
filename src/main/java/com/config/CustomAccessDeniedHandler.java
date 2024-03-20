package com.config;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.util.HttpResult;


@Component("customAccessDeniedHandler")
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

   
	   @Override
	    public void handle(HttpServletRequest req, HttpServletResponse res,
	                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

		   
	        String requestUri = req.getRequestURI();

	        
	        
	        // 使用 JavaScript 处理重定向和倒计时
//	        String scheme = req.getScheme();
//	        String serverName = req.getServerName();
//	        int port = req.getServerPort();
//	        String contextPath = req.getContextPath();
//	        String path = "/seller/sellerAuthError403";
//
//	        String fullPath = scheme + "://" + serverName + ":" + port + contextPath + path;
//	        System.out.println(fullPath);
//	        res.sendRedirect(fullPath);
	           
		   Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    if (auth instanceof AnonymousAuthenticationToken) {
		        // 匿名用户
		    	if(requestUri.contains("/front/seller")) {
		    		res.sendRedirect(req.getContextPath() + "/seller/login"); 
		    	} else if (requestUri.contains("/front/buyer")) {
		    		res.sendRedirect(req.getContextPath() + "/buyer/login"); 
		    	} else {
		    		res.sendRedirect(req.getContextPath() + "/");
		    	}
		        
		        
		    } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_BUYER"))) {
		        res.sendRedirect(req.getContextPath()+ "/error/buyer/buyerAuthError403");
		    } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SELLER"))) {
		        // 依據Seller身分去做分流
		        if (requestUri.contains("/front/buyer")) {
		            // 如果被拒絕 而且 訪問路徑包含front/buyer 
		            res.sendRedirect(req.getContextPath() + "/error/seller/sellerAuthError403"); 
		        } else if (requestUri.contains("/back")) {
		        	 // 被拒絕 而且 訪問路徑包含back 
		            res.sendRedirect(req.getContextPath() + "/error/seller/sellerAuthError403"); 
		        } else if (requestUri.contains("/front/seller/report")){
		            res.sendRedirect(req.getContextPath() + "/error/seller/sellerLvError403"); 
		        }  else if (requestUri.contains("/front/seller/ad")){
		            res.sendRedirect(req.getContextPath() + "/error/seller/sellerLvError403"); 
		        }
		        else {
		            res.sendRedirect(req.getContextPath() +"front/seller/main"); 
		        }
		    } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
		    	
		    	System.out.println(req.getContextPath());
		    	res.sendRedirect(req.getContextPath() +"/back/main?error=AccessDeniedError"); 
		    } else 
		    {
		        res.sendRedirect("/"); 
		    }
		    
		    
		    
//	        res.setContentType("application/json;charset=UTF-8");
//		   	res.setStatus(403);
//	        String msg = "請考慮升級";
//	        String data = "賣家中心>賣家等級，導向賣家等級";
//	        HttpResult httpResult = new HttpResult(403, data, msg);
//	        ObjectMapper objectMapper = new ObjectMapper();
//	        String json = objectMapper.writeValueAsString(httpResult);
//	        res.getWriter().write(json);
	        
	        
	   
	   }
}