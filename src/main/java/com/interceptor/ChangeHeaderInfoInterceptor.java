package com.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.seller.entity.SellerVO;

@Component
public class ChangeHeaderInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    return true; // 如果返回false，则请求不会继续向下执行
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 在处理请求之后、视图渲染之前执行的逻辑
    	
        if (modelAndView != null) {
            // 在这里添加你想要的逻辑，例如将用户登录信息添加到模型中
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();

            boolean isAnonymous = authentication instanceof AnonymousAuthenticationToken;
            if (isAnonymous) {
                modelAndView.addObject("loggedIn", false);
            } else if (authentication != null && authentication.isAuthenticated()) {
                modelAndView.addObject("loggedIn", true);
                Object principal = authentication.getPrincipal();
                if (principal instanceof SellerVO) {
                    SellerVO sellerVO = (SellerVO) principal;
                    modelAndView.addObject("sellerVO", sellerVO);
                    modelAndView.addObject("theName", sellerVO.getSellerCompany());
                }
            } else {
                modelAndView.addObject("loggedIn", false);
            }
        }
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在请求完成后执行的逻辑，包括在异常抛出之后
//        System.out.println("Executing afterCompletion method");
    } 

}
