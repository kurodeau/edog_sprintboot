package com.sellerLv.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seller.entity.SellerVO;
import com.seller.service.SellerService;
import com.sellerLv.entity.SellerLvVO;
import com.sellerLv.service.SellerLvService;

@Controller
@RequestMapping("/front/sellerLv")
public class SellerLvControllerFront extends HttpServlet {

	@Autowired
	SellerService sellerSvc;

	@Autowired
	SellerLvService sellerLvSvc;

	@ModelAttribute("sellerLvListData")
	protected List<SellerLvVO> referenceListData() {
		List<SellerLvVO> list = sellerLvSvc.getAll();
//			System.out.println("==============================");
//			list.forEach(data -> System.out.println(data));
//			System.out.println("==============================");
		return list;
	}

	@GetMapping("edit")
	public String selleredit(Model model, HttpSession session) {

		// SecCtx取出資料
		SecurityContext secCtx = SecurityContextHolder.getContext();
		System.out.println("secCtx" + secCtx);

		Authentication authentication = secCtx.getAuthentication();
		SellerVO sellerVO = (SellerVO) authentication.getPrincipal();
		model.addAttribute("sellerVO", sellerVO);

		return "front-end/seller/seller-sellerLv-edit";
	}

	@PostMapping("update")
	public String sellerupdate(@Valid @NonNull SellerVO sellerVO, Model model, BindingResult result) {
//			System.out.println(sellerVO);

		if (result.hasErrors()) {

			return "front-end/seller/seller-sellerLv-edit";
		}
		
		// secCtx存入資料
		sellerSvc.updateSecureContext(sellerVO);
		model.addAttribute("sellerVO", sellerVO);
		
		
		sellerSvc.updateSeller(sellerVO);

		return "redirect:/front/seller/main" + "?updateSuccess=true";
	}

	@PutMapping("edit/api/v1/sellers/{id}")
	@ResponseBody
	public ResponseEntity<?> ajaxUpdate(@PathVariable("id") Integer targetSellerId,
			@RequestBody Map<String, Object> requestData, HttpSession session) {
		System.out.println(targetSellerId);
		System.out.println(requestData);
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			// Convert requestData map to JSON
			String jsonData = objectMapper.writeValueAsString(requestData);

			// Deserialize JSON to MyData object
			UpdateData updateData = objectMapper.readValue(jsonData, UpdateData.class);
			// System.out.println(updateData);

			Integer targetLvId = updateData.data.getTargetSellerLvId();
			SellerLvVO sellerLvVO = sellerLvSvc.getById(targetLvId);
			SellerVO sellerUpdateVO = sellerSvc.getById(targetSellerId);
			sellerUpdateVO.setSellerLvId(sellerLvVO);
			sellerSvc.updateSeller(sellerUpdateVO);

			List<SellerLvVO> firstThreeElements = null;
			List<SellerLvVO> sellerLvVOList = sellerLvSvc.getAll();
			if (sellerLvVOList.size() >= 3) {
				firstThreeElements = sellerLvVOList.subList(0, 3);

			}
			System.out.println(sellerLvVOList);

			ResponseData responseData = new ResponseData(targetSellerId, targetLvId, firstThreeElements);
			String jsonString = objectMapper.writeValueAsString(responseData);

			System.out.println(jsonString);

			// SECURIT設定資訊
			sellerSvc.updateSecureContext(sellerUpdateVO);

			
			return ResponseEntity.status(HttpStatus.OK).body(jsonString);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("解析 SellerLvID 时出错: ");
	}

