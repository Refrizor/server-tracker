package me.devin;

import me.devin.api.models.ServerUpdateRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerSessionTracker {
    private final HashMap<UUID, ServerUpdateRequest.PlayerSession> sessionCache;

    // Debugging purposes: do NOT recommend global access
    public HashMap<UUID, ServerUpdateRequest.PlayerSession> getSessionCache() {
        return sessionCache;
    }

    public PlayerSessionTracker() {
        this.sessionCache = new HashMap<>();
    }

    public void onPlayerJoin(UUID uuid, String username, boolean vanished) {
        long joinedAt = System.currentTimeMillis() / 1000L;
        sessionCache.put(uuid, new ServerUpdateRequest.PlayerSession(uuid, username, joinedAt, vanished));
    }

    public void onPlayerQuit(UUID uuid) {
        sessionCache.remove(uuid);
    }

    public List<ServerUpdateRequest.PlayerSession> getActiveSessions() {
        return new ArrayList<>(sessionCache.values());
    }
}
