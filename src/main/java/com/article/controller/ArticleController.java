package com.article.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
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
import com.buyer.service.BuyerService;
import com.seller.entity.SellerVO;
import com.article.entity.ArticleVO;
import com.article.service.ArticleService;

@Controller
@ComponentScan(basePackages = {"com.article", "com.articleType"})
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	ArticleTypeService articleTypeSvc;

	@Autowired
	ArticleService articleSvc;
	
	@Autowired
	BuyerService buyerSvc;

	/*
	 * This method will serve as addEmp.html handler.
	 */
	
	 @ModelAttribute("articleListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
		protected List<ArticleVO> referenceListData_Article(Model model) {
			
	    	List<ArticleVO> list = articleSvc.getAll();
			return list;
		}
	 
	 @ModelAttribute("articleTypeListData") // for select_page.html 第135行用
		protected List<ArticleTypeVO> referenceListData_ArticleType(Model model) {
			model.addAttribute("articleTypeVO", new ArticleTypeVO()); // for select_page.html 第133行用
			List<ArticleTypeVO> list = articleTypeSvc.getAll();
			return list;
		}

	
	// /front/article/listAll
		@GetMapping("listAll")
		public String listAllArticle(ModelMap model) {
//			return "front-end/article/front-article-list";
			return "front-end/article/forum-home";
		}

		@GetMapping("add")
		public String addArticle(ModelMap model) {
			ArticleVO articleVO = new ArticleVO();
			model.addAttribute("articleVO", articleVO);
			return "front-end/addArticle";
		}
		@GetMapping("getOne")
		public String getOneArticle(@RequestParam("id") Integer articleId, ModelMap model) {
			ArticleVO articleVO = articleSvc.getOneArticle(articleId);
			model.addAttribute("articleVO", articleVO);
			
			return "front-end/article/list-one-article";
		}
		
		@GetMapping("getOne_For_Update")
		public String getOneArticleUpdate(@RequestParam("id") Integer articleId, ModelMap model) {
		    ArticleVO articleVO = articleSvc.getOneArticle(articleId);
		    model.addAttribute("articleVO", articleVO);
		    System.out.println("==============XXXXXXXXXXXXXX");
		    System.out.println("getOne_For_Update");
		    System.out.println(articleVO);
		    System.out.println("==============XXXXXXXXXXXXXX");

		    return "front-end/update_article_input";
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

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "員工照片: 請上傳照片");
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				articleVO.setUpFiles(buf);
			}
		}
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "front-end/article/addArticle";
//			return "front-end/article/post-article";
		}
		articleVO.setArtUpdateTime(new Date());
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		articleSvc.addArticle(articleVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ArticleVO> list = articleSvc.getAll();
		model.addAttribute("articleListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/article/listAllArticle"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
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
			return "front-end/article/update_article_input";
		}
		articleVO.setArtUpdateTime(new Date());
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		articleSvc.updateArticle(articleVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		articleVO = articleSvc.getOneArticle(Integer.valueOf(articleVO.getArticleId()));
		model.addAttribute("articleVO", articleVO);
		return "front-end/article/listOneArticle"; // 修改成功後轉交listOneEmp.html
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
	
	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */
//	@PostMapping("listArticles_ByCompositeQuery")
//	public String listAllArticle(HttpServletRequest req, Model model) {
//		Map<String, String[]> map = req.getParameterMap();
//		List<ArticleVO> list = articleSvc.getAll(map);
//		model.addAttribute("articleListData", list); // for listAllEmp.html 第85行用
//		return "front-end/article/listAllArticle";
//	}

}