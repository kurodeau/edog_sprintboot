package com.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import com.google.gson.Gson;
import java.util.TreeSet;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import com.product.model.*;

public class JedisUtil {
	// Singleton單例模式, 使連線池是唯一存在
	private static JedisPool pool = null;

	private JedisUtil() {
	}

	// static 方法, 可直接被呼叫
	public static JedisPool getJedisPool() {
		// double lock
		// first lock, check pool is null
		if (pool == null) {
			// second lock, check just one tread doing this init pool work
			synchronized (JedisUtil.class) {
				if (pool == null) {
					JedisPoolConfig config = new JedisPoolConfig();
					// 設定最大連線總數
					config.setMaxTotal(30);
					// 設定最大閒置連線允許數
					config.setMaxIdle(30);
					// 設定最大連線等待時間, 若等待超過沒有拿到連線則傳回Jedis連線錯誤的例外
					config.setMaxWaitMillis(10000);
					pool = new JedisPool(config, "localhost", 6379);
				}
			}
		}
		return pool;
	}

	// 關閉server前, 銷毀已經創立的pool
	public static void shutdownJedisPool() {
		if (pool != null)
			pool.destroy();
	}

//	public static void main(String[] args) {
//		// 取得 JedisPool 實例
//
//		JedisPool jedisPool = JedisUtil.getJedisPool();
//
//		// 從 JedisPool 中取得 Jedis 連線
//		try (Jedis jedis = jedisPool.getResource()) {
//			//塞入假資料, 直接存VO, 不好的方法?
////			TreeSet<ProductVO> collection = new TreeSet<>();			
////			for (int i= 1; i<= 5; i++) {
////				ProductVO product = new ProductVO();
////				product.setProductId(i);
////	            product.setProductName("Product " + (i + 1));
////	            product.setProductPrice(BigDecimal.valueOf(10 * (i + 1)));
////	            product.setProductStockQuantity(100 + i);
////	            product.setProductDetails("This is product " + (i + 1));
////	            product.setProductStatus("Active");
////	            product.setProductCreationTime(new Timestamp(System.currentTimeMillis()));
////	            product.setTotalStars(0);
////	            product.setTotalReviews(0);
////	            product.setIsEnabled(true);
////				collection.add(product);
////			}
//
//			//塞入假資料, 只存商品編號 拿出來還要處理?
//			TreeSet<Integer> collection = new TreeSet<>();
//			collection.add(1);
//			collection.add(3);
//			collection.add(4);
//			
//			// 將資料寫入 Redis 並且指定為db10 試圖分流分類資料
//			jedis.select(10);
//			String json = new Gson().toJson(collection);
//			// 模擬把用戶9001 的收藏清單包成json 後傳到redis
//			jedis.set("9", json);
//			System.out.println("應該有存入資料了");
//
//		} catch (Exception e) {
//			System.out.println("不知道為啥, 存入資料失敗");
//			e.printStackTrace();
//		}
//
//		// 從 JedisPool 中取得 Jedis 連線
//		try (Jedis jedis = jedisPool.getResource()) {
//			// 從 Redis 中讀取資料 並且指定為db10 試圖分流分類資料
//			jedis.select(10);
//			String value = jedis.get("9");
//			System.out.println("買家編號9的value:" + value);
//			System.out.println("應該有印出資料了");
//		} catch (Exception e) {
//			System.out.println("讀出資料有問題");
//			e.printStackTrace();
//		}
//
//		// 釋放 JedisPool 資源
//		JedisUtil.shutdownJedisPool();
//	}
	
	
}