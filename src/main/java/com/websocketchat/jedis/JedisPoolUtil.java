package com.websocketchat.jedis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtil {
	private static JedisPool pool = null;

	private JedisPoolUtil() {
	}

	public static JedisPool getJedisPool() {
		if (pool == null) {   //  第一次執行不一定能確認只有一個執行序進入
			synchronized (JedisPoolUtil.class) {  // synchronized 就只會有一個執行序進入
				if (pool == null) {  //  第一個執行序進來就會先創建pool , 所以第二個執行序就不需要再創建直接return pool
					JedisPoolConfig config = new JedisPoolConfig();
					config.setMaxTotal(8);  //如果大量使用redis改數字
					config.setMaxIdle(8);
					config.setMaxWaitMillis(10000);
					pool = new JedisPool(config, "localhost", 6379);
				}
			}
		}
		return pool;
	}

	// 可在ServletContextListener的contextDestroyed裡呼叫此方法註銷Redis連線池
	public static void shutdownJedisPool() {
		if (pool != null)
			pool.destroy();
	}
}
