package com.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.product.model.ProductService;
import com.product.model.ProductVO;

@Controller
@RequestMapping("/back/product")

public class ProductBackController {
	
	@Autowired
	private ProductService productSvc;
	

	@GetMapping("list")
	public String listAllProduct(ModelMap model) {
		return "back-end/back-product-list";
	}
	
	@ModelAttribute("productListData")
	protected List<ProductVO> referenceListData(){
		
		List<ProductVO> list = productSvc.getAll();
		return list;
		
	}

}
