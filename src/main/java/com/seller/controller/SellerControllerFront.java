package com.seller.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServlet;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.seller.entity.SellerVO;
import com.seller.service.SellerService;
import com.sellerLv.entity.SellerLvVO;
import com.sellerLv.service.SellerLvService;
import com.user.model.UserVO;

@Controller
@ComponentScan(basePackages = { "com.seller", "com.sellerLv" })
@RequestMapping("/front/seller/seller")
public class SellerControllerFront extends HttpServlet {

	@Autowired
	SellerService sellerSvc;

	@Autowired
	SellerLvService sellerLvSvc;

	
	@ModelAttribute("sellerLvListData")
	protected List<SellerLvVO> referenceListData() {
		List<SellerLvVO> list = sellerLvSvc.getAll();
//		System.out.println("==============================");
//		list.forEach(data -> System.out.println(data));
//		System.out.println("==============================");
		return list;
	}
	
	
	
	// /front/seller/register
	@GetMapping("checkregister")
	public String checkregisterSeller(@Valid @NonNull SellerVO sellerVO, BindingResult result, ModelMap model) 	throws IOException {
		if (result.hasErrors()) {
			return "/front/seller/register";
		}
		sellerSvc.addSeller(sellerVO);
		model.addAttribute("success", "註冊成功");

		return "/front/seller/success";
	}
	
	// /front/seller/sellermain
//	@GetMapping("sellermain")
//	public String sellerMain(
//            Model model)  {
//
//		return "redirect:/front/seller/seller-main";
//	}
 
	

}
