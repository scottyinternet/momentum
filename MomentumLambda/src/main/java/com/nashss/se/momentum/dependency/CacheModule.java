package com.nashss.se.momentum.dependency;


import dagger.Module;
import dagger.Provides;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.inject.Singleton;
import java.time.Duration;

@Module
public class CacheModule {

	@Provides
	@Singleton
	public JedisPool provideJedisPool() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();

		poolConfig.setTestWhileIdle(true);
		poolConfig.setMinEvictableIdleTime(Duration.ofMillis(60000));
		poolConfig.setTimeBetweenEvictionRuns(Duration.ofMillis(30000));
		poolConfig.setNumTestsPerEvictionRun(-1);

		return new JedisPool(poolConfig, "momentum-redis-guxapv.serverless.use2.cache.amazonaws.com", 6379);
	}
}
