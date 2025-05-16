package me.devin.events.middleware;

import me.devin.ServerTrackerPlugin;
import me.devin.events.payloads.EventPayload;

public class LoggingMiddleware implements EventMiddleware {
    @Override
    public EventPayload process(EventPayload event) {
        ServerTrackerPlugin.getInstance().getLogger().info("[LOG] Received event: " + event.getEventId());
        return event; // Pass event through
    }
}