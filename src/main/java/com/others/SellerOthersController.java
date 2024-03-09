package com.others;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.seller.entity.SellerVO;

@Controller
@RequestMapping("/front/seller")
public class SellerOthersController {
	
	@GetMapping("/report")
	public String exportSellerExport() throws IOException{
		
		
		
		 return "/front-end/seller/seller-report";
	}
	
	
	
	
	@RequestMapping("/report/api/orders")
	public class OrderController {

	    @GetMapping("")
	    public ResponseEntity<?> allOrders() {
	        try {
	            SecurityContext secCtx = SecurityContextHolder.getContext();
	            Authentication authentication = secCtx.getAuthentication();
	            SellerVO sellerVO = (SellerVO) authentication.getPrincipal();
	            
	            
	            
	            
	            
	            return ResponseEntity.ok("Hello, "  + "! This is your orders endpoint.");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving orders: " + e.getMessage());
	        }
	    }
	}
	
	
	
}
