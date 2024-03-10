package com.cart.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.json.*;

@Service("cartService")
public class CartService {

	@Autowired
	ProductService productSvc;

	public Map<String, Map<ProductVO, String>> getAllByMemberId(String memberId) {

		// 假定memberId 測試用
		memberId = "1";

		// 取得連線
		JedisPool jedisPool = JedisUtil.getJedisPool();

		// 本方法會用到的跨區域變數
		// Key:sellerId -> key:ProductId value:String
		Map<String, Map<ProductVO, String>> cartClassfi = new HashMap<>();
		Map<ProductVO, String> productAndQty = new HashMap<>();
		ProductVO product = new ProductVO();
		String Key1 = memberId;
		// 測試Key用的參數
		System.out.println("Key1=" + Key1);

		// 索取redis連線, 用try 整個包起來
		try (

				// 從 Redis 中讀取資料 並且指定為db10
				Jedis jedis = jedisPool.getResource()) {
			jedis.select(10);

			String datajsonS = jedis.get("3");
			System.out.println("datajsonS" + datajsonS);
			JSONObject datajasonJ = new JSONObject(datajsonS);
			System.out.println("datajasonJ" + datajasonJ.toString());

			// 測試, 拿出每一個key
			// 獲取 JSONObject 中所有的 key
			Iterator<String> keys = datajasonJ.keys();

			// 遍歷 keys 並打印每個 key
			while (keys.hasNext()) {
				String key = keys.next();
				System.out.println("Key: " + key);
			}

//			for (String str : jedis.lrange(cartMember, 0, -1)) {
////				product = productSvc.getOneProduct(Integer.parseInt(str));
//				String sellerCompany = product.getSellerVO().getSellerCompany();
//
//				// 將每個 ProductVO 透過 "sellerCompany" 鍵關聯起来
//				productList = cartClassfi.getOrDefault(sellerCompany, new ArrayList<>()); // 拉出對應Key的value
//																							// List，再對其更新
//				productList.add(product);
//				cartClassfi.put(sellerCompany, productList);
//			}
		} catch (Exception e) {
			System.out.println("從redis讀出資料有問題");
			e.printStackTrace();
		}
		System.out.println(cartClassfi.toString()); // 測試資料
		return cartClassfi;
	}

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

}
