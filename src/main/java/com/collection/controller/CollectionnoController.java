package com.collection.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cart.service.CartService;
import com.collection.service.CollectionService;
import com.product.model.ProductImgVO;
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

	@Autowired
	CartService cartService;
	
	// 用戶取出自己所有收藏清單資料 /front/buyer/collection/list
	@GetMapping("list")
	public String collectionlist(String memberId, Model model) {
		// 先給定 memberId 以便測試
		memberId = "9";
		model.addAttribute("collectionClassfi", collectionService.getAllByMemberId(memberId));
		return "front-end/buyer/buyer-collection-list";
	}

	// 更新特定一個商品編號的收藏狀態, 並回到我的收藏 /front/buyer/collection/switchState
	@PostMapping("switchState") // 改用POST
	public String switchOneToCollection(@RequestParam("memberId") String memberId,
			@RequestParam("productId") String productId, Model model) {
		// 先給定 memberId , productId 以便測試
		System.out.println("到controller");
//		memberId = "9";
//		productId = "5";
		model.addAttribute("collectionClassfi", collectionService.switchStateByProductId(memberId, productId));
		return "front-end/buyer/buyer-collection-list";
	}

	// /front/buyer/collection/product/{id}
	@GetMapping("/product/{id}")
	public String loginBuyer(@PathVariable("id") String id, ModelMap model) throws IOException {

		System.out.println("有進到找商品詳細的controller");

		ProductVO productVO = productSvc.getOneProduct(Integer.valueOf(id));
		model.addAttribute("productVO", productVO);
		
		// 使用從URL獲取的id參數執行你的邏輯
		// 在這裡，你可以使用id來進行相應的處理，例如查找特定商品
		
		System.out.println("只差沒有跳轉她媽的");
		return "front-end/buyer/buyer-commidity";
	}

	// 將指定商品加到購物車, 並回到我的收藏 /front/buyer/collection/addToCart
	@PostMapping("addToCart") 
	public String addToCart(@RequestParam("memberId") String memberId,
			@RequestParam("productId") String productId, Model model) {
		System.out.println("有到加入購物車 controller");
//		model.addAttribute("collectionClassfi", cartService.memberAddOneByProductId(memberId, productId));
		cartService.memberAddOneByProductId(memberId, productId);
		return "front-end/buyer/buyer-collection-list";
	}
	

}
