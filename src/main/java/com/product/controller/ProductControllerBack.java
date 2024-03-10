package com.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/back/product")

public class ProductControllerBack {

	@GetMapping("list")
	public String listAllProduct(ModelMap model) {
		return "back-end/back-product-list";
	}
	
	

}
