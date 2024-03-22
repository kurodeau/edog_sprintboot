package com;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexOAuthController {


    @GetMapping("/login/oauth2/code/google")
    public String handleGoogleOAuth2Redirect() {
    	
    	System.out.println("AAA");
        // 在这里处理从 Google 返回的授权码
        // 可以将授权码用于获取访问令牌等进一步的认证流程
        // 返回适当的响应或者重定向到您应用程序中的其他页面
        return "redirect:/"; // 重定向到您应用程序的首页或其他页面
    }
    

	

}
