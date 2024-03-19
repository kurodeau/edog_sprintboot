package com.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.config.JwtService;
import com.config.TokenDTO;
import com.config.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.ManagerPasswordEncoder;
import com.manager.ManagerService;
import com.manager.ManagerVO;

@Service
@ComponentScan("com.manager")
public class AuthenticationService {

	@Value("${application.security.jwt.expiration}")
	private long jwtExpiration;

	@Autowired
	private ManagerService managerService;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private ManagerPasswordEncoder managerPasswordEncoder;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	public AuthenticationResponse register(RegisterRequest registerRequest) {

		ManagerVO managerVO = managerService.findByOnlyOneEmail(registerRequest.getEmail());
		String encodedPassword = managerPasswordEncoder.encode(managerVO.getManagerPassword());
		UserDetails userdetails = User.builder().username(managerVO.getManagerEmail()).password(encodedPassword)
				.roles("ROLE_MANAGER").build();
		managerVO.setManagerPassword(encodedPassword);

		Integer id = managerService.addManager(managerVO);
		var jwtToken = jwtService.generateToken(userdetails);
		var refreshToken = jwtService.generateRefreshToken(userdetails);
		saveUserToken(managerVO, jwtToken ,List.of("ROLE_MANAGER"));
		return new AuthenticationResponse.Builder().accessToken(jwtToken).refreshToken(refreshToken).build();
	}

	private void saveUserToken(ManagerVO managerVO, String jwtToken , List<String> authorities) {
		
		
				
				
		  TokenDTO token = new TokenDTO.Builder()
                  .id(managerVO.getManagerId())
                  .authorities(authorities)
                  .sub(managerVO.getManagerEmail())
                  .exp(jwtService.extractExpirationDate(jwtToken).getTime())
                  .iat(jwtService.extractIssuedAt(jwtToken).getTime())
                  .build();		
		  
		  tokenRepository.saveToken("managerId:" + managerVO.getManagerId(), token, jwtExpiration);
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
	    Authentication auth = null;
	    
	    try {
	        auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
	    } catch (Exception e) {
	        return new AuthenticationResponse.Builder().accessToken(e.getMessage()).build();
	    }

	    ManagerVO managerVO = (ManagerVO) auth.getPrincipal();
	    String encodedPassword = managerPasswordEncoder.encode(managerVO.getManagerPassword());
	    Integer rolesNo = managerVO.getManagerPer();
	    
	    List<String> authorities = new ArrayList<>();
	    authorities.add("ROLE_MANAGER");
	    if (rolesNo == 10) {
	        authorities.add("ROLE_MANAGERJWT");
	    } else if (rolesNo == 20){
	        authorities.add("ROLE_MANAGERJWTV2");
	    }
	    
	    // 生成JWT令牌
	    Map<String, Object> extraClaims = new HashMap<>();
	    extraClaims.put("authorities", authorities);
	    extraClaims.put("id", managerVO.getManagerId());
	    UserDetails userDetails = User.builder()
	            .username(managerVO.getManagerEmail())
	            .password(encodedPassword)
	            .authorities(authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()))
	            .build();
	    
	    String jwtToken  = null;
	    if (authorities.contains("ROLE_MANAGERJWTV2")) {
		     jwtToken = jwtService.generateShortToken(extraClaims, userDetails);
	    } else {
		     jwtToken = jwtService.generateToken(extraClaims, userDetails);
	    }
	    
	    
	    
	    // 生成刷新令牌
	    String refreshToken = jwtService.generateRefreshToken(userDetails);

	    // 將JWT令牌和刷新令牌存儲到數據庫中
	    revokeUserToken(userDetails, managerVO.getManagerId());
	    saveUserToken(managerVO, jwtToken,authorities);
	    Optional<TokenDTO> tokenDto =tokenRepository.findToken("managerId:"+managerVO.getManagerId());
	    
//	   if( tokenDto.isPresent()) {
//		    TokenDTO token = tokenDto.get();
//
//		   System.out.println(token.getSub());
//	   }
	    // 設置身份驗證上下文
	    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(managerVO, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
	    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

	    
	    // 返回身份驗證響應
	    return new AuthenticationResponse.Builder().accessToken(jwtToken).refreshToken(refreshToken).build();
	}


	private void revokeUserToken(UserDetails userdetails, Integer id) {

		Optional<TokenDTO> validUserToken = tokenRepository.findToken("managerId:" + id);
		if (validUserToken.isEmpty()) {
			return;
		}
		tokenRepository.revokeToken("managerId:" + id);
	}

	public void refreshToken(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userEmail;
		AuthenticationResponse authRes = null;

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			authRes = new AuthenticationResponse.Builder().accessToken("Not Valid").refreshToken("").build();
			return;
		}

		refreshToken = authHeader.substring(7);
		userEmail = jwtService.extractUsername(refreshToken);

		ManagerVO managerVO = managerService.findByOnlyOneEmail(userEmail);

		String encodedPassword = managerPasswordEncoder.encode(managerVO.getManagerPassword());
		UserDetails userdetails = User.builder().username(managerVO.getManagerEmail()).password(encodedPassword)
				.roles("MANAGER").build();

		if (jwtService.isTokenValid(refreshToken, userdetails)) {
			var accessToken = jwtService.generateToken(userdetails);
			revokeUserToken(userdetails, managerVO.getManagerId());
			saveUserToken(managerVO, accessToken ,List.of("ROLE_MANAGER"));

			// 構建新的身份驗證響應

			 authRes = new AuthenticationResponse.Builder().accessToken(accessToken)
					.refreshToken(refreshToken).build();

		}else {
			   authRes = new AuthenticationResponse.Builder()
		                .accessToken("Token has been expired")
		                .refreshToken("")
		                .build();
		}

		// 如果無法刷新令牌，則返回未授權狀態碼
		new ObjectMapper().writeValue(response.getOutputStream(), authRes);
	}
}
