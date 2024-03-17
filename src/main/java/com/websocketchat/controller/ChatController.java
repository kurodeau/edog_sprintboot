package com.websocketchat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.seller.entity.SellerVO;

@RequestMapping("/front/seller")
@Controller
public class ChatController {

	
	@PostMapping(value="getusername" , produces = "application/json")
	public ResponseEntity<?> getUserName(){
		
		
		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        SellerVO sellerVO = (SellerVO) authentication.getPrincipal();

        String sellerName = sellerVO.getSellerCompany();
        
    	Gson gson = new Gson();
		
        String userName = gson.toJson(sellerName);
		
		return ResponseEntity.ok(userName);
	}
	
	
	@GetMapping("chatroom")
		public String chatRoom (Model model) {
			return "front-end/seller/seller-chat";
		}
	
	
	
	
	
	
}
