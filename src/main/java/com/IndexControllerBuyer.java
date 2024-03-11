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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.buyer.service.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.login.PasswordForm;
import com.seller.entity.SellerVO;
import com.buyer.entity.*;
import com.buyer.service.*;
//import com.sellerLv.entity.SellerLvVO;
//import com.sellerLv.service.SellerLvService;
import com.user.model.UserService;
import com.util.HttpResult;
import com.util.JedisUtil;
import com.util.MailService;

import redis.clients.jedis.Jedis;

//@PropertySource("classpath:application.properties") 
// 於https://start.spring.io 建立Spring Boot專案時
// (1) @SpringBootApplication 註解包含了 @ComponentScan，預設會掃描與主應用程式相同套件及其子套件中的組件
// (2) @Controller 註解標註的類別放在主應用程式套件及其子套件中
// (3) src/main/java / src/main/{language} /  src/main/webapp / src/main/resources

@Controller
public class IndexControllerBuyer {
	
	
	@Autowired
	BuyerService buyerSvc;
	
	@PostMapping("/buyer/register/check")
	public String checkregisterBuyer(@Valid @NonNull BuyerVO buyerVO, BindingResult result, ModelMap model) 
			throws IOException {
		System.out.println("有進入buyer/register/check");
		
		// 方便註冊測試, 先把錯誤驗證關閉, 之後要補
//		if (result.hasErrors()) {
//	        return "/front-end/buyer/buyer-register";
//		}
		buyerSvc.saveUserDetails(buyerVO);
//		model.addAttribute("success", "買家用戶註冊成功");
//
//		// TESTING 註冊登入後保存sellerVO狀態
//		session.setAttribute("buyerVO", buyerVO);
//		
		System.out.println("重導回/front/buyer/login");
		return "redirect:/buyer/login";
	}

    @GetMapping("/front/buyer/main")
    public String buyermain(Model model) {
        return "/front-end/buyer/buyer-main";
        // resources/template//index.html
    }
    
    @GetMapping("/front/buyer/pic")
    public String TestV2(Model model) {
        return "/front-end/buyer/buyer-commidityV2";
    }
	
    @GetMapping("/front/buyer/buyer-like")
    public String buyerLike(Model model) {
        return "/front-end/buyer/buyer-like";
        // resources/template//index.html
    }
	
	@PostMapping({ "/buyer/login", "/buyer/login/errors" })
	public String loginBuyer(ModelMap model, HttpServletRequest req) throws IOException {

		System.out.println("=======OOOOOOOO");
		String error = (String) req.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION.message");
		if (error != null) {
			model.addAttribute("error", error);
		}

		return "front-end/buyer/buyer-login";
	}

