package com.petdraw.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
import com.petdraw.enums.PetDrawStatusEnum;
import com.petdraw.model.PairResponse;
import com.petdraw.model.PetDrawService;
import com.petdraw.model.PetDrawVO;

@Controller
@Validated
@RequestMapping("/front/forum/petdraw")
public class PetDrawController {

	private PairResponse pairResponse = new PairResponse();
	private PairResponse pairPassiveResponse = new PairResponse();

	@Autowired
	private PetDrawService petDrawSvc;

	@Autowired
	private BuyerService buyerSvc;

	@GetMapping("")
	public String petdrawHomePage(ModelMap model) {

		return "front-end/article/forum-petdraw";
	}

//    http://localhost:8080/servername/pairPets?=memberMainId=123456&memberPa
	@PostMapping("/startpairing")
	public String startpairing(ModelMap model) throws IOException {
		pairResponse = new PairResponse();

		// Test
		BuyerVO buyerCurrentVO = buyerSvc.getOneBuyer(1);

		PetDrawVO petDrawVO = new PetDrawVO();

		// 設定抽卡人ID
		petDrawVO.setMemberId(buyerCurrentVO.getMemberId());
		petDrawVO.setPetDrawTime(new Date());

		pairResponse.setPetDraw(petDrawVO);
		pairResponse.setMemberVO(buyerCurrentVO);

		pairResponse.setPetDraw(petDrawVO);

		BuyerVO buyerPairVO = petDrawSvc.getRandomBuyerVONotEqualTo(buyerCurrentVO);
		pairResponse.setMemberPairVO(buyerPairVO);

		model.addAttribute("pairResponse", pairResponse);

		return "front-end/article/getOne-petdraw";
	}

	@PostMapping("/startpairing/update")
	public String startpairingUpdate(PairResponse pairesponse, @RequestParam("isMemberLike") boolean isMemberLike,
			BindingResult result, ModelMap model) throws IOException {

		// "this.pairResponse" is null
//		pairResponse = (PairResponse)model.getAttribute("pairResponse");
//		System.out.println(pairesponse);
//		System.out.println(isMemberLike);
		// com.petdraw.model.PairResponse@57f2784a
		if (isMemberLike) {
			PetDrawVO petDrawVO = pairResponse.getPetDraw();
//			System.out.println(pairResponse.getMemberVO().getMemberId());
			// 設定抽卡人回覆時間
			petDrawVO.setMemberResTime(new Date());
			petDrawVO.setIsMemberLike(true);
			Integer petDrawId = petDrawSvc.addPetDraw(petDrawVO);

			return "redirect:/front/forum/petdraw/checkpairing";
		} else {
			PetDrawVO petDrawVO = pairResponse.getPetDraw();
			petDrawVO.setMemberResTime(new Date());
			petDrawVO.setIsMemberLike(false);
			Integer petDrawId = petDrawSvc.addPetDraw(petDrawVO);
			return "redirect:/front/forum/petdraw";
		}

	}

	@GetMapping("/checkpairing")
	public String showpairing(ModelMap model) {

		// Test
		BuyerVO buyerCurrentVO = buyerSvc.getOneBuyer(1);

		LocalDateTime localDate = LocalDate.now().atStartOfDay();

		List<PetDrawVO> petDrawVOs = petDrawSvc.getByMemberIdAndAfterPetDrawTime(buyerCurrentVO.getMemberId(),
				localDate);

		System.out.println(localDate);

		System.out.println(petDrawVOs.size());

		if (petDrawVOs.size() == 0) {
			model.addAttribute("pairStatus", PetDrawStatusEnum.NOT_DRAWN_YET.getCode());
			return "front-end/article/petdraw-status";
		}

		if (petDrawVOs.size() > 0) {
			model.addAttribute("pairStatus", PetDrawStatusEnum.DRAWN_WAITING_FOR_RESPONSE.getCode());
			return "front-end/article/petdraw-status";
		}

		if (!petDrawVOs.get(0).getIsMemberPairLike() && petDrawVOs.get(0).getIsMemberLike()) {
			model.addAttribute("pairStatus", PetDrawStatusEnum.DRAWN_REJECTED.getCode());
			return "front-end/article/petdraw-status";
		}

		if (petDrawVOs.get(0).getIsMemberPairLike() && petDrawVOs.get(0).getIsMemberLike()) {
			model.addAttribute("pairStatus", PetDrawStatusEnum.DRAWN_SUCCESS.getCode());
			return "front-end/article/petdraw-success";
		}

		return "front-end/article/forum-petdraw";
	}


	@GetMapping("/showpairing/update")
	public String showpairingUpdate(ModelMap model) {

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		return "front-end/article/getOne-petdraw"; // 查詢完成後轉交update_emp_input.html
	}

	@GetMapping("/showpairing/like")
	public String showpairingLike(@RequestParam("petDrawId") String petDrawId, ModelMap model) {

		PetDrawVO petDrawVO = petDrawSvc.getOnePetDraw(Integer.valueOf(petDrawId));
		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("petDrawVO", petDrawVO);
		return "front-end/article/getOne-petdraw"; // 查詢完成後轉交update_emp_input.html
	}

	@GetMapping("/showpaired")
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
		int petDrawId = petDrawVO.getpetDrawId();

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

	public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		final int R = 6371; // 地球半径，单位为千米

		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c; // 单位千米

		return distance;
	}
}