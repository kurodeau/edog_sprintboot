package com.productorder.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.orderdetails.model.OrderDetailsService;
import com.productorder.model.ProductOrderService;
import com.productorder.model.ProductOrderVO;
import com.seller.service.SellerService;

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
		
		
		
		
		
		
//		/*
//		 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
//		 */
//		@PostMapping("insert")
//		public String insert(@Valid ProductOrderVO productOrderVO, BindingResult result, ModelMap model) throws IOException {
//			
//			
//			if (result.hasErrors() ) {
//					return "back-end/productOrder/addProductOrder";
//				}
//				
//			/*************************** 2.開始新增資料 *****************************************/
//			// EmpService productOrderSvc = new EmpService();
//			productOrderSvc.addProductOrder(productOrderVO);
//			/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
//			List<ProductOrderVO> list = productOrderSvc.getAll();
//			model.addAttribute("productOrderListData", list);
//			model.addAttribute("success", "- (新增成功)");
//			return "redirect:/productOrder/listAllProductOrder"; // 新增成功後重導至IndexController_inSpringBoot.java的第50行@GetMapping("/emp/listAllEmp")
//		}

		/*
		 * This method will be called on listAllEmp.html form submission, handling POST request
		 */
//		@PostMapping("getOne_For_Update")
//		public String getOne_For_Update(@RequestParam("orderId") String orderId, ModelMap model) {
//			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//			/*************************** 2.開始查詢資料 *****************************************/
//			// EmpService productOrderSvc = new EmpService();
//			ProductOrderVO productOrderVO = productOrderSvc.getOneProductOrder(Integer.valueOf(orderId));
//
//			/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
//			model.addAttribute("productOrderVO", productOrderVO);
//			return "back-end/productOrder/update_productOrder_input"; // 查詢完成後轉交update_emp_input.html
//		}

		/*
		 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
		 */
//		@PostMapping("update")
//		public String update(@Valid ProductOrderVO productOrderVO, BindingResult result, ModelMap model) throws IOException {
//
//			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//			
//			if (result.hasErrors()) {
//				return "back-end/productOrder/update_productOrder_input";
//			}
//			/*************************** 2.開始修改資料 *****************************************/
//			// EmpService productOrderSvc = new EmpService();
//			productOrderSvc.updateProductOrder(productOrderVO);
//
//			/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
//			model.addAttribute("success", "- (修改成功)");
//			productOrderVO = productOrderSvc.getOneProductOrder(Integer.valueOf(productOrderVO.getOrderId()));
//			model.addAttribute("productOrderVO", productOrderVO);
//			return "back-end/productOrder/listOneProductOrder"; // 修改成功後轉交listOneEmp.html
//		}

		/*
		 * This method will be called on listAllEmp.html form submission, handling POST request
		 */
//		@PostMapping("delete")
//		public String delete(@RequestParam("sellerId") String sellerId, ModelMap model) {
//			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//			/*************************** 2.開始刪除資料 *****************************************/
//			// EmpService productOrderSvc = new EmpService();
//			productOrderSvc.deleteProductOrder(Integer.valueOf(sellerId));
//			/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
////			List<ProductOrderVO> list = productOrderSvc.getAll();
////			model.addAttribute("productOrderListData", list);
//			model.addAttribute("success", "- (刪除成功)");
//			return "back-end/productOrder/listAllProductOrder"; // 刪除完成後轉交listAllEmp.html
//		}

		
		//顯示所有訂單
		@ModelAttribute("sellerProductOrderList") 
		protected List<ProductOrderVO> sellerProductOrderList(HttpSession session, Model model) {
//			sellerId = session.getAttribute(sellerId);
			Integer sellerId =1;
			List<ProductOrderVO> list = productOrderSvc.findBySellerId(sellerId);
			return list;
		}
		/*
		 * 第一種作法 Method used to populate the List Data in view. 如 : 
		 * <form:select path="deptno" id="deptno" items="${deptListData}" itemValue="deptno" itemLabel="dname" />
		 */
//		@ModelAttribute("deptListData")
//		protected List<DeptVO> referenceListData() {
//			// DeptService deptSvc = new DeptService();
//			List<DeptVO> list = deptSvc.getAll();
//			return list;
//		}

		/*
		 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : 
		 * <form:select path="deptno" id="deptno" items="${depMapData}" />
		 */
//		@ModelAttribute("deptMapData") //
//		protected Map<Integer, String> referenceMapData() {
//			Map<Integer, String> map = new LinkedHashMap<Integer, String>();
//			map.put(10, "財務部");
//			map.put(20, "研發部");
//			map.put(30, "業務部");
//			map.put(40, "生管部");
//			return map;
//		}

		// 去除BindingResult中某個欄位的FieldError紀錄
//		public BindingResult removeFieldError(ProductOrderVO productOrderVO, BindingResult result, String removedFieldname) {
//			List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
//					.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
//					.collect(Collectors.toList());
//			result = new BeanPropertyBindingResult(productOrderVO, "productOrderVO");
//			for (FieldError fieldError : errorsListToKeep) {
//				result.addError(fieldError);
//			}
//			return result;
		}

