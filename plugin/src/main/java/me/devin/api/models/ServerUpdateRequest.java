package me.devin.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class ServerUpdateRequest {
    @JsonProperty("playerCount")
    private int playerCount;
    @JsonProperty("playerList")
    private List<PlayerSession> playerList;
    @JsonProperty("lastHeartbeat")
    private long lastHeartbeat;

    // Getters and Setters
    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public List<PlayerSession> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<PlayerSession> playerList) {
        this.playerList = playerList;
    }

    public long getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(long lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    public record PlayerSession(@JsonProperty("uuid") UUID uuid, @JsonProperty("username") String username,
                                @JsonProperty("joinedAt") long joinedAt, @JsonProperty("vanished") boolean vanished) {
        public PlayerSession(UUID uuid, String username, long joinedAt, boolean vanished) {
            this.uuid = uuid;
            this.username = username;
            this.joinedAt = joinedAt;
            this.vanished = vanished;
        }

        // Getters
        @Override
        public UUID uuid() {
            return uuid;
        }

        @Override
        public String username() {
            return username;
        }

        @Override
        public long joinedAt() {
            return joinedAt;
        }

        @Override
        public boolean vanished() {
            return vanished;
        }
    }
}
