package com.redis;

import java.util.List;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

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

	
	//測試用的main 方法
//	public void test(String[] args) {
//		// 取得 JedisPool 實例
//
//		JedisPool jedisPool = JedisUtil.getJedisPool();
//
//		// 從 JedisPool 中取得 Jedis 連線
//		try (Jedis jedis = jedisPool.getResource()) {
//
//			
//			// 將資料寫入 Redis 並且指定為db10 試圖分流分類資料
//			jedis.select(10);
//
//			jedis.lpush("9", "1", "3", "4");
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
//			List<String> value;
//			
//			for( int i= 0; i< jedis.llen("9"); i++ ) {
//				System.out.println( "Key" + 9 + "的值" + jedis.lindex("9", i) );
//			}
//			
//		} catch (Exception e) {
//			System.out.println("讀出資料有問題");
//			e.printStackTrace();
//		}
//
//		// 釋放 JedisPool 資源
//		JedisUtil.shutdownJedisPool();
//	}
	
	
}