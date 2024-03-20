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