package com.sellerorderreport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.productorder.model.ProductOrderService;
import com.productorder.model.ProductOrderVO;
import com.seller.entity.SellerVO;
import com.util.HttpResult;

@Controller
@RequestMapping("/front/seller")
public class SellerOrderReportController {

	@Autowired
	ProductOrderService productOrderService;

	@Autowired
	FileService fileSvc;

	@GetMapping("/report")
	public String exportSellerExport() throws IOException {

		return "/front-end/seller/seller-report";
	}

	@GetMapping("/report/api/orders")
	public ResponseEntity<?> allOrders() {

		SecurityContext secCtx = SecurityContextHolder.getContext();
		Authentication authentication = secCtx.getAuthentication();
		SellerVO sellerVO = null;
		Integer sellerTargetId = null;
		if (authentication != null && authentication.getPrincipal() instanceof SellerVO) {
			sellerVO = (SellerVO) authentication.getPrincipal();
			sellerTargetId = sellerVO.getSellerId();
		}

		System.out.println(sellerTargetId);

		try {

			if (sellerTargetId == null) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new HttpResult<>(500, null, "找不到該用戶"));
			}

			final Integer finalSellerTargetId = sellerTargetId; // Make sellerTargetId effectively final

			List<ProductOrderVO> productOrderVOs = productOrderService.findBySellerId(finalSellerTargetId);

			// !!!! 注意stream內部一定要用固定常數
			List<ProductOrderVO> filteredOrders = productOrderVOs.stream()
					.sorted((order1, order2) -> order1.getOrderTime().compareTo(order2.getOrderTime()))
					.collect(Collectors.toList());

			List<OrderDiagramDTO> orderReports = new ArrayList<>();
			List<Map<String, Object>> jsonList = new ArrayList<>();

			// 假如完全沒有資料
			if (filteredOrders.size() == 0) {
				OrderDiagramDTO orderDiagramDTO = new OrderDiagramDTO();
				orderDiagramDTO.setOrderCount(0);
				orderDiagramDTO.setTimestamp(LocalDateTime.now());
				orderReports.add(orderDiagramDTO);

				Map<String, Object> jsonMap = new HashMap<>();

				String isoDateTime = orderDiagramDTO.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME);
				jsonMap.put("timestamp", isoDateTime);
				jsonMap.put("orderCount", orderDiagramDTO.getOrderCount());
				jsonList.add(jsonMap);

				System.out.println(jsonList);
				return ResponseEntity.status(HttpStatus.OK).body(new HttpResult<>(200, jsonList, "該用戶無資料"));
			}

			// 假如有資料
			ZoneId taipeiZone = ZoneId.of("Asia/Taipei");
			LocalDateTime startTime = filteredOrders.get(0).getOrderTime().toInstant().atZone(taipeiZone)
					.toLocalDateTime();
			LocalDateTime endTime = filteredOrders.get(filteredOrders.size() - 1).getOrderTime().toInstant()
					.atZone(taipeiZone).toLocalDateTime();
			// LocalDateTime startTime = filteredOrders.get(0).getOrderTime().toLocalTime();

			orderReports = generateOrderReports(startTime, endTime, filteredOrders);

			for (OrderDiagramDTO orderReport : orderReports) {
//				System.out.println(orderReport.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME) + "count"+ orderReport.getOrderCount());
				String isoDateTime = orderReport.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME);
			}

			jsonList = new ArrayList<>();
			for (OrderDiagramDTO orderReport : orderReports) {
				Map<String, Object> jsonMap = new HashMap<>();
				String isoDateTime = orderReport.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME);

				jsonMap.put("timestamp", isoDateTime);
				jsonMap.put("orderCount", orderReport.getOrderCount());
				jsonList.add(jsonMap);
			}

			JSONObject responseData = new JSONObject();
			responseData.put("status", 200);
			responseData.put("data", jsonList);
			responseData.put("msg", "正常");

			System.out.println(responseData.toString());
			return ResponseEntity.ok().body(responseData.toString());

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HttpResult<>(500, null, "內部錯誤"));
		}

//		try {
//			
//			SecurityContext secCtx = SecurityContextHolder.getContext();
//			Authentication authentication = secCtx.getAuthentication();
//			if (authentication != null && authentication.getPrincipal() instanceof SellerVO) {
//				SellerVO sellerTargetVO = (SellerVO) authentication.getPrincipal();
//
//				Integer sellerTargetId = Integer.valueOf(sellerIdstr); 
//				
//				List<ProductOrderVO> productOrderVOs = productOrderService.getAll();
//
//				
//				List<ProductOrderVO> filteredOrders = productOrderVOs.stream()
//						.filter(order -> order.getSellerId() ==sellerTargetId)
//						.collect(Collectors.toList());
//				for (ProductOrderVO filteredOrder : filteredOrders) {
//					System.out.println(filteredOrder);
//				}
//
//				return ResponseEntity.ok().body(new HttpResult<>(200, null, "正常"));
//			} else {
//				return ResponseEntity.badRequest().body(new HttpResult<>(400, null, "請求格式不對"));
//			}
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HttpResult<>(500, null, "內部錯誤"));
//		}
	}

	@Autowired
	private ResourceLoader resourceLoader;

	@PostMapping("/report/download")
	public ResponseEntity<?> downloadReport(Model model, @RequestBody String jsonStr , HttpServletRequest req) throws IOException {

		String startTimeStr = null;
		String endTimeStr = null;
		try {

			JSONObject jsonObj = new JSONObject(jsonStr);
			startTimeStr = jsonObj.getString("startTime");
			endTimeStr = jsonObj.getString("endTime");
		} catch (JSONException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HttpResult<>(400, null, "資料包裝錯誤"));
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate startTimeDate = LocalDate.parse(startTimeStr, formatter);
		LocalDate endTimeDate = LocalDate.parse(endTimeStr, formatter);
		System.out.println(jsonStr);

		
		HttpSession session = req.getSession();
		SellerVO  sellerVO = (SellerVO) session.getAttribute("sellerVO");
		String filename = fileSvc.generateFiles(startTimeDate, endTimeDate, sellerVO.getSellerId());
		
		System.out.println(filename);
		System.out.println(jsonStr);


		Resource resource = resourceLoader.getResource("classpath:static/" + filename);

		InputStream in = resource.getInputStream();
		ByteArrayOutputStream byOut = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = in.read(buffer)) != -1) {
			byOut.write(buffer, 0, length);
		}

		byte[] byteFileContent = byOut.toByteArray();

		return ResponseEntity.ok()
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename).body(byteFileContent);
	}

	public List<OrderDiagramDTO> generateOrderReports(LocalDateTime startTime, LocalDateTime endTime,
			List<ProductOrderVO> filteredOrders) {
		LocalDate startDate = startTime.toLocalDate();
		LocalDate endDate = endTime.toLocalDate().plusDays(1);

		List<OrderDiagramDTO> orderReports = new ArrayList<>();

		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			LocalDateTime midnight = date.atStartOfDay().plusNanos(1);
			LocalDateTime nextMidnight = date.plusDays(1).atStartOfDay();
			long orderCount = filteredOrders.stream()
					.filter(order -> order.getOrderTime().toLocalDateTime().isAfter(midnight)
							&& order.getOrderTime().toLocalDateTime().isBefore(nextMidnight))
					.count();

			OrderDiagramDTO orderReport = new OrderDiagramDTO(midnight, (int) orderCount);
			orderReports.add(orderReport);
		}

		return orderReports;
	}

}
