package com.petdraw.controller;

import java.io.IOException;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.buyer.entity.BuyerVO;
import com.buyer.service.BuyerService;
import com.petdraw.model.PetDrawService;
import com.petdraw.model.PetDrawVO;

@Controller
@Validated
@RequestMapping("/petdraw")
public class PetDrawController {

	@Autowired
	private PetDrawService petDrawSvc;
	
	@Autowired
	private BuyerService buyerSvc;

	@GetMapping("addpetdraw")
	public String showaddpetdraw(ModelMap model) {
		PetDrawVO petDrawVO = new PetDrawVO();
		model.addAttribute("petDrawVO", petDrawVO);
		return "front-end/article/forum-petdraw";
	}

//    http://localhost:8080/servername/pairPets?=memberMainId=123456&memberPa
	@PostMapping("insert")
	public String insert(@Valid PetDrawVO petDrawVO, BindingResult result, ModelMap model) throws IOException {
		//抽卡者 預設為1
		BuyerVO memberId = new BuyerVO();
		memberId.setMemberId(1);
		petDrawVO.setMemberId(memberId.getMemberId());
		
		//抽到隨機數
		 int randomBuyerId;
	        do {
	            randomBuyerId = petDrawSvc.getRandomMemberIdNotEqualTo(memberId.getMemberId());
	        } while (randomBuyerId == memberId.getMemberId());
        BuyerVO memberPairId = new BuyerVO();
        memberPairId.setMemberId(randomBuyerId);
		
		petDrawVO.setMemberPairId(memberPairId);
		
		petDrawVO.setPetDrawTime(new Date());
		Integer petDrawIdNew = petDrawSvc.addPetDraw(petDrawVO);
		

		// 添加成功的提示信息
		model.addAttribute("successMessage", "寵物配對成功！");
		// 重新導向到寵物配對列表頁面
		return "redirect:/petdraw/getOne-petdraw?petDrawId=" + petDrawIdNew;
//		return "redirect:/article/forum-petdraw";
	}
	
	@GetMapping("getOne-petdraw")
	public String updateArticle(@RequestParam("petDrawId") String petDrawId, ModelMap model) {
		
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		
		PetDrawVO petDrawVO = petDrawSvc.getOnePetDraw(Integer.valueOf(petDrawId));
		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("petDrawVO", petDrawVO);
		return "front-end/article/getOne-petdraw"; // 查詢完成後轉交update_emp_input.html
	}
	@GetMapping("getOne-petdraw2")
	public String updateArticle2(@RequestParam("petDrawId") String petDrawId, ModelMap model) {
		
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		
		PetDrawVO petDrawVO = petDrawSvc.getOnePetDraw(Integer.valueOf(petDrawId));
		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("petDrawVO", petDrawVO);
		return "front-end/article/getOne-petdraw2"; // 查詢完成後轉交update_emp_input.html
	}
	@PostMapping("update")
	public String update(@Valid PetDrawVO petDrawVO, BindingResult result, ModelMap model) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		petDrawVO.setMemberResTime(new Date());
		int petDrawId=petDrawVO.getpetDrawId();
		
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		petDrawSvc.updatePetDraw(petDrawVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		petDrawVO = petDrawSvc.getOnePetDraw(Integer.valueOf(petDrawVO.getpetDrawId()));
		model.addAttribute("petDrawVO", petDrawVO);
		return "redirect:/petdraw/getOne-petdraw2?petDrawId=" + petDrawId;
	}
	
	@PostMapping("update2")
	public String update2(@Valid PetDrawVO petDrawVO, BindingResult result, ModelMap model) throws IOException {
		
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		petDrawVO.setMemberPairResTime(new Date());
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		petDrawSvc.updatePetDraw(petDrawVO);
		
		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		petDrawVO = petDrawSvc.getOnePetDraw(Integer.valueOf(petDrawVO.getpetDrawId()));
		model.addAttribute("petDrawVO", petDrawVO);
		return "front-end/article/matching-result"; // 修改成功後轉交listOneEmp.html
	}
	// 顯示寵物配對列表頁面
	@GetMapping("/petdraw/list")
	public String showPetDrawList(ModelMap model) {
		// 取得所有寵物配對的資料
		model.addAttribute("petDrawList", petDrawSvc.getAll());
		return "front-end/article/forum-petdraw";
	}
}