	@PostMapping("/buyer/register/checkVerificationCode")
	public ResponseEntity<?> checkVerificationCode(@RequestBody String json) {
		
		
		JSONObject jsonObj = new JSONObject(json);
		String email = (String) jsonObj.get("email");
		String verifyCode = (String) jsonObj.get("code");

		
		
		try (Jedis jedis = JedisUtil.getJedisPool().getResource()) {
			jedis.select(15);
			String code = jedis.get("email:" + email);

			
			
			
			if (code != null && (verifyCode.equals(code) || "ok".equals(code))) {
				jedis.set("email:" + email, "ok");
				return ResponseEntity.status(HttpStatus.OK).body(new HttpResult<>(HttpStatus.OK.value(), null, "驗證成功"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.badRequest().body("輸入有誤");
	}

	@PostMapping("/buyer/register/sendVerificationCode")
	public ResponseEntity<?> sendVerificationCode(@RequestBody String emailJson) {

		try (Jedis jedis = JedisUtil.getJedisPool().getResource()) {
			jedis.select(15);

			char[] random6 = new char[6];
			for (int i = 0; i < 6; i++) {
				int randomValue = (int) (Math.random() * 26);
				random6[i] = (char) (randomValue + 65);
			}

			JSONObject jsonObj = new JSONObject(emailJson);
			String email = jsonObj.getString("email");

			
			if (jedis.exists("email:" + email)) {
				
				return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResult<>(HttpStatus.CONFLICT.value(), null, "稍後再試"));
			}

			jedis.setex("email:" + email, 300, String.valueOf(random6));

		
			MailService mailSvc = new MailService();
			mailSvc.sendMail(email, "驗證信件", "您的驗證碼是" + String.valueOf(random6));
			mailSvc = null;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResult<>(HttpStatus.CONFLICT.value(), null, "信箱格式錯誤"));
		}
		// 返回成功或其他適當的回應
		return ResponseEntity.status(HttpStatus.OK).body(new HttpResult<>(HttpStatus.CONFLICT.value(), null, "請於300秒輸入驗證碼"));
	}

	@GetMapping({ "/auth/emailbuyer" })
	public String authInputEmail(ModelMap model) throws IOException {
		if (!model.containsAttribute("memberEmail")) {
			model.addAttribute("memberEmail", "");
		}

		return "/login/auth-email";
	}

	@PostMapping({ "/auth/email/checkbuyer" })
	public ResponseEntity<?> authInputEmailCheck(@RequestBody String json, ModelMap model, BindingResult bindingResult,
			HttpServletRequest req) throws IOException {
		JSONObject jsonObj = new JSONObject(json);
		String buyerEmail = (String) jsonObj.get("buyerEmail");

		if (buyerEmail == null || !buyerEmail.matches("[\\w+_-]*\\@\\w+\\.\\w+")) {
			// System.out.println("輸入格式錯誤");
			return ResponseEntity.badRequest().body(new HttpResult<>(400, null, "輸入格式錯誤"));
		}

		BuyerVO buyerVO = buyerSvc.findByOnlyOneEmail(buyerEmail);
		if (buyerVO == null) {
			// System.out.println("找不到信箱");
			return ResponseEntity.badRequest().body(new HttpResult<>(404, null, "找不到信箱"));
		}

		try (Jedis jedis = JedisUtil.getJedisPool().getResource()) {
			jedis.select(15);

			String urlUUID = UUID.randomUUID().toString();
			String scheme = req.getScheme();
			String servletName = req.getServerName();
			String port = String.valueOf(req.getServerPort());
			String path = req.getRequestURI();
			String url = String.format("activate/buyer/%s/%s", buyerVO.getMemberId(), urlUUID);
			String ctxPath = req.getContextPath();
			String authPath = scheme + "://" + servletName + ":" + port + ctxPath + "/" + url + "/add";

			jedis.setex("FORGOT:BUYER:" + buyerVO.getMemberId(), 1 * 60 * 60, urlUUID);

			MailService mailSvc = new MailService();

			mailSvc.sendMail(buyerVO.getMemberEmail(), "驗證信件", "驗證碼網址:" + authPath);
			mailSvc = null;

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new HttpResult<>(500, null, "系統忙線中，稍後"));
		}

		return ResponseEntity.ok().body(new HttpResult<>(200, null, "傳送成功"));
	}
	
	@PostMapping("/activate/buyer/{memberId}/{tokenId}/check")
	public String authenticationCheckUser(@Valid PasswordForm form, BindingResult bindingResult,
			@PathVariable Integer memberId, @PathVariable String tokenId) {
		// System.out.println(bindingResult);
		if (bindingResult.hasErrors()) {
			return "/login/reset-password";
		}

		if (form != null && !form.getConfirmPassword().equals(form.getPassword())) {
			bindingResult.rejectValue("password", "password.not.match", "密碼和確認密碼不匹配");
			return "/login/reset-password";
		}

		try (Jedis jedis = JedisUtil.getJedisPool().getResource()) {
			jedis.select(15);
			String authKey = "FORGOT:BUYER:" + memberId;
			if (jedis.get(authKey) != null) {
				jedis.del(authKey);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		BuyerVO buyerVO = buyerSvc.getOneBuyer(memberId);
		buyerVO.setMemberPassword(form.getPassword());
		buyerSvc.updateUserDetails(buyerVO);

		return "redirect:/buyer/login";
	}
	
}



