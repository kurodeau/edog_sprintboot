package com.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public String handleError(HttpServletRequest request) {
    	
    	
        Object status = request.getAttribute("javax.servlet.error.status_code");
        if (status != null && Integer.parseInt(status.toString()) == HttpStatus.NOT_FOUND.value()) {
            // 檢查用戶權限
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                // 根據用戶權限分別處理404錯誤
                if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SELLER"))) {
                    return "/error/sellerError404";
                } else if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_BUYER"))) {
                    return "/error/buyerError404"; 
                }
            }
            return "/error/generalError404"; // 如果無法識別用戶角色，返回一般的錯誤頁面
        }
        return null; // 如果不是404錯誤，同樣返回一般的錯誤頁面
    }

    public String getErrorPath() {
        return ERROR_PATH;
    }
}