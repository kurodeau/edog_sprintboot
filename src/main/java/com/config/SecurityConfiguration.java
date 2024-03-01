package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import com.seller.service.SellerService;

@Configuration
//開啟SpringSecurity的自訂義配置，SpringBoot 中可省略
@EnableWebSecurity
// 启用对 @PreAuthorize, @Secured, 和 @RolesAllowed 等注解的支持，SpringBoot 中可省略
@EnableMethodSecurity
public class SecurityConfiguration {

	private static final String[] WHITE_LIST_URL = { "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
			"/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
			"/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html" };

//    private final PasswordEncoder passwordEncoder;

    
//	public SecurityConfiguration(PasswordEncoder passwordEncoder) {
//		super();
//		this.passwordEncoder = passwordEncoder;
//	}

	// (1) 把資料存在在Spring Security記憶體中
//	@Bean
//	public UserDetailsService userDetailsService() {
//		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//		// (1.1 創建一個名為TestUser的使用者(withDefaultPasswordEncoder只建議在Demo展示)
//		
//		// SpringSecurity自動使用InMemoryDetailsManager的loadUserByUserName方法從內存ˊ翁獲取User對象
//		// 在UsernamePasswordAuthenticationFilter過濾器中的attemptAuthenitication方法中將用戶輸入的用戶密碼(明文) 和
//		// 內存獲得的密碼 (加密文) 進行比較及驗證
//		manager.createUser(
//				User.withDefaultPasswordEncoder().username("TestUser").password("password").roles("USER").build());
//		return manager;
//		
//		// * request Method必須為Post
//	}
	
	// (2) 創建基於數據庫的存取層
//	@Bean
//	public UserDetailsService userDetailsService() {
//		DBUserDetailsManagerSeller manager = new DBUserDetailsManagerSeller();
//		return manager;
//	}
	

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http
        // 禁用 CSRF 保护，特别是对于一些无需保护的端点或应用程序
        .csrf(csrf -> csrf.disable())
        
        // 配置 HTTP 请求的授权规则
        .authorizeHttpRequests(authorize -> authorize
            .anyRequest().authenticated()
        );
        //.formLogin(withDefaults())

        
        // 配置表单登录
        
        // 配置基本身份验证
        // .httpBasic(withDefaults());
        return http.build();
    }
	
	
	
	
	
	
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
////		return http;
//	}
	
	
//	@Bean
//	// 用戶輸入的密碼會加密過，而非使用明文去做存入
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}

}
