package com.ad.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ad.model.AdService;
import com.ad.model.AdVO;

@Controller
@RequestMapping("/back/ad")

public class AdBackController {

	
	@Autowired
	private AdService adSvc;	
	
	@GetMapping("list")
	public String listAllProduct(ModelMap model) {
		return "back-end/back-ad-list";
	}
	
	@ModelAttribute("adListData")
	protected List<AdVO> referenceListData(){
		List<AdVO> list = adSvc.getAll();		
		return list;		
	}
	
	

}
