package com.seller.controller;

import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.seller.entity.SellerVO;
import com.seller.service.SellerService;
import com.sellerLv.entity.SellerLvVO;
import com.sellerLv.service.SellerLvService;

@Controller
@RequestMapping("/front/seller")
@ComponentScan(basePackages = { "com.seller", "com.sellerLv" })
public class SellerControllerFront extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Autowired
	SellerService sellerSvc;

	@Autowired
	SellerLvService sellerLvSvc;

	
    
    @GetMapping("main")
	   public String backMain(Model model) {
    	

	        return "front-end/seller/seller-main"; 
    }
    
    @ModelAttribute("sellerLvListData")
	protected List<SellerLvVO> referenceListData() {
		List<SellerLvVO> list = sellerLvSvc.getAll();
//		System.out.println("==============================");
//		list.forEach(data -> System.out.println(data));
//		System.out.println("==============================");
		return list;
	}
    

    @GetMapping("/seller/edit")
	   public String selleredit(
	            Model model,HttpSession session) {
    	
    	SecurityContext secCtx = SecurityContextHolder.getContext();
    	Authentication authentication = secCtx.getAuthentication();
    	SellerVO sellerVO = (SellerVO)authentication.getPrincipal();
		model.addAttribute("sellerVO",sellerVO);
		
	    return "front-end/seller/seller-seller-edit"; 
    }
    
    
    @PostMapping("/seller/update")
	   public String sellerupdate(@Valid @NonNull SellerVO sellerVO, 
	            Model model, BindingResult result) {
//		System.out.println(sellerVO);

		if (result.hasErrors()) {
//			System.out.println("==============XXXXXXXXXXXXXX");
//			System.out.println("updateSeller");
//			System.out.println(result);
//			System.out.println("==============XXXXXXXXXXXXXX");
			return "front-end/seller/seller-seller-edit";
		}

		sellerSvc.updateSeller(sellerVO);

		
		
		return "redirect:/front/seller/main"+"?updateSuccess=true";
 }
	


}
