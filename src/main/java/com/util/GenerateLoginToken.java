package com.util;

import java.util.Optional;

import redis.clients.jedis.Jedis;

public class GenerateLoginToken {

	
    private static final GenerateLoginToken INSTANCE = new GenerateLoginToken();

    private GenerateLoginToken() {
    }
    
    public static GenerateLoginToken getInstance() {
        return INSTANCE;
    }
    
    
	public static Optional<StringBuilder> generateRedisToken(String name, long timesecond) {
		Jedis jedis = null;
		StringBuilder sb = new StringBuilder();
		try {
			jedis = JedisUtil.getJedisPool().getResource();
			jedis.select(15);
			sb = returnAuthCode();
			jedis.setex(name, (int) timesecond, sb.toString());

			return Optional.of(sb);
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	private static StringBuilder returnAuthCode() {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= 8; i++) {
			int condition = (int) (Math.random() * 3) + 1;
			switch (condition) {
			case 1:
				char c1 = (char) ((int) (Math.random() * 26) + 65);
				sb.append(c1);
				break;
			case 2:
				char c2 = (char) ((int) (Math.random() * 26) + 97);
				sb.append(c2);
				break;
			case 3:
				sb.append((int) (Math.random() * 10));
			}
		}
		return sb;
	}

}
