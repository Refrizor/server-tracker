package me.devin.events.middleware;

import me.devin.events.EventDispatcher;
import me.devin.redis.RedisProvider;
import redis.clients.jedis.Jedis;

public class DeadLetterQueue {
    private static final String DLQ_KEY = "dlq";

    public static void saveFailedEvent(RedisProvider redisProvider, String eventName, String message) {
            redisProvider.getJedisPool().getResource().lpush(DLQ_KEY, eventName + ":" + message);
            System.err.println("Saved failed event to DLQ: " + eventName);
    }

    public static void retryFailedEvents(RedisProvider redisProvider, EventDispatcher dispatcher) {
        try(Jedis jedis = redisProvider.getJedisPool().getResource()){
            while (jedis.llen(DLQ_KEY) > 0) {
                String eventEntry = jedis.rpop(DLQ_KEY);
                if (eventEntry != null) {
                    String[] parts = eventEntry.split(":", 2);
                    dispatcher.dispatch(parts[0], parts[1]);
                }
            }
        }
    }
}
