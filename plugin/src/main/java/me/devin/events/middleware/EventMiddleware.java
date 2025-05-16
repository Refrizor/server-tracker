package me.devin.events.middleware;

import me.devin.events.payloads.EventPayload;

public interface EventMiddleware {
    EventPayload process(EventPayload event);
}
