package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.user.model.UserService;
import com.user.model.UserVO;

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
    
    // http://localhost/myApp......./hello?name=peter1
    @GetMapping("/seller")
    public String indexWithParam(
            @RequestParam(name = "account", required = false, defaultValue = "") String name, Model model) {
        model.addAttribute("message", name);
        return "/selle-main"; 
        // resources/template//seller-main.html
    }
    
    
    
    @GetMapping("/TEST/all")
    public String testDemo(
            Model model) {
        return "/back-end/TEST/back-seller-all"; 
    }
    
    @GetMapping("/TEST/add")
    public String testDemo2(
            Model model) {
        return "/back-end/TEST/back-seller-add"; 
    }
    
    @GetMapping("/TEST/update")
    public String testDemo3(
            Model model) {
        return "/back-end/TEST/back-seller-edit"; 
    }

  
    @GetMapping("/user/select_page")
	public String select_page(Model model) {
		return "back-end/user/select_page";
	}
    
    @GetMapping("/user/listAllUser")
	public String listAllUser(Model model) {
		return "back-end/user/listAllUser";
	}
    
    
    // Cpntroller forBack
    


}

