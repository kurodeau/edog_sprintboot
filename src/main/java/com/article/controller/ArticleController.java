package com.article.controller;

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
import com.reply.entity.ReplyVO;
import com.reply.service.ReplyService;
import com.replyLike.entity.ReplyLikeVO;
import com.replyLike.service.ReplyLikeService;
import com.seller.entity.SellerVO;
import com.article.entity.ArticleVO;
import com.article.service.ArticleService;
import com.articleLike.entity.ArticleLikeVO;
import com.articleLike.service.ArticleLikeService;
import com.report.entity.ReportVO;
import com.report.service.ReportService;
import com.reportType.entity.ReportTypeVO;
import com.reportType.service.ReportTypeService;
import com.msg.entity.MsgVO;
import com.msg.service.MsgService;
import com.msgType.entity.MsgTypeVO;

@Controller
@ComponentScan(basePackages = { "com.article", "com.articleType" })
@RequestMapping("/article")
public class ArticleController {

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
	ReportTypeService reportTypeSvc;

	@Autowired
	MsgService msgSvc;

	@Autowired
	ArticleLikeService articleLikeSvc;

	@Autowired
	ReplyLikeService replyLikeSvc;

	@ModelAttribute("articleListData")
	protected List<ArticleVO> referenceListData_Article(Model model) {

		List<ArticleVO> list = articleSvc.getAll();
		return list;
	}

	@ModelAttribute("articleTypeListData")
	protected List<ArticleTypeVO> referenceListData_ArticleType(Model model) {
		model.addAttribute("articleTypeVO", new ArticleTypeVO());
		List<ArticleTypeVO> list = articleTypeSvc.getAll();
		return list;
	}

	@ModelAttribute("buyerListData")
	protected List<BuyerVO> referenceListData(Model model) {
		model.addAttribute("buyerVO", new BuyerVO());
		List<BuyerVO> list = buyerSvc.getAll();
		return list;
	}

	@ModelAttribute("reportListData")
	protected List<ReportVO> referenceListData_Report(Model model) {
		List<ReportVO> list = reportSvc.getAll();
		return list;
	}

	@ModelAttribute("reportTypeListData")
	protected List<ReportTypeVO> referenceListData_ReportType(Model model) {
		model.addAttribute("reportTypeVO", new ReportTypeVO());
		List<ReportTypeVO> list = reportTypeSvc.getAll();
		return list;
	}

	@ModelAttribute("articleTypeListData")
	protected List<ArticleTypeVO> referenceListData() {
		List<ArticleTypeVO> list = articleTypeSvc.getAll();
		return list;
	}

	@ModelAttribute("buyerListData")
	protected List<BuyerVO> referenceListData1() {
		List<BuyerVO> list = buyerSvc.getAll();
		return list;
	}

	@GetMapping("listAll")
	public String listAllArticle(ModelMap model) {
		return "front-end/article/forum-home";
	}

	@GetMapping("add")
	public String addArticle(ModelMap model) {
		SecurityContext secCtx = SecurityContextHolder.getContext();
		Authentication authentication = secCtx.getAuthentication();
		BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
		Integer memberId = buyerVO.getMemberId();
		ArticleVO articleVO = new ArticleVO();
		articleVO.setBuyerVO(buyerVO);
		model.addAttribute("buyerVO", buyerVO);
		model.addAttribute("articleVO", articleVO);
		return "front-end/article/post-article";
	}

	@GetMapping("getOne")
	public String getOneArticle(@RequestParam("id") Integer articleId, ModelMap model) {
		//取得使用者ID
		SecurityContext secCtx = SecurityContextHolder.getContext();
		Authentication authentication = secCtx.getAuthentication();
		BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
		Integer memberId = buyerVO.getMemberId();
		model.addAttribute("buyerVO", buyerVO);

		//取得文章
		ArticleVO articleVO = articleSvc.getOneArticle(articleId);
		model.addAttribute("articleVO", articleVO);

		//使用者是否對此文章點過喜歡
		Integer articleLikeId = articleLikeSvc.findArticleLikeIdByMemberIdAndArticleId(buyerVO, articleVO);
		if (articleLikeId != null) {
			ArticleLikeVO articleLikeVO = articleLikeSvc.getOneArticleLike(articleLikeId);
			model.addAttribute("articleLikeVO", articleLikeVO);
		}
		
		//取得此文章的留言
		List<ReplyVO> replyVOList = replySvc.getByArticleId(articleVO);
		model.addAttribute("replyVOList", replyVOList);

		//確認使用者對每一篇留言有沒有點過喜歡
		for (ReplyVO replyVO : replyVOList) {
			
			//根據使用者VO跟留言VO找有沒有此留言喜歡ID
			Integer replyLikeId = replyLikeSvc.findReplyLikeIdByMemberIdAndArticleId(buyerVO, replyVO);
			replyVO.setLikeIt(0);
			
			//有的話把LikeIt改為1
			if (replyLikeId != null) {
				replyVO.setLikeIt(1);
				System.out.println("replyLikeId:" + replyLikeId);
				System.out.println("replyId:" + replyVO.getReplyId());
			}
		}

		ReportVO reportVO = new ReportVO();
		model.addAttribute("reportVO", reportVO);
		return "front-end/article/list-one-article";
	}

