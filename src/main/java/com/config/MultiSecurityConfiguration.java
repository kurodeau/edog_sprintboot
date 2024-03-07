package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.buyer.entity.BuyerVO;
import com.buyer.service.BuyerService;
import com.seller.entity.SellerVO;
import com.seller.service.SellerService;

@Configuration
// @EnableWebSecurity開啟SpringSecurity的自訂義配置，SpringBoot 中可省略
@EnableWebSecurity
// EnableMethodSecurity 启用对 @PreAuthorize, @Secured, 和 @RolesAllowed 等注解的支持，SpringBoot 中可省略
@EnableMethodSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启方法级别的安全性
public class MultiSecurityConfiguration {

	private static final String[] WHITE_LIST_URL = { "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
			"/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
			"/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html" };

	@Autowired
	@Qualifier("sellerDetailsService") // Use the correct qualifier if needed
	private UserDetailsService sellerDetailsService;

	
	@Autowired
	@Qualifier("sellerPasswordEncoder")
	private SellerPasswordEncoder sellerPasswordEncoder;

	@Autowired
	@Qualifier("sellerAuthenticationSuccessHandler")
	private AuthenticationSuccessHandler sellerAuthenticationSuccessHandler;
	
	@Autowired
	@Qualifier("buyerDetailsService") // Use the correct qualifier if needed
	private BuyerDetailsService buyerDetailsService;

	@Autowired
	@Qualifier("buyerPasswordEncoder")
	private BuyerPasswordEncoder buyerPasswordEncoder;

	@Autowired
	SellerService sellerSvc;
	
	@Autowired
	BuyerService buyerSvc;
	
	@Bean
    public DaoAuthenticationProvider daoAuthenticationProvider1() {
        DaoAuthenticationProvider authenticationProvider 
                                  = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(sellerDetailsService);
        authenticationProvider.setPasswordEncoder(sellerPasswordEncoder);
        return authenticationProvider;
    }
	
	
	@Bean
    public DaoAuthenticationProvider daoAuthenticationProvider2() {
        DaoAuthenticationProvider authenticationProvider 
                                  = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(buyerDetailsService);
        authenticationProvider.setPasswordEncoder(buyerPasswordEncoder);
        return authenticationProvider;
    }

	
	private class GossipAuthenticationProvider implements AuthenticationProvider {
		
		 public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		        String name = authentication.getName();
		        String password = authentication.getCredentials().toString();

		        // Split the name using "-http"
		        String[] parts = name.split("-http");
		        System.out.println(parts[0]);
		        // john.doe@example.com
		        System.out.println(parts[1]);
		        // ://localhost:8081/seller/login?error


		        // Check if there is a second part
		     // Check if there is a second part
		        if (parts.length < 2) {
		            throw new UsernameNotFoundException("Invalid Input");
		        }

		        // The second part is the true name
		        String trueName = parts[0];
		       
		        if (parts[1].contains("seller")) {
		            // Fetch seller details by email
		            SellerVO sellerVO = sellerSvc.findByOnlyOneEmail(trueName);
		            System.out.println(sellerVO);
		            // Check if the sellerVO is not null and the password matches
		            if (sellerVO != null ) {
		            	if (!sellerVO.getIsConfirm()) {
		            		throw new BadCredentialsException("帳戶尚未被啟用，請於信箱收信");
		            	}
		            	
		            	if(sellerPasswordEncoder.matches(password, sellerVO.getSellerPassword())) {
		            		
		            		 return new UsernamePasswordAuthenticationToken(
				                        trueName,
				                        password,
				                        AuthorityUtils.createAuthorityList("ROLE_SELLER"));
		            	} else {
			                throw new BadCredentialsException("密碼輸入有誤");
		            	}
		            } else {
		                throw new UsernameNotFoundException("帳號輸入有誤 SB");
		            }
		        } else if (parts[0].contains("buyer")) {
		            // Fetch buyer details by email (assuming you have a buyer service)
//		            BuyerVO buyerVO = buyerSvc.findByOnlyOneEmail(trueName);
		        	  BuyerVO buyerVO = new BuyerVO();
		            // Check if the buyerVO is not null and the password matches
		            if (buyerVO != null && buyerPasswordEncoder.matches(password, buyerVO.getMemberPassword())) {
		                return new UsernamePasswordAuthenticationToken(
		                        trueName,
		                        password,
		                        AuthorityUtils.createAuthorityList("ROLE_BUYER"));
		            } else {
		                throw new UsernameNotFoundException("Invalid credentials for buyer");
		            }
		        } else {
		            // Handle other cases or throw an exception for unsupported users
		            throw new UsernameNotFoundException("Invalid user type");
		        }
		 }


        @Override
        public boolean supports(Class<?> authentication) {
            return authentication.equals(UsernamePasswordAuthenticationToken.class);
        }
    }
	
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		// retrieve builder from httpSecurity
        return new ProviderManager(new GossipAuthenticationProvider());
	}

	@Autowired
	CustomAccessDeniedHandler customAccessDeniedHandler;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeRequests(
				authorize -> authorize.antMatchers("/css/**", "/vendors/**", "/images/**", "/mainjs/**").permitAll()
						.antMatchers("/seller/register").permitAll()
						.antMatchers("/seller/register/check").permitAll()
						.antMatchers("/buyer/register").permitAll()
						.antMatchers("/buyer/register/check").permitAll()
						.antMatchers("/front/seller/**").hasRole("SELLER")
						.antMatchers("/front/buyer/**").hasRole("BUYER")
						.antMatchers("/seller/login").permitAll() // Permit access to the specific URL
//             .antMatchers("/seller/login/check").permitAll() 
						// .antMatchers("/**").permitAll() // Permit access to the specific URL
						.anyRequest().authenticated())
         .formLogin(form -> form.loginPage("/seller/login").usernameParameter("usernameinhtml").passwordParameter("passwordinhtml").successHandler(sellerAuthenticationSuccessHandler));
         // .successHandler(sellerAuthenticationSuccessHandler).failureUrl("/loginXXXXX");
		
		http.exceptionHandling(customizer -> customizer.accessDeniedHandler(customAccessDeniedHandler));
		http.csrf().disable();
		// 配置表单登录

		// 配置基本身份验证
		// .httpBasic(withDefaults());
		return http.build();
	}

}
