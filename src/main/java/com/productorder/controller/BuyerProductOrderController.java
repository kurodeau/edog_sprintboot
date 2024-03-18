package com.productorder.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.buyer.service.BuyerService;
import com.orderdetails.model.OrderDetailsService;
import com.product.model.ProductService;
import com.productorder.model.ProductOrderService;
import com.productorder.model.ProductOrderVO;
import com.productorder.model.ShoppingCartDTO;
import com.seller.entity.SellerVO;
import com.seller.service.SellerService;
import com.util.HttpResult;
import com.buyer.entity.BuyerVO;
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
		
		
	//訂單結帳正式入口
		@GetMapping("order_checkout") 
		public String orderCheckout(Integer memberIdd, Model model, HttpSession session){

			
			
			
			
			SecurityContext secCtx = SecurityContextHolder.getContext();
	        Authentication authentication = secCtx.getAuthentication();
	        BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();

			
			
			
			
			model.addAttribute("cartClassfi", productOrderSvc.getAllByMemberId(buyerVO.getMemberId())); 
			return "front-end/buyer/buyer-order-checkout";
			
			
		}
		
	//訂單結帳測試用臨時入口
		@GetMapping("order_checkout111") //GoToCheckoutPage
		public String cartlist(Model model,HttpSession session) {
//			Integer memberId = session.getAttribute(memberId);
			Integer memberId = 8;
			model.addAttribute("cartClassfi", productOrderSvc.getAllByMemberId(memberId)); 
			return "front-end/buyer/buyer-order-checkout";
		}
		
		
		
	//訂單結帳按鈕  	
		@PostMapping("create_orders")
		public ResponseEntity<?> orderCheckout(@Valid @RequestBody ShoppingCartDTO shoppingCartDTO ,HttpSession session ) {
//		    Integer memberId =  session.getAttribute(memberId);
//			Integer memberId = 1;
			
			SecurityContext secCtx = SecurityContextHolder.getContext();
	        Authentication authentication = secCtx.getAuthentication();
	        BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
			
			
			
	        productOrderSvc.deletCartOnRedis(buyerVO.getMemberId());
	        productOrderSvc.deletOrderOnRedis(buyerVO.getMemberId());
	        
	        
	        
			return productOrderSvc.createOrders(shoppingCartDTO, buyerVO.getMemberId());
			
			
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
		
		
		
		/*
		 * 直接從商品詳細頁面點立刻購買按鈕用, /front/buyer/buyProductWithoutCart
		 * 再 Redis 上創立資料後, 回到前端再跳轉到訂單結帳畫面
		 */
		@RequestMapping(value = "buyProductWithoutCart", method = RequestMethod.POST)	
		public ResponseEntity<?> buyProductWithoutCart(@RequestBody String jsonData) {
			System.out.println("測試訊息:有進入buyProductWithoutCart 這個controller");
			JSONObject jsonObject = new JSONObject(jsonData);
	        System.out.println(jsonData);
	        if(jsonData==null || jsonData.equals("{}")){
	            HttpResult<String> result = new HttpResult<>(400, "", "FUCKU");
	            return ResponseEntity.badRequest().body(result);
	        }
	        // 呼叫生成order Redis 資料的方法
	        productOrderSvc.buyProductWithoutCart( jsonObject );
	        
		    // 返回適當的響應
		    return ResponseEntity.ok().body(new HttpResult<>(200,"","success"));
		}	

		
		}


