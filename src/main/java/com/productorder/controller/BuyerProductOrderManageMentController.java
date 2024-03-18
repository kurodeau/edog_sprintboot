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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.buyer.entity.BuyerVO;
import com.orderdetails.model.OrderDetailsService;
import com.orderdetails.model.OrderDetailsVO;
import com.productorder.model.ProductOrderService;
import com.productorder.model.ProductOrderVO;



@Controller
@RequestMapping("/front/buyer/productorder")
public class BuyerProductOrderManageMentController {
	
	
	@Autowired
	ProductOrderService productOrderSvc;
	@Autowired
	OrderDetailsService orderDetailsSvc;
	
	@GetMapping("buyerproductorderlistall")
	public String buyerProductOrderList(Model model) {
		return "front-end/buyer/buyer-order-overall";
	}
	

	//查看詳情按鈕
		@GetMapping("getOrderdetails") 
		public String getOneOrderdetails(@RequestParam("orderId")  String orderId, ModelMap model) {
			
			List <OrderDetailsVO> orderdetails = null;
				try {
					orderdetails = orderDetailsSvc.findByOrderId(Integer.valueOf(orderId));

				} catch (NumberFormatException e) {
					model.addAttribute("errorMessage", "Invalid orderId format");
					return "errorPage";
				} catch (Exception e) {
					e.printStackTrace();
				}
				model.addAttribute("Orderdetails", orderdetails);
				return "front-end/buyer/buyer-orderdetails-searchforone";

			}
	
	
	
	
	
	
	
	
@PostMapping("cancel")
public String buyerCancelProductOrder(@RequestParam("orderId") String orderId , Model model, HttpSession session) {
	
	ProductOrderVO productOrderVO = productOrderSvc.getOneProductOrder(Integer.valueOf(orderId));
	productOrderVO.setOrderStatus(2);
	productOrderSvc.addProductOrder(productOrderVO);
	
//	memberId = session.getAttribute(memberId);
//	int memberId = 1;
//	List<ProductOrderVO> list = productOrderSvc.findByBuyerId(memberId);
//	model.addAttribute("productOrderList",list);
	model.addAttribute("success" , "-(刪除訂單成功)");
	return "redirect:/front/buyer/productorder/buyerproductorderlistall";

}


//確認訂單完成
@PostMapping("buyerconfirm")
public String buyerConfirmProductOrder(@RequestParam("orderId") String orderId , Model model, HttpSession session) {
//	System.out.println(orderId+"++++++++");
	ProductOrderVO productOrderVO = productOrderSvc.getOneProductOrder(Integer.valueOf(orderId));
	productOrderVO.setOrderStatus(7);
	productOrderSvc.addProductOrder(productOrderVO);
//	int memberId = 1;
//	List<ProductOrderVO> list = productOrderSvc.findByBuyerId(memberId);
//	model.addAttribute("productOrderList",list);
	model.addAttribute("success" , "-(接受訂單成功)");
	return "redirect:/front/buyer/productorder/buyerproductorderlistall";
}


	

	
//ModelAttribute//////////////////////////////////////
	//顯示buyer所有訂單
	@ModelAttribute("buyerProductOrderList")
	protected List<ProductOrderVO> buyerProductOrderList (Integer buyerId , Model model, HttpSession session){
//		buyerId = session.getAttribute(memberId);
//		buyerId=1;
		
		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
		
		
		
		List<ProductOrderVO> list = productOrderSvc.findByBuyerId(buyerVO.getMemberId());
		return list;
	}
	
	//顯示buyer待處理訂單
	@ModelAttribute("buyerProductOrderPendingConfirm") 
	protected List<ProductOrderVO> buyerProductOrderPendingConfirm(Integer buyerId, Model model, HttpSession session) {
//		buyerId = session.getAttribute(memberId);
//		buyerId =1;
		
		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
		
		
		List<ProductOrderVO> list = productOrderSvc.getBuyerProductOrderPendingConfirm(buyerVO.getMemberId());
		return list;
	}
	
	
	//顯示buyer顯示處理中訂單
	@ModelAttribute("buyerProductOrderProcessing")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<ProductOrderVO> buyerProductOrderSellerProcessing(Integer buyerId, Model model, HttpSession session) {
//		buyerId = session.getAttribute(memberId);
//		buyerId =1;
		
		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
		
		List<ProductOrderVO> list = productOrderSvc.getProductOrderBuyerProcessing(buyerVO.getMemberId());
		return list;
	
	}
	
	
	//顯示buyer顯示已完成訂單
	@ModelAttribute("buyerProductOrderCompleted") 
	protected List<ProductOrderVO> buyerProductOrderCompleted(Integer buyerId, Model model, HttpSession session) {
//		buyerId = session.getAttribute(memberId);
//		buyerId =1;
		
		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
		
		
		List<ProductOrderVO> list = productOrderSvc.getProductOrderBuyerCompleted(buyerVO.getMemberId());
		return list;
	}
	
	
	//顯示buyer已取消訂單
	@ModelAttribute("buyerProductOrderCanceled") 
	protected List<ProductOrderVO> buyerProductOrderCanceled(Integer buyerId, Model model, HttpSession session) {
//		buyerId =1;
		
		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
		
		
		
		List<ProductOrderVO> list = productOrderSvc.getProductOrderBuyerCanceled(buyerVO.getMemberId());
		return list;
	}
	

}

