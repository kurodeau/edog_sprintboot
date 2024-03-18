package com.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.seller.entity.SellerVO;
import com.seller.service.SellerService;

@Component("sellerAuthenticationSuccessHandler")
public class SellerAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	SellerService sellerSvc;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		// (1) SellerVOToSecurityContext去取的用戶資訊
		// No need beacause we have Stored UserAuth in (MultiSecurityConfiguration -- GossipAuthenticationProvider)
		// 在這裡獲取登入成功的使用者資訊，例如 SellerVO
//		String sellerEmail = getLoggedInSellerVO(authentication);
//
//		SellerVO sellerVO = sellerSvc.findByOnlyOneEmail(sellerEmail);
//
//		// 將 SellerVO 存儲在安全上下文中
//		setSellerVOToSecurityContext(sellerVO, authentication);

		
		// (2) HttpSession去取的用戶資訊
		HttpSession session = request.getSession();
		session.setAttribute("sellerVO", authentication.getPrincipal());
		
		// 這裡可以進行其他登入成功後的處理，例如重定向等
		response.sendRedirect("/front/seller/main");
	}

	
}
