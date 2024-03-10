package com;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.seller.entity.SellerVO;
import com.seller.service.SellerService;
import com.sellerLv.entity.SellerLvVO;
import com.sellerLv.service.SellerLvService;
import com.user.model.UserService;
import com.util.JedisUtil;

import redis.clients.jedis.Jedis;

//@PropertySource("classpath:application.properties") 
// 於https://start.spring.io 建立Spring Boot專案時
// (1) @SpringBootApplication 註解包含了 @ComponentScan，預設會掃描與主應用程式相同套件及其子套件中的組件
// (2) @Controller 註解標註的類別放在主應用程式套件及其子套件中
// (3) src/main/java / src/main/{language} /  src/main/webapp / src/main/resources

@Controller
public class IndexControllerSeller {
	
	
	@Autowired
	SellerService sellerSvc;

	@Autowired
	SellerLvService sellerLvSvc;

	
	@ModelAttribute("sellerLvListData")
	protected List<SellerLvVO> referenceListData() {
		List<SellerLvVO> list = sellerLvSvc.getAll();
//		System.out.println("==============================");
//		list.forEach(data -> System.out.println(data));
//		System.out.println("==============================");
		return list;
	}
	
	
	@PostMapping("/seller/register/check")
	public String checkregisterSeller(@Valid @NonNull SellerVO sellerVO, ModelMap model, BindingResult result,HttpSession session) 	throws IOException {
		if (result.hasErrors()) {
	        return "/front-end/seller/seller-register";
		}
		
		try (Jedis jedis = JedisUtil.getJedisPool().getResource()) {
			jedis.select(15);
			String code = jedis.get("email:"+sellerVO.getSellerEmail());


			if(code!= null && code.equals("ok")) {
				jedis.del("email:"+sellerVO.getSellerEmail());
			} else {
		        return "/front-end/seller/seller-register";

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("success", "註冊成功");
		sellerSvc.saveUserDetails(sellerVO);
		
		// TESTING 註冊登入後保存sellerVO狀態
		session.setAttribute("sellerVO", sellerVO);
		
		return "redirect:/front/seller/main";
	}
	
	

	 
	


}

