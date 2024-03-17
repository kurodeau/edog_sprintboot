package com.cart.service;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

	// Redis 存Key List+ DTO 作法, 獲取member 所有購物車資訊
	public Map<String, Set<CartVO>> getAllByMemberId(String memberId) {

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
		if (jsonData == null || jsonData.equals("{}")) {
			HttpResult<String> result = new HttpResult<>(400, "", "FUCKU");
			System.out.println("controller 來的 JSON 的資料是空的???");
		}

		// =================整理 JSON 資料, 拿出memberId start
		List<String> members = new ArrayList();
		Map<String, String> orderData = new HashMap<>();
		String memberId = "9";
		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        BuyerVO buyerVO = (BuyerVO) authentication.getPrincipal();
        memberId = String.valueOf(buyerVO.getMemberId());        
        
        System.out.println("測試訊息:透過購物車新建memberId="+memberId+"的訂單");

		
		
		System.out.println("---");
		for (String member : jsonData.keySet()) {
			members.add(member);
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

	
	
	// 這個之後應該要改成可以改數量讓他更因, 現在就先放著
	public String addOneProductToCart(String memberId, String productId) {

		// 取得連線
		JedisPool jedisPool = JedisUtil.getJedisPool();

		// 本方法會用到的變數
		List<ProductVO> productList = new ArrayList();
		ProductVO product = new ProductVO();
		String redisKey = "cart:" + memberId;
//		System.out.println("redisKey= " + redisKey + " , productId= " + productId);

		// 索取redis連線, 用try 整個包起來
		try (
			// 從 Redis 中讀取資料
			Jedis jedis = jedisPool.getResource()) {
			jedis.select(10);
			
			// 檢查 redisKey 對應的 Redis 資料是正確的型別
			if ( jedis.exists(redisKey) ) {
//				System.out.println("測試訊息:redisKey=" + redisKey + "存在");
				String redisKeyType = jedis.type(redisKey);
				if( !(redisKeyType.equals("hash")) ) {
//					System.out.println("異常已經存在,且不是Hash型別");
					return "異常已經存在,且不是Hash型別";
				}
			}			
//			System.out.println("測試訊息:" + redisKey + "型別檢查OK");
			
			// 確認指定的 ProductId 是否存在於 redisKey 內, 並將之加一
			String productNum = "1";
			if( (jedis.hget(redisKey, productId)) != null ) {
				productNum = String.valueOf(Integer.parseInt(jedis.hget(redisKey, productId)) + 1)  ;
//				System.out.println("測試訊息productId=" + productId + ", productNum=" + productNum);
			}
			
			// 將 productId 跟 數量, 塞回對應的redisKey
			jedis.hset(redisKey, productId, productNum);

		} catch (Exception e) {
			System.out.println("從redis讀出資料有問題");
			e.printStackTrace();
		}
		System.out.println("redisKey:" + redisKey + "資料更新");
		return "addOneProductToCart,圓滿完成";
	}

	
	
	// 從商品詳細加入多個到購物車
	public String addManyProductToCart(String memberId, String productId, String productNum) {

		System.out.println("測試訊息:有進入addManyProductToCart()");
		
		// 取得連線
		JedisPool jedisPool = JedisUtil.getJedisPool();

		// 本方法會用到的變數
		List<ProductVO> productList = new ArrayList();
		ProductVO product = new ProductVO();
		String redisKey = "cart:" + memberId;
//		System.out.println("redisKey= " + redisKey + " , productId= " + productId);

		// 索取redis連線, 用try 整個包起來
		try (
			// 從 Redis 中讀取資料
			Jedis jedis = jedisPool.getResource()) {
			jedis.select(10);
			
			// 檢查 redisKey 對應的 Redis 資料是正確的型別
			if ( jedis.exists(redisKey) ) {
//				System.out.println("測試訊息:redisKey=" + redisKey + "存在");
				String redisKeyType = jedis.type(redisKey);
				if( !(redisKeyType.equals("hash")) ) {
//					System.out.println("異常已經存在,且不是Hash型別");
					return "這個redisKey不是Hash型別";
				}
			}			
//			System.out.println("測試訊息:" + redisKey + "型別檢查OK");
			
			// 確認指定的 ProductId 是否存在於 redisKey 內, 並將之加一
			String productNumF = "0";
			if( (jedis.hget(redisKey, productId)) != null ) {
				productNumF = String.valueOf( Integer.parseInt(jedis.hget(redisKey, productId)) + Integer.parseInt(productNum) );
//				System.out.println("測試訊息productId=" + productId + ", productNum=" + productNum);
			}
			
			// 將 productId 跟 數量, 塞回對應的redisKey
			jedis.hset(redisKey, productId, productNumF);

		} catch (Exception e) {
			System.out.println("從redis讀出資料有問題");
			e.printStackTrace();
		}
		System.out.println("redisKey:" + redisKey + "資料更新");
		return "addOneProductToCart,圓滿完成";
	}

	
	
	// List + DTO作法
	public Map<String, Set<CartVO>> removeOneFromCart(String memberId, String productId) {

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
			
			// 自該購物車中移除指定商品
			if (jedis.hexists(redisKey, productId)) {
			    jedis.hdel(redisKey, productId);
			    System.out.println("測試訊息:已將productId"+productId+"自購物車移除");
			    }
			else {System.out.println("測試訊息:productId"+productId+"本就不在");}
			
			// 獲得該筆Redis 內容Keys
			Set<String> allProductId = jedis.hkeys(redisKey);
			System.out.println("測試訊息:這個Redis的所有Key_allProuductId= " + allProductId);

			// 宣告要裝入回傳List<CartVO> 的參數
			String sellerCompany;
			SellerVO sellerVO;
			String productNum;
			
			
			// ===== 把Mep中的資 實體化為要回傳的CartVO
			for (String Id : allProductId) {
				System.out.println("測試訊息:迴圈中的,productId: " + Id);
				System.out.println("測試訊息:迴圈中的,productId="+Id+" ,數量value=" + jedis.hget(redisKey, Id));
				CartVO cartVO = new CartVO();
				ProductVO productVO = productSvc.getOneProduct(Integer.valueOf(Id));
				cartVO.setProductVO(productVO);
				sellerCompany = productVO.getSellerVO().getSellerCompany();
				cartVO.setSellerCompany(sellerCompany);
				cartVO.setSellerVO(productVO.getSellerVO());
				cartVO.setProductNum(jedis.hget(redisKey, Id).toString());

				// 將每個 CartVO 透過 "sellerCompany" 鍵關聯起来
				cartSet = cartClassfi.getOrDefault(cartVO.getSellerCompany(), new HashSet<CartVO>());
				cartSet.add(cartVO);
				cartClassfi.put(sellerCompany, cartSet);
			}

		} catch (Exception e) {
			System.out.println("從redis讀出資料有問題");
			e.printStackTrace();
		}
		System.out.println("測試訊息:已將productId="+productId+" ,移出memberId="+memberId+"的購物車, 並回傳classfi給前端炫染"); // 測試資料
		return cartClassfi;
	}

}
