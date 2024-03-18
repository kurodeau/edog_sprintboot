package com.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {

	@Autowired
	private  TokenRepository tokenRepository;

  @Override
  public void logout(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    jwt = authHeader.substring(7);
    
    JwtService jwtsc =  new JwtService ();
    Integer userId = jwtsc.extractId(jwt);
    String redisTokenKey = "managerId:" +userId;
    var storedToken = tokenRepository.findToken(redisTokenKey);
    
    if (storedToken != null) {
    	tokenRepository.revokeToken(redisTokenKey);
      SecurityContextHolder.clearContext();
    }
  }
}
