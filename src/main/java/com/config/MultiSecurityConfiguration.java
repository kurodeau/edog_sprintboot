package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		// retrieve builder from httpSecurity

		
        return new ProviderManager(daoAuthenticationProvider1(),daoAuthenticationProvider2());


	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeRequests(
				authorize -> authorize.antMatchers("/css/**", "/vendors/**", "/images/**", "/mainjs/**").permitAll()

						.antMatchers("/seller/register").permitAll()
						.antMatchers("/seller/register/check").permitAll()
						.antMatchers("/buyer/register").permitAll()
						.antMatchers("/buyer/register/check").permitAll()
						.antMatchers("/front/seller/**").hasRole("SELLER")
//             .antMatchers("/seller/login").permitAll() // Permit access to the specific URL
//             .antMatchers("/seller/login/check").permitAll() 
						// .antMatchers("/**").permitAll() // Permit access to the specific URL
						.anyRequest().authenticated())
//         .formLogin(form -> form.loginPage("/seller/login").usernameParameter("usernameinhtml").passwordParameter("passwordinhtml"));
				.formLogin().successHandler(sellerAuthenticationSuccessHandler).failureUrl("/loginXXXXX");


		http.csrf().disable();
		// 配置表单登录

		// 配置基本身份验证
		// .httpBasic(withDefaults());
		return http.build();
	}

}
