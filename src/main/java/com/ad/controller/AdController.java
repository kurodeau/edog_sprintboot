package com.ad.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ad.model.AdService;
import com.ad.model.AdVO;
import com.allenum.AdStatusEnum;


@Controller
@RequestMapping("/front/seller/ad")
public class AdController {
	//  /front/seller/ad/add
	@Autowired
	AdService adSvc;
	
	@GetMapping("add")
	    public String selleradsadd(Model model){
	        return "front-end/seller/seller-ads-add";
	    }
	 
	 
	@GetMapping("seller-ads-add")
	public String addAd(ModelMap model) {
		AdVO adVO = new AdVO();
		model.addAttribute("adVO" , adVO);
		return "front-end/seller/seller-ads-add" ;
		
	}
	
	
	@PostMapping("insert")
	public String insert(@Valid AdVO adVO , BindingResult result , ModelMap model,
			@RequestParam("adImg") MultipartFile[] parts) throws IOException{
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		result = removeFieldError(adVO , result , "adImg");
		
		if(parts[0].isEmpty()) {
			model.addAttribute("errorMessage" , "廣告照片:請上傳照片");
		}else {
			for(MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				adVO.setAdImg(buf);
			}
		}
		if(result.hasErrors()||parts[0].isEmpty()) {
			System.out.println("sssss");
			
			System.out.println(result);
			System.out.println(parts[0].isEmpty());
			return "front-end/seller/seller-ads-add";
		}
		
		long currentTime = System.currentTimeMillis();
		Timestamp timestamp = new Timestamp(currentTime);
		adVO.setAdCreateTime(timestamp);
		
		adVO.setAdStatus(AdStatusEnum.REVIEWING.getStatus());
		
		System.out.println("AAAA");

		/*************************** 2.開始新增資料 *****************************************/
		adSvc.addAd(adVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<AdVO> list = adSvc.getAll();
		model.addAttribute("AdListData" , list);
		model.addAttribute("success" , "-(新增成功)");
		return "redirect:/front/seller/ad/add";
	}
	
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("adId") String adId,ModelMap model ) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		AdVO adVO = adSvc.getOneAd(Integer.valueOf(adId));
		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("adVO" ,adVO);
		return "back-end/ad/update_ad_input";
	}
	
	@PostMapping("update")
	public String update(@Valid AdVO adVO , BindingResult result , ModelMap model,
			@RequestParam("upFiles") MultipartFile[] parts) throws IOException {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		result = removeFieldError(adVO,result,"upFiles");
		
		if(parts[0].isEmpty()) {
			byte[] upFiles = adSvc.getOneAd(adVO.getAdId()).getAdImg();
			adVO.setAdImg(upFiles);
		}else {
			for(MultipartFile multipartFile : parts) {
				byte[] upFiles = multipartFile.getBytes();
				adVO.setAdImg(upFiles);
			}
		}
		if(result.hasErrors()) {
			return "back-end/ad/update_ad_input";
		}
		/*************************** 2.開始修改資料 *****************************************/
		adSvc.updateAd(adVO);
		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success" , "-(修改成功)");
		adVO = adSvc.getOneAd(Integer.valueOf(adVO.getAdId()));
		model.addAttribute("adVO",adVO);
		return "back-end/ad/listOneAd";
	}
	
	@PostMapping("delete")
	public String delete(@RequestParam("adid") String adId , ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		adSvc.deleteAd(Integer.valueOf(adId));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<AdVO> list = adSvc.getAll();
		model.addAttribute("adListData",list);
		model.addAttribute("success" , "-(刪除成功)");
		return "back-end/emp/ListAllAd";
	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(AdVO adVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(adVO, "adpVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

	

}
