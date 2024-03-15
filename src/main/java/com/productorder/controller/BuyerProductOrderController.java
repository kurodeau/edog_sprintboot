package com.productorder.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.buyer.entity.BuyerVO;
import com.buyer.service.BuyerService;
import com.orderdetails.model.OrderDetailsService;
import com.orderdetails.model.OrderDetailsVO;
import com.product.model.ProductService;
import com.product.model.ProductVO;
import com.productorder.model.ProductInfoDTO;
import com.productorder.model.ProductOrderService;
import com.productorder.model.ProductOrderVO;
import com.productorder.model.ReceiverInfoDTO;
import com.productorder.model.ShoppingCartDTO;
import com.seller.entity.SellerVO;
import com.seller.service.SellerService;
import com.util.GenerateInvoiceNumber;

@Controller
@RequestMapping("/front/buyer")
public class BuyerProductOrderController {
	
		@Autowired
		ProductOrderService productOrderSvc;
		@Autowired
		ProductService productSvc;
		@Autowired
		OrderDetailsService orderDetailsSvc;
		@Autowired
		SellerService sellerSvc;
		@Autowired
		BuyerService buyerSvc;
		@Autowired
		ProductOrderService ProductOrderSvc;
		
		@PostMapping("order_checkout") 
		public String orderCheckout(Model model){
			
			//接過購物車的DTO顯示在結帳畫面
			
			
			return "front-end/buyer/buyer-order-checkout";
		}
		
		
		
	//臨時入口：跳轉訂單結帳畫面
//		@GetMapping("order_checkout111") 
//		public String orderCheckout111(Model model){
//	        return "front-end/buyer/buyer-order-checkout";
//	    }
		
	// 用戶取出自己所有購物車資料 /front/buyer/cart/list
		@GetMapping("order_checkout111")
		public String cartlist(String memberId, Model model) {
			// 先給定 memberId 以便測試
			memberId = "8";
			model.addAttribute("cartClassfi", productOrderSvc.getAllByMemberId(memberId)); 
			return "front-end/buyer/buyer-order-checkout";
		}
		
		
		
		
		@PostMapping("create_orders")
		public ResponseEntity<?> createOrders(@RequestBody ShoppingCartDTO shoppingCartDTO ) {
//			public ResponseEntity<?> createOrders(@RequestBody ShoppingCartDTO shoppingCartDTO, HttpSession session) {
//		    Integer memberId = (Integer) session.getAttribute("memberId");
			Integer memberId = 1;
		    shoppingCartDTO.getCarts().forEach(cart -> {
		        ProductOrderVO productOrder = new ProductOrderVO();
		        BuyerVO buyerVO = buyerSvc.getOneBuyer(memberId);
		        SellerVO sellerVO = sellerSvc.getById(cart.getSellerId());
		        ReceiverInfoDTO receiverInfo = cart.getReceiverInfo();

		        productOrder.setBuyerVO(buyerVO);
		        productOrder.setSellerVO(sellerVO);
		        productOrder.setReceiver(receiverInfo.getReceiver());
		        productOrder.setMobile(receiverInfo.getMobile());
		        productOrder.setContactAddress(receiverInfo.getContactAddress());
		        
		      //用來累加訂單明細裡商品的價格
				int actualPayment = 0;
				int detailsTotal = 0;
				
			  //int orderOrigPrice = actualPay + discount; //新增優惠券功能用 (//訂單總金額 = 買家實付金額 + 平台優惠折抵)
		      //新增訂單內容
				long currentTimeMillis = System.currentTimeMillis();
				Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
				Timestamp crCreateDate = currentTimestamp;
				productOrder.setOrderTime(crCreateDate);
				productOrder.setOrderStatus(1);
				int invoiceNumber = GenerateInvoiceNumber.generateInvoiceNumber();
				productOrder.setInvoiceNumber(invoiceNumber);
				productOrder.setIsDelivered(0);

		        Set<OrderDetailsVO> orderDetailsSet = new HashSet<>(); //建立訂單裡的訂單明細Set
		        for (ProductInfoDTO productInfo : cart.getProducts()) {
		            ProductVO productVO = productSvc.getOneProduct(productInfo.getProductId());
		            OrderDetailsVO orderDetails = new OrderDetailsVO(); //依據每個訂單Id新增每筆訂單明細
		            
		          //設定訂單明細的內容
		            orderDetails.setProductOrderVO(productOrder); // 關聯訂單
		            orderDetails.setProductVO(productVO); //關聯商品
		            orderDetails.setPurchaseQuantity(productInfo.getQuantity()); //設定購買數量
		            orderDetails.setIsCommented(false);
		            orderDetails.setIsEnable(true);
		            detailsTotal += productVO.getPrice().intValue() * orderDetails.getPurchaseQuantity(); //訂單明細金額加總
		            orderDetailsSet.add(orderDetails);
		        }
		        
		     // 檢查是否需要增加運費
			    int shippingFee = 0;
			    if (detailsTotal < 999) {
			        shippingFee = 70;
			    }
			    actualPayment += shippingFee;
			    int orderOrigPrice = actualPayment + detailsTotal;
			    productOrder.setSellerPaysShipping(0);//設定賣家負擔運費
			    productOrder.setMemberPaysShipping(shippingFee);//設置買家負擔運費
		        productOrder.setOrderDetailss(orderDetailsSet); // 關聯訂單明細
		        productOrder.setActualPay(actualPayment); //設置買家實付金額
		        productOrder.setOrderOrigPrice(orderOrigPrice); //設置訂單的總金額(訂單總金額 = 買家實付金額 + 平台優惠折抵)
		        productOrder.setOrderDetailss(orderDetailsSet); // 訂單關聯訂單明細

		        
		        
		        // 保存訂單及訂單明細
		        productOrderSvc.addProductOrder(productOrder);
		    });
		    return ResponseEntity.ok().body(Map.of("message", "Orders created successfully."));

//		    return ResponseEntity.ok("Orders created successfully.");
		}



		
	
		
//更改訂單狀態/////////////////////////////////////////

		
		@PostMapping("confirm")
		public String confirmProductOrder(@RequestParam("orderId") String orderId ,ModelMap model) {
			ProductOrderVO productOrderVO = productOrderSvc.getOneProductOrder(Integer.valueOf(orderId));
			productOrderVO.setOrderStatus(5);
			productOrderSvc.addProductOrder(productOrderVO); //將修改的資料存進資料庫
			
			List<ProductOrderVO> list = productOrderSvc.getAll();
			model.addAttribute("productOrderList",list);
			model.addAttribute("success" , "-(接受訂單成功)");
			return "redirect:/front/seller/productorder/sellerproductorderlistall";

		}
		
