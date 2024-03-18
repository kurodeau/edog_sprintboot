package com.reply.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.reply.entity.ReplyVO;
import com.reply.service.ReplyService;
import com.article.entity.ArticleVO;
import com.article.service.ArticleService;
import com.articleLike.entity.ArticleLikeVO;
import com.replyLike.entity.ReplyLikeVO;
import com.replyLike.service.ReplyLikeService;
import com.buyer.entity.BuyerVO;
import com.msg.entity.MsgVO;
import com.msg.service.MsgService;
import com.msgType.entity.MsgTypeVO;

@Controller
@RequestMapping("/reply")
public class ReplyController {

	@Autowired
	ArticleService articleSvc;

	@Autowired
	ReplyService replySvc;
	
	@Autowired
	MsgService msgSvc;
	
	@Autowired
	ReplyLikeService replyLikeSvc;
	/*
	 * This method will serve as addEmp.html handler.
	 */
	@GetMapping("addReply")
	public String addReply(ModelMap model) {
		ReplyVO replyVO = new ReplyVO();
		model.addAttribute("replyVO", replyVO);
		return "back-end/reply/addReply";
//		return "back-end/article/post-article";
		
	}


	@PostMapping("/insert")
    public ResponseEntity<String> insertReply(@RequestParam("commentContent") String commentContent,
                                              @RequestParam("articleId") String articleId) {
        try {
        	SecurityContext secCtx = SecurityContextHolder.getContext() ;
			 Authentication authentication = secCtx.getAuthentication();
			 BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
			 Integer memberId = buyerVO.getMemberId();
            // 创建一个新的回复对象
            ReplyVO replyVO = new ReplyVO();
            ArticleVO articleVO = articleSvc.getOneArticle(Integer.valueOf(articleId));
            articleVO.setArticleComment(articleVO.getArticleComment()+1);
            articleSvc.updateArticle(articleVO);

            replyVO.setReplyContent(commentContent);
            replyVO.setReplyTime(new Date());
            replyVO.setArticleVO(articleVO);
    	    replyVO.setBuyerVO(buyerVO);
    	    replyVO.setReplyTime(new Date());
    	    replyVO.setReplyLike(0);
    	    replyVO.setIsEnabled(true);

            // 保存回复到数据库
            replySvc.addReply(replyVO);
            MsgVO msgVO = new MsgVO();
	        msgVO.setReplyVO(replyVO); // 设置关联的文章 ID
	        msgVO.setSenderMember(buyerVO);
	        msgVO.setReceiverMember(articleVO.getBuyerVO());
	        MsgTypeVO msgTypeVO =new MsgTypeVO();
	        msgTypeVO.setMsgTypeId(2);
	        msgVO.setMsgTypeVO(msgTypeVO);
	        msgVO.setMsgTime(new Date());
	        msgVO.setIsRead(false);
	        msgVO.setIsEnabled(true);
	        // 其他需要设置的属性
	        msgSvc.addMsg(msgVO);
            // 返回成功响应
            return ResponseEntity.ok("评论已成功保存！");
        } catch (Exception e) {
            // 如果保存评论时发生错误，返回错误响应
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("保存评论时出现错误：" + e.getMessage());
        }
    }
	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("replyId") String replyId, ModelMap model) {
		
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		ReplyVO replyVO = replySvc.getOneReply(Integer.valueOf(replyId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("replyVO", replyVO);
		return "back-end/reply/update_reply_input"; // 查詢完成後轉交update_emp_input.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid ReplyVO replyVO, BindingResult result, ModelMap model
			) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		replyVO.setReplyTime(new Date());
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		replySvc.updateReply(replyVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		replyVO = replySvc.getOneReply(Integer.valueOf(replyVO.getReplyId()));
		model.addAttribute("replyVO", replyVO);
		return "back-end/reply/listOneReply"; // 修改成功後轉交listOneEmp.html
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("replyId") String replyId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		replySvc.deleteReply(Integer.valueOf(replyId));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<ReplyVO> list = replySvc.getAll();
		model.addAttribute("replyListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/reply/listAllReply"; // 刪除完成後轉交listAllEmp.html
	}

	@PostMapping("/like")
	public ResponseEntity<String> increaseLikes(@RequestParam("replyId") String replyId) {
		ReplyVO replyVO = replySvc.getOneReply(Integer.valueOf(replyId)); // 根据文章 ID 获取文章对象
	    if (replyVO != null) {
	    	SecurityContext secCtx = SecurityContextHolder.getContext() ;
			 Authentication authentication = secCtx.getAuthentication();
			 BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
			 Integer memberId = buyerVO.getMemberId();
			 System.out.println(memberId);
	    	ReplyLikeVO replyLikeVO = new ReplyLikeVO();
	    	replyLikeVO.setBuyerVO(buyerVO);
	    	replyLikeVO.setReplyVO(replyVO);
	    	replyLikeVO.setReplyLikeTime(new Date());
	    	replyLikeSvc.addReplyLike(replyLikeVO);
	    	replyVO.setReplyLike(replyVO.getReplyLike()+1); // 增加喜欢数
	    	replySvc.updateReply(replyVO); // 更新文章信息到数据库
	    	MsgVO msgVO = new MsgVO();
	        msgVO.setReplyVO(replyVO); // 设置关联的文章 ID
	        msgVO.setSenderMember(buyerVO);
	        msgVO.setReceiverMember(replyVO.getBuyerVO());
	        MsgTypeVO msgTypeVO =new MsgTypeVO();
	        msgTypeVO.setMsgTypeId(3);
	        msgVO.setMsgTypeVO(msgTypeVO);
	        msgVO.setMsgTime(new Date());
	        msgVO.setIsRead(false);
	        msgVO.setIsEnabled(true);
	        // 其他需要设置的属性
	        msgSvc.addMsg(msgVO);
	        return new ResponseEntity<>("Likes increased successfully", HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>("Article not found", HttpStatus.NOT_FOUND);
	    }
	}
	@PostMapping("/unlike")
	public ResponseEntity<String> decreaseLikes(@RequestParam("replyId") String replyId) {
		ReplyVO replyVO = replySvc.getOneReply(Integer.valueOf(replyId)); // 根据文章 ID 获取文章对象
	    if (replyVO != null) {
	    	SecurityContext secCtx = SecurityContextHolder.getContext() ;
			Authentication authentication = secCtx.getAuthentication();
			BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
			Integer memberId = buyerVO.getMemberId();
			System.out.println(memberId);
			System.out.println(Integer.valueOf(replyId));
			Integer replyLikeId = replyLikeSvc.findReplyLikeIdByMemberIdAndArticleId(buyerVO, replyVO);
	    	replyLikeSvc.deleteReplyLike(replyLikeId);
	    	MsgTypeVO msgTypeVO = new MsgTypeVO();
	    	msgTypeVO.setMsgTypeId(3);
	    	Integer msgId = msgSvc.findArticleLikeIdByMemberIdAndReplyIdAndMsgTypeId(buyerVO, replyVO,msgTypeVO);
	    	msgSvc.deleteMsg(msgId);
	        int currentLikes = replyVO.getReplyLike();
	        if (currentLikes > 0) { // 只有喜欢数大于 0 时才能执行减少操作
	        	replyVO.setReplyLike(currentLikes - 1); // 减少喜欢数
	        	replySvc.updateReply(replyVO); // 更新文章信息到数据库
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
	@ModelAttribute("articleListDataPK")
	protected List<ArticleVO> referenceListData_ArticlePK() {
		// DeptService deptSvc = new DeptService();
		List<ArticleVO> list = articleSvc.getAll();
		return list;
	}

	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${depMapData}" />
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
	public BindingResult removeFieldError(ReplyVO replyVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(replyVO, "replyVO");
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
//		return "back-end/article/listAllArticle";
//	}

}