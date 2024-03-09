package com;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.buyer.entity.BuyerVO;
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

//@PropertySource("classpath:application.properties") 
// 於https://start.spring.io 建立Spring Boot專案時
// (1) @SpringBootApplication 註解包含了 @ComponentScan，預設會掃描與主應用程式相同套件及其子套件中的組件
// (2) @Controller 註解標註的類別放在主應用程式套件及其子套件中
// (3) src/main/java / src/main/{language} /  src/main/webapp / src/main/resources

@Controller
public class IndexControllerMain {

	@Autowired
	UserService userSvc;

	@Autowired
	SellerLvService sellerLvSvc;

	@Autowired
	SellerService sellerSvc;

	@GetMapping("/")
	public String index(Model model) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			// 用户已认证，添加相应属性
			
			System.out.println("loggedIn true");
			System.out.println(authentication.getPrincipal());
			
			model.addAttribute("loggedIn", true);
			Object principal = authentication.getPrincipal();
			if (principal instanceof SellerVO) {
				SellerVO sellerVO = (SellerVO) principal;
				model.addAttribute("sellerVO", sellerVO);
				model.addAttribute("theName", sellerVO.getSellerCompany());
			}

		} else {
			System.out.println("loggedIn false");
			model.addAttribute("loggedIn", false);
		}

		System.out.println("loggedIn false");

		// System.out.println("authentication" + authentication);
		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();
			System.out.println("securityContext" + username);
			System.out.println("getCredentials" + authentication.getCredentials());
			System.out.println("getDetails" + authentication.getDetails());
			System.out.println("getAuthorities" + authentication.getAuthorities());
		}
		return "index";
		// resources/template//index.html
	}

	@ModelAttribute("sellerLvListData")
	protected List<SellerLvVO> referenceListData() {
		List<SellerLvVO> list = sellerLvSvc.getAll();
		// System.out.println("==============================");
		// list.forEach(data -> System.out.println(data));
		// System.out.println("==============================");
		return list;
	}

	@GetMapping("/seller/register")
	public String registerSeller(ModelMap model) throws IOException {

		model.addAttribute("verificationCode", "");
		SellerVO sellerVO = new SellerVO();

		// TEST
		sellerVO.setSellerEmail("ncku4015@gmail.com");
		sellerVO.setSellerCompany("ABC Company");
		sellerVO.setSellerTaxId("12345");
		sellerVO.setSellerCapital(500000);
		sellerVO.setSellerContact("John Doe");
		sellerVO.setSellerCompanyPhone("1234567890");
		sellerVO.setSellerCompanyExtension("123");
		sellerVO.setSellerMobile("0912345678");
		sellerVO.setSellerCounty("台北市");
		sellerVO.setSellerDistrict("大安區");
		sellerVO.setSellerAddress("123 Main St");
		sellerVO.setSellerPassword("Password123");
		sellerVO.setSellerBankAccount("ABC Bank");
		sellerVO.setSellerBankCode("123");
		sellerVO.setSellerBankAccountNumber("0988319004");

		// 防止被修改
		sellerVO.setSellerPassword(null);
		sellerVO.setIsConfirm(false);

		model.addAttribute("sellerVO", sellerVO);
		return "/front-end/seller/seller-register";
	}

	@GetMapping({ "/seller/login", "/seller/login/errors" })
	public String loginSeller(ModelMap model, HttpServletRequest req) throws IOException {

		String error = (String) req.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION.message");
		if (error != null) {
			model.addAttribute("error", error);
		}

		return "front-end/seller/seller-login";
	}

	@GetMapping("/buyer/register")
	public String registerBuyer(ModelMap model) throws IOException {
		BuyerVO buyerVO = new BuyerVO();

		// TEST
		buyerVO.setMemberEmail("lulu.doe@example.com");
		buyerVO.setThirdFrom(null);
		buyerVO.setMemberName("Lulu");
		buyerVO.setMemberPhone("03123321");
		buyerVO.setMemberMobile("09777666");
		buyerVO.setMemberBirthday(null);
		buyerVO.setMemberAddress("地址");
		buyerVO.setIsMemberEmail(false);

		java.util.Date utilDate = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		buyerVO.setMemberRegistrationTime(sqlDate);
		buyerVO.setPetName("寵物啦");
		buyerVO.setPetImg(null);
		buyerVO.setPetImgUploadTime(null);
		buyerVO.setPetVaccName1(null);
		buyerVO.setPetVaccTime1(null);
		buyerVO.setPetVaccName2(null);
		buyerVO.setPetVaccTime2(null);

		// 防止被修改
		buyerVO.setMemberPassword(null);
		buyerVO.setIsConfirm(true);

		model.addAttribute("buyerVO", buyerVO);
		return "/front-end/buyer/buyer-register";
	}

	@GetMapping("/buyer/login")
	public String loginBuyer(ModelMap model) throws IOException {
		return "/front-end/buyer/buyer-login";
	}

	@PostMapping("/seller/register/checkVerificationCode")
	public ResponseEntity<?> checkVerificationCode(@RequestBody String json) {
		JSONObject jsonObj = new JSONObject(json);
		String email = (String) jsonObj.get("email");
		String verifyCode = (String) jsonObj.get("code");
		try (Jedis jedis = JedisUtil.getJedisPool().getResource()) {
			jedis.select(15);
			String code = jedis.get("email:" + email);

			// System.out.println(email);
			// System.out.println(code);

			if (code != null && (verifyCode.equals(code) || "ok".equals(code))) {
				jedis.set("email:" + email, "ok");
				return ResponseEntity.ok("success");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.badRequest().body("輸入有誤");
	}

	@PostMapping("/seller/register/sendVerificationCode")
	public ResponseEntity<?> sendVerificationCode(@RequestBody String emailJson) {

		try (Jedis jedis = JedisUtil.getJedisPool().getResource()) {
			char[] random6 = new char[6];
			for (int i = 0; i < 6; i++) {
				int randomValue = (int) (Math.random() * 26);
				random6[i] = (char) (randomValue + 65);
			}

			JSONObject jsonObj = new JSONObject(emailJson);
			String email = jsonObj.getString("email");

			System.out.println(email);

			if (jedis.exists("email:" + email)) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("稍後再試");
			}

			jedis.select(15);
			jedis.setex("email:" + email, 300, String.valueOf(random6));

			MailService mailSvc = new MailService();
			mailSvc.sendMail(email, "驗證信件", "您的驗證碼是" + String.valueOf(random6));
			mailSvc = null;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("信箱格式錯誤");

		}
		// 返回成功或其他適當的回應
		return ResponseEntity.ok("請於300秒輸入驗證碼");
	}

	@GetMapping({ "/auth/phone" })
	public String authInputPhone(ModelMap model) throws IOException {

		if (!model.containsAttribute("verificationCode")) {
			model.addAttribute("verificationCode", "");
		}

		if (!model.containsAttribute("sellerMobile")) {
			model.addAttribute("sellerMobile", "");
		}

		return "/login/auth-phone";
	}

	@PostMapping({ "/auth/phone/check" })
	@ResponseBody
	public ResponseEntity<?> authInputPhoneCheck(@RequestBody String json, ModelMap model, BindingResult bindingResult)
			throws IOException {

		JSONObject jsonObj = new JSONObject(json);
		String sellerMobile = (String) jsonObj.get("sellerMobile");
		System.out.println(sellerMobile);

		if (sellerMobile == null || !sellerMobile.matches("09\\d{8}")) {
			System.out.println("輸入格式錯誤");
			return ResponseEntity.badRequest().body(new HttpResult<>(400, null, "輸入格式錯誤"));
		}

		SellerVO sellerVO = sellerSvc.findByOnlyPhone(sellerMobile);
		if (sellerVO == null) {
			System.out.println("找不到電話");
			return ResponseEntity.badRequest().body(new HttpResult<>(404, null, "找不到電話"));
		}

		return ResponseEntity.ok(new HttpResult<>(200, "success", "Success"));
	}

	@GetMapping({ "/auth/email" })
	public String authInputEmail(ModelMap model) throws IOException {
		if (!model.containsAttribute("sellerEmail")) {
			model.addAttribute("sellerEmail", "");
		}

		return "/login/auth-email";
	}

	@PostMapping({ "/auth/email/check" })
	public ResponseEntity<?> authInputEmailCheck(@RequestBody String json, ModelMap model, BindingResult bindingResult,
			HttpServletRequest req) throws IOException {
		JSONObject jsonObj = new JSONObject(json);
		String sellerEmail = (String) jsonObj.get("sellerEmail");

		if (sellerEmail == null || !sellerEmail.matches("[\\w+_-]*\\@\\w+\\.\\w+")) {
			// System.out.println("輸入格式錯誤");
			return ResponseEntity.badRequest().body(new HttpResult<>(400, null, "輸入格式錯誤"));
		}

		SellerVO sellerVO = sellerSvc.findByOnlyOneEmail(sellerEmail);
		if (sellerVO == null) {
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
			String url = String.format("activate/seller/%s/%s", sellerVO.getSellerId(), urlUUID);
			String ctxPath = req.getContextPath();
			String authPath = scheme + "://" + servletName + ":" + port + ctxPath + "/" + url + "/add";

			jedis.setex("FORGOT:SELLER:" + sellerVO.getSellerId(), 1 * 60 * 60, urlUUID);

			MailService mailSvc = new MailService();

			mailSvc.sendMail(sellerVO.getSellerEmail(), "驗證信件", "驗證碼網址:" + authPath);
			mailSvc = null;

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new HttpResult<>(500, null, "系統忙線中，稍後"));
		}

		return ResponseEntity.ok().body(new HttpResult<>(200, null, "傳送成功"));
	}

	@GetMapping("/activate/seller/{sellerId}/{tokenId}/add")
	public String authenticationUser(@PathVariable Integer sellerId, @PathVariable String tokenId, ModelMap model)
			throws IOException {
		PasswordForm passwordForm = new PasswordForm();
		model.addAttribute("passwordForm", passwordForm);

		try (Jedis jedis = JedisUtil.getJedisPool().getResource()) {
			jedis.select(15);
			String authKey = "FORGOT:SELLER:" + sellerId;
			String storedToken = jedis.get(authKey);
			if (storedToken == null) {
				model.addAttribute("error", "驗證信已經過期");
				return "/login/authentication-failure";
			}
			if (storedToken.equals(tokenId)) {

				return "/login/reset-password";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "/login/authentication-failure";
		}

		return "/login/authentication-failure";
	}

	@PostMapping("/activate/seller/{sellerId}/{tokenId}/check")
	public String authenticationCheckUser(@Valid PasswordForm form, BindingResult bindingResult,
			@PathVariable Integer sellerId, @PathVariable String tokenId) {
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
		    String authKey = "FORGOT:SELLER:" + sellerId;
		    if (jedis.get(authKey) != null) {
		        jedis.del(authKey);
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		
		SellerVO sellerVO = sellerSvc.getById(sellerId);
		sellerVO.setSellerPassword(form.getPassword());
		sellerSvc.updateUserDetails(sellerVO);
	
		

		return "redirect:/seller/login";
	}
}
