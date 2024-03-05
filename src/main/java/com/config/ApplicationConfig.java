package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.seller.repositary.SellerRepository;

@Configuration
public class ApplicationConfig {

	private final SellerRepository sellerRepo;

	public ApplicationConfig(SellerRepository sellerRepo) {
		super();
		this.sellerRepo = sellerRepo;
	}

//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//		return config.getAuthenticationManager();
//	}

	@Bean
	// 用戶輸入的密碼會加密過，而非使用明文去做存入
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
