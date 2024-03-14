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
		        return "front-end/article/list-one-article"; // articleVO不为null，返回到对应的页面
		        
			 } else if (msgVO.getReplyVO() != null) {
		        ReplyVO replyVO = msgVO.getReplyVO();
		        ArticleVO articleVO = replyVO.getArticleVO();
		        // 获取对应留言的文章信息
		        model.addAttribute("articleVO", articleVO);
		        List<ReplyVO> replyVOList = replySvc.getByArticleId(articleVO);
		        model.addAttribute("replyVOList", replyVOList);
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
		 public String getMyMsg(@RequestParam("id") Integer memberId, Model model) {
			 BuyerVO buyerVO = buyerSvc.getOneBuyer(memberId);
			 model.addAttribute("buyerVO", buyerVO);
			 List<MsgVO> myMsgList = msgSvc.getByMemberId(buyerVO);
			 
			 model.addAttribute("myMsgList", myMsgList);
			 
			 return "front-end/article/Msg-list";
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
	@PostMapping("insert")
	public String insert(@Valid ArticleVO articleVO, BindingResult result, ModelMap model,
			@RequestParam("upFiles") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(articleVO, result, "upFiles");
		articleVO.setArtUpdateTime(new Date());
		articleVO.setArtCreateTime(null);
		articleVO.setArticleLike(0);
		articleVO.setArticleComment(0);
		articleVO.setIsEnabled(true);
		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "員工照片: 請上傳照片");
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				articleVO.setUpFiles(buf);
			}
		}
//		if (result.hasErrors() || parts[0].isEmpty()) {
//			System.out.println("Id: " + articleVO.getArticleId());
//			System.out.println("Buyer: " + articleVO.getBuyerVO());
//			System.out.println("ArticleType: " + articleVO.getArticleTypeVO());
//			System.out.println("Title: " + articleVO.getArticleTitle());
//			System.out.println("Content: " + articleVO.getArticleContent());
//			System.out.println("Time: " + articleVO.getArtUpdateTime());
//			System.out.println("Like: " + articleVO.getArticleLike());
//			System.out.println("Comment: " + articleVO.getArticleComment());
//			System.out.println("Share: " + articleVO.getArticleShare());
//			System.out.println("IsEnabled: " + articleVO.getIsEnabled());
//			System.out.println("UpFiles: " + articleVO.getUpFiles());
////			return "front-end/article/addArticle";
//			return "front-end/article/post-article";
//		}
//		articleVO.setArtUpdateTime(new Date());
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		articleSvc.addArticle(articleVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ArticleVO> list = articleSvc.getAll();
		model.addAttribute("articleListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/article/listAll"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}
	@PostMapping("insert-reply-report")
	public String insertReplyReport(@Valid ReportVO reportVO, BindingResult result, ModelMap model) throws IOException {
		
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		reportVO.setReportTargetType(1);
		reportVO.setArticleVO(null);
		reportVO.setReportState(0);
		reportVO.setReportTime(new Date());
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		reportSvc.addReport(reportVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ReportVO> list = reportSvc.getAll();
		model.addAttribute("reportListData", list);
		
		return "redirect:/article/listAll"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}
	@PostMapping("insert-article-report")
	public String insertArticleReport(@Valid ReportVO reportVO, BindingResult result, ModelMap model) throws IOException {
		
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		reportVO.setReportTargetType(0);
		reportVO.setReplyVO(null);
		reportVO.setReportState(0);
		reportVO.setReportTime(new Date());
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		reportSvc.addReport(reportVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ReportVO> list = reportSvc.getAll();
		model.addAttribute("reportListData", list);
		
		return "redirect:/article/listAll"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}
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
	public String update(@Valid ArticleVO articleVO, BindingResult result, ModelMap model,
			@RequestParam("upFiles") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(articleVO, result, "upFiles");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
			// EmpService empSvc = new EmpService();
			byte[] upFiles = articleSvc.getOneArticle(articleVO.getArticleId()).getUpFiles();
			articleVO.setUpFiles(upFiles);
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] upFiles = multipartFile.getBytes();
				articleVO.setUpFiles(upFiles);
			}
		}
		if (result.hasErrors()) {
			return "front-end/article/article-edit";
		}
		
		articleVO.setArtUpdateTime(new Date());
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		articleSvc.updateArticle(articleVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		articleVO = articleSvc.getOneArticle(Integer.valueOf(articleVO.getArticleId()));
		model.addAttribute("articleVO", articleVO);
		return "redirect:/article/listAll"; // 修改成功後轉交listOneEmp.html
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

	@PostMapping("/like")
	public ResponseEntity<String> increaseLikes(@RequestParam("articleId") String articleId) {
	    ArticleVO articleVO = articleSvc.getOneArticle(Integer.valueOf(articleId)); // 根据文章 ID 获取文章对象
	    if (articleVO != null) {
	        articleVO.setArticleLike(articleVO.getArticleLike()+1); // 增加喜欢数
	        articleSvc.updateArticle(articleVO); // 更新文章信息到数据库
	        return new ResponseEntity<>("Likes increased successfully", HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>("Article not found", HttpStatus.NOT_FOUND);
	    }
	}
	@PostMapping("/unlike")
	public ResponseEntity<String> decreaseLikes(@RequestParam("articleId") String articleId) {
	    ArticleVO articleVO = articleSvc.getOneArticle(Integer.valueOf(articleId)); // 根据文章 ID 获取文章对象
	    if (articleVO != null) {
	        int currentLikes = articleVO.getArticleLike();
	        if (currentLikes > 0) { // 只有喜欢数大于 0 时才能执行减少操作
	            articleVO.setArticleLike(currentLikes - 1); // 减少喜欢数
	            articleSvc.updateArticle(articleVO); // 更新文章信息到数据库
	            return new ResponseEntity<>("Likes decreased successfully", HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>("Likes already at minimum", HttpStatus.BAD_REQUEST);
	        }
	    } else {
	        return new ResponseEntity<>("Article not found", HttpStatus.NOT_FOUND);
	    }
	}


	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${deptListData}" itemValue="deptno" itemLabel="dname" />
	 */
	@ModelAttribute("articleTypeListData")
	protected List<ArticleTypeVO> referenceListData() {
		// DeptService deptSvc = new DeptService();
		List<ArticleTypeVO> list = articleTypeSvc.getAll();
		return list;
	}
	@ModelAttribute("buyerListData")
	protected List<BuyerVO> referenceListData1() {
		// DeptService deptSvc = new DeptService();
		List<BuyerVO> list = buyerSvc.getAll();
		return list;
	}

	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${depMapData}" />
	 */
	@ModelAttribute("articleMapData") //
	protected Map<Integer, String> referenceMapData() {
		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
		map.put(1, "文章分類一");
		map.put(2, "文章分類二");
		map.put(3, "文章分類三");
		map.put(4, "文章分類四");
		map.put(5, "文章分類五");
		return map;
	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(ArticleVO articleVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(articleVO, "articleVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
}