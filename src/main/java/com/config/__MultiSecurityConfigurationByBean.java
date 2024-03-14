//package com.config;
//
//import java.nio.file.spi.FileSystemProvider;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//import com.buyer.entity.BuyerVO;
//import com.buyer.service.BuyerService;
//import com.seller.entity.SellerVO;
//import com.seller.service.SellerService;
//
//@Configuration
//// @EnableWebSecurity開啟SpringSecurity的自訂義配置，SpringBoot 中可省略
//@EnableWebSecurity
//// EnableMethodSecurity 启用对 @PreAuthorize, @Secured, 和 @RolesAllowed
//// 等注解的支持，SpringBoot 中可省略
//@EnableMethodSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启方法级别的安全性
//public class MultiSecurityConfigurationByBean {
//
//	private static final String[] WHITE_LIST_URL = { "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
//			"/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
//			"/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html" };
//
//	@Autowired
//	@Qualifier("sellerAuthenticationSuccessHandler")
//	private AuthenticationSuccessHandler sellerAuthenticationSuccessHandler;
//
//	@Autowired
//	CustomAccessDeniedHandler customAccessDeniedHandler;
//
//	@Autowired
//	CustomAccessDeniedHandler2 customAccessDeniedHandler2;
//
//	@Autowired
//	@Qualifier("buyerDetailsService") // Use the correct qualifier if needed
//	private BuyerDetailsService buyerDetailsService;
//
//
//
//	@Autowired
//	@Qualifier("buyerAuthenticationSuccessHandler")
//	private AuthenticationSuccessHandler buyerAuthenticationSuccessHandler;
//
//	@Autowired
//	SellerService sellerSvc;
//
//	@Autowired
//	BuyerService buyerSvc;
//
//	@Autowired
//	@Qualifier("sellerDetailsService") // Use the correct qualifier if needed
//	private UserDetailsService sellerDetailsService;
//
//	@Autowired
//	@Qualifier("sellerPasswordEncoder")
//	private SellerPasswordEncoder sellerPasswordEncoder;
//	
//	
//	 @Bean
//	    public GossipAuthenticationProvider gossipAuthenticationProvider() {
//	        return new GossipAuthenticationProvider();
//	    }
//
//	@Bean
//	public DaoAuthenticationProvider daoAuthenticationProvider1() {
//		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//		authenticationProvider.setUserDetailsService(sellerDetailsService);
//		authenticationProvider.setPasswordEncoder(sellerPasswordEncoder);
//		return authenticationProvider;
//	}
////
////	@Bean
////	public DaoAuthenticationProvider daoAuthenticationProvider2() {
////		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
////		authenticationProvider.setUserDetailsService(buyerDetailsService);
////		authenticationProvider.setPasswordEncoder(buyerPasswordEncoder);
////		return authenticationProvider;
////	}
//
////	@Order(Ordered.HIGHEST_PRECEDENCE)                                                      
////    @Bean
////    SecurityWebFilterChain apiHttpSecurity(ServerHttpSecurity http) {
////        http
////            .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/**"))      
////            .authorizeExchange((exchanges) -> exchanges
////                .anyExchange().authenticated()
////            )
////            .oauth2ResourceServer(OAuth2ResourceServerSpec::jwt);                           
////        return http.build();
////    }
//
////    @Bean
////    SecurityWebFilterChain webHttpSecurity(ServerHttpSecurity http) {                       
////        http.securityMatcher("/user/**)
////            .authorizeExchange((exchanges) -> exchanges
////                .anyExchange().authenticated()
////            )
////            .httpBasic();
////        
////        return http.build();
////    }
//
//	@Bean
//	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//		// retrieve builder from httpSecurity
//		return new ProviderManager(gossipAuthenticationProvider());
//	}
//
//	@Bean
//	public WebSecurityCustomizer webSecurityCustomizer() {
//		return (web) -> web.ignoring().antMatchers("/auth/phone", "/auth/phone/check", "/image/**", "/css/**",
//				"/vendors/**", "/mainjs/**", "/icons/**");
//	}
//
//	@Bean
//	@Order(1)
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//			System.out.println("filterChainfilterChainfilterChainfilterChainfilterChain");
//		// TESTING
////		http.authorizeRequests(authorize -> authorize
////				.antMatchers("/**").permitAll()
////				.anyRequest().authenticated());
//		http.authenticationManager(authenticationManager(http));
//		System.out.println();
//		// FORMAL
////		http.authorizeRequests(authorize -> authorize.antMatchers("/auth/phone/check", "/auth/phone").permitAll()
////				.antMatchers("/seller/register", "/seller/register/**").permitAll()
////				.antMatchers("/buyer/register", "/buyer/register/**").permitAll().antMatchers("/front/seller/report")
////				.hasAnyRole("SELLERLV2", "SELLERLV3").antMatchers("/front/seller/**").hasRole("SELLER")
////				.antMatchers("/front/buyer/**").hasRole("BUYER").antMatchers("/auth/email/check", "/auth/email")
////				.permitAll().antMatchers("/activate/seller/**").permitAll().antMatchers("/").permitAll());
//
//
//		http.authorizeRequests(authorize -> authorize.antMatchers("/front/seller/**").hasRole("SELLER"));
//		http.formLogin(form -> form.loginPage("/seller/login").permitAll().loginProcessingUrl("/seller/login")
//				.usernameParameter("usernameinhtml").passwordParameter("passwordinhtml")
//				.successHandler(sellerAuthenticationSuccessHandler))
//				.exceptionHandling(customizer -> customizer.accessDeniedHandler(customAccessDeniedHandler)).csrf()
//				.disable();
//		
//
//		
////		http.authorizeRequests(authorize -> authorize.antMatchers("/front/buyer/**").hasRole("BUYER"));
////		http.formLogin(form -> form.loginPage("/buyer/login").permitAll().loginProcessingUrl("/buyer/login")
////				.usernameParameter("usernameinhtml").passwordParameter("passwordinhtml")
////				.successHandler(sellerAuthenticationSuccessHandler))
////				.exceptionHandling(customizer -> customizer.accessDeniedHandler(customAccessDeniedHandler)).csrf()
////				.disable();
//		
//
////		http.formLogin(form -> form
////				.loginPage("/buyer/login").permitAll()
////				.usernameParameter("usernameinhtml")
////				.passwordParameter("passwordinhtml")
////				.successHandler(buyerAuthenticationSuccessHandler))
////				.exceptionHandling(customizer -> customizer.accessDeniedHandler(customAccessDeniedHandler))
////				.csrf().disable();
//
//		return http.build();
//	}
//
//	@Bean
//	@Order(2)
//	public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
//
//		// TESTING
////		http.authorizeRequests(authorize -> authorize
////				.antMatchers("/**").permitAll()
////				.anyRequest().authenticated());
//		
//	
//		http.authenticationManager(authenticationManager(http));
//
//		System.out.println("filterChain2filterChain2filterChain2filterChain2filterChain2filterChain2");
//
//		
//		http.authorizeRequests(authorize -> authorize.antMatchers("/front/buyer/**").hasRole("BUYER"));
//		http.formLogin(form -> form.loginPage("/buyer/login").permitAll().loginProcessingUrl("/buyer/login")
//				.usernameParameter("usernameinhtml").passwordParameter("passwordinhtml")
//				.successHandler(buyerAuthenticationSuccessHandler))
//				.exceptionHandling(customizer -> customizer.accessDeniedHandler(customAccessDeniedHandler)).csrf()
//				.disable();
//		
//
//		return http.build();
//	}
//
//	private class GossipAuthenticationProvider implements AuthenticationProvider {
//
//		public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//			String name = authentication.getName();
//			String password = authentication.getCredentials().toString();
//
//			// Split the name using "-http"
//			String[] parts = name.split("-http");
//			System.out.println(parts[0]);
//			// john.doe@example.com
//			System.out.println(parts[1]);
//			// ://localhost:8081/seller/login?error
//
//			// Check if there is a second part
//			// Check if there is a second part
//			if (parts.length < 2) {
//				throw new UsernameNotFoundException("Invalid Input");
//			}
//
//			// The second part is the true name
//			String trueName = parts[0];
//
//			if (parts[1].contains("seller")) {
//				// Fetch seller details by email
//				SellerVO sellerVO = sellerSvc.findByOnlyOneEmail(trueName);
//				System.out.println("GossipAuthenticationProvider" + sellerVO);
//				// Check if the sellerVO is not null and the password matches
//				if (sellerVO != null) {
//					if (!sellerVO.getIsConfirm()) {
//						throw new BadCredentialsException("帳戶尚未被啟用，請於信箱收信");
//					}
//
//					if (sellerPasswordEncoder.matches(password, sellerVO.getSellerPassword())) {
//
//						return new UsernamePasswordAuthenticationToken(trueName, password,
//								AuthorityUtils.createAuthorityList("ROLE_SELLER"));
//					} else {
//						throw new BadCredentialsException("密碼輸入有誤");
//					}
//				} else {
//					throw new UsernameNotFoundException("帳號輸入有誤 SB");
//				}
//			} else if (parts[1].contains("buyer")) {;
//				// Fetch buyer details by email (assuming you have a buyer service)
//				// BuyerVO buyerVO = buyerSvc.findByOnlyOneEmail(trueName);
//			
//				BuyerVO buyerVO = buyerSvc.findByOnlyOneEmail(trueName);
//				System.out.println(buyerVO);
//				System.out.println(password);
//
//				// Check if the buyerVO is not null and the password matches
//				if (buyerVO != null && buyerPasswordEncoder.matches(password, buyerVO.getMemberPassword())) {
//					return new UsernamePasswordAuthenticationToken(trueName, password,
//							AuthorityUtils.createAuthorityList("ROLE_BUYER"));
//				} else {
//					throw new UsernameNotFoundException("Invalid credentials for buyer");
//				}
//			} else {
//				// Handle other cases or throw an exception for unsupported users
//				throw new UsernameNotFoundException("Invalid user type");
//			}
//		}
//
//		@Override
//		public boolean supports(Class<?> authentication) {
//			return authentication.equals(UsernamePasswordAuthenticationToken.class);
//		}
//	}
//
//}
