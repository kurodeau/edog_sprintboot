package com.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	@Autowired
	private ChangeHeaderInfoInterceptor  changeHeaderInfoInterceptor ;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/seller/main"); 
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/seller/report"); 
	}

}