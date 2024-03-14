package com.cart.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import org.json.*;

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
import com.cart.model.*;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.json.*;

@Service("cartService")
public class CartService {

	@Autowired
	ProductService productSvc;
	
	// List + DTO作法, 獲取mamber所有購物車資訊
//	public Map<String, Set<CartVO>> getAllByMemberId(String memberId) {
//
//		memberId = "9";
//		
//		// 設定redisKey, 取得連線
//		String redisKey = "cart"+ memberId;
//		System.out.println("redisKey= " + redisKey); //測試訊息
//		JedisPool jedisPool = JedisUtil.getJedisPool();
//
//		// 本方法會用到的跨區域變數
//		Map<String, Set<CartVO>> cartClassfi = new HashMap<>(); // 最後要傳回前端的資料包
//		Set<CartVO> cartSet = new HashSet<CartVO>(); // 要被裝入 cartClassfi 的Value
//		List<String> redisData = new ArrayList<String>(); // Redis 中的 ProductID 撈出裝成List來遍歷或檢查
//
//		// 索取redis連線, 用try 整個包起來
//		try (
//			// 從 Redis 中讀取資料 並且指定為db10
//			Jedis jedis = jedisPool.getResource()) {
//			jedis.select(10);
//
//			redisData = jedis.lrange(redisKey, 0, -1);
//			System.out.println("redisData= " + redisData);
//
//			// 宣告要裝入回傳List<CartVO> 的參數
//			String sellerCompany;
//				
//			SellerVO sellerVO;
//			String productNum;
//						
//			//===== 先計算List中每個商品編號有幾個
//			Map<String, Integer> productAndNum = new HashMap<>();
//			for (String p : redisData) {
//				productAndNum.put(p, productAndNum.getOrDefault(p, 0) + 1);
//			}
//            //===== 把Mep中的資 實體化為要回傳的CartVO
//		    for (Map.Entry<String, Integer> p : productAndNum.entrySet()) {
//	            System.out.println("測試訊息,Element: " + p.getKey() + ", Count: " + p.getValue());
//	            CartVO cartVO = new CartVO();    
//	            ProductVO productVO = productSvc.getOneProduct( Integer.valueOf( p.getKey() ) );
//	            cartVO.setProductVO(productVO);
//	            sellerCompany = productVO.getSellerVO().getSellerCompany();
//	            cartVO.setSellerCompany( sellerCompany );
//	            cartVO.setSellerVO( productVO.getSellerVO() );
//	            cartVO.setProductNum( p.getValue().toString() );
////	            System.out.println( "測試cartVO=" + cartVO.toString() );
//
//				// 將每個 CartVO 透過 "sellerCompany" 鍵關聯起来
////	            System.out.println("將每個 CartVO 透過 \"sellerCompany\" 鍵關聯起来");
//	            cartSet = cartClassfi.getOrDefault(cartVO.getSellerCompany(), new HashSet<CartVO>());
//	            cartSet.add(cartVO);
//				cartClassfi.put(sellerCompany, cartSet);
//	        }
//			
//		} catch (Exception e) {
//			System.out.println("從redis讀出資料有問題");
//			e.printStackTrace();
//		}
////		System.out.println("回傳 cartClassfi"); // 測試資料
//		return cartClassfi;
//	}

	// Redis 存Key List+ DTO 作法, 獲取member 所有購物車資訊
	public Map<String, Set<CartVO>> getAllByMemberId(String memberId) {

		memberId = "8";
		
		// 設定redisKey, 取得連線
		String redisKey = "cart:"+ memberId;
		System.out.println("redisKey= " + redisKey); //測試訊息
		JedisPool jedisPool = JedisUtil.getJedisPool();

		// 本方法會用到的跨區域變數
		Map<String, Set<CartVO>> cartClassfi = new HashMap<>(); // 最後要傳回前端的資料包
		Set<CartVO> cartSet = new HashSet<CartVO>(); // 要被裝入 cartClassfi 的Value
		List<String> redisData = new ArrayList<String>(); // Redis 中的 ProductID 撈出裝成List來遍歷或檢查

		// 索取redis連線, 用try 整個包起來
		try (
			// 從 Redis 中讀取資料 並且指定為db10
			Jedis jedis = jedisPool.getResource()) {
			jedis.select(10);

			// 獲得該筆Redis 內容Keys
			Set<String> allProductId = jedis.hkeys(redisKey);
			System.out.println("allProuductId= " + allProductId);

			// 宣告要裝入回傳List<CartVO> 的參數
			String sellerCompany;
			SellerVO sellerVO;
			String productNum;
						
//			//===== 先計算List中每個商品編號有幾個
//			Map<String, Integer> productAndNum = new HashMap<>();
//			for (String p : redisData) {
//				productAndNum.put(p, productAndNum.getOrDefault(p, 0) + 1);
//			}
            //===== 把Keys Map 中的值 實體化為要回傳的CartVO
		    for (String productId : allProductId) 
		    {
	            System.out.println("service測試訊息,productId: " + productId );
	            System.out.println("service測試訊息,productId 數量value: " + jedis.hget(redisKey, productId) );
	            CartVO cartVO = new CartVO();    
	            ProductVO productVO = productSvc.getOneProduct( Integer.valueOf( productId ) );
	            cartVO.setProductVO(productVO);
	            sellerCompany = productVO.getSellerVO().getSellerCompany();
	            cartVO.setSellerCompany( sellerCompany );
	            cartVO.setSellerVO( productVO.getSellerVO() );
	            cartVO.setProductNum( jedis.hget(redisKey, productId).toString() );
//	            System.out.println( "測試cartVO=" + cartVO.toString() );

				// 將每個 CartVO 透過 "sellerCompany" 鍵關聯起来
//	            System.out.println("將每個 CartVO 透過 \"sellerCompany\" 鍵關聯起来");
	            cartSet = cartClassfi.getOrDefault(cartVO.getSellerCompany(), new HashSet<CartVO>());
	            cartSet.add(cartVO);
				cartClassfi.put(sellerCompany, cartSet);
	        }
			
		} catch (Exception e) {
			System.out.println("service從redis讀出資料有問題");
			e.printStackTrace();
		}
//		System.out.println("回傳 cartClassfi"); // 測試資料
		return cartClassfi;
	}
	
	
	// 這個還沒完成
	public void memberAddOneByProductId(String memberId, String prouductId) {

		// 取得連線
		JedisPool jedisPool = JedisUtil.getJedisPool();

		// 本方法會用到的變數
//		Map<String, List<ProductVO>> collectionClassfi = new HashMap<>();
		List<ProductVO> productList = new ArrayList();
		ProductVO product = new ProductVO();
//		String collectionMember = "collection" + memberId;
//		System.out.println("collectionMember=" + collectionMember);
		String key = "cart" + memberId;
		System.out.println("Key= " + key + " , prouductId= " + prouductId);

		// 索取redis連線, 用try 整個包起來
		try (
				// 從 Redis 中讀取資料
				Jedis jedis = jedisPool.getResource()) {
			jedis.select(10);

			List<String> memberCart = jedis.lrange(key, 0, -1);
			// 判斷 Jedis 該用戶是否已經有登錄該商品收藏
			if (!key.contains(prouductId)) {
				memberCart.add(prouductId); // 如果他不存在購物車清單, 就新增他
			}

			// 如果已經存在該用戶的收藏清單, 則清除並加入修改過的清單
			if (jedis.exists(key)) {
				jedis.del(key); // 刪除已存在的 key
			}

			for (String s : memberCart) {
				jedis.lpush(key, s);
			}

		} catch (Exception e) {
			System.out.println("從redis讀出資料有問題");
			e.printStackTrace();
		}
		System.out.println("key:" + key + "資料更新");
//		System.out.println( collectionClassfi.toString() ); //測試到底cart有沒有被改
		System.out.println("方法結束, 沒有return");
	}

