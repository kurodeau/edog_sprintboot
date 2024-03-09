package com.collection.controller;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
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

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Controller
@RequestMapping("/front/buyer/collection")
public class CollectionnoController {

	@Autowired
	ProductService productSvc;

	// 定義自訂方法, 將String 轉為Set<String>
	private static Set<String> parseAndConvertToSet(String value) {
		// 如果資料為空或不合法，回傳空集合
		if (value == null || value.isEmpty() || value.equals("null")) {
			System.out.println("來源資料是空的,或只由null,空白組成");
			return new HashSet<>();
		}

		// 移除字串頭尾的方括號以及空白
		value = value.substring(1, value.length() - 1).trim();

		// 以逗號分割字串並建立集合
		return new HashSet<>(Arrays.asList(value.split("\\s*,\\s*")));
	}

	// 用戶取出自己所有購物車資料
	// //front/buyer/collection/list
	@GetMapping("list")
	public String collectionlist(Model model) {
		/***************************
		 * 0.(測試) 假定memberID以利測試
		 *********************************************/

		
		// 測試用的, 建立連線池
		JedisPool jedisPool = JedisUtil.getJedisPool();

		// 索取redis連線, 用try 整個包起來
		try (

			/****************************
			 * 1.透過已知的 memberId 自 Redis 要出收藏清單
			 *********************************************/

			Jedis jedis = jedisPool.getResource()) {
			// 從 Redis 中讀取資料 並且指定為db10 試圖分流分類資料
			jedis.select(10);
			// 指定要讀取的memberId
			String value = jedis.get("9");
			System.out.println("value" + value); // 測試資料
			
			
			/***************************
			 * 2.處理收藏清單的編號, 並逐個生成VO 放入清單後轉交
			 *********************************************/			
//		    List<ProductVO> productVOList = new ArrayList<ProductVO>();
			
			Set<String> valueSet = parseAndConvertToSet(value);
			for (String s : valueSet) {
				System.out.println( "s:" + s); //測試資料
				
			};

		} catch (Exception e) {
			System.out.println("從redis讀出資料有問題");
			e.printStackTrace();
		}
		// 好像不需要特別加上這個, 但還要研究為什麼
//		finally {
//            // 無論連線操作是否成功都釋放當前的 Jedis 連線，將其返回到連線池中
//			jedisPool.close();
//		}	




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
