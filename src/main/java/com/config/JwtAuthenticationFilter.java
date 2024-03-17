package com.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.manager.ManagerService;
import com.manager.ManagerVO;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final TokenRepository tokenRepo;
	private final RequestMatcher requestMatcher;

	@Autowired
	ManagerService managerSvc;

	public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService,
			TokenRepository tokenRepo, RequestMatcher requestMatcher) {
		super();
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
		this.tokenRepo = tokenRepo;
		this.requestMatcher = requestMatcher;
		System.out.println("$$JwtAuthenticationFilter Activated");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!requestMatcher.matches(request)) {
			// 如果请求不匹配你的条件，直接放行
			filterChain.doFilter(request, response);
			return;
		}
		
		

		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userEmailKeyFromJwt;
		System.out.println("http://localhost:8081/back/seller/list");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		jwt = authHeader.substring(7);
		userEmailKeyFromJwt = jwtService.extractUsername(jwt);

//		System.out.println(userEmailKeyFromJwt);
//		System.out.println(SecurityContextHolder.getContext().getAuthentication());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Boolean hasManagerJWTToken = false;
		if (authentication != null) {
		    for (GrantedAuthority authority : authentication.getAuthorities()) {
		        if ("ROLE_MANAGERJWT".equals(authority.getAuthority())) {
		            hasManagerJWTToken = true;
		            break;
		        }
		    }
		}
		
		
		System.out.println("http://localhost:8081/back/seller/list");
		
		
		if (userEmailKeyFromJwt != null && !hasManagerJWTToken) {
			ManagerVO managerVO = (ManagerVO) authentication.getPrincipal();

			List<GrantedAuthority> newAuthorities = new ArrayList<>();
			newAuthorities.add(new SimpleGrantedAuthority("ROLE_MANAGERJWT"));
			newAuthorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
			UserDetails userdetails = User.builder().username(userEmailKeyFromJwt)
					.password(managerVO.getManagerPassword()).authorities(newAuthorities).build();

			Boolean isTokenValidwithRedis = tokenRepo.isTokenExists("managerId:" + managerVO.getManagerId());

			System.out.println(userdetails.getAuthorities());
			System.out.println(jwtService.isTokenValid(jwt, userdetails));
			System.out.println(isTokenValidwithRedis);
			if (jwtService.isTokenValid(jwt, userdetails) && isTokenValidwithRedis) {

				System.out.println("The token is valid");
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(managerVO, null,
						userdetails.getAuthorities());
				authToken.setDetails(
						// 当前的 HTTP 请求对象，通过它可以获取到与该请求相关的一些信息，如远程主机地址、请求参数等。
						new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
//			    response.setStatus(HttpServletResponse.SC_OK);
//				response.setContentType("application/json");

				System.out.println(request.getContextPath() + "/back/newsTicker/listAllGet");
//				response.sendRedirect(request.getContextPath() + "/back/newsTicker/listAllGet");
				 doFilter(request,response,filterChain);

			} else {
				System.out.println("The token is in valid");

				List<GrantedAuthority> authorities = new ArrayList<>();
				authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(managerVO, null,
						authorities);
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			}
		}
		
		
		@Override
		protected void successfulAuthentication(HttpServletRequest request,
		                                        HttpServletResponse response,
		                                        FilterChain chain,
		                                        Authentication authResult) throws IOException, ServletException {
		    final SecurityContext context = SecurityContextHolder.createEmptyContext();
		    context.setAuthentication(authResult);
		    SecurityContextHolder.setContext(context);
		    chain.doFilter(request, response);
		}
	}

}