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
@RequestMapping("/report")
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
	
	@GetMapping("addReport")
	public String addReport(ModelMap model) {
		ReportVO reportVO = new ReportVO();
		model.addAttribute("reportVO", reportVO);
		return "back-end/report/addReport";
//		return "back-end/article/post-article";

	}

	/*
	 * This method will be called on addEmp.html form submission, handling POST
	 * request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid ReportVO reportVO, BindingResult result, ModelMap model) throws IOException {
		
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		if (reportVO.getReportTargetType() == 0) {
	        reportVO.setReplyVO(null); 
	    } else if (reportVO.getReportTargetType() == 1) {
	        reportVO.setArticleVO(null); 
	    }
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		reportVO.setReportTime(new Date());
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		reportSvc.addReport(reportVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ReportVO> list = reportSvc.getAll();
		model.addAttribute("reportListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/report/listAllReport"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST
	 * request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("reportId") String reportId, ModelMap model) {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		ReportVO reportVO = reportSvc.getOneReport(Integer.valueOf(reportId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("reportVO", reportVO);
		return "back-end/report/update_report_input"; // 查詢完成後轉交update_emp_input.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling
	 * POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid ReportVO reportVO, BindingResult result, ModelMap model) throws IOException {

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

	/*
	 * This method will be called on select_page.html form submission, handling POST
	 * request
	 */
//	@PostMapping("listArticles_ByCompositeQuery")
//	public String listAllArticle(HttpServletRequest req, Model model) {
//		Map<String, String[]> map = req.getParameterMap();
//		List<ArticleVO> list = articleSvc.getAll(map);
//		model.addAttribute("articleListData", list); // for listAllEmp.html 第85行用
//		return "back-end/article/listAllArticle";
//	}

}