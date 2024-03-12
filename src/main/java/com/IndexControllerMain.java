package com;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ad.model.AdService;
import com.ad.model.AdVO;
import com.buyer.entity.BuyerVO;
import com.login.PasswordForm;

import com.product.model.ProductImgService;
import com.product.model.ProductImgVO;

import com.product.model.ProductService;
import com.product.model.ProductVO;
import com.seller.entity.SellerVO;
import com.seller.service.SellerService;
import com.sellerLv.entity.SellerLvVO;
import com.sellerLv.service.SellerLvService;
import com.user.model.UserService;
import com.util.HttpResult;
import com.util.JedisUtil;
import com.util.MailService;

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
    ProductService productSvc;
    
    
    @Autowired
	ProductImgService productImgSvc;
    
    @Autowired
    AdService adSvc;
	


	@Autowired
	SellerService sellerSvc;



	@GetMapping("/")
	public String index(Model model) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();

		boolean isAnonymous = authentication instanceof AnonymousAuthenticationToken;
		if (isAnonymous) {
			// 用戶登入後，預設會使用anonymousUser
			model.addAttribute("loggedIn", false);
		} else if (authentication != null && authentication.isAuthenticated()) {
			model.addAttribute("loggedIn", true);
			Object principal = authentication.getPrincipal();
			if (principal instanceof SellerVO) {
				SellerVO sellerVO = (SellerVO) principal;
				model.addAttribute("sellerVO", sellerVO);
				model.addAttribute("theName", sellerVO.getSellerCompany());
			}
		} else {
			model.addAttribute("loggedIn", false);
		}

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
//		System.out.println("==============================");
//		list.forEach(data -> System.out.println(data));
//		System.out.println("==============================");
		return list;
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

	@GetMapping("/product/{id}")
	public String loginBuyer(@PathVariable("id") String id, ModelMap model) throws IOException {
		Integer productId = null;
		try {
			productId = Integer.valueOf(id);
		} catch (NumberFormatException e) {
			return "/";
		}

		ProductVO productVO = productSvc.getOneProduct(productId);

		model.addAttribute("productVO", productVO);
		// 使用從URL獲取的id參數執行你的邏輯
		// 在這裡，你可以使用id來進行相應的處理，例如查找特定商品

		return "/front-end/buyer/buyer-commidity";
	}

	@GetMapping("/test")
	public String loginBuyer2(ModelMap model) throws IOException {

		return "/front-end/buyer/buyer-commidityV2";
	}


	@PostMapping("/search")
	public ResponseEntity<?> seachProducts(@RequestBody FormData formData) {

		System.out.println(formData);

		List<ProductVO> prodList = productSvc.compositeQuery(formData);
		System.out.println("Size = " + prodList.size());
		prodList.forEach(System.out::println);

productSvc.getBy(formData.getAnimalType(),
				formData.getProductCategory(),
				formData.getRatings(),
				formData.getPriceFrom(),
				formData.getPriceTo(),
				formData.getKeyword()
				);

		return ResponseEntity.ok(formData);
		
		
	}

	public static class FormData {
		public List<String> getAnimalType() {
			return animalType;
		}

		public void setAnimalType(List<String> animalType) {
			this.animalType = animalType;
		}

		public List<String> getProductCategory() {
			return productCategory;
		}

		public void setProductCategory(List<String> productCategory) {
			this.productCategory = productCategory;
		}

		public List<Integer> getRatings() {
			return ratings;
		}

		public void setRatings(List<Integer> ratings) {
			this.ratings = ratings;
		}

		public String getPriceFrom() {
			return priceFrom;
		}

		public void setPriceFrom(String priceFrom) {
			this.priceFrom = priceFrom;
		}

		public String getPriceTo() {
			return priceTo;
		}

		public void setPriceTo(String priceTo) {
			this.priceTo = priceTo;
		}

		public String getKeyword() {
			return keyword;
		}

		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}

		@Override
		public String toString() {
			return "FormData [animalType=" + animalType + ", productCategory=" + productCategory + ", ratings="
					+ ratings + ", priceFrom=" + priceFrom + ", priceTo=" + priceTo + ", keyword=" + keyword + "]";
		}

		private List<String> animalType;
		private List<String> productCategory;
		private List<Integer> ratings;
		private String priceFrom;
		private String priceTo;
		private String keyword;

	}

	
	
	@ModelAttribute("allProductListData")
	protected List<ProductVO> referenceListData1() {
		List<ProductVO> list = productSvc.getProductLaunch();
		return list;
	}
	
	
	@ModelAttribute("launchPremiumAdListData")
	protected List<AdVO> referenceListData2(){
		List<AdVO> list = adSvc.getPremiumHomePageAd();
		return list;
	}
	
	
	@ModelAttribute("launchBaseAdListData")
	protected List<AdVO> referenceListData3(){
		List<AdVO> list = adSvc.getBaseHomePageAd();
		return list;
	}
	

}