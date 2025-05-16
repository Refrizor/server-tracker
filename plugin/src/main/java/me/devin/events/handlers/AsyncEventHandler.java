package me.devin.events.handlers;

import me.devin.ServerTrackerPlugin;
import me.devin.events.payloads.EventPayload;

import java.util.concurrent.CompletableFuture;

public class AsyncEventHandler<T extends EventPayload> implements EventHandler<T> {
    private final EventHandler<T> delegate;

    public AsyncEventHandler(EventHandler<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void handle(T event) {
        CompletableFuture.runAsync(() -> {
            try {
                delegate.handle(event);
            } catch (Exception e) {
                ServerTrackerPlugin.getInstance().getLogger().warning("Async event handling failed: " + e.getMessage());
            }
        });
    }

    @Override
    public Class<T> getPayloadType() {
        return delegate.getPayloadType();
    }
}
