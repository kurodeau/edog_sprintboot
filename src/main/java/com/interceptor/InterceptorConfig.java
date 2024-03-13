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
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/sellerLv/edit"); 
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/seller/seller/edit"); 
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/seller/product/add"); 
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/seller/report");
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/seller/product/productlist"); 
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/seller/productorder/productorderlist"); 
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/seller/productorder/productordersearch"); 
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/seller/ad/add"); 
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/seller/ad/adlist"); 

		
	}

}