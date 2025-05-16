package me.devin.events;

import me.devin.events.handlers.EventHandler;
import me.devin.events.middleware.DeadLetterQueue;
import me.devin.events.middleware.EventMiddleware;
import me.devin.events.payloads.EventPayload;
import me.devin.redis.RedisProvider;

import java.util.List;
import java.util.concurrent.*;

public class EventDispatcher {
    private final ConcurrentMap<String, EventHandler<? extends EventPayload>> handlers = new ConcurrentHashMap<>();
    private final List<EventMiddleware> middlewares = new CopyOnWriteArrayList<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final RedisProvider redisProvider;

    public <T extends EventPayload> void registerHandler(String eventName, EventHandler<T> handler) {
        handlers.put(eventName, handler);
    }

    public EventDispatcher(RedisProvider redisProvider) {
        this.redisProvider = redisProvider;
    }

    public void addMiddleware(EventMiddleware middleware) {
        middlewares.add(middleware);
    }

    public void dispatch(String eventName, String message) {
        EventHandler<? extends EventPayload> handler = handlers.get(eventName);
        if (handler == null) {
            System.err.println("No handler found for event: " + eventName);
            return;
        }

        executor.submit(() -> {
            try {
                EventPayload event = EventPayload.fromPayloadString(message, handler.getPayloadType());

                // Apply middleware processing
                for (EventMiddleware middleware : middlewares) {
                    event = middleware.process(event);
                }

                @SuppressWarnings("unchecked")
                EventHandler<EventPayload> safeHandler = (EventHandler<EventPayload>) handler;
                safeHandler.handle(event);
            } catch (Exception e) {
                System.err.println("Handler execution failed: " + e.getMessage());
                DeadLetterQueue.saveFailedEvent(redisProvider, eventName, message);
            }
        });
    }
}
