package com.product.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ad.model.AdVO;
import com.product.model.ProductService;
import com.product.model.ProductVO;

@Controller
@RequestMapping("/front/seller/product")
public class ProductnoController {

	@Autowired
	ProductService productSvc;

	@GetMapping("productlist")
	public String sellerproductlist(Model model) {
		return "front-end/seller/seller-product-all";
	}

	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 *************************/
	@RequestParam("productId") String productId, ModelMap model) {
		/***************************
		 * 2.開始查詢資料
		 *********************************************/

		ProductVO productVO = productSvc.getOneProduct(Integer.valueOf(productId));

		List<ProductVO> list = productSvc.getAll();
		model.addAttribute("productListData", list);

		if (productVO == null) {
			model.addAttribute("errorMessage", "查無資料");
			return null;
		}
		/***************************
		 * 3.查詢完成,準備轉交(Send the Success view)
		 *****************/
		model.addAttribute("ProductVO", productVO);
		model.addAttribute("getOne_FOR_Display", "true");

		return null;
	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	// @ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		StringBuilder strBuilder = new StringBuilder();
		for (ConstraintViolation<?> violation : violations) {
			strBuilder.append(violation.getMessage() + "<br>");
		}
		// ==== 以下第80~85行是當前面第69行返回
		// /src/main/resources/templates/back-end/emp/select_page.html 第97行 與 第109行 用的
		// ====
//	    model.addAttribute("empVO", new EmpVO());
//    	EmpService empSvc = new EmpService();
		List<ProductVO> list = productSvc.getAll();
		model.addAttribute("productListData", list); // for select_page.html 第97 109行用

		String message = strBuilder.toString();
		return new ModelAndView("front-end/seller/seller-product-all", "errorMessage", "請修正以下錯誤:<br>" + message);
	}

}
