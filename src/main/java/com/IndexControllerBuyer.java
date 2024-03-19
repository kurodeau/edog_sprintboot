package com;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.buyer.entity.BuyerVO;
import com.buyer.service.BuyerService;
import com.config.BuyerPasswordEncoder;
import com.login.PasswordForm;
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
	BuyerPasswordEncoder buyerPasswordEncoder;
	
	@Autowired
	BuyerService buyerSvc;
	
	
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

	@GetMapping({"/buyer/login","/buyer/login?error"})
	public String loginBuyer(ModelMap model) throws IOException {
		return "/front-end/buyer/buyer-login";
	}
	
	
	@PostMapping({"/buyer/login/check"})
	public String loginCheckBuyer(ModelMap model) throws IOException {
		
		return "redirect:/front/buyer/main";
	}
	
	
	@PostMapping("/buyer/register/check")
	public String checkregisterBuyer(@Valid BuyerVO buyerVO,  BindingResult result , ModelMap model 
	 ,@RequestParam("petImg") MultipartFile[] parts ,@RequestParam("checkMemberPassword") String dupPassword) throws IOException{

		buyerVO.setMemberRegistrationTime(new Date());
// checkMemberPassword
		result = removeFieldError(buyerVO, result, "petImg");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
			model.addAttribute("errorMessage", "請上傳PET圖片");

		} 
		
		System.out.println(dupPassword);
		System.out.println(buyerVO.getMemberPassword());

		
		if (!dupPassword.equals(buyerVO.getMemberPassword())) {
			model.addAttribute("duplicateError", "密碼不一致");
		}
		
		System.out.println(result);
		if (result.hasErrors()|| parts[0].isEmpty()) {
			return "/front-end/buyer/buyer-register";
		}
		
		
		for (MultipartFile multipartFile : parts) {
			byte[] petImg = multipartFile.getBytes();
			buyerVO.setPetImg(petImg);
		}
		
		buyerSvc.saveUserDetails(buyerVO);
	
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

	@GetMapping({ "/buyer/auth/email" })
	public String authInputBuyerEmail(ModelMap model) throws IOException {
		if (!model.containsAttribute("memberEmail")) {
			model.addAttribute("memberEmail", "");
		}

		return "/login/buyer-auth-email";
	}

	@PostMapping({ "/buyer/auth/email/check" })
	public ResponseEntity<?> authInputEmailBuyerCheck(@RequestBody String json, ModelMap model, BindingResult bindingResult,
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
			String url = String.format("buyer/activate/%s/%s", buyerVO.getMemberId(), urlUUID);
			String ctxPath = req.getContextPath();
			String authPath = scheme + "://" + servletName + ":" + port + ctxPath + "/" + url + "/add";

			jedis.setex("FORGOT:BUYER:" + buyerVO.getMemberId(), 1 * 60 * 60, urlUUID);

			MailService mailSvc = new MailService();

			System.out.println(url);
			mailSvc.sendMail(buyerVO.getMemberEmail(), "驗證信件", "驗證碼網址:" + authPath);
			mailSvc = null;

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new HttpResult<>(500, null, "系統忙線中，稍後"));
		}

		return ResponseEntity.ok().body(new HttpResult<>(200, null, "傳送成功"));
	}


	@GetMapping("/buyer/activate/{buyerId}/{tokenId}/add")
	public String authenticationUser(@PathVariable Integer buyerId, @PathVariable String tokenId, ModelMap model)
			throws IOException {

		PasswordForm passwordForm = new PasswordForm();
		model.addAttribute("passwordForm", passwordForm);

		try (Jedis jedis = JedisUtil.getJedisPool().getResource()) {
			jedis.select(15);
			String authKey = "FORGOT:BUYER:" + buyerId;
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
	
	
	@PostMapping("/buyer/activate/{buyerId}/{tokenId}/check")
	public String authenticationCheckUser(@Valid PasswordForm form, BindingResult bindingResult,
			@PathVariable Integer buyerId, @PathVariable String tokenId) {
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
			String authKey = "FORGOT:BUYER:" + buyerId;
			if (jedis.get(authKey) != null) {
				jedis.del(authKey);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		BuyerVO buyerVO = buyerSvc.getOneBuyer(buyerId);
		buyerVO.setMemberPassword(buyerPasswordEncoder.encode(form.getPassword()));
		buyerSvc.updateBuyer(buyerVO);
		
		return "redirect:/buyer/login";
	}

	
	
	public BindingResult removeFieldError(BuyerVO buyerVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(buyerVO, "buyerVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
}



