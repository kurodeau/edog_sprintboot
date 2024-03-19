package com.ad.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ad.model.AdService;
import com.ad.model.AdVO;
import com.allenum.AdStatusEnum;
import com.seller.entity.SellerVO;
import com.sellerLv.entity.SellerLvVO;

@Controller
@RequestMapping("/back/ad")

public class AdBackController {

	@Autowired
	private AdService adSvc;

	@GetMapping("list")
	public String listAllProduct(ModelMap model) {
		return "back-end/back-ad-list";
	}

	@ModelAttribute("adListData")
	protected List<AdVO> referenceListData() {
		List<AdVO> list = adSvc.getAll();
		return list;
	}

	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("adId") String adId, ModelMap model) {
		
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		AdVO adVO = adSvc.getOneAd(Integer.valueOf(adId));
//		SecurityContext secCtx = SecurityContextHolder.getContext();
//		Authentication authentication = secCtx.getAuthentication();
//		SellerVO sellerVO = (SellerVO) authentication.getPrincipal();
//
//		SellerLvVO sellerLv = sellerVO.getSellerLvId();
		
		SellerLvVO sellerLv = adVO.getSellerVO().getSellerLvId();
		
		

		Integer sellerLvId = Integer.valueOf(sellerLv.getSellerLvId());
		
		System.out.println(sellerLvId);
		
		
		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("sellerLvId",sellerLvId);
		model.addAttribute("adVO", adVO);
		return "back-end/back-ad-update_ad";
	}

	@PostMapping("update")
	public String update(@Valid AdVO adVO, BindingResult result, ModelMap model,
			@RequestParam("adImg") MultipartFile[] parts ,
			@RequestParam("adId") String adId) throws IOException {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		result = removeFieldError(adVO, result, "adImg");

		if (parts[0].isEmpty()) {
			byte[] upFiles = adSvc.getOneAd(adVO.getAdId()).getAdImg();
			adVO.setAdImg(upFiles);
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] upFiles = multipartFile.getBytes();
				adVO.setAdImg(upFiles);
			}
		}
		if (result.hasErrors()) {
			
			
			AdVO adVO1 = adSvc.getOneAd(Integer.valueOf(adId));
//			SecurityContext secCtx = SecurityContextHolder.getContext();
//			Authentication authentication = secCtx.getAuthentication();
//			SellerVO sellerVO = (SellerVO) authentication.getPrincipal();
	//
//			SellerLvVO sellerLv = sellerVO.getSellerLvId();
			
			SellerLvVO sellerLv = adVO1.getSellerVO().getSellerLvId();
			
			

			Integer sellerLvId = Integer.valueOf(sellerLv.getSellerLvId());

		
			
			model.addAttribute("sellerLvId",sellerLvId);
			
			return "back-end/back-ad-update_ad";
		}
		/*************************** 2.開始修改資料 *****************************************/

		AdVO orginalSellerId = adSvc.getOneAd(adVO.getAdId());
		adVO.setSellerVO(orginalSellerId.getSellerVO());

		long currentTime = System.currentTimeMillis();
		Timestamp timestamp = new Timestamp(currentTime);
		adVO.setAdImgUploadTime(timestamp);
		adVO.setIsEnabled(true);
		adVO.setAdStatus(AdStatusEnum.REVIEWING.getStatus());

		adSvc.updateAd(adVO);
		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "-(修改成功)");
		adVO = adSvc.getOneAd(Integer.valueOf(adVO.getAdId()));
		model.addAttribute("adVO", adVO);
		return "redirect:/back/ad/list";
	}

	
	@PostMapping("deleteStatus")
	public String deleteStatus(@RequestParam("adId") String adId ,Model model) {
		
		AdVO adVO = adSvc.getOneAd(Integer.valueOf(adId));
		adVO.setIsEnabled(false);
		adSvc.updateAd(adVO);
		
		
		return "redirect:/back/ad/list";
		
	}
	
	
	@PostMapping("reviewConfirm")
	public String reviewConfirm(@RequestParam("adId") String adId , Model model) {
		
		AdVO adVO = adSvc.getOneAd(Integer.valueOf(adId));
		adVO.setAdStatus(AdStatusEnum.DISABLED.getStatus());
		adSvc.updateAd(adVO);
		
		return "redirect:/back/ad/list";
	}
	
	@PostMapping("reviewFail")
	public String reviewFail(@RequestParam("adId") String adId , Model model) {
		
		AdVO adVO = adSvc.getOneAd(Integer.valueOf(adId));
		adVO.setAdStatus(AdStatusEnum.REVIEWFAIL.getStatus());
		adSvc.updateAd(adVO);		
		
		return "redirect:/back/ad/list";
	}
	
	
	
	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(AdVO adVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(adVO, "adVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

}
