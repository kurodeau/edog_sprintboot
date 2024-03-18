package com;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//@PropertySource("classpath:application.properties") 
// 於https://start.spring.io 建立Spring Boot專案時
// (1) @SpringBootApplication 註解包含了 @ComponentScan，預設會掃描與主應用程式相同套件及其子套件中的組件
// (2) @Controller 註解標註的類別放在主應用程式套件及其子套件中
// (3) src/main/java / src/main/{language} /  src/main/webapp / src/main/resources

@Controller
@RequestMapping("/back")
public class IndexControllerBack {

	@GetMapping("/main")
	public String backMain(Model model) {
		return "/back-end/back-main";
	}

	@GetMapping("/${myserver.back.entry}/login")
	public String backLogin(Model model) {
		
		return "/back-end/back-login";
	}
}
