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
@RequestMapping("/back/seller")
public class SellerController extends HttpServlet {


	private static final long serialVersionUID = 1L;

	@Autowired
	SellerService sellerSvc;

	@Autowired
	SellerLvService sellerLvSvc;

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 :
	 * <form:select path="deptno" id="deptno" items="${deptListData}"
	 * itemValue="deptno" itemLabel="dname" />
	 */
	@ModelAttribute("sellerLvListData")
	protected List<SellerLvVO> referenceListData() {
		List<SellerLvVO> list = sellerLvSvc.getAll();
//		System.out.println("==============================");
//		list.forEach(data -> System.out.println(data));
//		System.out.println("==============================");
		return list;
	}

	@ModelAttribute("sellerListData")
	protected List<SellerVO> referenceListData(Model model) {
		List<SellerVO> list = sellerSvc.getAll();
//		System.out.println("==============================");
//		list.forEach(data -> System.out.println(data));
//		System.out.println("==============================");

		return list;
	}

	// /back/seller/listAll
	@GetMapping("list")
	public String listAllSeller(ModelMap model) {
		System.out.println();
		return "back-end/back-seller-list";
	}
	
	
	// FOR JWT
	@PostMapping("list")
	public String listAllSellerJWT(ModelMap model) {
		return "back-end/back-seller-list";
	}

	@GetMapping("add")
	public String addSeller(ModelMap model) {
		SellerVO sellerVO = new SellerVO();
		model.addAttribute("sellerVO", sellerVO);
		return "back-end/back-seller-add";
	}

	@GetMapping("getOne_For_Update")
	public String getOneSeller(@RequestParam("id") Integer sellerId, ModelMap model) {
		SellerVO sellerVO = sellerSvc.getById(sellerId);
		model.addAttribute("sellerVO", sellerVO);
//		System.out.println("==============XXXXXXXXXXXXXX");
//		System.out.println("getOne_For_Update");
//		System.out.println(sellerVO);
//		System.out.println("==============XXXXXXXXXXXXXX");

		return "back-end/back-seller-edit";
	}

	@PostMapping("update")
	public String updateSeller(@Valid @NonNull SellerVO sellerVO, BindingResult result, ModelMap model)
			throws IOException {
//		System.out.println(sellerVO);

		if (result.hasErrors()) {
//			System.out.println("==============XXXXXXXXXXXXXX");
//			System.out.println("updateSeller");
//			System.out.println(result);
//			System.out.println("==============XXXXXXXXXXXXXX");
			return "back-end/back-seller-edit";
		}

		sellerSvc.updateSeller(sellerVO);
		model.addAttribute("success", "- (修改成功)");
		model.addAttribute("sellerVO", sellerVO);

		return "redirect:/back/seller/list";
	}
	

	@DeleteMapping("delete")
	public String delete(@RequestParam("id") @NonNull Integer sellerId, ModelMap model) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行

		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		sellerSvc.deleteSeller(sellerId);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/

		return "redirect:/back/seller/list";
		// 一定要用Redirect，不然會會導致資料重複送
		// 新增成功後重導至IndexController_inSpringBoot.java的第50行@GetMapping("/user/listAllUser")
	}

	@PostMapping("insert")
	public String insert(@Valid @NonNull SellerVO sellerVO, BindingResult result, ModelMap model) throws IOException {
		
		if (result.hasErrors()) {
			return "back-end/back-seller-add";
		}
		sellerSvc.addSeller(sellerVO);
		
		
		List<SellerVO> list = sellerSvc.getAll();
		model.addAttribute("sellerListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/seller/listAllSeller";
		// 一定要用Redirect，不然會會導致資料重複送
		// 新增成功後重導至IndexController_inSpringBoot.java的第50行@GetMapping("/user/listAllUser")
	}
	
	
	 // Add this method to handle AJAX requests
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> handleAjaxRequest(@RequestBody JsonNode jsonNode) {
	    try {
	    	SellerVO sellerVO = new SellerVO();

	        String username = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();


	    	sellerVO.setSellerEmail(username);
	    	sellerVO.setSellerPassword(password);

	    	// For Testing
			sellerVO.setSellerBankAccount("123456789");
			sellerVO.setSellerMobile("0988319004");
			sellerVO.setSellerContact("JohnDoe");
			sellerVO.setSellerTaxId("123");
			
			sellerVO.setSellerCompanyPhone("987654321");
			sellerVO.setSellerCompany("ABC Company");
			sellerVO.setSellerCompanyExtension("123");

	        sellerSvc.saveUserDetails(sellerVO);

	        return ResponseEntity.status(HttpStatus.OK).body("{\"success\":true}");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("{\"success\":false, \"error\":\"" + e.getMessage() + "\"}");
	    }
	}


	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(UserVO userVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(userVO, "userVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	
	

}
