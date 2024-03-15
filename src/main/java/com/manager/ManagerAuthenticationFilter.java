package com.manager;

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

@Component
public class ManagerAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final ManagerService managerSvc;
	private final ManagerPasswordEncoder managerPasswordEncoder;
	private final ManagerAuthenticationFailureHandler managerAuthenticationFailureHandler;

	public ManagerAuthenticationFilter(AuthenticationManager authenticationManager,
			@Autowired @Qualifier("managerRequestMatcher") RequestMatcher requestMatcher, ManagerService managerSvc,
			ManagerPasswordEncoder managerPasswordEncoder,
			ManagerAuthenticationFailureHandler managerAuthenticationFailureHandler) {
		super(requestMatcher);
		this.setAuthenticationManager(authenticationManager);
		this.managerSvc = managerSvc;
		this.managerPasswordEncoder = managerPasswordEncoder;
		this.managerAuthenticationFailureHandler = new ManagerAuthenticationFailureHandler();

		System.out.println("$$$ManagerAuthenticationFilter Activated");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException {

		// 获取用户名和密码
		String username = obtainUsername(request);
		String password = obtainPassword(request);

		String path = request.getRequestURI();
		if (path.contains("manager")) {

			ManagerVO managerVO = managerSvc.findByOnlyOneEmail(username);

			System.out.println(managerSvc);
			System.out.println(managerPasswordEncoder);
			System.out.println(managerVO);

			if (username == null || username.isEmpty()) {
				throw new UsernameNotFoundException("請輸入用戶名");
			}

			if (managerVO == null) {
				throw new UsernameNotFoundException("查無用戶請選擇註冊");
			}

			if (!managerPasswordEncoder.matches(password, managerVO.getManagerPassword())) {
				throw new BadCredentialsException("密碼輸入錯誤");
			}

			Authentication authenticationResult = new UsernamePasswordAuthenticationToken(username, password,
					AuthorityUtils.createAuthorityList("ROLE_MANAGER"));
			SecurityContextHolder.getContext().setAuthentication(
	                new UsernamePasswordAuthenticationToken(managerVO, null, authenticationResult.getAuthorities()));
			
//			System.out.println("BuyerAuthenticationFilter"+"管理員認證成功");
			return authenticationResult;

		}

		return null;
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		managerAuthenticationFailureHandler.onAuthenticationFailure(request, response, failed);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("ManagerAuthenticationFilter"+"管理員認證成功" +"successfulAuthentication");

		List<String> roles = authResult.getAuthorities().stream().map(authority -> authority.getAuthority())
				.collect(Collectors.toList());
		HttpSession session = request.getSession();
		session.setAttribute("username", obtainUsername(request));
		session.setAttribute("roles", roles);
		
		
		chain.doFilter(request, response);
	}

	private String obtainUsername(HttpServletRequest request) {
		return request.getParameter("usernameinhtml");
	}

	private String obtainPassword(HttpServletRequest request) {
		return request.getParameter("passwordinhtml");
	}
}