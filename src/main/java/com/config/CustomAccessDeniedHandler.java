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
	public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException accessDeniedException)
			throws IOException, ServletException {

		String requestUri = req.getRequestURI();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_BUYER"))) {

			res.sendRedirect(req.getContextPath() + "/error/buyer/buyerAuthError403");

		} else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SELLER"))) {
			// 依據Seller身分去做分流
			if (requestUri.contains("/front/seller/report")) {
				// 被拒絕 而且 訪問路徑包含 report
				res.sendRedirect(req.getContextPath() + "/error/seller/sellerLvError403");
			} else {
				// 如果被拒絕
				res.sendRedirect(req.getContextPath() + "/error/seller/sellerAuthError403");

			}
		} else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {

			res.sendRedirect(req.getContextPath() + "/back/main?error=AccessDeniedError");
		} else {
			res.sendRedirect("/");
		}



	           
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
		    
		    
		   

	}
}