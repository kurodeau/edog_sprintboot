package com.petdraw.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.petdraw.model.PetDrawService;
import com.petdraw.model.PetDrawVO;

@Controller
@Validated
@RequestMapping("/petdraw")
public class PetDrawController {

	@Autowired
	private PetDrawService petDrawSvc;

	@GetMapping("addpetdraw")
	public String showaddpetdraw(ModelMap model) {
		PetDrawVO petDrawVO = new PetDrawVO();
		model.addAttribute("petDrawVO", petDrawVO);
		return "front-end/article/forum-petdraw";
	}

//    http://localhost:8080/servername/pairPets?=memberMainId=123456&memberPa
	@GetMapping("insert")
	public String addPetDraw(@ModelAttribute("petDrawVO") @Valid PetDrawVO petDrawVO, BindingResult bindingResult,
			ModelMap model) {
		if (bindingResult.hasErrors()) {
			// 如果有驗證錯誤，返回表單頁面
			return "front-end/article/addpetdraw";
		}

		// 呼叫服務層的方法進行新增
		petDrawSvc.addPetDraw(petDrawVO);

		// 添加成功的提示信息
		model.addAttribute("successMessage", "寵物配對成功！");

		// 重新導向到寵物配對列表頁面
		return "redirect:/article/forum-petdraw";
	}

	// 顯示寵物配對列表頁面
	@GetMapping("/petdraw/list")
	public String showPetDrawList(ModelMap model) {
		// 取得所有寵物配對的資料
		model.addAttribute("petDrawList", petDrawSvc.getAll());
		return "front-end/article/forum-petdraw";
	}
}