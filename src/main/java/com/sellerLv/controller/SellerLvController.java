package com.sellerLv.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;

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

	@GetMapping("new")
	public String addSellerLv(ModelMap model) {
		SellerLvVO sellerLvVO = new SellerLvVO();

		sellerLvVO.setLvName("Gold"); // 设置等级名
		sellerLvVO.setPlatformCommission(BigDecimal.valueOf(5.0)); // 平台佣金
		sellerLvVO.setAdAllowType(2); // 广告允许类型
		sellerLvVO.setIsExportGoldflow(true); // 是否导出金流
		sellerLvVO.setFreightSub(3); // 运费减免
		sellerLvVO.setReturnSubPerMonth(5); // 每月退货数
		sellerLvVO.setIsShowPriority(true); // 是否显示优先级
		sellerLvVO.setShelvesNumber(10); // 货架数量

		model.addAttribute("sellerLvVO", sellerLvVO);
		return "back-end/back-sellerLv-new";
	}

	@GetMapping("getOne_For_Update")
	public String getOneSeller(@RequestParam("id") @NonNull String sellerLvId, ModelMap model) {

		SellerLvVO sellerLvVO = null;
		try {
			sellerLvVO = sellerLvSvc.getById(Integer.valueOf(sellerLvId));

		} catch (NumberFormatException e) {
			model.addAttribute("errorMessage", "Invalid sellerId format");
			return "errorPage";
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("==============XXXXXXXXXXXXXX");
//		System.out.println("getOne_For_Update");
//		System.out.println(sellerLvVO);
//		System.out.println("==============XXXXXXXXXXXXXX");
		model.addAttribute("sellerLvVO", sellerLvVO);
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
    @ResponseBody
    public Map<String, String> delete(@RequestParam("id") @NonNull String sellerLvId, ModelMap model) {
        Map<String, String> response = new HashMap<>();

        try {
            Integer valueOf = Integer.valueOf(sellerLvId);
            // 处理转换成功的情况
            sellerLvSvc.deleteSellerLv(valueOf);

            response.put("success", "true");
            response.put("message", "删除成功");
        } catch (NumberFormatException e) {
        	response.put("success", "false");
            response.put("message", "Invalid sellerLvId format");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "false");
            response.put("message", "删除失败");
        }

        return response;
    }

	@PostMapping("insert")
	public String insert(@Valid @NonNull SellerLvVO sellerLvVO, BindingResult result, ModelMap model)
			throws IOException {

		if (result.hasErrors()) {
			return "back-end/back-sellerLv-new";
		}
		sellerLvSvc.addSellerLv(sellerLvVO);

		List<SellerLvVO> list = sellerLvSvc.getAll();
		model.addAttribute("sellerLvListData", list);
		model.addAttribute("success", "success");
		return "redirect:/back/sellerLv/list";

	}

}
