package com.config;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.donotupload.JwtRolesControl;
import com.manager.ManagerService;
import com.manager.ManagerVO;
import com.util.HttpResult;

import io.jsonwebtoken.ExpiredJwtException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final TokenRepository tokenRepo;
	private final RequestMatcher requestMatcher;

	private final ManagerService managerSvc;

	public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService,
			TokenRepository tokenRepo, RequestMatcher requestMatcher, ManagerService managerSvc) {
		super();
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
		this.tokenRepo = tokenRepo;
		this.requestMatcher = requestMatcher;
		this.managerSvc = managerSvc;

		System.out.println("$$JwtAuthenticationFilter Activated");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		
		System.out.println(request.getRequestURL());


		if (!requestMatcher.matches(request)) {
			// 如果请求不匹配你的条件，直接放行
			filterChain.doFilter(request, response);
			return;
		}

		 String authHeader = request.getHeader("Authorization");
		 String jwt;
		 String userEmailFromJwt = null;
		 Integer userIdFromJwt;
		 List<String> userRoles;

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		
		
		jwt = authHeader.substring(7);
		userEmailFromJwt =null;


		try {
			userEmailFromJwt =jwtService.extractUsername(jwt);
			userIdFromJwt = jwtService.extractId(jwt);
			userRoles = jwtService.extractRoles(jwt);
		}catch (ExpiredJwtException e) {
			
		    List<String> authorities = e.getClaims().get("authorities", List.class);
			
		    Integer userId =  e.getClaims().get("id", Integer.class);
		    
		    
		    
		    HttpResult<String> result = new HttpResult<>(400, "OUTDATED", "Token已經過期，請重新登入");
			
			response.getWriter().write(result.toJsonString());
			
//			String ctxPath = request.getContextPath();
//			response.sendRedirect(ctxPath+"/back/main");	
			filterChain.doFilter(request, response);
			return;
		}
		
		Boolean hasAccess = false;
		String requestPath = request.getRequestURI();

		for (String role : userRoles) {
			List<String> allowedPaths = JwtRolesControl.getApiAccessPaths(JwtRolesControl.valueOf(role));
			for (String allowedPath : allowedPaths) {
				if(requestPath.contains(allowedPath)) {
					hasAccess = true;
					break;
				}
			}
		}
		
		if (!hasAccess) {
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			HttpResult<String> result = new HttpResult<>(403, "ACCESS_ERR", "該權限無法請求此API");
			response.getWriter().write(result.toJsonString());
			filterChain.doFilter(request, response);
			return;
		}

		// 安全上下文為null且有userEmailKeyFromJwt
		if (userEmailFromJwt != null) {
			Optional<TokenDTO> tokenDtoOpt = tokenRepo.findToken("managerId:" + userIdFromJwt);

			// Redis 找不到或過期了
			if (tokenDtoOpt.isEmpty()) {
				HttpResult<String> result = new HttpResult<>(400, "NOT_FOUNDTOKEN", "Redis找不到該Token");
				response.getWriter().write(result.toJsonString());
				filterChain.doFilter(request, response);
				return;
			}

			TokenDTO tokenDto = tokenDtoOpt.get();
			ManagerVO managerVO = managerSvc.getOneManager(tokenDto.getId());
			if (userEmailFromJwt.equals(tokenDto.getSub())) {

				List<GrantedAuthority> authorities = tokenDto.getAuthorities().stream().map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());

				String url = request.getContextPath() + "/back/seller/list";
				HttpResult<String> result = new HttpResult<>(200, url, "success");
				
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(managerVO, null,
						authorities);

				SecurityContextHolder.getContext().setAuthentication(authToken);
				response.getWriter().write(result.toJsonString());
				response.setStatus(HttpServletResponse.SC_OK);
				doFilter(request, response, filterChain);
				return;

			}
			
			HttpResult<String> result = new HttpResult<>(400, "ACCOUNT_ERR", "帳號有誤");
		    response.getWriter().write(result.toJsonString());
		    // 如果ROLES本身包含ROLE_JWT，确保在这里调用doFilter
		    doFilter(request, response, filterChain);
			return;
		}
		
		if (SecurityContextHolder.getContext().getAuthentication() != null 
		        && SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
		            .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGERJWT"))) {

		    response.setContentType("application/json");
		    String url = request.getContextPath() + "/back/seller/list";
		    HttpResult<String> result = new HttpResult<>(200, url, "success");
		    response.getWriter().write(result.toJsonString());
		    // 如果ROLES本身包含ROLE_JWT，确保在这里调用doFilter
		    doFilter(request, response, filterChain);
			return;
		}
		
		
	    HttpResult<String> result = new HttpResult<>(500, "INTERNAL", "unknown error");
	    response.getWriter().write(result.toJsonString());
	    doFilter(request, response, filterChain);
		return;


	}

}