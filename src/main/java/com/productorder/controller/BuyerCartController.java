//package com.productorder.controller;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import com.productorder.model.ProductOrderService;
//import com.util.HttpResult;
//
////@RestController
//	@Controller
//	@RequestMapping("/front/buyer")
//	public class BuyerCartController {
//		
//			@Autowired
//			ProductOrderService productOrderSvc;
//			
////			@Autowired
////			DeptService deptSvc;
////			SellerService sellerService;
////			BuyerService buyerService;
////			CouponService couponService;
//
//			
//			@RequestMapping(value = "order_checkout", method = RequestMethod.POST)
////			public ResponseEntity<?> yourControllerMethod(@RequestBody String jsonData) {
//		        JSONObject jsonObject = new JSONObject(jsonData);
//		        System.out.println(jsonData);
//		        if(jsonData==null || jsonData.equals("{}")){
//		            HttpResult<String> result = new HttpResult<>(400, "", "FUCKU");
//		        
//		            return ResponseEntity.badRequest().body(result);
//
//		        }
//		        
//		        List<String> sellerNames =  new ArrayList();
//		        
//		        
//		        System.out.println("---");
//		        for (String seller : jsonObject.keySet()) {
//		        	sellerNames.add(seller);
//		            System.out.println("Seller: " + seller);
//		            JSONArray productList = jsonObject.getJSONArray(seller);
//		            for (int i = 0; i < productList.length(); i++) {
////		            	CartVO cartVO  = new ProductVO();
//		                JSONObject product = productList.getJSONObject(i);
//		                int productId = product.getInt("productId");
//		                int productQty = product.getInt("productQty");
//		                System.out.println("Product ID: " + productId + ", Quantity: " + productQty);
//		            }
//		        }
//		        
////				System.out.println("Received data from AJAX request: " + datamap.getClass() );
////				
////				System.out.println("datamap到底有沒有資料?"+ datamap.isEmpty());
////				System.out.println( datamap );
//
//				// 遍歷外層的 Map
////			    for (Map.Entry<String, List<Map<String, String>>> entry : datamap.entrySet()) {
////			        String sellerId = entry.getKey();
////			        List<Map<String, Integer>> productList = entry.getValue();
////			        
////			        System.out.println("=======測試======Seller ID: " + sellerId);
////			        
////			        // 遍歷內層的 List<Map<String, Integer>>
////			        for (Map<String, Integer> productMap : productList) {
////			            // 遍歷內層的 Map
////			            for (Map.Entry<String, Integer> productEntry : productMap.entrySet()) {
////			                String productId = productEntry.getKey();
////			                Integer productQty = productEntry.getValue();
////			                
////			                System.out.println("Product ID: " + productId + ", Quantity: " + productQty);
////			            }
////			        }
////			    }
//			    
//			    
//			    
//			    // 返回適當的響應
//			    return ResponseEntity.ok().body(new HttpResult<>(200,"","success"));
//			    
//			    
//			}
//}
