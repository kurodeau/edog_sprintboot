package com.cart.service;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.util.HttpResult;
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
		String redisKey = "cart:" + memberId;
		System.out.println("redisKey= " + redisKey); // 測試訊息
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
			// ===== 把Keys Map 中的值 實體化為要回傳的CartVO
			for (String productId : allProductId) {
				System.out.println("service測試訊息,productId: " + productId);
				System.out.println("service測試訊息,productId 數量value: " + jedis.hget(redisKey, productId));
				CartVO cartVO = new CartVO();
				ProductVO productVO = productSvc.getOneProduct(Integer.valueOf(productId));
				cartVO.setProductVO(productVO);
				sellerCompany = productVO.getSellerVO().getSellerCompany();
				cartVO.setSellerCompany(sellerCompany);
				cartVO.setSellerVO(productVO.getSellerVO());
				cartVO.setProductNum(jedis.hget(redisKey, productId).toString());
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

	// Redis 存的格式等同Cart:[memberId]
	public void creatOrderByMemberId(JSONObject jsonData) {
//        System.out.println("有進入creatOrderByMemberId方法");
//		JSONObject jsonObject = new JSONObject(jsonData);
		if (jsonData == null || jsonData.equals("{}")) {
			HttpResult<String> result = new HttpResult<>(400, "", "FUCKU");
			System.out.println("controller 來的 JSON 的資料是空的???");
		}

		// =================整理 JSON 資料, 拿出memberId start
		List<String> members = new ArrayList();
		Map<String, String> orderData = new HashMap<>();
		String memberId = "1";
		System.out.println("---");
		for (String member : jsonData.keySet()) {
			members.add(member);
			memberId = member;
			System.out.println("member: " + member);
			JSONArray memberList = jsonData.getJSONArray(member);
			for (int i = 0; i < memberList.length(); i++) {
//        	CartVO cartVO  = new ProductVO();
				JSONObject product = memberList.getJSONObject(i);
				String productId = String.valueOf(product.getInt("productId"));
				String productQty = String.valueOf(product.getInt("productQty"));
				orderData.put(productId, productQty);
				System.out.println("productId: " + productId + ", productQty: " + productQty);
			}
		}
		// =================整理 JSON 資料 end
		// =================創建Redis 資料 start
		// 設定redisKey, 取得連線
		String redisKey = "order:" + memberId;
		System.out.println("redisKey= " + redisKey); // 測試訊息
		JedisPool jedisPool = JedisUtil.getJedisPool();

		// 索取redis連線, 用try 整個包起來
		try (
			// 從 Redis 中讀取資料 並且指定為db10
			Jedis jedis = jedisPool.getResource()) {
			jedis.select(10);

			// 檢查該會員是否已經有訂單, 如果有就移除, 然後塞入新的值
			if (jedis.exists(redisKey)) {
				jedis.del(redisKey);
			}
			System.out.println("打資料上Redis前最後確認:" + redisKey + orderData);
			jedis.hmset(redisKey, orderData);
			// =================創建Redis 資料 end

		} catch (Exception e) {
			System.out.println("service對Redis的操作有問題");
			e.printStackTrace();
		}
//		System.out.println("回傳 cartClassfi"); // 測試資料
		System.out.println("這個沒有return 到這裡order redis資料 已經成立");
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
		String redisKey = "cart" + memberId;
		System.out.println("redisKey= " + redisKey); // 測試訊息
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

			// ===== 先計算List中每個商品編號有幾個
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

			// ===== 把Mep中的資 實體化為要回傳的CartVO
			for (Map.Entry<String, Integer> p : productAndNum.entrySet()) {
				System.out.println("測試訊息,Element: " + p.getKey() + ", Count: " + p.getValue());
				CartVO cartVO = new CartVO();
				ProductVO productVO = productSvc.getOneProduct(Integer.valueOf(p.getKey()));
				cartVO.setProductVO(productVO);
				sellerCompany = productVO.getSellerVO().getSellerCompany();
				cartVO.setSellerCompany(sellerCompany);
				cartVO.setSellerVO(productVO.getSellerVO());
				cartVO.setProductNum(p.getValue().toString());
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
