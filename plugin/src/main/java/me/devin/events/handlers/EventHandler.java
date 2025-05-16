package me.devin.events.handlers;

import me.devin.events.payloads.EventPayload;

public interface EventHandler<T extends EventPayload> {
    void handle(T event);
    Class<T> getPayloadType();
}