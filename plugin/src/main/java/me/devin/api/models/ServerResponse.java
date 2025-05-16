package me.devin.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class ServerResponse {
    @JsonProperty("serverId")
    private UUID serverId;
    @JsonProperty("serverName")
    private String serverName;
    @JsonProperty("status")
    private String status;
    @JsonProperty("playerCount")
    private int playerCount;
    @JsonProperty("lastHeartbeat")
    private long lastHeartbeat;

    public UUID getServerId() {
        return serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public String getStatus() {
        return status;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public long getLastHeartbeat() {
        return lastHeartbeat;
    }
}
