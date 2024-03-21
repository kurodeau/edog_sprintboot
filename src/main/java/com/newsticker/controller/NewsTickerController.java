package com.newsticker.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.newsticker.model.NewsTickerVO;
import com.newsticker.model.NewsTickerService;

@Controller
@RequestMapping("/back/newsTicker")
public class NewsTickerController extends HttpServlet{
		
	@Autowired
	NewsTickerService newsTickerSvc;

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 :
	 * <form:select path="deptno" id="deptno" items="${deptListData}"
	 * itemValue="deptno" itemLabel="dname" />
	 */
	@ModelAttribute("newsTickerListData") 
	protected List<NewsTickerVO> referenceListData(Model model) {
		List<NewsTickerVO> list = newsTickerSvc.getAll();
		
		//測試有撈出資料
//		System.out.println("==============================");
//	    list.forEach(data -> System.out.println(data)); 
//		System.out.println("==============================");
		return list;
	}
	
	// /back/newsTicker/addNewsTicker
	@GetMapping("addNewsTicker")
	public String addNewsTicker(ModelMap model) {
		NewsTickerVO newsTickerVO = new NewsTickerVO();
		model.addAttribute("newsTickerVO", newsTickerVO);
		return "back-end/back-newsticker-add";
	}
	
	// /back/newsTicker/listAllGet
	@GetMapping("listAllGet")
	public String listAllNewsTickerGet(ModelMap model) {
		return "back-end/back-newsticker-list";
	}
	
	// /back/newsTicker/listAllPost
	@PostMapping("listAllPost")
	public String listAllNewsTickerPost(ModelMap model) {
		return "back-end/back-newsticker-list";
	}
	
	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	// /back/newsTicker/getOne_For_Update
	@GetMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("newsTickerId") String newsTickerId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		NewsTickerVO newsTickerVO = newsTickerSvc.getOneNewsTicker(Integer.valueOf(newsTickerId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("newsTickerVO", newsTickerVO);
		return "back-end/back-newsticker-edit"; // 查詢完成後轉交只有一個資料要更新的
	}
	
	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insertNewsTicker")
	public String insert(@Valid @NonNull NewsTickerVO newsTickerVO, BindingResult result, ModelMap model) throws IOException{
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
//		result = removeFieldError(newsTickerVO, result, "upFiles");

		if (result.hasErrors()) {
			System.out.println("測試訊息:insertNewsTicker");
			System.out.println("測試訊息:result= "+result);
			return "back-end/back-newsTicker-add";
		}
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();\
		newsTickerSvc.addNewsTicker(newsTickerVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<NewsTickerVO> list = newsTickerSvc.getAll();
		model.addAttribute("newsTickerListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/back/newsTicker/listAllGet"; // 新增成功後重導至IndexController_inSpringBoot.java的第50行@GetMapping("/emp/listAllEmp")
	}

	
	@PostMapping("updateNewsTicker")
	public String update(@Valid NewsTickerVO newsTickerVO, BindingResult result, ModelMap model, HttpSession session)throws IOException{

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		if (result.hasErrors()) {
			System.out.println("測試訊息:updateNewsTicker");
			System.out.println("測試訊息:result= "+result);
			return "back-end/back-newsTicker-edit";
		}
		/*************************** 2.開始修改資料 *****************************************/
		newsTickerSvc.updateNewsTicker(newsTickerVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		List<NewsTickerVO> list = newsTickerSvc.getAll();
//		newsTickerVO = newsTickerSvc.getOneNewsTicker(Integer.valueOf(newsTickerVO.getNewsTickerId()));
		model.addAttribute("success", "- (修改成功)");
		model.addAttribute("newsTickerVO", newsTickerVO);
		return "redirect:/back/newsTicker/listAllGet"; // 修改成功後轉交back-newsticker-list.html
	}


//
	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(NewsTickerVO newsTickerVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(newsTickerVO, "newsTickerVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

}