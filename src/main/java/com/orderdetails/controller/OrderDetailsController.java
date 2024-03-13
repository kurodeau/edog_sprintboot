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
@RequestMapping("/orderdetails")
public class OrderDetailsController {

	
	@Autowired
	OrderDetailsService orderDetailsSvc;
	
	@Autowired
	ProductOrderService productOrderSvc;
	
	/*
	 * This method will serve as addEmp.html handler.
	 */
	@GetMapping("addOrderDetails")
	public String addOrderDetails(ModelMap model) {
		OrderDetailsVO orderDetailsVO = new OrderDetailsVO();
		model.addAttribute("orderDetailsVO", orderDetailsVO);
		return "back-end/orderdetails/addOrderDetails";
	}
	
	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid OrderDetailsVO orderDetailsVO, BindingResult result, ModelMap model,
			@RequestParam("attachments") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(orderDetailsVO, result, "attachments");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "附件: 請上傳附件");
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				orderDetailsVO.setAttachments(buf);
			}
		}
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "back-end/orderdetails/addOrderDetails";
		}
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		orderDetailsSvc.addOrderDetails(orderDetailsVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<OrderDetailsVO> list = orderDetailsSvc.getAll();
		model.addAttribute("orderDetailsListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/orderdetails/listAllOrderDetails"; // 新增成功後重導至IndexController_inSpringBoot.java的第50行@GetMapping("/emp/listAllEmp")
	}
	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("orderDetailsId") String orderDetailsId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		OrderDetailsVO orderDetailsVO = orderDetailsSvc.getOneOrderDetails(Integer.valueOf(orderDetailsId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("orderDetailsVO", orderDetailsVO);
		return "back-end/orderdetails/update_orderdetails_input"; // 查詢完成後轉交update_emp_input.html
	}
		
	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
//	@PostMapping("update")
//	public String update(@Valid OrderDetailsVO orderDetailsVO, BindingResult result, ModelMap model,
//			@RequestParam("attachments") MultipartFile[] parts) throws IOException {
//
//		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
//		result = removeFieldError(orderDetailsVO, result, "attachments");
//
//		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
//			// EmpService empSvc = new EmpService();
//			byte[] attachments = orderDetailsSvc.getOneOrderDetails(orderDetailsVO.getOrderDetailsId()).getAttachments();
//			orderDetailsVO.setAttachments(attachments); //拿原本的圖片出來再存進去
//		} else {
//			for (MultipartFile multipartFile : parts) { //讀取新的照片存入VO
//				byte[] attachments = multipartFile.getBytes();
//				orderDetailsVO.setAttachments(attachments);
//			}
//		}
//		if (result.hasErrors()) { //畫面不跳轉
//			return "back-end/orderdetails/update_orderdetails_input";
//		}
//		/*************************** 2.開始修改資料 *****************************************/
//		// EmpService empSvc = new EmpService();
//		orderDetailsSvc.updateOrderDetails(orderDetailsVO);
//
//		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
//		model.addAttribute("success", "- (修改成功)");
//		orderDetailsVO = orderDetailsSvc.getOneOrderDetails(Integer.valueOf(orderDetailsVO.getOrderDetailsId()));
//		model.addAttribute("orderDetailsVO", orderDetailsVO);
//		return "back-end/orderdetails/listOneOrderDetails"; // 修改成功後轉交listOneEmp.html
//	}
	
	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("empno") String orderDetailsId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		orderDetailsSvc.deleteOrderDetails(Integer.valueOf(orderDetailsId));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<OrderDetailsVO> list = orderDetailsSvc.getAll();
		model.addAttribute("orderDetailsListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/orderDetails/listAllEmp"; // 刪除完成後轉交listAllEmp.html
	}

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${deptListData}" itemValue="deptno" itemLabel="dname" />
	 */
	@ModelAttribute("productOrderListData")
	protected List<ProductOrderVO> referenceListData() {
		// DeptService deptSvc = new DeptService();
		List<ProductOrderVO> list = productOrderSvc.getAll();
		return list;
	}

	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${depMapData}" />
	 */
//	@ModelAttribute("deptMapData") //
//	protected Map<Integer, String> referenceMapData() {
//		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
//		map.put(10, "財務部");
//		map.put(20, "研發部");
//		map.put(30, "業務部");
//		map.put(40, "生管部");
//		return map;
//	}

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
