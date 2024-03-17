package com.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/back/api/v1/auth")
public class AuthenticationRestController {
	
	@Autowired
	private AuthenticationService authSvc;

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
		return ResponseEntity.ok(authSvc.register(request));
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request , HttpServletRequest req) {		
		
		// 為了AuthProvider(因為要區分各種用戶)
		String fullUrl =req.getRequestURL().toString();
		request.setEmail(request.getEmail()+"-"+fullUrl);
		
		
		return ResponseEntity.ok(authSvc.authenticate(request));
	}

//  @PostMapping("/refresh-token")
//  public void refreshToken(
//      HttpServletRequest request,
//      HttpServletResponse response
//  ) throws IOException {
//	  authSvc.refreshToken(request, response);
//  }

}
