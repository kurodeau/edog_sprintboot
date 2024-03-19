package com.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	@Autowired
	private ChangeHeaderInfoInterceptor  changeHeaderInfoInterceptor ;

	@Value("${myserver.back.entry}")
	String loginPath;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		// 首頁部分
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/"); 
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/searchresult"); 
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/product/{id}"); 
		
		// 賣家部分
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
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/seller/productorder/sellerproductorderlistall"); 
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/seller/productorder/sellerproductordersearch");

		
		// 買家部分
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/buyer/main"); 
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/buyer/updateBuyer"); 

		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/buyer/collection/list");
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/buyer/cart/list");
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/buyer/productorder/buyerproductorderlistall");
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/buyer/updateBuyer");
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/buyer/order_checkout111");
		    
    


    
		// 後台部分
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/back/"+loginPath+"/login"); 
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/back/main");
		
		
		// 論壇部分
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/article/listAll");
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/article/{id}");
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/forum/article/add");
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/forum/article/{id}");
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/forum/articleLike/{id}");
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/forum/msg/{id}");
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/forum/petdraw");
		registry.addInterceptor(changeHeaderInfoInterceptor).addPathPatterns("/front/forum/petdraw/{id}");

	


	}

}