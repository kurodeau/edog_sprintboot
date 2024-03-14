package com.cart.controller;

import java.util.*;
import java.io.IOException;
import java.sql.Timestamp;

import javax.validation.Valid;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.cart.service.CartService;
import com.product.model.ProductService;
import com.seller.service.SellerService;
import com.util.HttpResult;

@Controller
@RequestMapping("/front/buyer/cart")
public class CartnoController extends HttpServlet {

	@Autowired
	ProductService productSvc;

	@Autowired
	SellerService sellerSvc;

	@Autowired
	CartService cartService;

	
	//	用戶取出自己所有購物車資料 /front/buyer/cart/list
	@GetMapping("list")
	public String cartlist(String memberId, Model model) {
		// 先給定 memberId 以便測試
		memberId = "9";
		model.addAttribute("cartClassfi", cartService.getAllByMemberId(memberId)); 
		return "front-end/buyer/buyer-cart-list";
	}
	
	
	
	//	收到購物車的結帳請求, 把資料整理後上Redis /front/buyer/cart/cart_creat_order
	@RequestMapping(value = "cart_creat_order", method = RequestMethod.POST)	
	public ResponseEntity<?> yourControllerMethod(@RequestBody String jsonData) {
//        System.out.println("有到cart_creat_order contrller");
		JSONObject jsonObject = new JSONObject(jsonData);
        System.out.println(jsonData);
        if(jsonData==null || jsonData.equals("{}")){
            HttpResult<String> result = new HttpResult<>(400, "", "FUCKU");
            return ResponseEntity.badRequest().body(result);
        }
        
        //=================原本用來寫依照商品名稱分類的作法 start
//        List<String> sellerNames =  new ArrayList();
//          
//        System.out.println("---");
//        for (String seller : jsonObject.keySet()) {
//        	sellerNames.add(seller);
//            System.out.println("Seller: " + seller);
//            JSONArray productList = jsonObject.getJSONArray(seller);
//            for (int i = 0; i < productList.length(); i++) {
////            	CartVO cartVO  = new ProductVO();
//                JSONObject product = productList.getJSONObject(i);
//                int productId = product.getInt("productId");
//                int productQty = product.getInt("productQty");
//                System.out.println("productId: " + productId + ", productQty: " + productQty);
//            }
//        }
        //=================原本用來寫依照商品名稱分類的作法 end
        
        // 呼叫生成order Redis 資料的方法
        cartService.creatOrderByMemberId( jsonObject );
       
        
	    // 返回適當的響應
	    return ResponseEntity.ok().body(new HttpResult<>(200,"","success"));
	    
	    
	}
	

	// 更新特定一個商品編號的收藏狀態, 並回到我的收藏 /front/buyer/collection/switchState
//	@PostMapping("switchState") //改用POST
//	public String switchOneToCart(String memberId,@RequestParam("productId") String productId, Model model) {
//		// 先給定 memberId , productId 以便測試
//		System.out.println("到controller");
//		memberId = "9";
////		productId = "5";
//		model.addAttribute("collectionClassfi", cartService.switchStateByProductId(memberId, productId)); 
//		return "front-end/buyer/buyer-cart-list";
//	}
	

//	// 更新特定一個商品編號的收藏狀態, 並回到我的收藏 /front/buyer/collection/switchState
//	@PostMapping("switchState") //改用POST
//	public String switchOneTocart(String memberId,@RequestParam("productId") String productId, Model model) {
//		// 先給定 memberId , productId 以便測試
//		System.out.println("到controller");
//		memberId = "9";
////		productId = "5";
//		model.addAttribute("collectionClassfi", collectionService.switchStateByProductId(memberId, productId)); 
//		return "front-end/buyer/buyer-collection-list";
//	}
	
}
