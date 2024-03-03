package com.sellerLv.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sellerLv.entity.SellerLvVO;
import com.sellerLv.service.SellerLvService;

@Controller
@RequestMapping("/back/sellerLv")
public class SellerLvController extends HttpServlet {

	
	@Autowired
	private SellerLvService sellerLvSvc;

	
	@ModelAttribute("sellerLvListData")
	protected List<SellerLvVO> referenceListData() {
		List<SellerLvVO> list = sellerLvSvc.getAll();
//		System.out.println("==============================");
//		list.forEach(data -> System.out.println(data));
//		System.out.println("==============================");
		return list;
	}
	
	
	@GetMapping("list")
	public String listAllSellerLv(ModelMap model) {
		return "back-end/back-sellerLv-list";
	}
	
	
	
	
	@GetMapping("add")
	public String addSellerLv(ModelMap model) {
		SellerLvVO sellerLvVO = new SellerLvVO();
		model.addAttribute("sellerLvVO", sellerLvVO);
		return "back-end/back-sellerLv-add";
	}
	
	
	@GetMapping("getOne_For_Update")
	public String getOneSeller(@RequestParam("id")@NonNull String sellerLvId, ModelMap model) {
		
		
		SellerLvVO sellerLvVO = null;
		try {
		 sellerLvVO = sellerLvSvc.getById(Integer.valueOf(sellerLvId));
		
		} catch (NumberFormatException e) {
	        model.addAttribute("errorMessage", "Invalid sellerId format");
	        return "errorPage";
		} catch (Exception e){
			e.printStackTrace();
		}
//		System.out.println("==============XXXXXXXXXXXXXX");
//		System.out.println("getOne_For_Update");
//		System.out.println(sellerLvVO);
//		System.out.println("==============XXXXXXXXXXXXXX");
		model.addAttribute("sellerLvVO",sellerLvVO);
		return "back-end/back-sellerLv-edit";
	
	}
	
	
	@PostMapping("update")
	public String updateSellerLv(@Valid @NonNull SellerLvVO sellerLvVO, BindingResult result, ModelMap model)
			throws IOException {

		if (result.hasErrors()) {
//			System.out.println("==============XXXXXXXXXXXXXX");
//			System.out.println("updateSeSellerLvLv;
//			System.out.println(result);
//			System.out.println("==============XXXXXXXXXXXXXX");
			return "back-end/back-sellerLv-edit";
		}

		sellerLvSvc.updateSellerLv(sellerLvVO);
		model.addAttribute("success", "- (修改成功)");
		model.addAttribute("sellerLvVO", sellerLvVO);
		
		return "redirect:/back/sellerLv/list";
	}
	
	@DeleteMapping("delete")
	public String delete(@RequestParam("id") @NonNull String sellerLvId, ModelMap model) throws IOException {
		
	
		try {
	        Integer valueOf = Integer.valueOf(sellerLvId);
	        // 在這裡處理轉換成功的情況
	        sellerLvSvc.deleteSellerLv(valueOf);
			return "redirect:/back/sellerLv/list";
	    } catch (NumberFormatException e) {
	        // 轉換失敗的情況，可以進行相應的處理，例如返回錯誤信息
	        model.addAttribute("errorMessage", "Invalid sellerLvId format");
	        return "errorPage";
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	return "errorPage";
	    }
		// 一定要用Redirect，不然會會導致資料重複送
		// 新增成功後重導至IndexController_inSpringBoot.java的第50行@GetMapping("/user/listAllUser")

	}
	
	@PostMapping("insert")
	public String insert (@Valid @NonNull SellerLvVO sellerLvVO ,  BindingResult result, ModelMap model) throws IOException {
		
		if (result.hasErrors()) {
			return "back-end/back-sellerLv-add";
		}
		sellerLvSvc.addSellerLv(sellerLvVO);
		
		List<SellerLvVO> list = sellerLvSvc.getAll();
		model.addAttribute("sellerLvListData", list);
		model.addAttribute("success", "success");
		return "redirect:/back/sellerLv/list";

	}


}
