package com.seller.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
	public String backMain(Model model, HttpSession session) {
	    Boolean updateSuccessShown = (Boolean) session.getAttribute("sellerEditSuccess");

	    model.addAttribute("sellerEditSuccess", false);

	    if (updateSuccessShown != null) {
	        if (updateSuccessShown) {
	            model.addAttribute("sellerEditSuccess", true);
	            session.setAttribute("sellerEditSuccess", false);
	            return "front-end/seller/seller-main";
	        } else if (!updateSuccessShown) {
	            session.removeAttribute("sellerEditSuccess");
	            return "redirect:/front/seller/main";
	        }
	    }

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
	public String selleredit(Model model, HttpSession session) {
		SecurityContext secCtx = SecurityContextHolder.getContext();
		Authentication authentication = secCtx.getAuthentication();
		SellerVO sellerVO = (SellerVO) authentication.getPrincipal();
		model.addAttribute("sellerVO", sellerVO);

		return "front-end/seller/seller-seller-edit";
	}

	@PostMapping("/seller/update")
	public String sellerupdate(@Valid @NonNull SellerVO sellerVO, Model model, BindingResult result,
			HttpSession session) {
//		System.out.println(sellerVO);

		if (result.hasErrors()) {
//			System.out.println("==============XXXXXXXXXXXXXX");
//			System.out.println("updateSeller");
//			System.out.println(result);
//			System.out.println("==============XXXXXXXXXXXXXX");
			return "front-end/seller/seller-seller-edit";
		}
		
		// 原則上重新更改後，也需要驗證才能登入
//		sellerVO.setIsConfirm(true);

		sellerSvc.updateSeller(sellerVO);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Integer sellerLvId = sellerVO.getSellerLvId().getSellerLvId();
		List<GrantedAuthority> authorities = new ArrayList<>();

		switch (sellerLvId) {
		case 1:
			authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
			authorities.add(new SimpleGrantedAuthority("ROLE_SELLERLV1"));
			break;
		case 2:
			authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
			authorities.add(new SimpleGrantedAuthority("ROLE_SELLERLV2"));
			break;
		case 3:
			authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
			authorities.add(new SimpleGrantedAuthority("ROLE_SELLERLV3"));
			break;
		}

		SecurityContextHolder.getContext()
				.setAuthentication(new UsernamePasswordAuthenticationToken(sellerVO, null, authorities));

		Boolean updateSuccessShown = (Boolean) session.getAttribute("sellerEditSuccess");

		// 如果成功消息未显示过，则设置标志位并重定向到带有成功消息的 URL
		session.setAttribute("sellerEditSuccess", true);
		return "redirect:/front/seller/main";

	}

}
