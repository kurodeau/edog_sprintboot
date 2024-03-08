package com.collection.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.product.model.ProductService;
import com.product.model.ProductVO;
import com.redis.JedisUtil;
import com.seller.entity.SellerVO;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Controller
@RequestMapping("/front/buyer/collection")
public class CollectionnoController {

	@Autowired
	ProductService productSvc;

	// 定義自訂方法, 將String 轉為Set<String>, 改成存list 應該用不到
//	private static Set<String> parseAndConvertToSet(String value) {
//		// 如果資料為空或不合法，回傳空集合
//		if (value == null || value.isEmpty() || value.equals("null")) {
//			System.out.println("來源資料是空的,或只由null,空白組成");
//			return new HashSet<>();
//		}
//
//		// 移除字串頭尾的方括號以及空白
//		value = value.substring(1, value.length() - 1).trim();
//
//		// 以逗號分割字串並建立集合
//		return new HashSet<>(Arrays.asList(value.split("\\s*,\\s*")));
//	}

	// 用戶取出自己所有購物車資料
	// //front/buyer/collection/list
	@GetMapping("list")
	public String collectionlist(Model model) {
		/***************************
		 * 0.(測試) 假定memberID以利測試
		 *********************************************/
		String memberId = "9";

		// 取得連線
		JedisPool jedisPool = JedisUtil.getJedisPool();

		// 本方法會用到的變數,
		Map<String, List<ProductVO>> collectionClassfi = new HashMap<>();
		List<ProductVO> productList = new ArrayList();
		ProductVO product = new ProductVO();

		// 索取redis連線, 用try 整個包起來
		try (
				/****************************
				 * 1.透過已知的 memberId 自 Redis 要出收藏清單
				 *********************************************/
				Jedis jedis = jedisPool.getResource()) {
			// 從 Redis 中讀取資料 並且指定為db10 試圖分流分類資料
			jedis.select(10);

			// 將所有收藏的資料包入 List<ProductVO>, 並透過公司名稱分為Map
			// Set<String> sellerCompanySet = new HashSet();
			int count = 0;
			for (String str : jedis.lrange(memberId, 0, -1)) {
				product = productSvc.getOneProduct(Integer.parseInt(str));
				String sellerCompany = product.getSellerVO().getSellerCompany();

				// 將每個 ProductVO 透過 "sellerCompany" 鍵關聯起来
				productList = collectionClassfi.getOrDefault(sellerCompany, new ArrayList<>()); // 拉出對應Key的value
																								// List，再對其更新
				productList.add(product);
				collectionClassfi.put(sellerCompany, productList);
			}
//			System.out.println("測試" + collectionClassfi.toString()); // 有成功分類並包成Map

			// 將 collectList 中的 Product 根据 sellerId 分组, 不知道怎麼用Stream寫
			// Map<String, List<ProductVO> > groupedProducts = collectList.stream()
			// .collect(Collectors.groupingBy(ProductVO::getSellerCompany ));

		} catch (Exception e) {
			System.out.println("從redis讀出資料有問題");
			e.printStackTrace();
		}
		
		
		model.addAttribute("collectionClassfi", collectionClassfi);
		return "front-end/buyer/buyer-collection-list";
	}

//	@PostMapping("getOne_For_Display")
//	public String getOne_For_Display(/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 *************************/
//	@RequestParam("productId") String productId, ModelMap model) {
//		/***************************
//		 * 2.開始查詢資料
//		 *********************************************/
//
//		ProductVO productVO = productSvc.getOneProduct(Integer.valueOf(productId));
//
//		List<ProductVO> list = productSvc.getAll();
//		model.addAttribute("productListData", list);
//
//		if (productVO == null) {
//			model.addAttribute("errorMessage", "查無資料");
//			return null;
//		}
//		/***************************
//		 * 3.查詢完成,準備轉交(Send the Success view)
//		 *****************/
//		model.addAttribute("ProductVO", productVO);
//		model.addAttribute("getOne_FOR_Display", "true");
//
//		return "front-end/seller/seller-product-all";
//	}
//
//	@ModelAttribute("productListData")
//	protected List<ProductVO> referenceListData(Model model) {
//
//		List<ProductVO> list = productSvc.getAll();
//		return list;
//	}
//
//	@ModelAttribute("productSellOut")
//	protected List<ProductVO> referenceListData1(Model model) {
//
//		List<ProductVO> list = productSvc.getSellOutProduct();
//		return list;
//	}
//
//	@ModelAttribute("productLaunch")
//	protected List<ProductVO> referenceListData3(Model model) {
//
//		List<ProductVO> list = productSvc.getProductLaunch();
//		return list;
//	}
//
//	@ModelAttribute("productUnLaunch")
//	protected List<ProductVO> referenceListData2(Model model) {
//
//		List<ProductVO> list = productSvc.getProductUnLaunch();
//		return list;
//	}
//
//	@ExceptionHandler(value = { ConstraintViolationException.class })
//	// @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//	public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
//		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
//		StringBuilder strBuilder = new StringBuilder();
//		for (ConstraintViolation<?> violation : violations) {
//			strBuilder.append(violation.getMessage() + "<br>");
//		}
//		// ==== 以下第80~85行是當前面第69行返回
//		// /src/main/resources/templates/back-end/emp/select_page.html 第97行 與 第109行 用的
//		// ====
////	    model.addAttribute("empVO", new EmpVO());
////    	EmpService empSvc = new EmpService();
//		List<ProductVO> list = productSvc.getAll();
//		model.addAttribute("productListData", list); // for select_page.html 第97 109行用
//
//		String message = strBuilder.toString();
//		return new ModelAndView("front-end/seller/seller-product-all", "errorMessage", "請修正以下錯誤:<br>" + message);
//	}

}
