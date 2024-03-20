package com.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.buyer.entity.BuyerVO;
import com.manager.ManagerPasswordEncoder;
import com.manager.ManagerService;
import com.manager.ManagerVO;
import com.seller.entity.SellerVO;
import com.seller.service.SellerService;

@Configuration
// @EnableWebSecurity開啟SpringSecurity的自訂義配置，SpringBoot 中可省略
@EnableWebSecurity
// EnableMethodSecurity 启用对 @PreAuthorize, @Secured, 和 @RolesAllowed
// 等注解的支持，SpringBoot 中可省略
@EnableMethodSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启方法级别的安全性
public class MultiSecurityConfiguration {

	private static final String[] WHITE_LIST_URL = { "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
			"/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
			"/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html" };

	// 處理驗證後成功
	@Autowired
	@Qualifier("sellerAuthenticationSuccessHandler")
	private AuthenticationSuccessHandler sellerAuthenticationSuccessHandler;

	// 處理403權限錯誤
	@Autowired
	CustomAccessDeniedHandler customAccessDeniedHandler;

	@Autowired
	SellerService sellerSvc;

	//
	@Autowired
	@Qualifier("sellerDetailsService") // Use the correct qualifier if needed
	private UserDetailsService sellerDetailsService;

	@Autowired
	@Qualifier("sellerPasswordEncoder")
	private SellerPasswordEncoder sellerPasswordEncoder;

