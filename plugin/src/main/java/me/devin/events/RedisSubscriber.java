package me.devin.events;

import me.devin.redis.RedisProvider;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class RedisSubscriber extends JedisPubSub {
    private final EventDispatcher dispatcher;
    private final JedisPool jedisPool;

    public RedisSubscriber(EventDispatcher dispatcher, RedisProvider redisProvider) {
        this.dispatcher = dispatcher;
        this.jedisPool = redisProvider.getJedisPool();
    }

    @Override
    public void onMessage(String channel, String message) {
        System.out.println("[RedisSubscriber] Received message on channel: " + channel);
        System.out.println("[DEBUG] Raw message from Redis: " + message);

        dispatcher.dispatch(channel, message);
    }

    public void subscribeToChannels(String... channels) {
        new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) { // Use JedisPool connection
                System.out.println("[RedisSubscriber] Subscribing to channels...");
                jedis.subscribe(this, channels);
            } catch (Exception e) {
                System.err.println("[RedisSubscriber] Failed to subscribe to channels: " + e.getMessage());
            }
        }).start();
    }
}
