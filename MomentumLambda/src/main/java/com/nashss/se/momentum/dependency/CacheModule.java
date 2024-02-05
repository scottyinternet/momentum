//package com.nashss.se.momentum.dependency;
//
//
//import dagger.Module;
//import dagger.Provides;
//
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.JedisPoolConfig;
//
//import javax.inject.Singleton;
//
//@Module
//public class CacheModule {
//
//	@Provides
//	@Singleton
//	public JedisPool provideJedisPool(final JedisPoolConfig jedisPoolConfig) {
//		final String redisEndpoint = "momentum-cache-guxapv.serverless.use2.cache.amazonaws.com:6379";
//		final int redisPort = 6379;
//		return new JedisPool(jedisPoolConfig, redisEndpoint, redisPort);
//	}
//}
