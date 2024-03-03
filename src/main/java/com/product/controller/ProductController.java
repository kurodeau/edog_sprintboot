package com.product.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ad.model.AdVO;
import com.product.model.ProductService;
import com.product.model.ProductVO;

@Controller
@RequestMapping("/front/seller/product")
public class ProductController {
	
	@Autowired
	ProductService productSvc;
	
	@GetMapping("add")
	public String sellerProductAdd(Model model) {
		ProductVO productVO = new ProductVO();
		model.addAttribute("product" , productVO);
		return "front-end/seller/seller-product-add";
	}
	

	

	
	@PostMapping("insert")
	public String insert(@Valid ProductVO productVO , BindingResult result , Model model ,
			@RequestParam("mainImage") MultipartFile[] parts ,
			@RequestParam("subImages") MultipartFile[] partsSec) throws IOException {
	
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/	
		result = removeFieldError(productVO , result , "mainImage");
		
		if(parts[0].isEmpty()) {
			model.addAttribute("errorMessage" , "商品照片:請上傳照片");
		}else {
			for(MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				productVO.setProductCoverImg(buf);
			}	
		}
		
		if(result.hasErrors()||parts[0].isEmpty()) {
			System.out.println(result);
			return "front-end/seller/seller-product-all";
			
		}
		/*************************** 2.開始新增資料 *****************************************/
		productSvc.addProduct(productVO);;	
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ProductVO> list = productSvc.getAll();
		model.addAttribute("ProductListData",list);
		model.addAttribute("success", "-(新增成功");
		return "redirect:/front/seller/product/productlist2";
	}
	
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("productId") String productId ,ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		ProductVO productVO = productSvc.getOneProduct(Integer.valueOf(productId));
		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("productVO" , productVO);
		return "front-end/seller/seller-product-all";
	}
	
	
	@PostMapping("update")
	public String update(@Valid ProductVO productVO, BindingResult result,ModelMap model,
			@RequestParam("upFiles") MultipartFile[] parts) throws IOException {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		result = removeFieldError(productVO , result , "upFiles");
		
		if(parts[0].isEmpty()) {
			byte[] upFiles = productSvc.getOneProduct(productVO.getProductId()).getProductCoverImg();
			productVO.setProductCoverImg(upFiles);
		}else {
			for(MultipartFile multipartFile : parts) {
				byte[] upFiles = multipartFile.getBytes();
				productVO.setProductCoverImg(upFiles);
			}			
		}
		if(result.hasErrors()) {
			return null;
		}
		/*************************** 2.開始修改資料 *****************************************/
		productSvc.updateProduct(productVO);
		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success" , "-(修改成功");
		productVO = productSvc.getOneProduct(Integer.valueOf(productVO.getProductId()));
		model.addAttribute("productVO" , productVO);
		return null;
	}
	
	@PostMapping("delete")
	public String delete(@RequestParam("productId") String productId , ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		productSvc.deleteProduct(Integer.valueOf(productId));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<ProductVO> list= productSvc.getAll();
		model.addAttribute("productListData" , list);
		model.addAttribute("success","-(刪除成功)");
		return null;
	}
	
	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(ProductVO productVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(productVO, "productVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

}
