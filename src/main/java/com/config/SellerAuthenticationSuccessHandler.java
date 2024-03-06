package com.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

		// 在這裡獲取登入成功的使用者資訊，例如 SellerVO
		String sellerEmail = getLoggedInSellerVO(authentication);

		SellerVO sellerVO = sellerSvc.findUserEmail(sellerEmail);

		// 將 SellerVO 存儲在安全上下文中
		setSellerVOToSecurityContext(sellerVO, authentication);

		// 這裡可以進行其他登入成功後的處理，例如重定向等
		response.sendRedirect("front/seller/main");
	}

	private String getLoggedInSellerVO(Authentication authentication) {
		// 根據你的 Authentication 實現方式獲取 SellerVO
		// 這裡只是一個示例，實際情況可能需要根據你的應用程式進行修改

		String userEmail;

		userEmail = authentication.getName();
		return userEmail;
	}

	private void setSellerVOToSecurityContext(SellerVO sellerVO, Authentication authentication) {
		// 在這裡將 SellerVO 存儲在安全上下文中
		// 這裡只是一個示例，實際情況可能需要根據你的應用程式進行修改

		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(sellerVO, null, authentication.getAuthorities()));
	}
}