		@PostMapping("shipping")
		public String shippingProduct(@RequestParam("orderId") String orderId ,ModelMap model) {
			ProductOrderVO productOrderVO = productOrderSvc.getOneProductOrder(Integer.valueOf(orderId));
			productOrderVO.setOrderStatus(6);
			productOrderSvc.addProductOrder(productOrderVO); //將修改的資料存進資料庫
			
			List<ProductOrderVO> list = productOrderSvc.getAll();
			model.addAttribute("productOrderList",list);
			model.addAttribute("success" , "-(接受訂單成功)");
			return "redirect:/front/seller/productorder/sellerproductorderlistall";

		}

		@PostMapping("cancel")
		public String cancelProductOrder(@RequestParam("orderId") String orderId ,ModelMap model) {
			ProductOrderVO productOrderVO = productOrderSvc.getOneProductOrder(Integer.valueOf(orderId));
			productOrderVO.setOrderStatus(3);
			productOrderSvc.addProductOrder(productOrderVO); //將修改的資料存進資料庫
			List<ProductOrderVO> list = productOrderSvc.getAll();
			model.addAttribute("productOrderList",list);
			model.addAttribute("success" , "-(接受訂單成功)");
			return "redirect:/front/seller/productorder/sellerproductorderlistall";

		}
		
		
		
		
		
		
		/*
		 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
		 */
		@PostMapping("insert")
		public String insert(@Valid ProductOrderVO productOrderVO, BindingResult result, ModelMap model) throws IOException {
			
			
			if (result.hasErrors() ) {
					return "back-end/productOrder/addProductOrder";
				}
				
			/*************************** 2.開始新增資料 *****************************************/
			// EmpService productOrderSvc = new EmpService();
			productOrderSvc.addProductOrder(productOrderVO);
			/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
			List<ProductOrderVO> list = productOrderSvc.getAll();
			model.addAttribute("productOrderListData", list);
			model.addAttribute("success", "- (新增成功)");
			return "redirect:/productOrder/listAllProductOrder"; // 新增成功後重導至IndexController_inSpringBoot.java的第50行@GetMapping("/emp/listAllEmp")
		}

		/*
		 * This method will be called on listAllEmp.html form submission, handling POST request
		 */
		@PostMapping("getOne_For_Update")
		public String getOne_For_Update(@RequestParam("orderId") String orderId, ModelMap model) {
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
			/*************************** 2.開始查詢資料 *****************************************/
			// EmpService productOrderSvc = new EmpService();
			ProductOrderVO productOrderVO = productOrderSvc.getOneProductOrder(Integer.valueOf(orderId));

			/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
			model.addAttribute("productOrderVO", productOrderVO);
			return "back-end/productOrder/update_productOrder_input"; // 查詢完成後轉交update_emp_input.html
		}

		

		/*
		 * This method will be called on listAllEmp.html form submission, handling POST request
		 */
		@PostMapping("delete")
		public String delete(@RequestParam("sellerId") String sellerId, ModelMap model) {
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
			/*************************** 2.開始刪除資料 *****************************************/
			// EmpService productOrderSvc = new EmpService();
			productOrderSvc.deleteProductOrder(Integer.valueOf(sellerId));
			/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
			List<ProductOrderVO> list = productOrderSvc.getAll();
			model.addAttribute("productOrderListData", list);
			model.addAttribute("success", "- (刪除成功)");
			return "back-end/productOrder/listAllProductOrder"; // 刪除完成後轉交listAllEmp.html
		}

		
		}