	@PostMapping("edit/ajax/all")
	@ResponseBody
	public ResponseEntity<?> ajaxAll(@RequestBody Map<String, Object> requestData, HttpSession session) {
		try {
			System.out.println(requestData);
			ObjectMapper objectMapper = new ObjectMapper();

			// Convert requestData map to JSON
			String jsonData = objectMapper.writeValueAsString(requestData);

			// Deserialize JSON to MyData object
			MyData myData = objectMapper.readValue(jsonData, MyData.class);

			// Now you can use myData object, which contains the data property
			List<Integer> dataList = myData.getData();
			List<SellerLvVO> targetList = new ArrayList<>();

			// Attempt to parse strings into integers
			try {
				List<Integer> sellerLvTargetId = dataList;
				System.out.println(sellerLvTargetId);

				for (int i = 0; i < sellerLvTargetId.size(); i++) {
					SellerLvVO sellerLvVO = sellerLvSvc.getById(sellerLvTargetId.get(i));
					if (sellerLvVO != null) {
						targetList.add(sellerLvVO);
					}
				}

				System.out.println(targetList);

				Integer targetSellerId = null;
				SellerVO sellerTargetVO = null;
				Integer sellerLvId = null;

				// SESSOIN取得資訊
				SellerVO sellerVOFromSession = (SellerVO) session.getAttribute("sellerVO");
				if (sellerVOFromSession != null) {
					targetSellerId = sellerVOFromSession.getSellerId();
					sellerTargetVO = sellerSvc.getById(targetSellerId);
					sellerLvId = sellerTargetVO.getSellerLvId().getSellerLvId();
				}
				// SECURITY取得資訊
				SecurityContext securityContext = SecurityContextHolder.getContext();
				Authentication authentication = securityContext.getAuthentication();
				SellerVO sellerVO = (SellerVO) authentication.getPrincipal();
				if (sellerVO != null) {
					targetSellerId = sellerVO.getSellerId();
					sellerTargetVO = sellerVO;
					sellerLvId = sellerVO.getSellerLvId().getSellerLvId();
				}

				// @JsonIgnore // Prevent including SellerLvVO reference during serialization
				ResponseData responseData = new ResponseData(targetSellerId, sellerLvId, targetList);
				String jsonString = objectMapper.writeValueAsString(responseData);

				return ResponseEntity.status(HttpStatus.OK).body(jsonString);

			} catch (NumberFormatException e) {
				System.out.println("解析 SellerLvID 时出错: " + e.getMessage());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("解析 SellerLvID 时出错: " + e.getMessage());
			}
		} catch (Exception e) {
			System.out.println("处理请求时出错: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("处理请求时出错: " + e.getMessage());
		}

	}

	static class UpdateData {
		private Data data;

		public Data getData() {
			return data;
		}

		public void setData(Data data) {
			this.data = data;
		}

		public static class Data {
			private int targetSellerId;
			private int targetSellerLvId;

			public int getTargetSellerId() {
				return targetSellerId;
			}

			public void setTargetSellerId(int targetSellerId) {
				this.targetSellerId = targetSellerId;
			}

			public int getTargetSellerLvId() {
				return targetSellerLvId;
			}

			public void setTargetSellerLvId(int targetSellerLvId) {
				this.targetSellerLvId = targetSellerLvId;
			}
		}
	}

	static class MyData {
		private List<Integer> data;

		public MyData() {
		}

		public MyData(List<Integer> data) {
			this.data = data;
		}

		public List<Integer> getData() {
			return data;
		}
	}

	// Define ResponseData class outside the method
	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class ResponseData {
		private Integer sellerId;
		private Integer sellerLvId;
		private List<SellerLvVO> targetList;

		public ResponseData() {
			// Default constructor
		}

		public Integer getSellerId() {
			return sellerId;
		}

		public void setSellerId(Integer sellerId) {
			this.sellerId = sellerId;
		}

		public Integer getSellerLvId() {
			return sellerLvId;
		}

		public void setSellerLvId(Integer sellerLvId) {
			this.sellerLvId = sellerLvId;
		}

		public ResponseData(Integer sellerId, Integer sellerLvId, List<SellerLvVO> targetList) {
			super();
			this.sellerId = sellerId;
			this.sellerLvId = sellerLvId;
			this.targetList = targetList;
		}

		public List<SellerLvVO> getTargetList() {
			return targetList;
		}

		public void setTargetList(List<SellerLvVO> targetList) {
			this.targetList = targetList;
		}

	}

}
