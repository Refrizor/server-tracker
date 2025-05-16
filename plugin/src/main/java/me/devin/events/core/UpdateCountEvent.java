package me.devin.events.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import me.devin.events.payloads.EventPayload;

import java.util.UUID;

public class UpdateCountEvent extends EventPayload {
    private final int visible;
    private final int actual;
    @JsonCreator
    public UpdateCountEvent(
            @JsonProperty("senderId") String senderId,
            @JsonProperty("eventId") UUID eventId,
            @JsonProperty("visible") int visible,
            @JsonProperty("actual") int actual
    ) {
        super(senderId, eventId);
        this.visible = visible;
        this.actual = actual;
    }

    public int getVisible() {
        return visible;
    }

    public int getActual() {
        return actual;
    }
}