package com.collection.controller;

import java.io.IOException;

import javax.servlet.http.HttpServlet;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.buyer.entity.BuyerVO;
import com.cart.service.CartService;
import com.collection.service.CollectionService;
import com.product.model.ProductService;
import com.product.model.ProductVO;
import com.seller.entity.SellerVO;
import com.seller.service.SellerService;

@Controller
@RequestMapping("/front/buyer/collection")
public class CollectionnoController extends HttpServlet {

	@Autowired
	ProductService productSvc;

	@Autowired
	SellerService sellerSvc;

	@Autowired
	CollectionService collectionService;

	@Autowired
	CartService cartService;

	
	
	// 用戶取出自己所有收藏清單資料 /front/buyer/collection/list
	@GetMapping("list")
	public String collectionlist(Model model) {
		// 先給定 memberId 以便測試
		
		String memberId = "9"; //測試有登入, 預設值
		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();

        memberId = String.valueOf(buyerVO.getMemberId());
        
        System.out.println(memberId);
 
		
//		SecurityContext secCtx = SecurityContextHolder.getContext();
//        Authentication authentication = secCtx.getAuthentication();
//        SellerVO sellerVO = (SellerVO) authentication.getPrincipal();
//
//        Integer SellerId = sellerVO.getSellerId();
		
		model.addAttribute("collectionClassfi", collectionService.getAllByMemberId(memberId));
		return "front-end/buyer/buyer-collection-list";
	}

	
	
	// 更新特定一個商品編號的收藏狀態, 並回到我的收藏清單
	// /front/buyer/collection/switchState
	@PostMapping("switchState") // 改用POST
	public String switchOneToCollection(@RequestParam("memberId") String memberId,
			@RequestParam("productId") String productId, Model model) {
		// 先給定 memberId , productId 以便測試
		System.out.println("到switchState controller");
		
		memberId = "9"; //測試有登入, 預設值
		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
		System.out.println("測試訊息:memberId="+ memberId +"productId=" + productId);
		
		model.addAttribute("collectionClassfi", collectionService.switchStateByProductIdInList(memberId, productId));
		return "front-end/buyer/buyer-collection-list";
	}


	// 更新特定一個商品編號的收藏狀態, 並回到商品詳細
	// /front/buyer/collection/switchState
	@GetMapping(value = "/switchState/{id}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> switchStateCollectionedByProductId(@PathVariable("id") String id, ModelMap model) throws IOException {

		String memberId = "9"; //測試有登入, 預設值
		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();

        memberId = String.valueOf(buyerVO.getMemberId());
        
//		ProductVO productVO = productSvc.getOneProduct(Integer.valueOf(id));
		System.out.println("變更商品收藏狀態controller,memberId="+memberId+" ,productId="+ id );
//		model.addAttribute("productVO", productVO);

        // 呼叫方法判斷是否已經加入收藏
		return ResponseEntity.ok(collectionService.switchStateCollectionedByProductId(memberId, id));
	}
	
	
	// 這個不知道是做甚麼用的? 搞懂之前註解起來
//	@GetMapping(value = "/product/{id}", produces = "application/json")
//	@ResponseBody
//	public ResponseEntity<?> productCommidityAJAX(@PathVariable("id") String id, ModelMap model) throws IOException {
//
//		System.out.println("有進到找商品詳細的controller");
//
//		ProductVO productVO = productSvc.getOneProduct(Integer.valueOf(id));
//		model.addAttribute("productVO", productVO);
//
//		// 使用從URL獲取的id參數執行你的邏輯
//		// 在這裡，你可以使用id來進行相應的處理，例如查找特定商品
//
//		// 建立 JSON 物件
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("sellerVO", JSONObject.valueToString(productVO.getSellerVO()));
//		jsonObject.put("productVO", JSONObject.valueToString(productVO.getProductSortVO()));
//		System.out.println("只差沒有跳轉");
//		System.out.println(jsonObject.toString());
//		return ResponseEntity.ok(productVO.getSellerVO());
//	}

	// 將指定商品加到購物車, 並回到我的收藏 /front/buyer/collection/addToCart
	@PostMapping("addToCart")
	public String addToCart(@RequestParam("memberId") String memberId, @RequestParam("productId") String productId,
			Model model) {
		System.out.println("有到加入購物車 controller");
//		model.addAttribute("collectionClassfi", cartService.memberAddOneByProductId(memberId, productId));
		cartService.memberAddOneByProductId(memberId, productId);
		return "front-end/buyer/buyer-collection-list";
	}
	
	// 每次刷新商品頁面觸發, 檢查這個商品有沒有被收藏?
	// /front/buyer/collection/product/{id}
	@GetMapping(value = "/product/{id}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> checkIfProductCollectioned(@PathVariable("id") String id, ModelMap model) throws IOException {

		String memberId = "9"; //測試有登入, 預設值
		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();

        memberId = String.valueOf(buyerVO.getMemberId());
        
//		ProductVO productVO = productSvc.getOneProduct(Integer.valueOf(id));
		System.out.println("測試檢查商品被收藏controller,memberId="+memberId+" ,productId="+ id );
//		model.addAttribute("productVO", productVO);

        // 呼叫方法判斷是否已經加入收藏
		return ResponseEntity.ok(collectionService.checkIfProductCollectioned(memberId, id));
	}
	
	
	

}
