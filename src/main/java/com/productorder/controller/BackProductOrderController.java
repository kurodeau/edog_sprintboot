package com.productorder.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.orderdetails.model.OrderDetailsService;
import com.orderdetails.model.OrderDetailsVO;
import com.productorder.model.ProductOrderService;
import com.productorder.model.ProductOrderVO;

@Controller
@Validated
@RequestMapping("/back/productorder")
public class BackProductOrderController {
	
	@Autowired
	ProductOrderService productOrderSvc;
	@Autowired
	OrderDetailsService orderDetailsSvc;


	
	
	
	
//畫面跳轉/////////////////////////
	
	//訂單管理
		//側邊欄跳轉
		@GetMapping("productordersearch") 
		public String productordersearch(Model model){
	        return "back-end/back-order-search-all";
	    }
		
		
		//查看詳情按鈕
		@GetMapping("getOrderdetails") 
		public String getOneOrderdetails(@RequestParam("orderId")  String orderId, ModelMap model) {
			
			List <OrderDetailsVO> orderdetails = null;
				try {
					orderdetails = orderDetailsSvc.findByOrderId(Integer.valueOf(orderId));
//					System.out.println("run here");
				} catch (NumberFormatException e) {
					model.addAttribute("errorMessage", "Invalid orderId format");
					return "errorPage";
				} catch (Exception e) {
					e.printStackTrace();
				}
				model.addAttribute("Orderdetails", orderdetails);
				return "back-end/back-orderdetails-searchByOrder";

			}
	
	//後台訂單內容更新（進去更新畫面）
		
		@PostMapping("getOne_Order_For_Update")
		public String getOne_Order_For_Update(@RequestParam("orderId") String orderId, ModelMap model) {
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
			/*************************** 2.開始查詢資料 *****************************************/
			ProductOrderVO productOrderVO = productOrderSvc.getOneProductOrder(Integer.valueOf(orderId));
			
			/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
			model.addAttribute("productOrderVO", productOrderVO);
			return "back-end/back-order-update";
		}
	
//////////////////////////////////////
	

	
	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(
		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
//			@NotEmpty(message="訂單編號: 請勿空白")
//			@Digits(integer = 4, fraction = 0, message = "訂單編號: 請填數字-請勿超過{integer}位數")
//			@Min(value = 1, message = "訂單編號: 不能小於{value}")
//			@Max(value = 9999, message = "訂單編號: 不能超過{value}")
			@RequestParam("orderId") String orderId,
		ModelMap model) {
		
		/***************************2.開始查詢資料*********************************************/
//		ProductOrderService productOrderSvc = new ProductOrderService();
		ProductOrderVO productOrderVO = productOrderSvc.getOneProductOrder(Integer.valueOf(orderId));
		
		List<ProductOrderVO> list = productOrderSvc.getAll();
		model.addAttribute("productOrderListData", list); // for select_page.html 第97 109行用
		
		if (productOrderVO == null) {
			model.addAttribute("errorMessage", "查無資料");
			return "";
		}
		
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
		model.addAttribute("productOrderVO", productOrderVO);
		model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第126行 -->
		
		return ""; // 查詢完成後轉交select_page.html由其第128行insert listOneProductOrder.html內的th:fragment="listOneProductOrder-div
	}
//後台更新完訂單跳轉回管理訂單頁面
	@PostMapping("update")
	public String update(@Valid ProductOrderVO productOrderVO, BindingResult result, ModelMap model) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		
		if (result.hasErrors()) {
			return "back-end/back-order-update";
		}
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService productOrderSvc = new EmpService();
		productOrderSvc.updateProductOrder(productOrderVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		productOrderVO = productOrderSvc.getOneProductOrder(Integer.valueOf(productOrderVO.getOrderId()));
		model.addAttribute("productOrderVO", productOrderVO);
		
		List<ProductOrderVO> list = productOrderSvc.getAll();
		model.addAttribute("allProductOrder", list);
		return "back-end/back-order-search-all"; //回到訂單管理
		
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("sellerId") String sellerId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService productOrderSvc = new EmpService();
		productOrderSvc.deleteProductOrder(Integer.valueOf(sellerId));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<ProductOrderVO> list = productOrderSvc.getAll();
		model.addAttribute("productOrderListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/productOrder/listAllProductOrder"; // 刪除完成後轉交listAllEmp.html
	}
	
	
// ModelAttribute /////////////////////////////////
	
	//顯示所有的訂單
	@ModelAttribute("allProductOrder") 
	protected List<ProductOrderVO> allProductOrder(Model model) {
		
    	List<ProductOrderVO> list = productOrderSvc.getAll();
		return list;
	}
	

	
	
	
	
///平台管理//////////////////////////////////////////////////////
	@ModelAttribute("productOrderPendingConfirm")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<ProductOrderVO> productOrderPendingConfirm(Model model) {
		
		List<ProductOrderVO> list = productOrderSvc.getProductOrderPendingConfirm();
		return list;
	}
	
	
	
	
	@ModelAttribute("productOrderNotAcceptedByBuyer")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<ProductOrderVO> productOrderNotAcceptedByBuyer(Model model) {
		
		List<ProductOrderVO> list = productOrderSvc.getProductOrderNotAcceptedByBuyer();
		return list;
	}
	
	
	@ModelAttribute("productOrderNotAcceptedBySeller")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<ProductOrderVO> productOrderNotAcceptedBySeller(Model model) {
		
		List<ProductOrderVO> list = productOrderSvc.getProductOrderNotAcceptedBySeller();
		return list;
	}
	
	
	@ModelAttribute("productOrderAccepted")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<ProductOrderVO> productOrderAccepted(Model model) {
		
		List<ProductOrderVO> list = productOrderSvc.getProductOrderAccepted();
		return list;
	}
	
	@ModelAttribute("productOrderSellerProcessing")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<ProductOrderVO> productOrderSellerProcessing(Model model) {
		
		List<ProductOrderVO> list = productOrderSvc.getProductOrderSellerProcessing();
		return list;
	}
	
	@ModelAttribute("productOrderItemShipped")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<ProductOrderVO> productOrderItemShipped(Model model) {
		
		List<ProductOrderVO> list = productOrderSvc.getProductOrderItemShipped();
		return list;
	}
	
	@ModelAttribute("productOrderCompleted")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<ProductOrderVO> productOrderCompleted(Model model) {
		
		List<ProductOrderVO> list = productOrderSvc.getProductOrderCompleted();
		return list;
	}
	
	@ModelAttribute("productOrderCanceled")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<ProductOrderVO> productOrderCanceled(Model model) {
		
		List<ProductOrderVO> list = productOrderSvc.getProductOrderCanceled();
		return list;
	}
	
	
	
	@ExceptionHandler(value = { ConstraintViolationException.class })
	//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    StringBuilder strBuilder = new StringBuilder();
	    for (ConstraintViolation<?> violation : violations ) {
	          strBuilder.append(violation.getMessage() + "<br>");
	    }
	    //==== 以下第80~85行是當前面第69行返回 /src/main/resources/templates/back-end/emp/select_page.html 第97行 與 第109行 用的 ====   
//	    model.addAttribute("productOrderVO", new ProductOrderVO());
//    	ProductOrderService productOrderSvc = new ProductOrderService();
		List<ProductOrderVO> list = productOrderSvc.getAll();
		model.addAttribute("productOrderListData", list); // for select_page.html 第97 109行用
		
		String message = strBuilder.toString();
	    return new ModelAndView("back-end/productOrder/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
	}
}
