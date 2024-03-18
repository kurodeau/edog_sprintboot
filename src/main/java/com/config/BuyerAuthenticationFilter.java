package com.config;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import com.buyer.entity.BuyerVO;
import com.buyer.service.BuyerService;

public class BuyerAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final BuyerService buyerSvc;
	private final BuyerPasswordEncoder buyerPasswordEncoder;
	private final BuyeAuthenticationFailureHandler buyeAuthenticationFailureHandler;

	public BuyerAuthenticationFilter(AuthenticationManager authenticationManager,
			@Autowired @Qualifier("buyerRequestMatcher") RequestMatcher requestMatcher, BuyerService buyerSvc,
			BuyerPasswordEncoder buyerPasswordEncoder,
			BuyeAuthenticationFailureHandler buyeAuthenticationFailureHandler) {
		super(requestMatcher);
		this.setAuthenticationManager(authenticationManager);
		this.buyerSvc = buyerSvc;
		this.buyerPasswordEncoder = buyerPasswordEncoder;
		this.buyeAuthenticationFailureHandler = new BuyeAuthenticationFailureHandler();

		System.out.println("$$$BuyerAuthenticationFilter Activated");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException {

		// 获取用户名和密码
		String username = obtainUsername(request);
		String password = obtainPassword(request);

		String path = request.getRequestURI();
		if (path.contains("buyer")) {

			BuyerVO buyerVO = buyerSvc.findByOnlyOneEmail(username);

//			System.out.println(buyerSvc);
//			System.out.println(buyerPasswordEncoder);
//			System.out.println(buyerVO);

			if (username == null || username.isEmpty()) {
				throw new UsernameNotFoundException("請輸入用戶名");
			}

			if (buyerVO == null) {
				throw new UsernameNotFoundException("查無用戶請選擇註冊");
			}

			if (!buyerPasswordEncoder.matches(password, buyerVO.getMemberPassword())) {
				throw new BadCredentialsException("密碼輸入錯誤");
			}

			Authentication authenticationResult = new UsernamePasswordAuthenticationToken(username, password,
					AuthorityUtils.createAuthorityList("ROLE_BUYER"));
			SecurityContextHolder.getContext().setAuthentication(
	                new UsernamePasswordAuthenticationToken(buyerVO, null, authenticationResult.getAuthorities()));
			
			return authenticationResult;

		}

		return null;
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		buyeAuthenticationFailureHandler.onAuthenticationFailure(request, response, failed);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		List<String> roles = authResult.getAuthorities().stream().map(authority -> authority.getAuthority())
				.collect(Collectors.toList());
		
		HttpSession session = request.getSession();
		session.setAttribute("username", obtainUsername(request));
		
		
		chain.doFilter(request, response);
	}

	private String obtainUsername(HttpServletRequest request) {
		return request.getParameter("usernameinhtml");
	}

	private String obtainPassword(HttpServletRequest request) {
		return request.getParameter("passwordinhtml");
	}
}