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

import com.buyer.entity.BuyerVO;
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
	public String cartlist(Model model) {
		// 先給定 memberId 以便測試
		String memberId = "9"; //測試有登入, 預設值
		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
        memberId = String.valueOf(buyerVO.getMemberId());        
        
        System.out.println("測試訊息:陳列出"+memberId+"的購物車");

        model.addAttribute("cartClassfi", cartService.getAllByMemberId(memberId)); 
		return "front-end/buyer/buyer-cart-list";
	}
	
	
	
	//	收到購物車的結帳請求, 把資料整理後上Redis /front/buyer/cart/cart_creat_order
	@RequestMapping(value = "cart_creat_order", method = RequestMethod.POST)	
	public ResponseEntity<?> cartCreatOrder(@RequestBody String jsonData) {
//      System.out.println("有到cart_creat_order contrller");
		JSONObject jsonObject = new JSONObject(jsonData);
        System.out.println(jsonData);
        if(jsonData==null || jsonData.equals("{}")){
            HttpResult<String> result = new HttpResult<>(400, "", "至少要選擇一項要結帳的商品");
            return ResponseEntity.badRequest().body(result);
        }
              
        // 呼叫生成order Redis 資料的方法
        cartService.creatOrderByMemberId( jsonObject );
        
	    // 返回適當的響應
	    return ResponseEntity.ok().body(new HttpResult<>(200,"","success"));
	}
	
	

	// 移除特定一個商品編號的購物車狀態(所有數量清空), 並回到我的購物車 /front/buyer/cart/removeProductFromList
	@PostMapping("removeProductFromList") //改用POST
	public ResponseEntity<?> removeProductFromList(@RequestBody String jsonData, Model model) {
		// 從 JSON 取出資料
		JSONObject jsonObject = new JSONObject(jsonData);
//      System.out.println("測試訊息:有收到jsonData="+jsonData);
		if(jsonData==null || jsonData.equals("{}")){
          HttpResult<String> result = new HttpResult<>(400, "", "請正確選擇要加入購物車的數量");
          return ResponseEntity.badRequest().body(result);
		}
		String productId = jsonObject.getString("productId");
		
		String memberId = "9"; //測試有登入, 預設值
		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
        memberId = String.valueOf(buyerVO.getMemberId());        
        
        System.out.println("測試訊息:將memberId="+memberId+"的購物車移除productId="+productId);
		
		
		model.addAttribute("collectionClassfi", cartService.removeOneFromCart(memberId, productId)); 
		return ResponseEntity.ok().body(new HttpResult<>(200,"","success"));
	}
	
	

	// 增加特定一個商品編號的購物車數量, 並回到我的購物車 /front/buyer/cart/removeProductFromList
	@PostMapping("addOneNum")
	public ResponseEntity<?> addOneNum(@RequestBody String jsonData, Model model) {
		// 從 JSON 取出資料
		JSONObject jsonObject = new JSONObject(jsonData);
//      System.out.println("測試訊息:有收到jsonData="+jsonData);
		if(jsonData==null || jsonData.equals("{}")){
          HttpResult<String> result = new HttpResult<>(400, "", "數量異常,請再試一次");
          return ResponseEntity.badRequest().body(result);
		}
		String productId = jsonObject.getString("productId");
		
		String memberId = "9"; //測試有登入, 預設值
		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
        memberId = String.valueOf(buyerVO.getMemberId());        
        
        System.out.println("測試訊息:將memberId="+memberId+"的購物車增加productId="+productId+"一個");
		
		
		model.addAttribute("collectionClassfi", cartService.addOneNum(memberId, productId)); 
		return ResponseEntity.ok().body(new HttpResult<>(200,"","success"));
	}	
	
	

	// 減少或移除特定一個商品編號的購物車數量, 並回到我的購物車 /front/buyer/cart/removeProductFromList
	@PostMapping("subOneNum")
	public ResponseEntity<?> subOneNum(@RequestBody String jsonData, Model model) {
		// 從 JSON 取出資料
		JSONObject jsonObject = new JSONObject(jsonData);
//      System.out.println("測試訊息:有收到jsonData="+jsonData);
		if(jsonData==null || jsonData.equals("{}")){
          HttpResult<String> result = new HttpResult<>(400, "", "數量異常,請再試一次");
          return ResponseEntity.badRequest().body(result);
		}
		String productId = jsonObject.getString("productId");
		
		String memberId = "9"; //測試有登入, 預設值
		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
        memberId = String.valueOf(buyerVO.getMemberId());        
        
        System.out.println("測試訊息:將memberId="+memberId+"的購物車增加productId="+productId+"一個");
		
		
		model.addAttribute("collectionClassfi", cartService.subOneNum(memberId, productId)); 
		return ResponseEntity.ok().body(new HttpResult<>(200,"","success"));
	}	
	
	
	
	//	收到商品詳細加入購物車的請求, 把資料整理後上Redis /front/buyer/cart/addToCartFromProduct
	@RequestMapping(value = "addToCartFromProduct", method = RequestMethod.POST)	
	public ResponseEntity<?> addToCartFromProduct(@RequestBody String jsonData) {
//        System.out.println("測試訊息:有到 addToCartFromProduct 的 contrller");
		JSONObject jsonObject = new JSONObject(jsonData);
//        System.out.println("測試訊息:有收到jsonData="+jsonData);
        if(jsonData==null || jsonData.equals("{}")){
            HttpResult<String> result = new HttpResult<>(400, "", "請正確選擇要加入購物車的數量");
            return ResponseEntity.badRequest().body(result);
        }
        String productId = jsonObject.getString("productId");
        String productNum = jsonObject.getString("productNum");
//        System.out.println("測試訊息:productId="+productId+" ,productNum="+productNum);
        
        String memberId = "9"; //測試有登入, 預設值
		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
        memberId = String.valueOf(buyerVO.getMemberId());        
        
        System.out.println("測試訊息:將memberId="+memberId+"的購物車,加入productId="+ productId + ", " + productNum + "個");
              
        // 呼叫將商品加入購物車 Redis 資料的方法
        String method = cartService.addManyProductToCart(memberId, productId, productNum);
//        cartService.creatOrderByMemberId( jsonObject );
        
	    // 返回適當的響應
	    return ResponseEntity.ok().body(new HttpResult<>(200,"","success"));
	}

	

}
