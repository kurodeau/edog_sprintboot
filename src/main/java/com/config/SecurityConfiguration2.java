package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// @EnableWebSecurity開啟SpringSecurity的自訂義配置，SpringBoot 中可省略
@EnableWebSecurity
// EnableMethodSecurity 启用对 @PreAuthorize, @Secured, 和 @RolesAllowed 等注解的支持，SpringBoot 中可省略
@EnableMethodSecurity
public class SecurityConfiguration2 {

	private static final String[] WHITE_LIST_URL = { "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
			"/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
			"/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html" };



	// (1 使用者提供 UserDetailsService Bean時，Spring會取消預設配置
	// (1.1 把資料存在在Spring Security記憶體中
//	@Bean
//	public UserDetailsService userDetailsService() {
//		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		// (1.1.1 創建一個名為user的使用者，密碼為明文

//		UserDetails user = User.builder().username("user").password("{noop}Password").roles("USER").build();
//		return new InMemoryUserDetailsManager(user);

		// (1.1.2 創建一個名為user的使用者，密碼為明文
		// SpringSecurity自動使用InMemoryDetailsManager的loadUserByUserName方法從內存獲取User對象
		// 在UsernamePasswordAuthenticationFilter過濾器中的attemptAuthenitication方法中
		// 將用戶輸入的用戶密碼(明文)內存獲得的密碼 (加密文) 進行比較及驗證
		// 可以使用 login底下的TestPassword測試
//		UserDetails admin = User.builder().username("admin").password(passwordEncoder().encode("abc"))
//				.roles("USER", "ADMIN").build();
//		return new InMemoryUserDetailsManager(admin);
//
//	}

	// (2) 創建基於數據庫的存取層 (也可在DBSellerDetailsManager使用Component註解，這樣就不用寫Bean)
//	@Bean
//	public UserDetailsService userDetailsService() {
//		DBSellerDetailsManager manager = new DBSellerDetailsManager();
//		return manager;
//	}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	 http
         .authorizeRequests(authorize -> authorize.
                 antMatchers("/css/**", "/vendors/**", "/images/**", "/mainjs/**").permitAll()

             .antMatchers("/seller/register").permitAll() // Permit access to the specific URL
             .antMatchers("/seller/register/check").permitAll() 
//             .antMatchers("/seller/login").permitAll() // Permit access to the specific URL
//             .antMatchers("/seller/login/check").permitAll() 
             //             .antMatchers("/**").permitAll() // Permit access to the specific URL
             .anyRequest().authenticated()
         )
//         .formLogin(form -> form.loginPage("/seller/login").usernameParameter("usernameinhtml").passwordParameter("passwordinhtml"));
    	 .formLogin();

     http.csrf().disable();
        // 配置表单登录
     
        // 配置基本身份验证
        // .httpBasic(withDefaults());
        return http.build();
    }



}
