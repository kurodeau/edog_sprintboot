package com.buyer.controller;

import java.io.IOException;
import java.sql.Timestamp;
//import java.sql.Timestamp;
import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServlet;
import javax.validation.Valid;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.buyer.entity.*;
import com.buyer.model.*;
import com.buyer.service.*;

@Controller
@RequestMapping("/front/buyer")
public class FrontBuyerController extends HttpServlet{

	@Autowired
	BuyerService buyerSvc;

    // 買家註冊可以考慮移到這裡
//	@GetMapping("addBuyer")
//	public String addBuyer(ModelMap model) {
//		BuyerVO buyerVO = new BuyerVO();
//		model.addAttribute("BuyerVO", buyerVO);
//		return "back-end/back-buyer-add"; // 應該改成創帳號PAGE
//	}


	// 前台新增買家帳號, 應該跟買家註冊重複, 待確認現在註冊是不是用這個?
    // /front/buyer/insertBuyer
	@PostMapping("insertBuyer")
	public String insert(@Valid BuyerVO buyerVO, BindingResult result, ModelMap model,
			@RequestParam("petImg") MultipartFile[] parts) throws IOException {

		//塞入當下時間
//		long currentTime = System.currentTimeMillis();
//		Date date = new Date(currentTime);
//		buyerVO.setMemberRegistrationTime(date);		
		java.util.Date utilDate = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		buyerVO.setMemberRegistrationTime(sqlDate);
		
		//圖片轉成byte[]
		byte[] buf = parts[0].getBytes();
		buyerVO.setPetImg(buf);
//		System.out.println("VO:" + buyerVO.toString() );

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中petImg欄位的FieldError紀錄 --> 見第172行
//		System.out.println("test我有進這個insertBuyer");
//		result = removeFieldError(buyerVO, result, "petImg");

		// 這段是當圖片必須存在時的, 追加條件的檢查檢查
//		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
//			model.addAttribute("errorMessage", "寵物照片: 請上傳照片");
//		} else {
//			for (MultipartFile multipartFile : parts) {
//				byte[] buf = multipartFile.getBytes();
//				buyerVO.setPetImg(buf);
//			}
//		}
//		if (result.hasErrors() || parts[0].isEmpty()) {
//			System.out.println("test進了錯誤檢查?");
//			return "back-end/back-buyer-add"; //看從哪裡創帳號就回到哪裡
//		}
		//檢查有沒有錯誤訊息
//		if (result.hasErrors()) {
//			System.out.println("test進了錯誤檢查?");
//			System.out.println(result);
//			return "back-end/back-buyer-add"; // 看從哪裡創帳號就回到哪裡
//		}

		/*************************** 2.開始新增資料 *****************************************/
		// BuyerService buyerSvc = new BuyerService();
		System.out.println("test新增資料之前??");
		buyerSvc.addBuyer(buyerVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<BuyerVO> list = buyerSvc.getAll();
		model.addAttribute("buyerListData", list);
		model.addAttribute("success", "- (新增成功)");
		// 這之後要重禱回買家登入頁面
		System.out.println("test重導之前???");
		return "back-end/back-buyer-list"; // 新增成功後重導至IndexController_inSpringBoot.java的第50行@GetMapping("/emp/listAllEmp")
	}               


	//從sidebar進入修改會員資料  /buyer/buyer/updateBuyer
	@GetMapping("updateBuyer")
	public String updateBuyer(ModelMap model) {
		
		// 從登入狀態抓取用戶ID
		String memberId = "9"; //測試有登入, 預設值
		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();

        memberId = String.valueOf(buyerVO.getMemberId());
        
        System.out.println("進入修改的會員資料是memberId=" + memberId);
        
        // 獲得buyerVO, 渲染到前端讓用戶修改		
		model.addAttribute("buyerVO", buyerVO);
		return "front-end/buyer/buyer-buyer-edit"; // 查詢完成後轉交update_emp_input.html
	}
	
	
	@PostMapping("submitUpdateBuyer")
	public String submitUpdateBuyer(@Valid BuyerVO buyerVO, BindingResult result, ModelMap model,
			@RequestParam("petImg") MultipartFile[] parts) throws IOException{
				
		// 從登入狀態抓取用戶ID對應的資料
		String memberId = "9"; //測試有登入, 預設值
		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        buyerVO = (BuyerVO) authentication.getPrincipal();
        memberId = String.valueOf(buyerVO.getMemberId());
        
        System.out.println("送交修改的會員資料是memberId=" + memberId);

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
			// 修改成進行修改資料的PAGE
			System.out.println("測試訊息:圖片的判斷有問題, 提早返回edit");
//			System.out.println("測試訊息:" + result.);
			return "front-end/buyer/buyer-buyer-edit";
		}
		
		/*************************** 2.開始修改資料 *****************************************/
		// BuyerService buyerSvc = new BuyerService();
		buyerSvc.updateBuyer(buyerVO);
		System.out.println("有走道updateBuyer方法");
		
		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		buyerVO = buyerSvc.getOneBuyer(Integer.valueOf(buyerVO.getMemberId()));
		model.addAttribute("buyerVO", buyerVO);
		System.out.println("有走道return之前");
        
		return "front-end/buyer/buyer-main"; // 
	}
	

	// 去除BindingResult中某個欄位的FieldError紀錄
	// 目前不知道用途是甚麼, 應該是資料驗證用的
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