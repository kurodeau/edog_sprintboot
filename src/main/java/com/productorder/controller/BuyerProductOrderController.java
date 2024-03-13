package com.productorder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.orderdetails.model.OrderDetailsService;
import com.productorder.model.ProductOrderService;
import com.productorder.model.ProductOrderVO;



@Controller
@RequestMapping("/front/buyer/productorder")

public class BuyerProductOrderController {
	
	
	@Autowired
	ProductOrderService productOrderSvc;
	@Autowired
	OrderDetailsService orderDetailsSvc;
	
	@GetMapping("buyerproductorderlistall")
	public String buyerProductOrderList(Model model) {
		return "front-end/buyer/buyer-order-overall";
	}
	
	
	
@PostMapping("cancel")
public String buyerCancelProductOrder(@RequestParam("orderId") String orderId , Model model) {
	
	ProductOrderVO productOrderVO = productOrderSvc.getOneProductOrder(Integer.valueOf(orderId));
	productOrderVO.setOrderStatus(2);
	productOrderSvc.addProductOrder(productOrderVO);
	
	
	
	List<ProductOrderVO> list = productOrderSvc.getAll();
	model.addAttribute("productOrderList",list);
	model.addAttribute("success" , "-(接受訂單成功)");
	return "redirect:/front/buyer/productorder/buyerproductorderlistall";

}

@PostMapping("buyerconfirm")
public String buyerConfirmProductOrder(@RequestParam("orderId") String orderId , Model model) {
	System.out.println(orderId+"++++++++");
	ProductOrderVO productOrderVO = productOrderSvc.getOneProductOrder(Integer.valueOf(orderId));
	productOrderVO.setOrderStatus(7);
	productOrderSvc.addProductOrder(productOrderVO);
	
	
	
	List<ProductOrderVO> list = productOrderSvc.getAll();
	model.addAttribute("productOrderList",list);
	model.addAttribute("success" , "-(接受訂單成功)");
	return "redirect:/front/buyer/productorder/buyerproductorderlistall";
}


	

	
	
	//顯示buyer所有訂單
	@ModelAttribute("buyerProductOrderList")
	protected List<ProductOrderVO> buyerProductOrderList (Integer buyerId , Model model){
		buyerId=1;
		List<ProductOrderVO> list = productOrderSvc.findByBuyerId(buyerId);
		return list;
	}
	
	//顯示buyer待處理訂單
	@ModelAttribute("buyerProductOrderPendingConfirm") 
	protected List<ProductOrderVO> buyerProductOrderPendingConfirm(Integer buyerId, Model model) {
		buyerId =1;
		List<ProductOrderVO> list = productOrderSvc.getBuyerProductOrderPendingConfirm(buyerId);
		return list;
	}
	
	
	//顯示buyer顯示處理中訂單
	@ModelAttribute("buyerProductOrderProcessing")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<ProductOrderVO> buyerProductOrderSellerProcessing(Integer buyerId, Model model) {
		buyerId =1;
		List<ProductOrderVO> list = productOrderSvc.getSellerProductOrderSellerProcessing(buyerId);
		return list;
	
	}
	
	
	//顯示buyer顯示已完成訂單
	@ModelAttribute("buyerProductOrderCompleted") 
	protected List<ProductOrderVO> buyerProductOrderCompleted(Integer buyerId, Model model) {
		buyerId =1;
		List<ProductOrderVO> list = productOrderSvc.getSellerProductOrderCompleted(buyerId);
		return list;
	}
	
	
	//顯示buyer已取消訂單
	@ModelAttribute("buyerProductOrderCanceled") 
	protected List<ProductOrderVO> buyerProductOrderCanceled(Integer buyerId, Model model) {
		buyerId =1;
		List<ProductOrderVO> list = productOrderSvc.getProductOrderBuyerCanceled(buyerId);
		return list;
	}
	

}
