package com.msg.controller;

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
import com.msg.entity.MsgVO;
import com.msg.service.MsgService;
import com.reply.entity.ReplyVO;
import com.reply.service.ReplyService;
import com.seller.entity.SellerVO;
import com.article.entity.ArticleVO;
import com.article.service.ArticleService;
import com.report.entity.ReportVO;
import com.report.service.ReportService;
import com.reportType.entity.ReportTypeVO;
import com.reportType.service.ReportTypeService;

@Controller
@ComponentScan(basePackages = {"com.msg"})
@RequestMapping("/msg")
public class MsgController {

	@Autowired
	ArticleTypeService articleTypeSvc;

	@Autowired
	ArticleService articleSvc;
	
	@Autowired
	BuyerService buyerSvc;
	
	@Autowired
	ReplyService replySvc;

	@Autowired
	ReportService reportSvc;
	
	@Autowired
	MsgService msgSvc;


	@Autowired
	ReportTypeService reportTypeSvc;
	/*
	 * This method will serve as addEmp.html handler.
	 */
	
	 @ModelAttribute("msgListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
		protected List<MsgVO> referenceListData_Msg(Model model) {
			
	    	List<MsgVO> list = msgSvc.getAll();
			return list;
		}
	 
	 @ModelAttribute("articleTypeListData") // for select_page.html 第135行用
		protected List<ArticleTypeVO> referenceListData_ArticleType(Model model) {
			model.addAttribute("articleTypeVO", new ArticleTypeVO()); // for select_page.html 第133行用
			List<ArticleTypeVO> list = articleTypeSvc.getAll();
			return list;
		}
	 
	 @ModelAttribute("buyerListData") 
		protected List<BuyerVO> referenceListData(Model model) {
		 	model.addAttribute("buyerVO", new BuyerVO()); // for select_page.html 第133行用
			List<BuyerVO> list = buyerSvc.getAll();
			return list;
		}
	 @ModelAttribute("reportListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	    protected List<ReportVO> referenceListData_Report(Model model) {
	    	
	    	List<ReportVO> list = reportSvc.getAll();
	    	return list;
	    }
		
		@ModelAttribute("reportTypeListData") // for select_page.html 第135行用
		protected List<ReportTypeVO> referenceListData_ReportType(Model model) {
			model.addAttribute("reportTypeVO", new ReportTypeVO()); // for select_page.html 第133行用
			List<ReportTypeVO> list = reportTypeSvc.getAll();
			return list;
		}
	
		@GetMapping("listAll")
		public String listAllArticle(ModelMap model) {
			return "front-end/article/msg-list";
		}

		@GetMapping("add")
		public String addArticle(ModelMap model) {
			ArticleVO articleVO = new ArticleVO();
			model.addAttribute("articleVO", articleVO);
			return "front-end/article/post-article";
		}
		
		@GetMapping("getOne")
		public String getOneMsg(@RequestParam("id") Integer msgId, ModelMap model) {
			
			MsgVO msgVO = msgSvc.getOneMsg(msgId);
			 // 检查msgVO中的articleVO、replyVO和reportVO是否为null
			 if (msgVO.getArticleVO() != null) {
		        model.addAttribute("msgVO", msgVO);
		        ArticleVO articleVO = articleSvc.getOneArticle(msgVO.getArticleVO().getArticleId());
		        model.addAttribute("articleVO", articleVO);
		        List<ReplyVO> replyVOList = replySvc.getByArticleId(articleVO);
		        model.addAttribute("replyVOList", replyVOList);
		        ReportVO reportVO = new ReportVO();
			    model.addAttribute("reportVO", reportVO);
		        return "front-end/article/list-one-article"; // articleVO不为null，返回到对应的页面
		        
			 } else if (msgVO.getReplyVO() != null) {
		        ReplyVO replyVO = msgVO.getReplyVO();
		        ArticleVO articleVO = replyVO.getArticleVO();
		        // 获取对应留言的文章信息
		        model.addAttribute("articleVO", articleVO);
		        List<ReplyVO> replyVOList = replySvc.getByArticleId(articleVO);
		        model.addAttribute("replyVOList", replyVOList);
		        ReportVO reportVO = new ReportVO();
			    model.addAttribute("reportVO", reportVO);
		        model.addAttribute("msgVO", msgVO);
		        return "front-end/article/list-one-article"; // replyVO不为null，返回到对应的页面
		        
		    } else if (msgVO.getReportVO() != null) {
		        ReportVO reportVO = msgVO.getReportVO();
		        if (reportVO.getReportTargetType() == 0) {
		        	
		        	ArticleVO articleVO = articleSvc.getOneArticle(reportVO.getArticleVO().getArticleId());
		    	    model.addAttribute("articleVO", articleVO);
		    	    model.addAttribute("reportVO", reportVO);
		            model.addAttribute("msgVO", msgVO);
		            return "front-end/article/article-report"; // 返回到檢舉文章页面
		        } else if (reportVO.getReportTargetType() == 1) {
		        	
		        	ReplyVO replyVO = replySvc.getOneReply(reportVO.getReplyVO().getReplyId());
		        	model.addAttribute("replyVO", replyVO);
		        	model.addAttribute("reportVO", reportVO);
		            model.addAttribute("msgVO", msgVO);
		            return "front-end/article/reply-report"; // 返回到檢舉留言页面
		        }
		    }
			 return "error-page"; 
		}
		
		 @GetMapping("/TypeList")
		    public String getTypeList(@RequestParam("id") Integer articleTypeId, Model model) {
		        ArticleTypeVO articleTypeVO = articleTypeSvc.getOneArticleType(articleTypeId);
		        model.addAttribute("articleTypeVO", articleTypeVO);
		        List<ArticleVO> articleList = articleSvc.getByArticleTypeId(articleTypeVO);
		        
		        model.addAttribute("articleList", articleList);

		        return "front-end/article/articletype-list";
		    }
		 @GetMapping("/MyMsg")
		 public String getMyArticle(@RequestParam("id") Integer memberId, Model model) {
			 SecurityContext secCtx = SecurityContextHolder.getContext() ;
			 Authentication authentication = secCtx.getAuthentication();
			 BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
			 memberId = buyerVO.getMemberId();
//			 BuyerVO buyerVO = buyerSvc.getOneBuyer(memberId);
			 model.addAttribute("buyerVO", buyerVO);
			 List<MsgVO> myMsgList = msgSvc.getByMemberId(buyerVO);
			 
			 model.addAttribute("myMsgList", myMsgList);
			 
			 return "front-end/article/myMsg-list";
		 }
		 
		@GetMapping("getOne_For_Update")
		public String getOneArticleUpdate(@RequestParam("id") Integer articleId, ModelMap model) {
		    ArticleVO articleVO = articleSvc.getOneArticle(articleId);
		    model.addAttribute("articleVO", articleVO);
		    System.out.println("==============XXXXXXXXXXXXXX");
		    System.out.println("getOne_For_Update");
		    System.out.println(articleVO);
		    System.out.println("==============XXXXXXXXXXXXXX");

		    return "front-end/article/article-edit";
		}
		

	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String updateArticle(@RequestParam("articleId") String articleId, ModelMap model) {
		
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		ArticleVO articleVO = articleSvc.getOneArticle(Integer.valueOf(articleId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("articleVO", articleVO);
		return "front-end/article/update_article_input"; // 查詢完成後轉交update_emp_input.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid MsgVO msgVO, BindingResult result, ModelMap model) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		MsgVO originalMsgVO = msgSvc.getOneMsg(Integer.valueOf(msgVO.getMsgId()));

	    // 将其他需要保持不变的字段也进行复制
	    msgVO.setIsRead(true);
	    msgVO.setBuyerVO(originalMsgVO.getBuyerVO());
	    msgVO.setArticleVO(originalMsgVO.getArticleVO());
	    msgVO.setReplyVO(originalMsgVO.getReplyVO());
	    msgVO.setReportVO(originalMsgVO.getReportVO());
	    msgVO.setMsgTypeVO(originalMsgVO.getMsgTypeVO());
	    msgVO.setMsgTime(originalMsgVO.getMsgTime());
	    msgVO.setIsEnabled(originalMsgVO.getIsEnabled());
	    // 开始修改数据
	    msgSvc.updateMsg(msgVO);
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();

		if (msgVO.getArticleVO() != null) {
	        model.addAttribute("msgVO", msgVO);
	        ArticleVO articleVO = articleSvc.getOneArticle(msgVO.getArticleVO().getArticleId());
	        model.addAttribute("articleVO", articleVO);
	        List<ReplyVO> replyVOList = replySvc.getByArticleId(articleVO);
	        model.addAttribute("replyVOList", replyVOList);
	        ReportVO reportVO = new ReportVO();
		    model.addAttribute("reportVO", reportVO);
	        return "front-end/article/list-one-article"; // articleVO不为null，返回到对应的页面
	        
		 } else if (msgVO.getReplyVO() != null) {
	        ReplyVO replyVO = msgVO.getReplyVO();
	        ArticleVO articleVO = replyVO.getArticleVO();
	        // 获取对应留言的文章信息
	        model.addAttribute("articleVO", articleVO);
	        List<ReplyVO> replyVOList = replySvc.getByArticleId(articleVO);
	        model.addAttribute("replyVOList", replyVOList);
	        ReportVO reportVO = new ReportVO();
		    model.addAttribute("reportVO", reportVO);
	        model.addAttribute("msgVO", msgVO);
	        return "front-end/article/list-one-article"; // replyVO不为null，返回到对应的页面
	        
	    } else if (msgVO.getReportVO() != null) {
	        ReportVO reportVO = msgVO.getReportVO();
	        if (reportVO.getReportTargetType() == 0) {
	        	
	        	ArticleVO articleVO = articleSvc.getOneArticle(reportVO.getArticleVO().getArticleId());
	    	    model.addAttribute("articleVO", articleVO);
	    	    model.addAttribute("reportVO", reportVO);
	            model.addAttribute("msgVO", msgVO);
	            return "front-end/article/article-report"; // 返回到檢舉文章页面
	        } else if (reportVO.getReportTargetType() == 1) {
	        	
	        	ReplyVO replyVO = replySvc.getOneReply(reportVO.getReplyVO().getReplyId());
	        	model.addAttribute("replyVO", replyVO);
	        	model.addAttribute("reportVO", reportVO);
	            model.addAttribute("msgVO", msgVO);
	            return "front-end/article/reply-report"; // 返回到檢舉留言页面
	        }
	    }
		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		return "error-page"; 
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("articleId") String articleId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		articleSvc.deleteArticle(Integer.valueOf(articleId));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<ArticleVO> list = articleSvc.getAll();
		model.addAttribute("articleListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "front-end/article/listAllArticle"; // 刪除完成後轉交listAllEmp.html
	}

	
}