package com.ad.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ad.model.AdService;
import com.ad.model.AdVO;


@Controller
@RequestMapping("/front/seller/ad")

public class AdnoController {
	@Autowired
	AdService adSvc;
	
	@GetMapping("adlist")
    public String selleradlist(Model model){
        return "front-end/seller/seller-ads-all";
    }
	
	
	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(
		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
		@NotEmpty(message="員工編號: 請勿空白")
		@RequestParam("adId") String adId , ModelMap model) {
		/***************************2.開始查詢資料*********************************************/
		
		AdVO adVO = adSvc.getOneAd(Integer.valueOf(adId));
		
		List<AdVO> list = adSvc.getAll();
		model.addAttribute("adListData",list);
		
		if(adVO == null) {
			model.addAttribute("errorMessage" , "查無資料");
			return "front-end/seller/seller-ads-all";
		}
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
		model.addAttribute("AdVO",adVO);
		model.addAttribute("getOne_FOR_Display" , "true");
				
		return "front-end/seller/seller-ads-all";
		
	}
	
	
	
	  @ModelAttribute("adListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
		protected List<AdVO> referenceListData(Model model) {
			
	    	List<AdVO> list = adSvc.getAll();
			return list;
		}
	  
	  
	  @ModelAttribute("adListReviewData")
	  protected List<AdVO> referenceListData2(Model model){
		  List<AdVO> list = adSvc.getReviewAd();
		  return list;
	  }
	  
	  @ModelAttribute("adListUnLaunchData")
	  protected List<AdVO> referenceListData3(Model model){
		  List<AdVO> list = adSvc.getUnLaunchAd();
		  return list;
	  }
	  
	  @ModelAttribute("adListLaunchData")
	  protected List<AdVO> referenceListData4(Model model){
		  List<AdVO> list = adSvc.getLaunchAd();
		  return list;
	  }
	  
	    
	
	@ExceptionHandler(value = { ConstraintViolationException.class })
	//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    StringBuilder strBuilder = new StringBuilder();
	    for (ConstraintViolation<?> violation : violations ) {
	          strBuilder.append(violation.getMessage() + "<br>");
	    }
	    //==== 以下第80~85行是當前面第69行返回 /src/main/resources/templates/back-end/emp/select_page.html 第97行 與 第109行 用的 ====   
//	    model.addAttribute("empVO", new EmpVO());
//    	EmpService empSvc = new EmpService();
		List<AdVO> list = adSvc.getAll();
		model.addAttribute("adListData", list); // for select_page.html 第97 109行用
		
		String message = strBuilder.toString();
	    return new ModelAndView("front-end/seller/seller-ads-all", "errorMessage", "請修正以下錯誤:<br>"+message);
	}
	

}
