package com.article.controller;

import javax.servlet.http.HttpServletRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import com.article.entity.ArticleVO;
import com.article.service.ArticleService;
//import com.article.service.ArticleService;

import com.articleType.entity.ArticleTypeVO;
import com.articleType.service.ArticleTypeService;

@Controller
@Validated
@RequestMapping("/article")
public class ArticleIdController {
	
	@Autowired
	ArticleTypeService articleTypeSvc;
	
	@Autowired
	ArticleService articleSvc;

	/*
	 * This method will be called on select_page.html form submission, handling POST
	 * request It also validates the user input
	 */
	
	@PostMapping("/getOne_For_Display")
    public String handleSearch(@RequestParam("searchText") String searchText, Model model) {
		System.out.println("搜索内容：" + searchText);
		
		List<ArticleVO> searchResults = articleSvc.searchArticles(searchText);
		
		if (searchResults.isEmpty()) {
			model.addAttribute("errorMessage", "查無資料");
			return "front-end/article/nothing";
		}
		model.addAttribute("searchResults", searchResults);
		return "front-end/article/forum-homesearch";
    }
	
	@ExceptionHandler(value = { ConstraintViolationException.class })
	//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    StringBuilder strBuilder = new StringBuilder();
	    for (ConstraintViolation<?> violation : violations ) {
	          strBuilder.append(violation.getMessage() + "<br>");
	    }
	    //==== 以下第92~96行是當前面第77行返回 /src/main/resources/templates/back-end/emp/select_page.html用的 ====   
//	    model.addAttribute("empVO", new EmpVO());
//    	EmpService empSvc = new EmpService();
		List<ArticleVO> list = articleSvc.getAll();
		model.addAttribute("articleListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("articleTypeVO", new ArticleTypeVO());  // for select_page.html 第133行用
		List<ArticleTypeVO> list2 = articleTypeSvc.getAll();
    	model.addAttribute("articleTypeListData",list2);    // for select_page.html 第135行用
		String message = strBuilder.toString();
	    return new ModelAndView("back-end/article/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
	}
	
}