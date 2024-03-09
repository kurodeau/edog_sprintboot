package com;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.buyer.service.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.seller.entity.SellerVO;
import com.buyer.entity.*;
import com.buyer.service.*;
//import com.sellerLv.entity.SellerLvVO;
//import com.sellerLv.service.SellerLvService;
import com.user.model.UserService;

//@PropertySource("classpath:application.properties") 
// 於https://start.spring.io 建立Spring Boot專案時
// (1) @SpringBootApplication 註解包含了 @ComponentScan，預設會掃描與主應用程式相同套件及其子套件中的組件
// (2) @Controller 註解標註的類別放在主應用程式套件及其子套件中
// (3) src/main/java / src/main/{language} /  src/main/webapp / src/main/resources

@Controller
public class IndexControllerBuyer {
	
	
	@Autowired
	BuyerService buyerSvc;
	
	@PostMapping("/buyer/register/check")
	public String checkregisterBuyer(@Valid @NonNull BuyerVO buyerVO, BindingResult result, ModelMap model, HttpSession session) throws IOException {
		System.out.println("有進入buyer/register/check");
		
		// 方便註冊測試, 先把錯誤驗證關閉, 之後要補
//		if (result.hasErrors()) {
//	        return "/front-end/buyer/buyer-register";
//		}
		System.out.println("11111111111");
		buyerSvc.addBuyer(buyerVO);
		model.addAttribute("success", "買家用戶註冊成功");

		System.out.println("22222222");
		// TESTING 註冊登入後保存sellerVO狀態
		session.setAttribute("buyerVO", buyerVO);
		
		System.out.println("重導回/front/buyer/main");
		return "redirect:/front/buyer/main";
	}

    @GetMapping("/front/buyer/main")
    public String buyermain(Model model) {
        return "/front-end/buyer/buyer-main";
        // resources/template//index.html
    }
	
    @GetMapping("/front/buyer/buyer-like")
    public String buyerLike(Model model) {
        return "/front-end/buyer/buyer-like";
        // resources/template//index.html
    }
	
}



