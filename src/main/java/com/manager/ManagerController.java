package com.manager;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/back")
public class ManagerController  {

//	@PostMapping("/login")
//	public ResponseEntity<?> login(@RequestBody String username , @RequestBody String password ){
//		
//		
//		
//		return ResponseEntity.ok(new JwtResponse(jwtToken));
//	}
//	
//	
//	
//	 @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//        // 實現 Manager 登入的邏輯，例如驗證用戶名和密碼等
//        // ...
//
//        // 登入成功後，生成 JWT Token
//        UserDetails userDetails = managerDetailsService.loadUserByUsername(loginRequest.getUsername());
//        String jwtToken = jwtService.generateToken(userDetails);
//
//        // 返回 JWT Token 給 Manager
//        return ResponseEntity.ok(new JwtResponse(jwtToken));
//    }
	
 

}
