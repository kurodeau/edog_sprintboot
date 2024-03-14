package com.config;
//package com.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
//
//@Configuration
//// @EnableWebSecurity開啟SpringSecurity的自訂義配置，SpringBoot 中可省略
//@EnableWebSecurity
//// EnableMethodSecurity 启用对 @PreAuthorize, @Secured, 和 @RolesAllowed
//// 等注解的支持，SpringBoot 中可省略
//@EnableMethodSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启方法级别的安全性
//public class MultiSecurityConfigurationByConfig2 {
//
//	private static final String[] WHITE_LIST_URL = { "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
//			"/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
//			"/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html" };
//
//	@Autowired
//	CustomAccessDeniedHandler customAccessDeniedHandler;
//	
//	
//	    @Configuration
//	    @Order(1)
//	    public static class App1ConfigurationAdapter {
//	
//	    	
//	    	@Autowired
//	    	@Qualifier("sellerDetailsService") // Use the correct qualifier if needed
//	    	private UserDetailsService sellerDetailsService;
//	
//	    	@Autowired
//	    	@Qualifier("sellerPasswordEncoder")
//	    	private SellerPasswordEncoder sellerPasswordEncoder;
//	    	
//	    	
//	    	@Bean
//	    	public DaoAuthenticationProvider daoAuthenticationProvider1() {
//	    		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//	    		authenticationProvider.setUserDetailsService(sellerDetailsService);
//	    		authenticationProvider.setPasswordEncoder(sellerPasswordEncoder);
//	    		return authenticationProvider;
//	    	}
//	    	
//	    	
//	    	@Autowired
//	    	@Qualifier("sellerAuthenticationSuccessHandler")
//	    	private AuthenticationSuccessHandler sellerAuthenticationSuccessHandler;	
//	    	
//	        @Bean
//	        public SecurityFilterChain filterChainApp1(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
//	        	http.antMatcher("/front/seller/**").authenticationProvider(daoAuthenticationProvider1())
//	                .authorizeHttpRequests(auth -> auth.antMatchers("/front/seller/**").hasRole("SELLER"))
//	                // log in
//	                .formLogin(httpSecurityFormLoginConfigurer ->
//	                        httpSecurityFormLoginConfigurer.loginPage("/seller/login") 
//	                        .usernameParameter("usernameinhtml")
//	                        .passwordParameter("passwordinhtml")
//	                        .successHandler(sellerAuthenticationSuccessHandler)
//	                                .failureUrl("/seller/login?error=loginError"))
//	                // logout
//	//                .logout(httpSecurityLogoutConfigurer ->
//	//                        httpSecurityLogoutConfigurer.logoutUrl("/admin_logout")
//	//                                .logoutSuccessUrl("/")
//	//                                .deleteCookies("JSESSIONID"))
//	                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
//	                        httpSecurityExceptionHandlingConfigurer.accessDeniedPage("/403"))
//	                .csrf().disable();
//	
//	            return http.build();
//	        }
//	    }
//	    
//	    
//	    
//	
//	    
//	
//	
//	    @Configuration
//	    @Order(2)
//	    public static class App2ConfigurationAdapter {
//	
//	    	
//	    	@Autowired
//	    	@Qualifier("buyerDetailsService") // Use the correct qualifier if needed
//	    	private BuyerDetailsService buyerDetailsService;
//	
//	    	@Autowired
//	    	@Qualifier("buyerPasswordEncoder")
//	    	private BuyerPasswordEncoder buyerPasswordEncoder;
//	    	
//	    	@Bean
//	    	public DaoAuthenticationProvider daoAuthenticationProvider2() {
//	    		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//	    		authenticationProvider.setUserDetailsService(buyerDetailsService);
//	    		authenticationProvider.setPasswordEncoder(buyerPasswordEncoder);
//	    		return authenticationProvider;
//	    	}
//	    	    	
//	    	
//	    	@Autowired
//	    	@Qualifier("buyerAuthenticationSuccessHandler")
//	    	private AuthenticationSuccessHandler buyerAuthenticationSuccessHandler;
//	    	
//	        public SecurityFilterChain filterChainApp2(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
//	            
//	        	http.antMatcher("/front/buyer/**").authenticationProvider(daoAuthenticationProvider2())
//	                .authorizeHttpRequests(auth -> auth.antMatchers("/front/buyer/**").hasRole("BUYER").anyRequest().authenticated())
//	                // log in
//	                .formLogin(httpSecurityFormLoginConfigurer ->
//	                        httpSecurityFormLoginConfigurer.loginPage("/buyer/login") 
//	                        .usernameParameter("usernameinhtml")
//	                        .passwordParameter("passwordinhtml")
//	                        .successHandler(buyerAuthenticationSuccessHandler)
//	                                .failureUrl("/sellerlogin?error=loginError"))
//	                // logout
//	                .logout(httpSecurityLogoutConfigurer ->
//	                        httpSecurityLogoutConfigurer.logoutUrl("/admin_logout")
//	                                .logoutSuccessUrl("/")
//	                                .deleteCookies("JSESSIONID"))
//	                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
//	                        httpSecurityExceptionHandlingConfigurer.accessDeniedPage("/403"))
//	                .csrf().disable();
//	
//	            return http.build();
//	        }
//	    }
//	
//	
//	    
//	    
//    
//    
//    
//    
//// 網路抓來
//    
//    
//    
////  @Configuration
////  @Order(1)
////  public static class App1ConfigurationAdapter {
////
////  	
////  	@Autowired
////  	@Qualifier("sellerDetailsService") // Use the correct qualifier if needed
////  	private UserDetailsService sellerDetailsService;
////
////  	@Autowired
////  	@Qualifier("sellerPasswordEncoder")
////  	private SellerPasswordEncoder sellerPasswordEncoder;
////  	
////  	
////  	@Bean
////  	public DaoAuthenticationProvider daoAuthenticationProvider1() {
////  		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
////  		authenticationProvider.setUserDetailsService(sellerDetailsService);
////  		authenticationProvider.setPasswordEncoder(sellerPasswordEncoder);
////  		return authenticationProvider;
////  	}
////  	
////  	
////  	@Autowired
////  	@Qualifier("sellerAuthenticationSuccessHandler")
////  	private AuthenticationSuccessHandler sellerAuthenticationSuccessHandler;	
////  	
////    @Bean
////    public SecurityFilterChain filterChainApp1(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
////        http
////            .authorizeHttpRequests(auth -> auth.mvcMatchers( "/front/seller/**").hasRole("SELLER"))
////            // log in
////            .formLogin(httpSecurityFormLoginConfigurer ->
////                    httpSecurityFormLoginConfigurer.loginPage("/seller/login") 
////                    .usernameParameter("usernameinhtml")
////                    .passwordParameter("passwordinhtml")
////                    .successHandler(sellerAuthenticationSuccessHandler)
////                            .failureUrl("/seller/login?error=loginError"))
////            // logout
//////            .logout(httpSecurityLogoutConfigurer ->
//////                    httpSecurityLogoutConfigurer.logoutUrl("/admin_logout")
//////                            .logoutSuccessUrl("/")
//////                            .deleteCookies("JSESSIONID"))
////            .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
////                    httpSecurityExceptionHandlingConfigurer.accessDeniedPage("/403"))
////            .csrf(AbstractHttpConfigurer::disable);
////
////        return http.build();
////    }
////  }
//
//}
