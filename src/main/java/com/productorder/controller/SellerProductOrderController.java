package com.productorder.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.orderdetails.model.OrderDetailsService;
import com.productorder.model.ProductOrderService;
import com.productorder.model.ProductOrderVO;
import com.seller.entity.SellerVO;

@Controller
@RequestMapping("/front/seller/productorder")
public class SellerProductOrderController {
	
		@Autowired
		ProductOrderService productOrderSvc;
		
		@Autowired
		OrderDetailsService orderDetailsSvc;
		
//		

		

//更改訂單狀態/////////////////////////////////////////

		
		@PostMapping("confirm")
		public String confirmProductOrder(@RequestParam("orderId") String orderId ,ModelMap model) {
			ProductOrderVO productOrderVO = productOrderSvc.getOneProductOrder(Integer.valueOf(orderId));
			productOrderVO.setOrderStatus(5);
			productOrderSvc.addProductOrder(productOrderVO); //將修改的資料存進資料庫
			
//			List<ProductOrderVO> list = productOrderSvc.getAll();
//			model.addAttribute("productOrderList",list);
			model.addAttribute("success" , "-(接受訂單成功)");
			return "redirect:/front/seller/productorder/sellerproductorderlistall";

		}
		
		@PostMapping("shipping")
		public String shippingProduct(@RequestParam("orderId") String orderId ,ModelMap model) {
			ProductOrderVO productOrderVO = productOrderSvc.getOneProductOrder(Integer.valueOf(orderId));
			productOrderVO.setOrderStatus(6);
			productOrderSvc.addProductOrder(productOrderVO); //將修改的資料存進資料庫
			
//			List<ProductOrderVO> list = productOrderSvc.getAll();
//			model.addAttribute("productOrderList",list);
			model.addAttribute("success" , "-(接受訂單成功)");
			return "redirect:/front/seller/productorder/sellerproductorderlistall";

		}

		@PostMapping("cancel")
		public String cancelProductOrder(@RequestParam("orderId") String orderId ,ModelMap model) {
			ProductOrderVO productOrderVO = productOrderSvc.getOneProductOrder(Integer.valueOf(orderId));
			productOrderVO.setOrderStatus(3);
			productOrderSvc.addProductOrder(productOrderVO); //將修改的資料存進資料庫
//			List<ProductOrderVO> list = productOrderSvc.getAll();
//			model.addAttribute("productOrderList",list);
			model.addAttribute("success" , "-(接受訂單成功)");
			return "redirect:/front/seller/productorder/sellerproductorderlistall";

		}
		
		
	

		
		//顯示所有訂單
		@ModelAttribute("sellerProductOrderList") 
		protected List<ProductOrderVO> sellerProductOrderList( Model model) {
//			
			
//			Integer sellerId =1;
			SecurityContext secCtx = SecurityContextHolder.getContext();
	        Authentication authentication = secCtx.getAuthentication();
	        SellerVO sellerVO = (SellerVO) authentication.getPrincipal();
//	        Integer sellerId = sellerVO.getSellerId();
			
			
			
			List<ProductOrderVO> list = productOrderSvc.findBySellerId(sellerVO.getSellerId());
			return list;
		}
		
		}

