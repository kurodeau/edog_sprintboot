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

import com.buyer.entity.BuyerVO;
import com.buyer.service.BuyerService;

@Component("buyerAuthenticationSuccessHandler")
public class BuyerAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	BuyerService buyerSvc;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		// (1) SellerVOToSecurityContext去取的用戶資訊
		// 在這裡獲取登入成功的使用者資訊，例如 BuyerVO
		String memberEmail = getLoggedInBuyerVO(authentication);

		BuyerVO buyerVO = buyerSvc.findByOnlyOneEmail(memberEmail);

		// 將 BuyerVO 存儲在安全上下文中
		setBuyerVOToSecurityContext(buyerVO, authentication);

		
		
		// (2) HttpSession去取的用戶資訊
		HttpSession session = request.getSession();
		session.setAttribute("buyerVO", buyerVO);
		
		
		System.out.println("BuyerAuthenticationSuccessHandler" +"onAuthenticationSuccess");
		// 這裡可以進行其他登入成功後的處理，例如重定向等
		response.sendRedirect(request.getContextPath()+"/front/buyer/main");
	}

	private String getLoggedInBuyerVO(Authentication authentication) {
		// 根據你的 Authentication 實現方式獲取 BuyerVO
		// 這裡只是一個示例，實際情況可能需要根據你的應用程式進行修改

		String userEmail;

		userEmail = authentication.getName();
		return userEmail;
	}

	private void setBuyerVOToSecurityContext(BuyerVO buyerVO, Authentication authentication) {
		// 在這裡將 BuyerVO 存儲在安全上下文中
		// 這裡只是一個示例，實際情況可能需要根據你的應用程式進行修改

		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(buyerVO, null, authentication.getAuthorities()));
	}
}
