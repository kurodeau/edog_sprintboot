package com.seller.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServlet;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.seller.entity.SellerVO;
import com.seller.service.SellerService;
import com.sellerLv.entity.SellerLvVO;
import com.sellerLv.service.SellerLvService;
import com.user.model.UserVO;

@Controller
@ComponentScan(basePackages = {"com.seller", "com.sellerLv"})
@RequestMapping("/back/seller")
public class SellerController extends HttpServlet {

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
		System.out.println("==============================");
	    list.forEach(data -> System.out.println(data));
		System.out.println("==============================");
	 return list;
	 }
	
	
	@ModelAttribute("sellerListData") 
	protected List<SellerVO> referenceListData(Model model) {
		List<SellerVO> list = sellerSvc.getAll();
		System.out.println("==============================");
	    list.forEach(data -> System.out.println(data));
		System.out.println("==============================");

		return list;
	}

	// /back/seller/listAll
	@GetMapping("listAll")
	public String listAllSeller(ModelMap model) {
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
	    System.out.println("==============XXXXXXXXXXXXXX");
	    System.out.println("getOne_For_Update");
	    System.out.println(sellerVO);
	    System.out.println("==============XXXXXXXXXXXXXX");

	    return "back-end/back-seller-edit";
	}
	
	@PostMapping("update")
	public String updateSeller(@Valid @NonNull SellerVO sellerVO, BindingResult result, ModelMap model) throws IOException {

		if (result.hasErrors()) {
		    System.out.println("==============XXXXXXXXXXXXXX");
		    System.out.println("updateSeller");
		    System.out.println(result);
		    System.out.println("==============XXXXXXXXXXXXXX");
		    return "back-end/back-seller-edit";
		}
		
		sellerSvc.updateSeller(sellerVO);
		model.addAttribute("success", "- (修改成功)");
		sellerVO = sellerSvc.getById(Integer.valueOf(sellerVO.getSellerId()));
		model.addAttribute("userVO", sellerVO);
		
	    return "redirect:/back/seller/listAll";
	}

	@PostMapping("insert")
	public String insert(@Valid SellerVO sellerVO, BindingResult result, ModelMap model) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行

		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		sellerSvc.addSeller(sellerVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<SellerVO> list = sellerSvc.getAll();
		model.addAttribute("sellerrListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/seller/listAllSeller";
		// 一定要用Redirect，不然會會導致資料重複送
		// 新增成功後重導至IndexController_inSpringBoot.java的第50行@GetMapping("/user/listAllUser")
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
