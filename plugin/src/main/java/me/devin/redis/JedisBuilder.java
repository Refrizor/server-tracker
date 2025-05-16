package me.devin.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisBuilder {
    private String redisAddress;
    private int redisPort;
    private String redisPassword;

    JedisBuilder(){}

    public JedisBuilder(String redisAddress, int redisPort) {
        this.redisAddress = redisAddress;
        this.redisPort = redisPort;
    }

    public JedisBuilder setPassword(String redisPassword) {
        this.redisPassword = redisPassword;
        return this;
    }

    public JedisPool build() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(64);
        poolConfig.setMinIdle(16);

        return new JedisPool(poolConfig, redisAddress, redisPort, 2000, redisPassword);
    }
}