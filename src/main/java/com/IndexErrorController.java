package com;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.login.PasswordForm;
import com.seller.entity.SellerVO;
import com.seller.service.SellerService;
import com.sellerLv.entity.SellerLvVO;
import com.sellerLv.service.SellerLvService;
import com.user.model.UserService;
import com.util.HttpResult;
import com.util.JedisUtil;
import com.util.MailService;

import redis.clients.jedis.Jedis;

@Controller
public class IndexErrorController {

	@GetMapping("/error/seller/sellerAuthError403")
	public String errorSellerAuth403(Model model) {
		return "/error/sellerAuthError403";
	}
	
	@GetMapping("/error/seller/sellerLvError403")
	public String errorSellerLv403(Model model) {
		return "/error/sellerLvError403";
	}
	
	@GetMapping("/error/buyer/buyerAuthError403")
	public String errorBuyerLv403(Model model) {
		return "/error/buyerAuthError403";
	}
	
	
	@GetMapping("/error/generalError404")
	public String errorGeneralError404(Model model) {
		return "/error/generalError404";
	}
	

}
