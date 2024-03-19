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
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.config.BuyerPasswordEncoder;
import com.config.SellerPasswordEncoder;
import com.login.PasswordForm;
import com.seller.entity.SellerVO;
import com.seller.service.SellerService;
import com.sellerLv.entity.SellerLvVO;
import com.sellerLv.service.SellerLvService;
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
public class IndexControllerSeller {

	@Autowired
	SellerPasswordEncoder sellerPasswordEncoder;

	@Autowired
	SellerService sellerSvc;

	@Autowired
	SellerLvService sellerLvSvc;

	@ModelAttribute("sellerLvListData")
	protected List<SellerLvVO> referenceListData() {
		List<SellerLvVO> list = sellerLvSvc.getAll();
		// System.out.println("==============================");
		// list.forEach(data -> System.out.println(data));
		return list;
	}

	@GetMapping("seller/register")
	public String registerSellerv3(ModelMap model) {
		SellerVO sellerVO = new SellerVO();

		if (!model.containsAttribute("verificationCode")) {
			model.addAttribute("verificationCode", "");
		}

//		if (!model.containsAttribute("verificationCodeError")) {
//			model.addAttribute("verificationCodeError", "");
//		}

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
		sellerVO.setSellerBankAccountNumber("12321312322");

		// 防止被修改
		sellerVO.setSellerPassword(null);
		sellerVO.setIsConfirm(false);

		model.addAttribute("sellerVO", sellerVO);
		return "/front-end/seller/seller-registerv3";
	}

	@PostMapping("seller/register/check")
	public String checkregisterSellerv3(@Valid @NonNull SellerVO sellerVO, BindingResult result, ModelMap model,
			@RequestParam("verificationCode") String verificationCode) throws IOException {
		Jedis jedis = null;
		String email = null;
		try {
			jedis = JedisUtil.getJedisPool().getResource();
			email = sellerVO.getSellerEmail();
			jedis.select(15);
			String jedisCode = jedis.get("email:" + email);

			String errorCode = "";
			if (model.containsAttribute("verificationCodeError")) {
				errorCode = (String) model.getAttribute("verificationCodeError");
			}

			if (jedisCode == null) {
				errorCode = "驗證碼已經逾期，請重新發送驗證碼(By Th)";
				model.addAttribute("verificationCodeError", errorCode);
			} else if (verificationCode == null || verificationCode.equals("")) {
				errorCode = "請輸入驗證碼(By Th)";
				model.addAttribute("verificationCodeError", errorCode);
			} else if (!jedisCode.equals("ok") && !verificationCode.equals(jedisCode)) {
				errorCode = "驗證碼有誤(By Th)";
				model.addAttribute("verificationCodeError", errorCode);
			}

			if (sellerVO != null && !sellerSvc.isDuplcateEmail(sellerVO.getSellerEmail())) {
				System.out.println("AAAAAAAAA");
				result.rejectValue("sellerEmail", "duplicate.email", "信箱已經存在");
			}

			if (result.hasErrors() || model.containsAttribute("verificationCodeError")) {
				return "/front-end/seller/seller-registerv3";
			}

			model.remove("verificationCodeError");
			jedis.del("email:" + email);
			sellerSvc.saveUserDetails(sellerVO);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (email != null && !email.isEmpty()) {
				jedis.del("email:" + email);
			}
			if (jedis != null) {
				jedis.close();
			}
		}

		return "redirect:/seller/login";
	}

	@GetMapping({ "/seller/login", "/seller/login/errors" })
	public String loginSeller(ModelMap model, HttpServletRequest req) throws IOException {

		String error = (String) req.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION.message");

		if (error != null) {
			model.addAttribute("error", error);
		}

		return "front-end/seller/seller-login";
	}