	@GetMapping("/TypeList")
	public String getTypeList(@RequestParam("id") Integer articleTypeId, Model model) {
		ArticleTypeVO articleTypeVO = articleTypeSvc.getOneArticleType(articleTypeId);
		model.addAttribute("articleTypeVO", articleTypeVO);
		List<ArticleVO> articleList = articleSvc.getByArticleTypeId(articleTypeVO);

		model.addAttribute("articleList", articleList);

		return "front-end/article/articletype-list";
	}

	@GetMapping("/MyArticle")
	public String getMyArticle(@RequestParam("id") Integer memberId, Model model) {
		SecurityContext secCtx = SecurityContextHolder.getContext();
		Authentication authentication = secCtx.getAuthentication();
		BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
		memberId = buyerVO.getMemberId();
		model.addAttribute("buyerVO", buyerVO);
		List<ArticleVO> myArticleList = articleSvc.getByMemberId(buyerVO);

		model.addAttribute("myArticleList", myArticleList);

		return "front-end/article/myArticle-list";
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

	@PostMapping("insert")
	public String insert(@Valid ArticleVO articleVO, BindingResult result, ModelMap model,
			@RequestParam("upFiles") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		SecurityContext secCtx = SecurityContextHolder.getContext();
		Authentication authentication = secCtx.getAuthentication();
		BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
		Integer memberId = buyerVO.getMemberId();
		model.addAttribute("buyerVO", buyerVO);
		result = removeFieldError(articleVO, result, "upFiles");
		articleVO.setArtCreateTime(new Date());
		articleVO.setArtUpdateTime(null);
		articleVO.setArticleLike(0);
		articleVO.setArticleComment(0);
		articleVO.setIsEnabled(true);
		articleVO.setBuyerVO(buyerVO);
		if (parts[0].isEmpty()) { 
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				articleVO.setUpFiles(buf);
			}
		}
		/*************************** 2.開始新增資料 *****************************************/
		articleSvc.addArticle(articleVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ArticleVO> list = articleSvc.getAll();
		model.addAttribute("buyerVO", buyerVO);
		model.addAttribute("articleListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/article/listAll";
	}

	@PostMapping("insert-reply-report")
	public String insertReplyReport(@Valid ReportVO reportVO, BindingResult result, ModelMap model) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		SecurityContext secCtx = SecurityContextHolder.getContext();
		Authentication authentication = secCtx.getAuthentication();
		BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
		Integer memberId = buyerVO.getMemberId();
		Integer replyId = reportVO.getReplyVO().getReplyId();
		ReplyVO replyVO = replySvc.getOneReply(Integer.valueOf(replyId));
		reportVO.setBuyerVO(replyVO.getBuyerVO());
		model.addAttribute("buyerVO", buyerVO);
		reportVO.setReportTargetType(1);
		reportVO.setArticleVO(null);
		reportVO.setReportState(0);
		reportVO.setReportTime(new Date());
		/*************************** 2.開始新增資料 *****************************************/
		reportSvc.addReport(reportVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ReportVO> list = reportSvc.getAll();
		model.addAttribute("reportListData", list);

		return "redirect:/article/listAll";
	}

	@PostMapping("insert-article-report")
	public String insertArticleReport(@Valid ReportVO reportVO, BindingResult result, ModelMap model)
			throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		SecurityContext secCtx = SecurityContextHolder.getContext();
		Authentication authentication = secCtx.getAuthentication();
		BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
		Integer memberId = buyerVO.getMemberId();
		Integer articleId = reportVO.getArticleVO().getArticleId();
		ArticleVO articleVO = articleSvc.getOneArticle(Integer.valueOf(articleId));
		reportVO.setBuyerVO(articleVO.getBuyerVO());
		model.addAttribute("buyerVO", buyerVO);
		reportVO.setReportTargetType(0);
		reportVO.setReplyVO(null);
		reportVO.setReportState(0);
		reportVO.setReportTime(new Date());
		/*************************** 2.開始新增資料 *****************************************/
		reportSvc.addReport(reportVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ReportVO> list = reportSvc.getAll();
		model.addAttribute("reportListData", list);

		return "redirect:/article/listAll";
	}

	@PostMapping("update")
	public String update(@Valid ArticleVO articleVO, BindingResult result, ModelMap model,
			@RequestParam("upFiles") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		result = removeFieldError(articleVO, result, "upFiles");

		if (parts[0].isEmpty()) { 
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
		ArticleVO originalArticleVO = articleSvc.getOneArticle(Integer.valueOf(articleVO.getArticleId()));
		articleVO.setArtUpdateTime(new Date());
		articleVO.setArtCreateTime(originalArticleVO.getArtCreateTime());
		/*************************** 2.開始修改資料 *****************************************/
		articleSvc.updateArticle(articleVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		articleVO = articleSvc.getOneArticle(Integer.valueOf(articleVO.getArticleId()));
		model.addAttribute("articleVO", articleVO);
		return "redirect:/article/listAll";
	}

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
	public ResponseEntity<String> increaseLikes(@RequestParam("articleId") String articleId, ModelMap model) {
		ArticleVO articleVO = articleSvc.getOneArticle(Integer.valueOf(articleId));
		if (articleVO != null) {
			SecurityContext secCtx = SecurityContextHolder.getContext();
			Authentication authentication = secCtx.getAuthentication();
			BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
			Integer memberId = buyerVO.getMemberId();
			model.addAttribute("buyerVO", buyerVO);
			ArticleLikeVO articleLikeVO = new ArticleLikeVO();
			articleLikeVO.setBuyerVO(buyerVO);
			articleLikeVO.setArticleVO(articleVO);
			articleLikeVO.setArticleLikeListTime(new Date());
			articleLikeSvc.addArticleLike(articleLikeVO);
			articleVO.setArticleLike(articleVO.getArticleLike() + 1);
			articleSvc.updateArticle(articleVO);
			MsgVO msgVO = new MsgVO();
			msgVO.setArticleVO(articleVO);
			msgVO.setSenderMember(buyerVO);
			msgVO.setReceiverMember(articleVO.getBuyerVO());
			MsgTypeVO msgTypeVO = new MsgTypeVO();
			msgTypeVO.setMsgTypeId(1);
			msgVO.setMsgTypeVO(msgTypeVO);
			msgVO.setMsgTime(new Date());
			msgVO.setIsRead(false);
			msgVO.setIsEnabled(true);
			msgSvc.addMsg(msgVO);
			model.addAttribute("msgVO", msgVO);
			return new ResponseEntity<>("Likes increased successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Article not found", HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/unlike")
	public ResponseEntity<String> decreaseLikes(@RequestParam("articleId") String articleId, ModelMap model) {
		ArticleVO articleVO = articleSvc.getOneArticle(Integer.valueOf(articleId));
		if (articleVO != null) {
			int currentLikes = articleVO.getArticleLike();
			if (currentLikes > 0) {
				SecurityContext secCtx = SecurityContextHolder.getContext();
				Authentication authentication = secCtx.getAuthentication();
				BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
				Integer memberId = buyerVO.getMemberId();
				model.addAttribute("buyerVO", buyerVO);
				Integer articleLikeId = articleLikeSvc.findArticleLikeIdByMemberIdAndArticleId(buyerVO, articleVO);
				articleLikeSvc.deleteArticleLike(articleLikeId);
				MsgTypeVO msgTypeVO = new MsgTypeVO();
				msgTypeVO.setMsgTypeId(1);
				Integer msgId = msgSvc.findArticleLikeIdByMemberIdAndArticleIdAndMsgTypeId(buyerVO, articleVO,
						msgTypeVO);
				msgSvc.deleteMsg(msgId);

				articleVO.setArticleLike(currentLikes - 1);
				articleSvc.updateArticle(articleVO);
				return new ResponseEntity<>("Likes decreased successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Likes already at minimum", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Article not found", HttpStatus.NOT_FOUND);
		}
	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(ArticleVO articleVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(articleVO, "articleVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

}