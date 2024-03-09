package com.cart.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.buyer.entity.BuyerVO;
import com.buyer.model.BuyerRepository;
import com.product.model.ProductService;
import com.product.model.ProductVO;
import com.redis.JedisUtil;
import com.seller.entity.SellerVO;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.UnifiedJedis;

@Service("cartService")
public class CartService {

	@Autowired
	ProductService productSvc;
	
	// 創建一個 Class 用來接 json 的東西
//	class CollectionAndCart {
//	    private String[] collectionList;
//	    private String[] cartList;
//
//	    public CollectionAndCart() {}
//	    
//		public String[] getCollectionList() {
//			return collectionList;
//		}
//		public void setCollectionList(String[] collectionList) {
//			this.collectionList = collectionList;
//		}
//		public String[] getCartList() {
//			return cartList;
//		}
//		public void setCartList(String[] cartList) {
//			this.cartList = cartList;
//		}
//		
//		@Override
//		public String toString() {
//			return "CollectionAndCart [collectionList=" + Arrays.toString(collectionList) + ", cartList="
//					+ Arrays.toString(cartList) + "]";
//		}
//	}
	
	public Map<String, List<ProductVO>> getAllByMemberId(String memberId) {

		// 取得連線
		JedisPool jedisPool = JedisUtil.getJedisPool();

		// 本方法會用到的跨區域變數
		Map<String, List<ProductVO>> cartClassfi = new HashMap<>();
		List<ProductVO> productList = new ArrayList();
		ProductVO product = new ProductVO();

		System.out.println("111111");
		// 索取redis連線, 用try 整個包起來
		try (
				
			// 與 Redis 取得連線 並且指定為db10
			Jedis jedis = jedisPool.getResource()) {
//			jedis.select(10);
//			
//			UnifiedJedis j = new 
//
//			System.out.println("2222222");
//			//將撈出的 json 摘出 cart 分類的內容 另存為 List
//			String jsonString = jedis
//			System.out.println("333333");
//			Gson gson = new Gson();
//			CollectionAndCart collectionAndCart = gson.fromJson(jsonString, CollectionAndCart.class);
//			System.out.println("test" + collectionAndCart);
//			List<String> memberCart = collectionAndCart.getCartList();
			
			// 將所有購物車的資料包入 List<ProductVO>, 並透過公司名稱分為Map
//			for (String str : memberCart) {
//				product = productSvc.getOneProduct(Integer.parseInt(str));
//				String sellerCompany = product.getSellerVO().getSellerCompany();
//
//				// 將每個 ProductVO 透過 "sellerCompany" 鍵關聯起来
//				productList = cartClassfi.getOrDefault(sellerCompany, new ArrayList<>()); // 拉出對應Key的value
//																								// List，再對其更新
//				productList.add(product);
//				cartClassfi.put(sellerCompany, productList);
//			}
		} catch (Exception e) {
			System.out.println("從redis讀出資料有問題");
			e.printStackTrace();
		}
		System.out.println( cartClassfi.toString() ); //測試資料
		return cartClassfi;
	}

//	// 不確定是不是這樣寫, 抓取買家登入資訊的 memberId, 還有操作對象的 productId
//	public Map<String, List<ProductVO>> switchStateByProductId(String memberId, String prouductId) {
//
//		// 取得連線
//		JedisPool jedisPool = JedisUtil.getJedisPool();
//
//		// 本方法會用到的變數
//		Map<String, List<ProductVO>> collectionClassfi = new HashMap<>();
//		List<ProductVO> productList = new ArrayList();
//		ProductVO product = new ProductVO();
//
//		// 索取redis連線, 用try 整個包起來
//		try (
//				/****************************
//				 * 1.透過已知的 memberId 自 Redis 要出收藏清單
//				 *********************************************/
//				Jedis jedis = jedisPool.getResource()) {
//			// 從 Redis 中讀取資料 並且指定為db10 試圖分流分類資料
//			jedis.select(10);
//
//			List<String> memberCollection = jedis.lrange(memberId, 0, -1);
//			// 判斷 Jedis 該用戶是否已經有登錄該商品收藏
//			if (memberCollection.contains(prouductId)) {
//				memberCollection.remove(prouductId); // 如果存在prouductId，则移除
//			} else {
//				memberCollection.add(prouductId); // 如果不存在prouductId，则新增
//			}
////			System.out.println(memberCollection); // 測試用訊息
//			
//			// 如果已經存在該用戶的收藏清單, 則清除並加入修改過的清單
//			if (jedis.exists(memberId)) {
//				jedis.del(memberId); // 刪除已存在的 key
//			}
//			for ( String s : memberCollection ) {
//				jedis.lpush(memberId, s);	
//			}
//			
////			jedis.lpush(memberId, memberCollection);
//
//			// 將所有收藏的資料包入 List<ProductVO>, 並透過公司名稱分為Map
//			// Set<String> sellerCompanySet = new HashSet();
//			for (String str : memberCollection) {
//				product = productSvc.getOneProduct(Integer.parseInt(str));
//				String sellerCompany = product.getSellerVO().getSellerCompany();
//
//				// 將每個 ProductVO 透過 "sellerCompany" 鍵關聯起来
//				productList = collectionClassfi.getOrDefault(sellerCompany, new ArrayList<>()); // 拉出對應Key的value
//																								// List，再對其更新
//				productList.add(product);
//				collectionClassfi.put(sellerCompany, productList);
//			}
//		} catch (Exception e) {
//			System.out.println("從redis讀出資料有問題");
//			e.printStackTrace();
//		}
//		System.out.println(collectionClassfi.toString()); // 測試資料
//		return collectionClassfi;
//	}

}
