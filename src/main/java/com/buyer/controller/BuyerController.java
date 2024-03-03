package com.buyer.controller;

import javax.validation.Valid;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.buyer.entity.*;
import com.buyer.model.*;
import com.buyer.service.*;

@Controller
@RequestMapping("/buyer")
public class BuyerController {

	@Autowired
	BuyerService buyerSvc;

	/*
	 * This method will serve as addEmp.html handler.
	 */
	@GetMapping("addBuyer")
	public String addBuyer(ModelMap model) {
		BuyerVO buyerVO = new BuyerVO();
		model.addAttribute("BuyerVO", buyerVO);
		return "back-end/back-buyer-add"; //應該改成創帳號PAGE
	}

	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid BuyerVO buyerVO, BindingResult result, ModelMap model,
			@RequestParam("petImg") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中petImg欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(buyerVO, result, "petImg");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "員工照片: 請上傳照片");
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				buyerVO.setPetImg(buf);
			}
		}
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "#"; //看從哪裡創帳號就回到哪裡
		}
		/*************************** 2.開始新增資料 *****************************************/
		// BuyerService buyerSvc = new BuyerService();
		buyerSvc.addBuyer(buyerVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<BuyerVO> list = buyerSvc.getAll();
		model.addAttribute("buyerListData", list);
		model.addAttribute("success", "- (新增成功)");
		// 這之後要重禱回買家登入頁面
		return "#"; // 新增成功後重導至IndexController_inSpringBoot.java的第50行@GetMapping("/emp/listAllEmp")
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("memberId") String memberId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		BuyerVO buyerVO = buyerSvc.getOneBuyer(Integer.valueOf(memberId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("buyerVO", buyerVO);
		// 我看範例沒有這個html檔案? 有問題
		return "#"; // 查詢完成後轉交update_emp_input.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid BuyerVO buyerVO, BindingResult result, ModelMap model,
			@RequestParam("petImg") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中petImg欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(buyerVO, result, "petImg");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
			// BuyerService buyerSvc = new BuyerService();
			byte[] petImg = buyerSvc.getOneBuyer(buyerVO.getMemberId()).getPetImg();
			buyerVO.setPetImg(petImg);
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] petImg = multipartFile.getBytes();
				buyerVO.setPetImg(petImg);
			}
		}
		if (result.hasErrors()) {
			// 我看範例沒有這個html檔案? 有問題, 但這裡要修改成進行修改資料的PAGE
			return "#";
		}
		/*************************** 2.開始修改資料 *****************************************/
		// BuyerService buyerSvc = new BuyerService();
		buyerSvc.updateBuyer(buyerVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		buyerVO = buyerSvc.getOneBuyer(Integer.valueOf(buyerVO.getMemberId()));
		model.addAttribute("buyerVO", buyerVO);
		// 這要轉到進行改資料的頁面, 有問題
		return "back-end/back-buyer-edit"; // 修改成功後轉交listOneEmp.html
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	// 理應不會有移除資料的操作
	@PostMapping("delete")
	public String delete(@RequestParam("memberId") String memberId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		buyerSvc.deleteBuyer(Integer.valueOf(memberId));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<BuyerVO> list = buyerSvc.getAll();
		model.addAttribute("buyerListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/back-buyer"; // 刪除完成後轉交listAllEmp.html
	}

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${deptListData}" itemValue="deptno" itemLabel="dname" />
	 */
//	@ModelAttribute("deptListData")
//	protected List<DeptVO> referenceListData() {
//		// DeptService deptSvc = new DeptService();
//		List<DeptVO> list = deptSvc.getAll();
//		return list;
//	}

	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${depMapData}" />
	 */
	// 配合前端設定再開來套用
//	@ModelAttribute("deptMapData") //
//	protected Map<Integer, String> referenceMapData() {
//		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
//		map.put(10, "財務部");
//		map.put(20, "研發部");
//		map.put(30, "業務部");
//		map.put(40, "生管部");
//		return map;
//	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(BuyerVO buyerVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(buyerVO, "buyerVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

}