package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.seller.repositary.SellerRepository;

@Configuration
public class ApplicationConfig {


//	private final SellerRepository sellerRepo;
//    private final SellerDetailsService sellerDetailsService;
//
//    @Autowired
//    public ApplicationConfig(SellerRepository sellerRepo, SellerDetailsService sellerDetailsService) {
//        this.sellerRepo = sellerRepo;
//        this.sellerDetailsService = sellerDetailsService;
//    }
    


//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//		return config.getAuthenticationManager();
//	}

	// (0 使用者提供 PasswordEncoder Bean時，Spring會取消預設配置，配合1.1.2
	@Bean
	// 用戶輸入的密碼會加密過，而非使用明文去做存入
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	
//	@Bean
//	public AuthenticationProvider buyerAuthenticationProvider() {
//	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//	    authProvider.setUserDetailsService(buyerDetailsService());
//	    authProvider.setPasswordEncoder(passwordEncoder());
//	    return authProvider;
//	}

	 
//	@Bean
//	public AuthenticationProvider sellerAuthenticationProvider() {
//	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//	    authProvider.setUserDetailsService(sellerDetailsService);
//	    authProvider.setPasswordEncoder(passwordEncoder());
//	    return authProvider;
//	}

//	@Bean
//	public AuthenticationProvider adminAuthenticationProvider() {
//	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//	    authProvider.setUserDetailsService(adminDetailsService());
//	    authProvider.setPasswordEncoder(passwordEncoder());
//	    return authProvider;
//	}
}
