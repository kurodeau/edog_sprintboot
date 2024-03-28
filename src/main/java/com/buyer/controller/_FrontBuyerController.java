//package com.buyer.controller;
//
//import java.io.IOException;
//import java.sql.Timestamp;
////import java.sql.Timestamp;
//import java.util.List;
//import java.util.Date;
//import java.util.stream.Collectors;
//
//import javax.servlet.http.HttpServlet;
//import javax.validation.Valid;
//
//import org.springframework.validation.BeanPropertyBindingResult;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.buyer.entity.*;
//import com.buyer.model.*;
//import com.buyer.service.*;
//
//@Controller
//@RequestMapping("/front/buyer")
//public class _FrontBuyerController extends HttpServlet{
//
//	@Autowired
//	BuyerService buyerSvc;
//         
//
//	//從sidebar進入修改會員資料  /buyer/buyer/updateBuyer
//	@GetMapping("updateBuyer")
//	public String updateBuyer(ModelMap model) {
//		
//		// 從登入狀態抓取用戶ID
//		String memberId = "9"; //測試有登入, 預設值
//		SecurityContext secCtx = SecurityContextHolder.getContext();
//        Authentication authentication = secCtx.getAuthentication();
//        BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
//
//        memberId = String.valueOf(buyerVO.getMemberId());
//        
//        System.out.println("進入修改的會員資料是memberId=" + memberId);
//        
//        // 獲得buyerVO, 渲染到前端讓用戶修改		
//		model.addAttribute("buyerVO", buyerVO);
//		return "front-end/buyer/buyer-buyer-edit"; // 查詢完成後轉交update_emp_input.html
//	}
//	
//    // 驗證有問題, 重寫為下面那個check
////	@PostMapping("submitUpdateBuyer")
////	public String submitUpdateBuyer(@Valid BuyerVO buyerVO,  BindingResult bindingResult, ModelMap model  ,@RequestParam("petImg") MultipartFile[] parts ) throws IOException{
////        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
////		
////
////		System.out.println("SSSSSSSSSSSSSSSSSSS"+bindingResult);
////		
////		if (bindingResult.hasErrors()) {
////			return "front-end/buyer/buyer-buyer-edit";
////		}
////
////		
////		
//////		bindingResult = removeFieldError(buyerVO, bindingResult, "petImg");
//////		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
//////			model.addAttribute("errorMessage", "請上傳PET圖片");
//////
//////		} 
//////		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
//////			model.addAttribute("errorMessage", "請上傳PET圖片");
////			// BuyerService buyerSvc = new BuyerService();
//////			byte[] petImg = buyerSvc.getOneBuyer(buyerVO.getMemberId()).getPetImg();
//////			buyerVO.setPetImg(petImg);
//////		} 
//////		else {
//////			for (MultipartFile multipartFile : parts) {
//////				byte[] petImg = multipartFile.getBytes();
//////				buyerVO.setPetImg(petImg);
//////			}
//////		}
//////		if (result.hasErrors()) {
//////			// 修改成進行修改資料的PAGE
//////			System.out.println("測試訊息:圖片的判斷有問題, 提早返回edit");
////////			System.out.println("測試訊息:" + result.);
//////			return "front-end/buyer/buyer-buyer-edit";
//////		}
////		
////		/*************************** 2.開始修改資料 *****************************************/
////		// BuyerService buyerSvc = new BuyerService();
//////		System.out.println("測試訊息:buyerVO= "+buyerVO);
//////		buyerSvc.updateBuyer(buyerVO);
//////		System.out.println("有走道updateBuyer方法");
//////		
//////		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
//////		model.addAttribute("success", "- (修改成功)");
//////		buyerVO = buyerSvc.getOneBuyer(Integer.valueOf(buyerVO.getMemberId()));
//////		model.addAttribute("buyerVO", buyerVO);
//////		System.out.println("有走道return之前");
//////        
////		return "front-end/buyer/buyer-main"; // 
////	}
//	
//
//	@PostMapping("/updatebuyer/check")
//	public String checkUpdateBuyer(@Valid BuyerVO buyerVO,  BindingResult result , ModelMap model 
//	 ,@RequestParam("petImg") MultipartFile[] parts ,@RequestParam("checkMemberPassword") String dupPassword) throws IOException{
//
//		buyerVO.setMemberRegistrationTime(new Date());
//// checkMemberPassword
//		result = removeFieldError(buyerVO, result, "petImg");
//
//		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
//			model.addAttribute("errorMessage", "請上傳PET圖片");
//
//		} 
//		
//		System.out.println(dupPassword);
//		System.out.println(buyerVO.getMemberPassword());
//
//		
//		if (!dupPassword.equals(buyerVO.getMemberPassword())) {
//			model.addAttribute("duplicateError", "密碼不一致");
//		}
//		
//		System.out.println(result);
//		if (result.hasErrors()|| parts[0].isEmpty()) {
//			return "/front-end/buyer/buyer-register";
//		}
//		
//		
//		for (MultipartFile multipartFile : parts) {
//			byte[] petImg = multipartFile.getBytes();
//			buyerVO.setPetImg(petImg);
//		}
//		
//		buyerSvc.saveUserDetails(buyerVO);
//	
//		return "redirect:/buyer/login";
//	}
//	
//	
//	// 去除BindingResult中某個欄位的FieldError紀錄
//	// 目前不知道用途是甚麼, 應該是資料驗證用的
//	public BindingResult removeFieldError(BuyerVO buyerVO, BindingResult result, String removedFieldname) {
//		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
//				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
//		result = new BeanPropertyBindingResult(buyerVO, "buyerVO");
//		for (FieldError fieldError : errorsListToKeep) {
//			result.addError(fieldError);
//		}
//		return result;
//	}
//	
//	
//	
//	
//
//}