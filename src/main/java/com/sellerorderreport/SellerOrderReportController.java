package com.sellerorderreport;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/report/api/orders/{sellerIdstr}")
	public ResponseEntity<?> allOrders(@PathVariable String sellerIdstr) {

		try {

			Integer sellerTargetId = null;

			if (sellerIdstr.equals("0")) {
				SecurityContext secCtx = SecurityContextHolder.getContext();
				Authentication authentication = secCtx.getAuthentication();
				if (authentication != null && authentication.getPrincipal() instanceof SellerVO) {
					SellerVO sellerTargetVO = (SellerVO) authentication.getPrincipal();
					sellerTargetId = Integer.valueOf(sellerIdstr);
				} else {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body(new HttpResult<>(500, null, "找不到該用戶"));

				}

			} else {
				sellerTargetId = Integer.valueOf(sellerIdstr);
			}

			final Integer finalSellerTargetId = sellerTargetId; // Make sellerTargetId effectively final

			List<ProductOrderVO> productOrderVOs = productOrderService.getAll();


			List<ProductOrderVO> filteredOrders = productOrderVOs.stream()
					.filter(order -> order.getSellerId() == finalSellerTargetId)
					.sorted((order1, order2) -> order1.getOrderTime().compareTo(order2.getOrderTime()))
					.collect(Collectors.toList());

//			for (ProductOrderVO filteredOrder : filteredOrders) {
//				System.out.println(filteredOrder.getOrderTime());
//			}

			ZoneId taipeiZone = ZoneId.of("Asia/Taipei");
			LocalDateTime startTime = filteredOrders.get(0).getOrderTime().toInstant().atZone(taipeiZone)
					.toLocalDateTime();
			LocalDateTime endTime = filteredOrders.get(filteredOrders.size() - 1).getOrderTime().toInstant()
					.atZone(taipeiZone).toLocalDateTime();
			// LocalDateTime startTime = filteredOrders.get(0).getOrderTime().toLocalTime();

			List<OrderDiagramDTO> orderReports = generateOrderReports(startTime, endTime, filteredOrders);

			for (OrderDiagramDTO orderReport : orderReports) {
//				System.out.println(orderReport.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME) + "count"+ orderReport.getOrderCount());
				String isoDateTime = orderReport.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME);
			}

			List<Map<String, Object>> jsonList = new ArrayList<>();
			for (OrderDiagramDTO orderReport : orderReports) {
				String isoDateTime = orderReport.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME);
				Map<String, Object> jsonMap = new HashMap<>();
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

	@GetMapping("/report/test")
	public ResponseEntity<?> generateReport(Model model) throws IOException {
		
		LocalDate startTime = LocalDate.of(2022, 1, 1);
		LocalDate endTime = LocalDate.of(2022, 12, 31);
		fileSvc.generateFiles(startTime, endTime,1);
		return ResponseEntity.ok().build();
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
