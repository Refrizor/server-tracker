package me.devin.events.payloads;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public abstract class EventPayload {
    private final String senderId;
    private final UUID eventId;

    protected EventPayload(String senderId, UUID eventId) {
        this.senderId = senderId;
        this.eventId = eventId;
    }

    public String getSenderId() { return senderId; }
    public UUID getEventId() { return eventId; }

    public String toPayloadString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
    }

    public static <T extends EventPayload> T fromPayloadString(String payload, Class<T> payloadClass) {
        try {
            return new ObjectMapper().readValue(payload, payloadClass);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize payload", e);
        }
    }
}