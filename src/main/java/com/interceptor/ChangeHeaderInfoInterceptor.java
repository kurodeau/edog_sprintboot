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

import com.buyer.entity.BuyerVO;
import com.manager.ManagerVO;
import com.seller.entity.SellerVO;

@Component
public class ChangeHeaderInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true; // 如果返回false，则請求不會繼續向下執行
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 在處理請求之後、視圖渲染之前執行的邏輯
        
        if (modelAndView != null) {
            // 在這裡添加你想要的邏輯，例如將使用者登錄信息添加到模型中
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();

            boolean isAnonymous = authentication instanceof AnonymousAuthenticationToken;
            if (isAnonymous) {
                modelAndView.addObject("loggedIn", false);
                modelAndView.addObject("loggedInJWT", false);

            } else if (authentication != null && authentication.isAuthenticated()) {
                modelAndView.addObject("loggedIn", true);
                modelAndView.addObject("loggedInJWT", false);

                Object principal = authentication.getPrincipal();
                


                if (principal instanceof SellerVO) {
                    SellerVO sellerVO = (SellerVO) principal;
                    modelAndView.addObject("theName", sellerVO.getSellerCompany());
                    // 添加賣家特定的邏輯
                } else if (principal instanceof BuyerVO) {
                    BuyerVO buyerVO = (BuyerVO) principal;
                    modelAndView.addObject("theName", buyerVO.getMemberName());
                    // 添加買家特定的邏輯
                }  else if (principal instanceof ManagerVO) {
                	ManagerVO managerVO = (ManagerVO) principal;
                    modelAndView.addObject("loggedIn", false);
                    modelAndView.addObject("loggedInJWT", true);
                    modelAndView.addObject("theName", managerVO.getManagerEmail());
                    // 添加買家特定的邏輯
                }
            } else {
                modelAndView.addObject("loggedIn", false);
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在請求完成後執行的邏輯，包括在異常拋出之後
        // System.out.println("Executing afterCompletion method");
    } 
}
