package me.devin.events;

import me.devin.events.payloads.EventPayload;
import me.devin.redis.RedisProvider;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisPublisher {
    private final JedisPool jedisPool;

    public RedisPublisher(RedisProvider redisProvider) {
        this.jedisPool = redisProvider.getJedisPool();
    }

    public void publish(String channel, EventPayload event) {
        try (Jedis jedis = jedisPool.getResource()) { // Use connection from pool
            jedis.publish(channel, event.toPayloadString());
        } catch (Exception e) {
            System.err.println("[RedisPublisher] Failed to publish event: " + e.getMessage());
        }
    }
}