	@PostMapping("/seller/register/checkVerificationCode")
	public ResponseEntity<?> checkVerificationCode(@RequestBody String json) {

//		System.out.println(json);

		JSONObject jsonObj = new JSONObject(json);
		String email = (String) jsonObj.get("email");
		String verifyCode = (String) jsonObj.get("code");

		try (Jedis jedis = JedisUtil.getJedisPool().getResource()) {
			jedis.select(15);
			String code = jedis.get("email:" + email);

			if (code == null) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResult<>(400, null, "驗證碼已經逾期，請重新發送驗證碼"));
			}
			if ((verifyCode.equals(code) || "ok".equals(code))) {
				jedis.set("email:" + email, "ok");
				return ResponseEntity.status(HttpStatus.OK).body(new HttpResult<>(200, null, "驗證成功"));
			} else if (!verifyCode.equals(code)) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResult<>(400, null, "驗證碼輸入有誤"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HttpResult<>(500, null, "系統異常，請稍後再試"));
	}

	@PostMapping("/seller/register/sendVerificationCode")
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
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.body(new HttpResult<>(HttpStatus.CONFLICT.value(), null, "稍後再試"));
			}

			jedis.setex("email:" + email, 300, String.valueOf(random6));

			MailService mailSvc = new MailService();
			mailSvc.sendMail(email, "驗證信件", "您的驗證碼是" + String.valueOf(random6));
			mailSvc = null;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new HttpResult<>(HttpStatus.CONFLICT.value(), null, "信箱格式錯誤"));
		}
		// 返回成功或其他適當的回應
		return ResponseEntity.status(HttpStatus.OK)
				.body(new HttpResult<>(HttpStatus.CONFLICT.value(), null, "請於300秒輸入驗證碼"));
	}

//	@GetMapping({ "/auth/phone" })
//	public String authInputPhone(ModelMap model) throws IOException {
//
//		if (!model.containsAttribute("verificationCode")) {
//			model.addAttribute("verificationCode", "");
//		}
//
//		if (!model.containsAttribute("sellerMobile")) {
//			model.addAttribute("sellerMobile", "");
//		}
//
//		return "/login/auth-phone";
//	}

//	@PostMapping({ "/auth/phone/check" })
//	@ResponseBody
//	public ResponseEntity<?> authInputPhoneCheck(@RequestBody String json, ModelMap model, BindingResult bindingResult)
//			throws IOException {
//
//		JSONObject jsonObj = new JSONObject(json);
//		String sellerMobile = (String) jsonObj.get("sellerMobile");
//		System.out.println(sellerMobile);
//
//		if (sellerMobile == null || !sellerMobile.matches("09\\d{8}")) {
//			System.out.println("輸入格式錯誤");
//			return ResponseEntity.badRequest().body(new HttpResult<>(400, null, "輸入格式錯誤"));
//		}
//
//		SellerVO sellerVO = sellerSvc.findByOnlyPhone(sellerMobile);
//		if (sellerVO == null) {
//			System.out.println("找不到電話");
//			return ResponseEntity.badRequest().body(new HttpResult<>(404, null, "找不到電話"));
//		}
//
//		return ResponseEntity.ok(new HttpResult<>(200, "success", "Success"));
//	}

	@GetMapping({ "/seller/auth/email" })
	public String authInputEmail(ModelMap model) throws IOException {
		if (!model.containsAttribute("sellerEmail")) {
			model.addAttribute("sellerEmail", "");
		}

		return "login/seller-auth-email";
	}

	@PostMapping({ "/seller/auth/email/check" })
	public ResponseEntity<?> authInputEmailCheck(@RequestBody String json, ModelMap model, BindingResult bindingResult,
			HttpServletRequest req) throws IOException {

		System.out.println("auth/email");

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
			String url = String.format("seller/activate/%s/%s", sellerVO.getSellerId(), urlUUID);
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

	@GetMapping("/seller/activate/{sellerId}/{tokenId}/add")
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

	@PostMapping("/seller/activate/{sellerId}/{tokenId}/check")
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
		sellerVO.setSellerPassword(sellerPasswordEncoder.encode(form.getPassword()));
		sellerSvc.updateSeller(sellerVO);

		return "redirect:/seller/login";
	}

}
