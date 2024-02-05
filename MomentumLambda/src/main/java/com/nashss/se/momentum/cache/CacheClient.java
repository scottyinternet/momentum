package com.nashss.se.momentum.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.inject.Inject;

public class CacheClient {
	private final JedisPool jedisPool;
	private final int DEFUALT = 28000; // 8 hours

	@Inject
	public CacheClient(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public void setValue(String key, int seconds, String val) {
		try (Jedis jedis = jedisPool.getResource()) {
			System.out.println("CACHE::SetValue::" + val);
			jedis.setex(key, seconds, val);
		}
	}

	public void setValueWithDefaultExpiration(String key, String val) {
		try (Jedis jedis = jedisPool.getResource()) {
			System.out.println("CACHE::setValueWithDefaultExpiration::" + val);
			jedis.setex(key, defaultOrEndOfDay(), val);
		}
	}

	public String getValue(String key) {
		try (Jedis jedis = jedisPool.getResource()) {
			System.out.println("CACHE::getValue::" + key);
			return jedis.get(key);
		}
	}

	public void deleteValue(String key) {
		try (Jedis jedis = jedisPool.getResource()) {
			System.out.println("CACHE::deleteValue::" + key);
			jedis.del(key);
		}
	}

	private int defaultOrEndOfDay() {
		return DEFUALT;
	}
}
