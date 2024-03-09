package com.cart.controller;

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

import com.cart.service.CartService;
import com.collection.service.CollectionService;
import com.product.model.ProductService;
import com.product.model.ProductVO;
import com.redis.JedisUtil;
import com.seller.entity.SellerVO;
import com.seller.service.SellerService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Controller
@RequestMapping("/front/buyer/cart")
public class CartnoController extends HttpServlet {

	@Autowired
	ProductService productSvc;

	@Autowired
	SellerService sellerSvc;

	@Autowired
	CartService cartService;

	// 用戶取出自己所有收藏清單資料 /front/buyer/cart/list
	@GetMapping("list")
	public String cartlist(String memberId, Model model) {
		// 先給定 memberId 以便測試
		memberId = "10";
		model.addAttribute("cartClassfi", cartService.getAllByMemberId(memberId)); 
		return "front-end/buyer/buyer-cart-list";
	}

//	// 更新特定一個商品編號的收藏狀態, 並回到我的收藏 /front/buyer/collection/switchState
//	@PostMapping("switchState") //改用POST
//	public String switchOneTocart(String memberId,@RequestParam("productId") String productId, Model model) {
//		// 先給定 memberId , productId 以便測試
//		System.out.println("到controller");
//		memberId = "9";
////		productId = "5";
//		model.addAttribute("collectionClassfi", collectionService.switchStateByProductId(memberId, productId)); 
//		return "front-end/buyer/buyer-collection-list";
//	}
	
}
