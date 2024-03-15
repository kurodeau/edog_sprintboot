package com.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.manager.ManagerService;
import com.manager.ManagerVO;
import com.token.TokenRepository;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	 private final JwtService jwtService;
	  private final UserDetailsService userDetailsService;
	  private final TokenRepository tokenRepo;
	  
	  
	  @Autowired 
	  ManagerService managerSvc;
	  
	public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService,
			TokenRepository tokenRepo) {
		super();
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
		this.tokenRepo = tokenRepo;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	    if (request.getServletPath().contains("/seller/login") || request.getServletPath().contains("/buyer/login")  ) {
	        filterChain.doFilter(request, response);
	        return;
	      }
	    
	    final String authHeader = request.getHeader("Authorization");
	    final String jwt;
	    final String userEmailKeyFromJwt;
	    
	    if(authHeader==null || !authHeader.startsWith("Bearer ")) {
	    	filterChain.doFilter(request, response);
	    	return;
	    }
	    jwt = authHeader.substring(7);
	    userEmailKeyFromJwt = jwtService.extractUsername(jwt);
	    
	    
	    if (userEmailKeyFromJwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	    	ManagerVO managerVO = managerSvc.findByOnlyOneEmail(userEmailKeyFromJwt);
	    	
	    	// UP
	    	UserDetails userdetails = User.builder().username(userEmailKeyFromJwt)
					.password(managerVO.getManagerPassword()).authorities("ROLE_ADMIN").build();
	    	
	        Boolean isTokenValid = tokenRepo.isTokenExists(userEmailKeyFromJwt);
	        
	        if (jwtService.isTokenValid(jwt, userdetails) && isTokenValid) {
	          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
	        		  managerVO,
	              null,
	              userdetails.getAuthorities()
	          );
	          authToken.setDetails(
	        		  //当前的 HTTP 请求对象，通过它可以获取到与该请求相关的一些信息，如远程主机地址、请求参数等。
	              new WebAuthenticationDetailsSource().buildDetails(request)
	          );
	          SecurityContextHolder.getContext().setAuthentication(authToken);
	        }
	      }
	}


}