	@Bean
	public GossipAuthenticationProvider gossipAuthenticationProvider() {
		return new GossipAuthenticationProvider();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider1() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(sellerDetailsService);
		authenticationProvider.setPasswordEncoder(sellerPasswordEncoder);
		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		// retrieve builder from httpSecurity
		return new ProviderManager(gossipAuthenticationProvider());
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/auth/phone", "/auth/phone/check", "/image/**", "/css/**",
				"/vendors/**", "/mainjs/**", "/icons/**");
	}

	/////////////////// MANAGER 配置///////////////////////////
	@Autowired
	private ManagerService managerSvc;

	@Autowired
	@Qualifier("managerPasswordEncoder")
	private ManagerPasswordEncoder managerPasswordEncoder;

	@Autowired
	private TokenRepository tokenRepository;
	
	@Autowired
	private JwtService jwtSvc;

	//////////////////// BUYER 配置///////////////////////////

	@Autowired
	MultiFilterConfig multiFilterConfig;
	//
	// @Autowired
	// @Qualifier("buyerRequestMatcher")
	// RequestMatcher buyerRequestMatcher;
	//
	// @Autowired
	// private BuyerService buyerSvc;
	//
	// @Autowired
	// @Qualifier("buyerPasswordEncoder")
	// private BuyerPasswordEncoder buyerPasswordEncoder;
	//
	// @Autowired
	// private BuyeAuthenticationFailureHandler buyeAuthenticationFailureHandler;

	/////////////////// 針對沒登入使用者的配置///////////////////////////

	@Autowired
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	/////////////////// filterChain配置///////////////////////////

	@Value("${myserver.back.entry}")
	String backEntryPoint;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		BuyerAuthenticationFilter buyerAuthenticationFilter = multiFilterConfig
				.createBuyerAuthenticationFilter(authenticationManager(http));
		JwtAuthenticationFilter jwtAuthenticationFilter = multiFilterConfig
				.createJwtAuthenticationFilter(authenticationManager(http));

		http.authorizeRequests(authorize -> authorize
				.antMatchers("/front/seller/report").hasAnyRole("SELLERLV2", "SELLERLV3")
				.antMatchers("/front/seller/ad/**").hasAnyRole("SELLERLV2", "SELLERLV3")
				.antMatchers("/front/seller/**").hasRole("SELLER")
				.antMatchers("/front/buyer/**").hasRole("BUYER").antMatchers("/back/" + backEntryPoint + "/login").permitAll()
				.antMatchers("/back/api/v1/auth/authenticate").permitAll()
				.antMatchers("/back/seller/list").hasRole("MANAGERJWT")

        .antMatchers("/back/**").hasRole("MANAGER")
				.antMatchers("/front/forum/**").hasRole("BUYER"))

				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

				.addFilterBefore(buyerAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		http.formLogin(form -> form.loginPage("/seller/login").loginProcessingUrl("/seller/login")
				.usernameParameter("usernameinhtml").passwordParameter("passwordinhtml")
				.successHandler(sellerAuthenticationSuccessHandler))
				.exceptionHandling(customizer -> customizer.accessDeniedHandler(customAccessDeniedHandler)
						.authenticationEntryPoint(customAuthenticationEntryPoint))
				.csrf().disable().authenticationManager(authenticationManager(http))
					.logout(logout -> logout.logoutUrl("/logout") // 配置登出 URL
						.logoutSuccessHandler((request, response, authentication) -> {
							if (authentication==null) {return;}
							Object targetVO = authentication.getPrincipal();
						
							final String authHeader = request.getHeader("Authorization");
							final String jwt;
							String finalPath = "/";
							
							
							System.out.println("authHeader"+authHeader);
							
							
							if (targetVO != null && targetVO instanceof BuyerVO) {
								SecurityContextHolder.clearContext();
								finalPath = "/buyer/login";

							} else if (targetVO != null && targetVO instanceof SellerVO) {
								SecurityContextHolder.clearContext();
								finalPath = "/seller/login";
							} else if (authHeader != null && authHeader.startsWith("Bearer ")) {
								jwt = authHeader.substring(7);
								Integer userId = jwtSvc.extractId(jwt);
								String redisTokenKey = "managerId:" + userId;
								var storedToken = tokenRepository.findToken(redisTokenKey);

								if (storedToken != null) {
									tokenRepository.revokeToken(redisTokenKey);
									SecurityContextHolder.clearContext();

								}
								finalPath = "/buyer/login";
							}
							
							// 這個處理器將應用於所有的登出請求
							response.sendRedirect(request.getContextPath() + finalPath);

						}));

		return http.build();
	}

	private class GossipAuthenticationProvider implements AuthenticationProvider {

		public Authentication authenticate(Authentication authentication) throws AuthenticationException {

			if (authentication != null && authentication.isAuthenticated()) {
				// 如果已存在secCtx(買家) 返回
				return authentication;
			}

			String name = authentication.getName();
			String password = authentication.getCredentials().toString();

			// Split the name using "-http"
			String[] parts = name.split("-http");
			// Check if there is a second part
			if (parts.length < 2) {
				throw new UsernameNotFoundException("Invalid Input");
			}

			// The second part is the true name
			String trueName = parts[0];

			if (parts[1].contains("seller")) {
				// Fetch seller details by email
				SellerVO sellerVO = sellerSvc.findByOnlyOneEmail(trueName);
				if (sellerVO != null) {
					if (!sellerVO.getIsConfirm()) {
						throw new BadCredentialsException("帳戶尚未被啟用，請靜待平台審核");
					}

					if (sellerPasswordEncoder.matches(password, sellerVO.getSellerPassword())) {
						List<SimpleGrantedAuthority> authorities = new ArrayList<>();

						switch (sellerVO.getSellerLvId().getSellerLvId()) {

						case 1:
							authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
							authorities.add(new SimpleGrantedAuthority("ROLE_SELLERLV1"));
							break;
						case 2:
							authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
							authorities.add(new SimpleGrantedAuthority("ROLE_SELLERLV2"));
							break;
						case 3:
							authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
							authorities.add(new SimpleGrantedAuthority("ROLE_SELLERLV3"));
							break;
						}
						return new UsernamePasswordAuthenticationToken(sellerVO, password, authorities);
					} else {
						throw new BadCredentialsException("密碼輸入有誤");
					}
				} else {
					throw new UsernameNotFoundException("沒有此帳戶");
				}
			}

			if (parts[1].contains("back") && parts[1].contains("auth/authenticate")) {
				ManagerVO managerVO = managerSvc.findByOnlyOneEmail(trueName);

				if (managerVO == null) {
					throw new UsernameNotFoundException("沒有此帳戶");
				}

				if (!managerPasswordEncoder.matches(password, managerVO.getManagerPassword())) {
					throw new BadCredentialsException("密碼輸入有誤");
				}

				List<SimpleGrantedAuthority> authorities = new ArrayList<>();
				authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));

				return new UsernamePasswordAuthenticationToken(managerVO, password, authorities);
			}

			throw new UsernameNotFoundException("路徑錯誤");

		}

		@Override
		public boolean supports(Class<?> authentication) {
			return authentication.equals(UsernamePasswordAuthenticationToken.class);
		}
	}

}
