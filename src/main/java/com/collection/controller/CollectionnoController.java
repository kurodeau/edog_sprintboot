package com.collection.controller;

import java.util.*;

import javax.servlet.http.HttpServlet;
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

import com.collection.service.CollectionService;
import com.product.model.ProductService;
import com.product.model.ProductVO;
import com.redis.JedisUtil;
import com.seller.entity.SellerVO;
import com.seller.service.SellerService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Controller
@RequestMapping("/front/buyer/collection")
public class CollectionnoController extends HttpServlet {

	@Autowired
	ProductService productSvc;

	@Autowired
	SellerService sellerSvc;

	@Autowired
	CollectionService collectionService;

	// 用戶取出自己所有收藏清單資料 /front/buyer/collection/list
	@GetMapping("list")
	public String collectionlist(String memberId, Model model) {
		// 先給定 memberId 以便測試
		memberId = "9";
		model.addAttribute("collectionClassfi", collectionService.getAllByMemberId(memberId)); 
		return "front-end/buyer/buyer-collection-list";
	}

	// 更新特定一個商品編號的收藏狀態, 並回到我的收藏 /front/buyer/collection/switchState
	@PostMapping("switchState") //改用POST
	public String switchOneToCollection(String memberId,@RequestParam("productId") String productId, Model model) {
		// 先給定 memberId , productId 以便測試
		System.out.println("到controller");
		memberId = "9";
//		productId = "5";
		model.addAttribute("collectionClassfi", collectionService.switchStateByProductId(memberId, productId)); 
		return "front-end/buyer/buyer-collection-list";
	}
	
	// 更新特定一個商品編號的收藏狀態, 並回到我的收藏 /front/buyer/collection/isCollection
//	@GetMapping("isCollection")
//	public String isCollection(String memberId, String productId, Model model) {
//		// 先給定 memberId 以便測試
//		// 先給定 memberId , productId 以便測試
//		memberId = "9";
//		productId = "1";
//		model.addAttribute("collectionClassfi", collectionService.getAllByMemberId(memberId)); 
//		return "front-end/buyer/buyer-collection-list";
//	}
	

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
