package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.user.model.UserService;
import com.user.model.UserVO;

import java.io.IOException;
import java.util.*;

//@PropertySource("classpath:application.properties") 
// 於https://start.spring.io 建立Spring Boot專案時
// (1) @SpringBootApplication 註解包含了 @ComponentScan，預設會掃描與主應用程式相同套件及其子套件中的組件
// (2) @Controller 註解標註的類別放在主應用程式套件及其子套件中
// (3) src/main/java / src/main/{language} /  src/main/webapp / src/main/resources

@Controller
public class IndexControllerMain {

    @Autowired
    UserService userSvc;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
        // resources/template//index.html
    }



    @GetMapping("/seller/register")
    public String registerSeller(ModelMap model) throws IOException {
        return "/front/seller/seller-seller-register";
    }

    @GetMapping("/seller/login")
    public String loginSeller(ModelMap model) throws IOException {
        model.addAttribute("success", "註冊成功");
        return "/front/seller/seller-login";
    }

}
