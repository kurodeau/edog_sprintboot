package com;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.buyer.entity.BuyerVO;
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
public class IndexControllerMain {

	@Autowired
	UserService userSvc;

	@Autowired
	SellerLvService sellerLvSvc;
	
	@Autowired
	SellerService sellerSvc;

	@GetMapping("/")
	public String index(Model model) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			// 用户已认证，添加相应属性
			model.addAttribute("loggedIn", true);
			Object principal = authentication.getPrincipal();
			if (principal instanceof SellerVO) {
				SellerVO sellerVO = (SellerVO) principal;
				model.addAttribute("sellerVO", sellerVO);
				model.addAttribute("theName", sellerVO.getSellerCompany());
			}

		} else {
			// 用户未认证，添加相应属性
			model.addAttribute("loggedIn", false);
		}

		System.out.println("authentication" + authentication);
		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();
			System.out.println("securityContext" + username);
			System.out.println("getCredentials" + authentication.getCredentials());
			System.out.println("getDetails" + authentication.getDetails());
			System.out.println("getAuthorities" + authentication.getAuthorities());
		} 
		return "index";
		// resources/template//index.html
	}

	@ModelAttribute("sellerLvListData")
	protected List<SellerLvVO> referenceListData() {
		List<SellerLvVO> list = sellerLvSvc.getAll();
		// System.out.println("==============================");
		// list.forEach(data -> System.out.println(data));
		// System.out.println("==============================");
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

	@GetMapping({"/seller/login", "/seller/login/errors"})
	public String loginSeller(ModelMap model,HttpServletRequest req) throws IOException {

	    String error = (String) req.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION.message");
		if(error!= null) {
			model.addAttribute("error",error);
		}
		
		return "front-end/seller/seller-login";
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
		buyerVO.setMemberRegistrationTime(sqlDate);
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
	
	
//	@GetMapping({"/authentication/forgot-password/input-email"})
//	public String forgotPassword(ModelMap model) throws IOException {
//		
//		return "/login/authentication-forgotPassword/input-email";
//	}
//	
//	@GetMapping({"/authentication/forgot-password/check"})
//	public String checkForgotPassword(ModelMap model) throws IOException {
//		Boolean isActive =null;
//		
//		
//		
//		if(!isActive) {
//			model.addAttribute("error","輸入有誤");	
//			return "/login/authentication-forgotPassword/input-email";
//		}
//		
//		
//		
//		return "/login/authentication-forgotPassword";
//	}
	
	@GetMapping("/activate/seller/{sellerId}/{tokenId}")
	public String authenticationUser(
	        @PathVariable Integer sellerId,
	        @PathVariable String tokenId,
	        ModelMap model) throws IOException {

		try ( Jedis jedis = JedisUtil.getJedisPool().getResource()) {
			 String authKey = "AUTH_SELLER:" + sellerId;
		     String storedToken = jedis.get(authKey);
		     if(storedToken==null ) {
		    	 
		    	 model.addAttribute("error" , "驗證信已經過期");
		    	 return "/login/authentication-failure";
		     }
		     if (tokenId==null ) {
		    	 model.addAttribute("error" , "請確認驗證信有沒有點錯");
		    	 return "/login/authentication-failure";
		     }

		     if(storedToken.equals(tokenId)) {
			        return "/login/authentication-success";
		     }
		     
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.err.println("Failed to obtain or use Jedis instance");
	        return "/login/authentication-failure";
	    }
		
   	 	return "/login/authentication-failure";

	}

}