	// List + DTO作法 
	public Map<String, Set<CartVO>> removeOneFromCart(String memberId, String prouductId) {

		memberId = "9";
		
		// 設定redisKey, 取得連線
		String redisKey = "cart"+ memberId;
		System.out.println("redisKey= " + redisKey); //測試訊息
		JedisPool jedisPool = JedisUtil.getJedisPool();

		// 本方法會用到的跨區域變數
		Map<String, Set<CartVO>> cartClassfi = new HashMap<>(); // 最後要傳回前端的資料包
		Set<CartVO> cartSet = new HashSet<CartVO>(); // 要被裝入 cartClassfi 的Value
		List<String> redisData = new ArrayList<String>(); // Redis 中的 ProductID 撈出裝成List來遍歷或檢查

		// 索取redis連線, 用try 整個包起來
		try (
			// 從 Redis 中讀取資料 並且指定為db10
			Jedis jedis = jedisPool.getResource()) {
			jedis.select(10);

			redisData = jedis.lrange(redisKey, 0, -1);
			System.out.println("redisData= " + redisData);

			// 宣告要裝入回傳List<CartVO> 的參數
			String sellerCompany;
				
			SellerVO sellerVO;
			String productNum;
			
			//===== 先計算List中每個商品編號有幾個
			Map<String, Integer> productAndNum = new HashMap<>();
			for (String p : redisData) {
				productAndNum.put(p, productAndNum.getOrDefault(p, 0) + 1);
			}
			
//			//===== 移除指定的Key 與對應的內容
//			String keyToRemove = prouductId;			
//			if (productAndNum.containsKey(keyToRemove)) {
//			    // 如果存在，則移除
//			    productAndNum.remove(keyToRemove);
//			    System.out.println("已移除 key: " + keyToRemove);
//			} else {
//			    // 如果不存在，印出訊息
//			    System.out.println("Key: " + keyToRemove + " 不存在於 Map 中");
//			}
			
            //===== 把Mep中的資 實體化為要回傳的CartVO
		    for (Map.Entry<String, Integer> p : productAndNum.entrySet()) {
	            System.out.println("測試訊息,Element: " + p.getKey() + ", Count: " + p.getValue());
	            CartVO cartVO = new CartVO();    
	            ProductVO productVO = productSvc.getOneProduct( Integer.valueOf( p.getKey() ) );
	            cartVO.setProductVO(productVO);
	            sellerCompany = productVO.getSellerVO().getSellerCompany();
	            cartVO.setSellerCompany( sellerCompany );
	            cartVO.setSellerVO( productVO.getSellerVO() );
	            cartVO.setProductNum( p.getValue().toString() );
//	            System.out.println( "測試cartVO=" + cartVO.toString() );

				// 將每個 CartVO 透過 "sellerCompany" 鍵關聯起来
//	            System.out.println("將每個 CartVO 透過 \"sellerCompany\" 鍵關聯起来");
	            cartSet = cartClassfi.getOrDefault(cartVO.getSellerCompany(), new HashSet<CartVO>());
	            cartSet.add(cartVO);
				cartClassfi.put(sellerCompany, cartSet);
	        }
			
		} catch (Exception e) {
			System.out.println("從redis讀出資料有問題");
			e.printStackTrace();
		}
//		System.out.println("回傳 cartClassfi"); // 測試資料
		return cartClassfi;
	}
	
	
}
