//package com.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfiguration {
//
//	@Autowired
//	private UserDetailsService userDetailsService;
//
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http.authorizeRequests(
//				authorize -> authorize.antMatchers("/css/**", "/vendors/**", "/images/**", "/mainjs/**").permitAll()
//
//						.antMatchers("/seller/register").permitAll() // Permit access to the specific URL
//						.antMatchers("/seller/register/check").permitAll()
////        .antMatchers("/seller/login").permitAll() // Permit access to the specific URL
////        .antMatchers("/seller/login/check").permitAll() 
//						// .antMatchers("/**").permitAll() // Permit access to the specific URL
//						.anyRequest().authenticated())
////    .formLogin(form -> form.loginPage("/seller/login").usernameParameter("usernameinhtml").passwordParameter("passwordinhtml"));
//				.formLogin();
//
//		http.csrf().disable();
//		// 配置表单登录
//
//		// 配置基本身份验证
//		// .httpBasic(withDefaults());
//		return http.build();
//	}
//
//}
