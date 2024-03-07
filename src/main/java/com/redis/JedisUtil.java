package com.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import com.google.gson.Gson;
import java.util.TreeSet;
import java.util.Set;

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
//			TreeSet<Integer> collection = new TreeSet<>();
//			collection.add(101);
//			collection.add(133);
//			collection.add(9);
//			collection.add(101);
//			
//			// 將資料寫入 Redis 並且指定為db10 試圖分流分類資料
//			jedis.select(10);
//			String json = new Gson().toJson(collection);
//			jedis.set("買家編號1", json);
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
//			String value = jedis.get("買家編號1");
//			System.out.println("Value of 買家編號1: " + value);
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