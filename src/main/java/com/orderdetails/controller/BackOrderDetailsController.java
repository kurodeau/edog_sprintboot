package com.orderdetails.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.orderdetails.model.OrderDetailsService;
import com.orderdetails.model.OrderDetailsVO;
import com.productorder.model.ProductOrderService;
import com.productorder.model.ProductOrderVO;

@Controller
@RequestMapping("/back/productorder")
public class BackOrderDetailsController {
	@Autowired
	OrderDetailsService orderDetailsSvc;
	
	@Autowired
	ProductOrderService productOrderSvc;
	
	
	//跳轉至修改內容畫面
	@PostMapping("getOne_OrderDetail_For_Update")
	public String getOneOrderDetailForUpdate(@RequestParam("orderDetailsId") String orderDetailsId, ModelMap model) {
		System.out.println(orderDetailsId);
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		OrderDetailsVO orderDetailsVO = orderDetailsSvc.getOneOrderDetails(Integer.valueOf(orderDetailsId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("orderDetailsVO", orderDetailsVO);
		return "back-end/back-orderdetails-update"; // 
		
		
	}
	//提交修改內容，並跳轉回查詢所有訂單畫面
	@PostMapping("updateOrderdetailFrombackend")
	public String updateOrderdetailFrombackend(@Valid OrderDetailsVO orderDetailsVO, BindingResult result, ModelMap model
			) throws IOException {
		
		System.out.println(result);
		if (result.hasErrors()) { //畫面不跳轉
			return "back-end/back-orderdetails-update";
		}
		/*************************** 2.開始修改資料 *****************************************/
		orderDetailsSvc.updateOrderDetails(orderDetailsVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		orderDetailsVO = orderDetailsSvc.getOneOrderDetails(Integer.valueOf(orderDetailsVO.getOrderDetailsId()));
		model.addAttribute("orderDetailsVO", orderDetailsVO);
//		System.out.println("run here!!");
		List<ProductOrderVO> list = productOrderSvc.getAll();
		model.addAttribute("allProductOrder", list);
		return "back-end/back-order-search-all"; 
	}
	
	// 去除BindingResult中某個欄位的FieldError紀錄
		public BindingResult removeFieldError(OrderDetailsVO orderDetailsVO, BindingResult result, String removedFieldname) {
			List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
					.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
					.collect(Collectors.toList());
			result = new BeanPropertyBindingResult(orderDetailsVO, "orderDetailsVO");
			for (FieldError fieldError : errorsListToKeep) {
				result.addError(fieldError);
			}
			return result;
		}
}
