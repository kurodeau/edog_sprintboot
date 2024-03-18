package com;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.ad.model.AdService;
import com.ad.model.AdVO;
import com.product.model.ProductImgService;
import com.product.model.ProductImgVO;
import com.product.model.ProductService;
import com.product.model.ProductVO;
import com.seller.entity.SellerVO;
import com.seller.service.SellerService;
import com.sellerLv.entity.SellerLvVO;
import com.sellerLv.service.SellerLvService;
import com.user.model.UserService;

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
		// System.out.println("==============================");
		// list.forEach(data -> System.out.println(data));
		// System.out.println("==============================");
		return list;
	}

	@GetMapping("/product/{id}")
	public String loginBuyer(@PathVariable("id") String id, ModelMap model) throws IOException {
		Integer productId = null;
		try {
			productId = Integer.valueOf(id);

			List<ProductImgVO> productImgVOs = productImgSvc.getProductImgs(productId);

			model.addAttribute("productImageList", productImgVOs);

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

//	@PostMapping(value = "/search", produces = "application/json")
//	public ResponseEntity<?> seachProducts(@RequestBody FormData formData) {
//		System.out.println("+++++++++++++++++++++++++++++++");
//
//		System.out.println(formData);
//
//		List<ProductVO> prodList = productSvc.compositeQuery(formData);
//		System.out.println("Size = " + prodList.size());
//		prodList.forEach(System.out::println);
//
//		productSvc.getBy(formData.getAnimalType(), formData.getProductCategory(), formData.getRatings(),
//				formData.getPriceFrom(), formData.getPriceTo(), formData.getKeyword());
//
//		List<Integer> productListId = prodList.stream().map(productVO -> productVO.getProductId())
//				.collect(Collectors.toList());
//
//		return ResponseEntity.ok(productListId);
//
//	}
//
//	@GetMapping("/searchresult")
//	public String productSearchResult(@RequestParam("productListId") String productListId, ModelMap model) {
//
//		String[] productIdArray = productListId.split(",");
//
//		List<ProductVO> productList = new ArrayList<>();
//		for (String productId : productIdArray) {
//			ProductVO product = productSvc.getOneProduct(Integer.valueOf(productId));
//			productList.add(product);
//		}
//
//		model.addAttribute("productIdList", productList);
//
//		return "/front-end/buyer/buyer-search";
//
//	}

	@GetMapping("/searchresult")
	public String searchResult(@RequestParam(value = "animalType", required = false) String animalType,
			@RequestParam(value = "productCategory", required = false) String productCategory,
			@RequestParam(value = "ratings", required = false) String ratings,
			@RequestParam(value = "priceFrom", required = false) String priceFrom,
			@RequestParam(value = "priceTo", required = false) String priceTo,
			@RequestParam(value = "keyword", required = false) String keyword, ModelMap model) {
		
		
		
		

		FormData formData = new FormData(); // 創建 FormData 對象

		// 填充 FormData 對象的屬性
		if (animalType != null && !animalType.isEmpty()) {
			List<String> animalTypeList = Arrays.asList(animalType.split(","));
			formData.setAnimalType(animalTypeList);
		}

		if (productCategory != null && !productCategory.isEmpty()) {
			List<String> productCategoryList = Arrays.asList(productCategory.split(","));
			formData.setProductCategory(productCategoryList);
		}

		if (ratings != null && !ratings.isEmpty()) {
			List<Integer> ratingsList = Arrays.stream(ratings.split(",")).map(Integer::parseInt)
					.collect(Collectors.toList());
			formData.setRatings(ratingsList);
		}

		if (priceFrom != null && !priceFrom.isEmpty()) {
			formData.setPriceFrom(priceFrom);
		}

		if (priceTo != null && !priceTo.isEmpty()) {
			formData.setPriceTo(priceTo);
		}

		if (keyword != null && !keyword.isEmpty()) {
			formData.setKeyword(keyword);
		}

		// 調用 ProductService 執行複合查詢
		List<ProductVO> productResult = productSvc.compositeQuery(formData);
			
		List<ProductVO> productList = productResult.stream()
				.filter(pt -> "已上架".equals(pt.getProductStatus()) && Boolean.TRUE.equals(pt.getIsEnabled()))
						.collect(Collectors.toList());
		model.addAttribute("productIdList", productList);

		return "/front-end/buyer/buyer-search"; // 返回結果
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
	protected List<AdVO> referenceListData2() {
		List<AdVO> list = adSvc.getPremiumHomePageAd();
		return list;
	}

	@ModelAttribute("launchBaseAdListData")
	protected List<AdVO> referenceListData3() {
		List<AdVO> list = adSvc.getBaseHomePageAd();
		return list;
	}

	
	@ModelAttribute("adhomePageUsed")
	protected List<AdVO> referenceListData5(Model model) {
		
		List<AdVO> list = adSvc.getHomePageUsed();
		
		return list;
	}
}

