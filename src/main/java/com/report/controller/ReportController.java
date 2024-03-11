package com.report.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.report.entity.ReportVO;
import com.report.service.ReportService;
import com.reportType.entity.ReportTypeVO;
import com.reportType.service.ReportTypeService;
import com.article.entity.ArticleVO;
import com.article.service.ArticleService;
import com.buyer.entity.BuyerVO;
import com.buyer.service.BuyerService;
import com.reply.entity.ReplyVO;
import com.reply.service.ReplyService;

@Controller
@RequestMapping("/back/report")
public class ReportController {

	@Autowired
	ArticleService articleSvc;

	@Autowired
	ReportService reportSvc;

	@Autowired
	ReplyService replySvc;

	@Autowired
	ReportTypeService reportTypeSvc;
	
	@Autowired
	BuyerService buyerSvc;
	
	
	
	@ModelAttribute("reportListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
    protected List<ReportVO> referenceListData_Report(Model model) {
    	
    	List<ReportVO> list = reportSvc.getAll();
    	return list;
    }
	@ModelAttribute("reportListArtData")
	protected List<ReportVO> referenceListArtData_Report(Model model) {
	    List<ReportVO> list = reportSvc.getAll();
	    List<ReportVO> filteredList = new ArrayList<>();

	    for (ReportVO report : list) {
	        if (report.getReportTargetType() != null && report.getReportTargetType() == 0) {
	            filteredList.add(report);
	        }
	    }

	    return filteredList;
	}
	@ModelAttribute("reportListReplyData")
	protected List<ReportVO> referenceListReplyData_Report(Model model) {
		List<ReportVO> list = reportSvc.getAll();
		List<ReportVO> filteredList = new ArrayList<>();
		
		for (ReportVO report : list) {
			if (report.getReportTargetType() != null && report.getReportTargetType() == 1) {
				filteredList.add(report);
			}
		}
		
		return filteredList;
	}

	
	@ModelAttribute("reportTypeListData") // for select_page.html 第135行用
	protected List<ReportTypeVO> referenceListData_ReportType(Model model) {
		model.addAttribute("reportTypeVO", new ReportTypeVO()); // for select_page.html 第133行用
		List<ReportTypeVO> list = reportTypeSvc.getAll();
		return list;
	}
	
	@ModelAttribute("buyerListData") // for select_page.html 第135行用
	protected List<BuyerVO> referenceListData_Buyer(Model model) {
		model.addAttribute("buyerVO", new BuyerVO()); // for select_page.html 第133行用
		List<BuyerVO> list = buyerSvc.getAll();
		return list;
	}

	/*
	 * This method will serve as addEmp.html handler.
	 */
	
	@GetMapping("listAll")
	public String listAllReport(ModelMap model) {
		return "back-end/back-report-list";
	}
	
	@GetMapping("listartAll")
	public String listArtAllReport(ModelMap model) {
		return "back-end/back-reportart-list";
	}
	@GetMapping("listreplyAll")
	public String listReplyAllReport(ModelMap model) {
		return "back-end/back-reportreply-list";
	}
	
	@GetMapping("addReport")
	public String addReport(ModelMap model) {
		ReportVO reportVO = new ReportVO();
		model.addAttribute("reportVO", reportVO);
//		return "back-end/report/addReport";
		return "back-end/report";
		
//		return "back-end/article/post-article";

	}

	/*
	 * This method will be called on addEmp.html form submission, handling POST
	 * request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid ReportVO reportVO, BindingResult result, ModelMap model) throws IOException {
		
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
		
		return "back-end/back-report-list"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}
	

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST
	 * request
	 */
//	@PostMapping("getOne_For_Update")
//	public String getOne_For_Update(@RequestParam("reportId") String reportId, ModelMap model) {
//
//		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//		/*************************** 2.開始查詢資料 *****************************************/
//		ReportVO reportVO = reportSvc.getOneReport(Integer.valueOf(reportId));
//
//		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
//		model.addAttribute("reportVO", reportVO);
//		return "back-end/report/update_report_input"; // 查詢完成後轉交update_emp_input.html
//	}
	@PostMapping("getArticle_For_Update")
	public String getArticle_For_Update(@RequestParam("reportId") String reportId, @RequestParam("articleId") String articleId, ModelMap model) {
	    // 接收请求参数，这里省略了格式错误处理部分
	    ReportVO reportVO = reportSvc.getOneReport(Integer.valueOf(reportId));
	    
	    // 获取对应的ArticleVO对象
	    ArticleVO articleVO = articleSvc.getOneArticle(Integer.valueOf(articleId));
	    
	    // 添加ArticleVO对象到模型中
	    model.addAttribute("articleVO", articleVO);
	    
	    // 添加reportVO对象到模型中
	    model.addAttribute("reportVO", reportVO);
	    
	    // 返回视图
	    return "back-end/back-reportart-edit";
	}
	@PostMapping("getReply_For_Update")
	public String getReply_For_Update(@RequestParam("reportId") String reportId, @RequestParam("replyId") String replyId, ModelMap model) {
		// 接收请求参数，这里省略了格式错误处理部分
		ReportVO reportVO = reportSvc.getOneReport(Integer.valueOf(reportId));
		
		// 获取对应的ArticleVO对象
		ReplyVO replyVO = replySvc.getOneReply(Integer.valueOf(replyId));
		
		// 添加ArticleVO对象到模型中
		model.addAttribute("replyVO", replyVO);
		
		// 添加reportVO对象到模型中
		model.addAttribute("reportVO", reportVO);
		
		// 返回视图
		return "back-end/back-reportreply-edit";
	}

	
	@PostMapping("getOne_For_Add")
	public String getOne_For_Add(@RequestParam("articleId") String articleId, ModelMap model) {
		
		ReportVO reportVO = new ReportVO();
		ArticleVO articleVO = new ArticleVO();
		articleVO.setArticleId(Integer.valueOf(articleId));
		reportVO.setArticleVO(articleVO);
		model.addAttribute("reportVO", reportVO);
		return "back-end/report";
	}
	

	/*
	 * This method will be called on update_emp_input.html form submission, handling
	 * POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid ReportVO reportVO,@Valid ArticleVO articleVO, BindingResult result, ModelMap model) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		if (reportVO.getReportTargetType() == 0) {
	        reportVO.setReplyVO(null); 
	    } else if (reportVO.getReportTargetType() == 1) {
	        reportVO.setArticleVO(null); 
	    }
		reportVO.setReportDealTime(new Date());
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		reportSvc.updateReport(reportVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		reportVO = reportSvc.getOneReport(Integer.valueOf(reportVO.getReportId()));
		model.addAttribute("reportVO", reportVO);
		return "back-end/report/listOneReport"; // 修改成功後轉交listOneEmp.html
	}
	
	@PostMapping("update-article-report")
	public String updateArticleReport(@Valid ReportVO reportVO,@Valid ArticleVO articleVO, BindingResult result, ModelMap model,
			@RequestParam("upFiles") MultipartFile[] parts) throws IOException {
		
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
			return "back-end/back-reportart-list";
		}
		
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		reportVO.setReplyVO(null); 
			
		reportVO.setReportState(1);
		reportVO.setReportDealTime(new Date());
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		reportSvc.updateReport(reportVO);
		articleSvc.updateArticle(articleVO);
		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		articleVO = articleSvc.getOneArticle(Integer.valueOf(articleVO.getArticleId()));
		model.addAttribute("articleVO", articleVO);
		reportVO = reportSvc.getOneReport(Integer.valueOf(reportVO.getReportId()));
		model.addAttribute("reportVO", reportVO);
		return "back-end/back-reportart-list"; // 修改成功後轉交listOneEmp.html
	}
	
	@PostMapping("update-reply-report")
	public String updateReplyReport(@Valid ReportVO reportVO,@Valid ReplyVO replyVO, BindingResult result, ModelMap model) throws IOException {
		
		if (result.hasErrors()) {
			return "back-end/back-reportreply-list";
		}
		
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		reportVO.setArticleVO(null); 
		
		reportVO.setReportState(1);
		reportVO.setReportDealTime(new Date());
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		reportSvc.updateReport(reportVO);
		replySvc.updateReply(replyVO);
		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		replyVO = replySvc.getOneReply(Integer.valueOf(replyVO.getReplyId()));
		model.addAttribute("replyVO", replyVO);
		reportVO = reportSvc.getOneReport(Integer.valueOf(reportVO.getReportId()));
		model.addAttribute("reportVO", reportVO);
		return "back-end/back-reportreply-list"; // 修改成功後轉交listOneEmp.html
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST
	 * request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("reportId") String reportId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		reportSvc.deleteReport(Integer.valueOf(reportId));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<ReportVO> list = reportSvc.getAll();
		model.addAttribute("reportListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/report/listAllReport"; // 刪除完成後轉交listAllEmp.html
	}

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : <form:select
	 * path="deptno" id="deptno" items="${deptListData}" itemValue="deptno"
	 * itemLabel="dname" />
	 */
	@ModelAttribute("articleListDataPK")
	protected List<ArticleVO> referenceListData_ArticlePK() {
		// DeptService deptSvc = new DeptService();
		List<ArticleVO> list = articleSvc.getAll();
		return list;
	}

	@ModelAttribute("replyListDataPK")
	protected List<ReplyVO> referenceListData_ReplyPK() {
		// DeptService deptSvc = new DeptService();
		List<ReplyVO> list = replySvc.getAll();
		return list;
	}

	@ModelAttribute("reportTypeListData")
	protected List<ReportTypeVO> referenceListData_ReportType() {
		// DeptService deptSvc = new DeptService();
		List<ReportTypeVO> list = reportTypeSvc.getAll();
		return list;
	}

	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : <form:select
	 * path="deptno" id="deptno" items="${depMapData}" />
	 */
//	@ModelAttribute("articleMapData") //
//	protected Map<Integer, String> referenceMapData() {
//		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
//		map.put(1, "文章分類一");
//		map.put(2, "文章分類二");
//		map.put(3, "文章分類三");
//		map.put(4, "文章分類四");
//		map.put(5, "文章分類五");
//		return map;
//	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(ReportVO reportVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(reportVO, "reportVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

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