package com.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

	private static final String ERROR_PATH = "/error";

	@RequestMapping(ERROR_PATH)
	public String handleError(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String ctxPath = request.getContextPath();

		Object status = request.getAttribute("javax.servlet.error.status_code");

		System.out.println("in CustomErrorController " + status.toString());
		
		// 屬於404 Error 
		if (status != null && Integer.parseInt(status.toString()) == HttpStatus.NOT_FOUND.value()) {
			// 檢查用戶權限
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			
			// 沒有權限返回首頁
			if (auth ==null || auth.getPrincipal()==null) {
				response.sendRedirect(ctxPath + "/error/generalError404");
				return "";
			}
			
			
			// Security 官方
			if (auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
				UserDetails userDetails = (UserDetails) auth.getPrincipal();
			
				
			} 
			// 根據用戶權限分別處理404錯誤
			else if (auth.isAuthenticated() && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SELLER"))) {
				response.sendRedirect(ctxPath + "/error/sellerError404");
				return "";
			}  else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_BUYER"))) {
				response.sendRedirect(ctxPath + "/error/buyerError404");
				return "";
			}  else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
				response.sendRedirect(ctxPath + "/error/managerError404");
				return "";
			}			

			// 如果無法識別用戶角色，返回一般的錯誤頁面
			response.sendRedirect(ctxPath + "/error/generalError404");
			return "";
		}
		// 如果不是404錯誤，同樣返回一般的錯誤頁面
		return null;
	}

}