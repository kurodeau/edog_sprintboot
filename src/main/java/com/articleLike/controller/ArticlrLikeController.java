package com.articleLike.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.articleType.entity.ArticleTypeVO;
import com.articleType.service.ArticleTypeService;
import com.buyer.entity.BuyerVO;
import com.buyer.service.BuyerService;
import com.article.service.ArticleService;
import com.articleLike.entity.ArticleLikeVO;
import com.articleLike.service.ArticleLikeService;
import com.report.entity.ReportVO;
import com.report.service.ReportService;
import com.reportType.entity.ReportTypeVO;
import com.reportType.service.ReportTypeService;

@Controller
@ComponentScan(basePackages = {"com.articleLike"})
@RequestMapping("/front/forum/articleLike")
public class ArticlrLikeController {

	@Autowired
	ArticleTypeService articleTypeSvc;

	@Autowired
	ArticleService articleSvc;
	
	@Autowired
	ArticleLikeService articleLikeSvc;
	
	@Autowired
	BuyerService buyerSvc;


	@Autowired
	ReportTypeService reportTypeSvc;
	/*
	 * This method will serve as addEmp.html handler.
	 */
	
		 @GetMapping("/MyArticleLike")
		 public String getMyArticle(@RequestParam("id") Integer memberId, Model model) {
			 SecurityContext secCtx = SecurityContextHolder.getContext() ;
			 Authentication authentication = secCtx.getAuthentication();
			 BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
			 memberId = buyerVO.getMemberId();
			 model.addAttribute("buyerVO", buyerVO);
			 List<ArticleLikeVO> myArticleLikeList = articleLikeSvc.getByMemberId(buyerVO);
			 
			 model.addAttribute("myArticleLikeList", myArticleLikeList);
			 
			 return "front-end/article/myArticleLike-list";
		 }
		 
}