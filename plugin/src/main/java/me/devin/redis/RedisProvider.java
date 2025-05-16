package me.devin.redis;

import com.google.inject.Inject;
import redis.clients.jedis.JedisPool;

public class RedisProvider {
    private final JedisPool jedisPool;

    @Inject
    public RedisProvider(JedisBuilder jedisBuilder) {
        this.jedisPool = jedisBuilder.build();
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void shutdown() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }
}