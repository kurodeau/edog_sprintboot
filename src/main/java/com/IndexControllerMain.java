package com;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.seller.entity.SellerVO;
import com.sellerLv.entity.SellerLvVO;
import com.sellerLv.service.SellerLvService;
import com.buyer.entity.*;
import com.user.model.UserService;
import com.user.model.UserVO;

//@PropertySource("classpath:application.properties") 
// 於https://start.spring.io 建立Spring Boot專案時
// (1) @SpringBootApplication 註解包含了 @ComponentScan，預設會掃描與主應用程式相同套件及其子套件中的組件
// (2) @Controller 註解標註的類別放在主應用程式套件及其子套件中
// (3) src/main/java / src/main/{language} /  src/main/webapp / src/main/resources

@Controller
public class IndexControllerMain {

    @Autowired
    UserService userSvc;
    
    @Autowired
	SellerLvService sellerLvSvc;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
        // resources/template//index.html
    }

    
    @ModelAttribute("sellerLvListData")
	protected List<SellerLvVO> referenceListData() {
		List<SellerLvVO> list = sellerLvSvc.getAll();
//		System.out.println("==============================");
//		list.forEach(data -> System.out.println(data));
//		System.out.println("==============================");
		return list;
	}


    @GetMapping("/seller/register")
    public String registerSeller(ModelMap model) throws IOException {
    	SellerVO sellerVO = new SellerVO();
    	
    	// TEST 
    	   sellerVO.setSellerEmail("john.doe@example.com");
    	    sellerVO.setSellerCompany("ABC Company");
    	    sellerVO.setSellerTaxId("12345");
    	    sellerVO.setSellerCapital(500000);
    	    sellerVO.setSellerContact("John Doe");
    	    sellerVO.setSellerCompanyPhone("1234567890");
    	    sellerVO.setSellerCompanyExtension("123");
    	    sellerVO.setSellerMobile("0912345678");
    	    sellerVO.setSellerCounty("台北市");
    	    sellerVO.setSellerDistrict("大安區");
    	    sellerVO.setSellerAddress("123 Main St");
    	    sellerVO.setSellerPassword("Password123");
    	    sellerVO.setSellerBankAccount("ABC Bank");
    	    sellerVO.setSellerBankCode("123");
    	    sellerVO.setSellerBankAccountNumber("9876543210");
    	
    	
    	    
    	    // 防止被修改
    	    sellerVO.setSellerPassword(null);
    	    sellerVO.setIsConfirm(false);
    	    
		model.addAttribute("sellerVO", sellerVO);
        return "/front-end/seller/seller-register";
    }

    @GetMapping("/seller/login")
    public String loginSeller(ModelMap model) throws IOException {
        return "/front-end/seller/seller-login";
    }

    @GetMapping("/buyer/register")
    public String registerBuyer(ModelMap model) throws IOException {
    	BuyerVO buyerVO = new BuyerVO();
    	
    	// TEST 
    	buyerVO.setMemberEmail("lulu.doe@example.com");
    	buyerVO.setThirdFrom(null);
    	buyerVO.setMemberName("Lulu");
    	buyerVO.setMemberPhone("03123321");
    	buyerVO.setMemberMobile("09777666");
    	buyerVO.setMemberBirthday(null);
    	buyerVO.setMemberAddress("地址");
    	buyerVO.setIsMemberEmail(false);
    	
    	java.util.Date utilDate = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
    	buyerVO.setMemberRegistrationTime( sqlDate );
    	buyerVO.setPetName("寵物啦");
    	buyerVO.setPetImg(null);
    	buyerVO.setPetImgUploadTime(null);
    	buyerVO.setPetVaccName1(null);
    	buyerVO.setPetVaccTime1(null);
    	buyerVO.setPetVaccName2(null);
    	buyerVO.setPetVaccTime2(null);
   	
    	    
    	// 防止被修改
    	buyerVO.setMemberPassword(null);
    	buyerVO.setIsConfirm(true);
    	    
		model.addAttribute("buyerVO", buyerVO);
        return "/front-end/buyer/buyer-register";
    }   
    
    
    @GetMapping("/buyer/login")
    public String loginBuyer(ModelMap model) throws IOException {
        return "/front-end/buyer/buyer-login";
    }
    
}
