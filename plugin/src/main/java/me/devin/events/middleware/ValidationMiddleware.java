package me.devin.events.middleware;

import me.devin.events.payloads.EventPayload;

public class ValidationMiddleware implements EventMiddleware {
    @Override
    public EventPayload process(EventPayload event) {
        if (event.getEventId() == null || event.getSenderId() == null) {
            throw new IllegalArgumentException("Invalid event: missing eventId or senderId.");
        }
        return event;
    }
}
