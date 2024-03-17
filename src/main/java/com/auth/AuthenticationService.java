package com.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
		saveUserToken(id, jwtToken);
		return new AuthenticationResponse.Builder().accessToken(jwtToken).refreshToken(refreshToken).build();
	}

	private void saveUserToken(Integer id, String jwtToken) {
		TokenDTO token = new TokenDTO.Builder().id(id).token(jwtToken).userType("MANAGER").build();
		tokenRepository.saveToken("managerId:" + id, token, jwtExpiration);
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {

		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		} catch (Exception e) {
			return new AuthenticationResponse.Builder().accessToken(e.getMessage()).build();
		}

		String trueEmail = request.getEmail().split("-http")[0];
		ManagerVO managerVO = managerService.findByOnlyOneEmail(trueEmail);

		
		String encodedPassword = managerPasswordEncoder.encode(managerVO.getManagerPassword());
		Integer rolesNo = managerVO.getManagerPer();
		
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
		if (rolesNo == 10) {
			authorities.add(new SimpleGrantedAuthority("ROLE_MANAGERJWT"));
		}
		UserDetails userdetails = User.builder().username(managerVO.getManagerEmail()).password(encodedPassword)
				.authorities(authorities).build();
		managerVO.setManagerPassword(encodedPassword);

		Map<String, Object> extraClaims = new HashMap<>();
		extraClaims.put("authorities", authorities);


		String jwtToken = jwtService.generateToken(extraClaims, userdetails);
		
		String refreshToken = jwtService.generateRefreshToken(userdetails);


		revokeUserToken(userdetails, managerVO.getManagerId());
		saveUserToken(managerVO.getManagerId(), jwtToken);
		
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(managerVO, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        
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
			saveUserToken(managerVO.getManagerId(), accessToken);

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
