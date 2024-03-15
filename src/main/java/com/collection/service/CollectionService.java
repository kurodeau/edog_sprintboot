package com.collection.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

@Service("collectionService")
public class CollectionService {

	@Autowired
	ProductService productSvc;

	
	// 拿取該用戶的收藏資訊, 並顯示在前端
	public Map<String, List<ProductVO>> getAllByMemberId(String memberId) {

		// 取得連線
		JedisPool jedisPool = JedisUtil.getJedisPool();

		// 本方法會用到的變數
		Map<String, List<ProductVO>> collectionClassfi = new HashMap<>();
		List<ProductVO> productList = new ArrayList();
		ProductVO product = new ProductVO();
		String jedisKey = "collection:" + memberId;
		//測試Key用的參數
		System.out.println("jedisKey=" + jedisKey);

		// 索取redis連線, 用try 整個包起來
		try (

			// 從 Redis 中讀取資料 並且指定為db10
			Jedis jedis = jedisPool.getResource()) {
			jedis.select(10);

			for (String str : jedis.lrange(jedisKey, 0, -1)) {
				product = productSvc.getOneProduct(Integer.parseInt(str));
				String sellerCompany = product.getSellerVO().getSellerCompany();

				// 將每個 ProductVO 透過 "sellerCompany" 鍵關聯起来
				productList = collectionClassfi.getOrDefault(sellerCompany, new ArrayList<>()); // 拉出對應Key的value
																								// List，再對其更新
				productList.add(product);
				collectionClassfi.put(sellerCompany, productList);
			}
		} catch (Exception e) {
			System.out.println("從redis讀出資料有問題");
			e.printStackTrace();
		}
//		System.out.println( collectionClassfi.toString() ); //測試資料
		return collectionClassfi;
	}

	
	
	// 不確定是不是這樣寫, 抓取買家登入資訊的 memberId, 還有操作對象的 productId
	public Map<String, List<ProductVO>> switchStateByProductIdInList(String memberId, String prouductId) {

		System.out.println("進入switchStateByProductId service");
		// 取得連線
		JedisPool jedisPool = JedisUtil.getJedisPool();

		// 本方法會用到的變數
		Map<String, List<ProductVO>> collectionClassfi = new HashMap<>();
		List<ProductVO> productList = new ArrayList();
		ProductVO product = new ProductVO();
		String redisKey = "collection:" + memberId;
		// 測試Key用的參數
		System.out.println("redisKey=" + redisKey);

		// 索取redis連線, 用try 整個包起來
		try (

			// 從 Redis 中讀取資料 並且指定為db10 試圖分流分類資料		
			Jedis jedis = jedisPool.getResource()) {
			jedis.select(10);

			List<String> memberCollection = jedis.lrange(redisKey, 0, -1);
			// 判斷 Jedis 該用戶是否已經有登錄該商品收藏
			if (memberCollection.contains(prouductId)) {
				memberCollection.remove(prouductId); // 如果存在prouductId，则移除
			} else {
				memberCollection.add(prouductId); // 如果不存在prouductId，则新增
			}

			// 如果已經存在該用戶的收藏清單, 則清除並加入修改過的清單
			if (jedis.exists(redisKey)) {
				jedis.del(redisKey); // 刪除已存在的 key
			}
			for (String s : memberCollection) {
				jedis.lpush(redisKey, s);
			}


			// 將所有收藏的資料包入 List<ProductVO>, 並透過公司名稱分為Map
			// Set<String> sellerCompanySet = new HashSet();
			for (String str : memberCollection) {
				product = productSvc.getOneProduct(Integer.parseInt(str));
				String sellerCompany = product.getSellerVO().getSellerCompany();

				// 將每個 ProductVO 透過 "sellerCompany" 鍵關聯起来
				productList = collectionClassfi.getOrDefault(sellerCompany, new ArrayList<>()); // 拉出對應Key的value
																								// List，再對其更新
				productList.add(product);
				collectionClassfi.put(sellerCompany, productList);
			}
		} catch (Exception e) {
			System.out.println("從redis讀出資料有問題");
			e.printStackTrace();
		}
//		System.out.println(collectionClassfi.toString()); // 測試資料
		return collectionClassfi;
	}
	
	
	
	// 不確定是不是這樣寫, 抓取買家登入資訊的 memberId, 還有操作對象的 productId
	public String switchStateCollectionedByProductId(String memberId, String prouductId) {

		System.out.println("進入switchStateCollectionedByProductId service");
		// 取得連線
		JedisPool jedisPool = JedisUtil.getJedisPool();

		// 本方法會用到的變數
		String redisKey = "collection:" + memberId;
		String returnMessage = "";
		// 測試Key用的參數
		System.out.println("redisKey=" + redisKey);

		// 索取redis連線, 用try 整個包起來
		try (
			// 從 Redis 中讀取資料 並且指定為db10 試圖分流分類資料		
			Jedis jedis = jedisPool.getResource()) {
			jedis.select(10);

			List<String> memberCollection = jedis.lrange(redisKey, 0, -1);
			// 判斷 Jedis 該用戶是否已經有登錄該商品收藏
			if (memberCollection.contains(prouductId)) {
				memberCollection.remove(prouductId); // 如果存在prouductId，则移除
				returnMessage = "已將商品移出收藏";
			} else {
				memberCollection.add(prouductId); // 如果不存在prouductId，则新增
				returnMessage = "已將商品加入收藏";
			}

			// 如果已經存在該用戶的收藏清單, 則清除並加入修改過的清單
			if (jedis.exists(redisKey)) {
				jedis.del(redisKey); // 刪除已存在的 key
			}
			for (String s : memberCollection) {
				jedis.lpush(redisKey, s);
			}
			
		} catch (Exception e) {
			System.out.println("從redis讀出資料有問題");
			e.printStackTrace();
		}
		return returnMessage;
	}
	
	
	
	//拿取用戶的收藏資訊, 以為標準比較當前ProductId 的收藏狀態, 並回傳boolean結果
	public Boolean checkIfProductCollectioned(String memberId, String productId) {

		// 取得連線
		JedisPool jedisPool = JedisUtil.getJedisPool();

		// 本方法會用到的變數
		Boolean isProductCollectioned = false;
		String redisKey = "collection:" + memberId;
		//測試Key用的參數
		System.out.println("redisKey=" + redisKey);

		// 索取redis連線, 用try 整個包起來
		try (

			// 從 Redis 中讀取資料 並且指定為db10
			Jedis jedis = jedisPool.getResource()) {
			jedis.select(10);
			// 检查指定 key 中有沒有存在這個商品?
			List<String> productList = jedis.lrange(redisKey, 0, -1);
			isProductCollectioned = productList.contains(productId);
			
		} catch (Exception e) {
			System.out.println("從redis讀出資料有問題");
			e.printStackTrace();
		}
		System.out.println( "回傳了,isProductCollectioned=" + isProductCollectioned ); //測試資料
		return isProductCollectioned;
	}
	

}
