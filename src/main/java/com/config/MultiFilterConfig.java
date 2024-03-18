package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import com.buyer.service.BuyerService;
import com.manager.ManagerPasswordEncoder;
import com.manager.ManagerService;


@Component
public class MultiFilterConfig {


	//////////////////// BUYER 配置///////////////////////////

	@Autowired
	@Qualifier("buyerRequestMatcher")
	RequestMatcher buyerRequestMatcher;

	@Autowired
	private BuyerService buyerSvc;

	@Autowired
	@Qualifier("buyerPasswordEncoder")
	private BuyerPasswordEncoder buyerPasswordEncoder;

	@Autowired
	private BuyeAuthenticationFailureHandler buyeAuthenticationFailureHandler;

	/////////////////// MANAGER 配置///////////////////////////
	@Autowired
	JwtService jwtservice;
	
	@Autowired
	@Qualifier("managerRequestMatcher")
	RequestMatcher managerRequestMatcher;

	@Autowired
	@Qualifier("managerDetailsService") 
	UserDetailsService userDetailsService;
	
	
	@Autowired
	private ManagerService managerSvc;

	@Autowired
	@Qualifier("managerPasswordEncoder")
	private ManagerPasswordEncoder managerPasswordEncoder;

	@Bean
	public TokenRepository tokenRepository() {
		return new TokenRepository();
	}
	
  

    public BuyerAuthenticationFilter createBuyerAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new BuyerAuthenticationFilter(authenticationManager, buyerRequestMatcher, buyerSvc,
                buyerPasswordEncoder, buyeAuthenticationFailureHandler);
    }

    
    
    public JwtAuthenticationFilter createJwtAuthenticationFilter(AuthenticationManager authenticationManager) {
   
        return  new JwtAuthenticationFilter( jwtservice,
    			userDetailsService,
    			tokenRepository(),
    			managerRequestMatcher,
    			managerSvc);
    }
}