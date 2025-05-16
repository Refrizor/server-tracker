package me.devin.tasks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.devin.PlayerSessionTracker;
import me.devin.ServerTrackerPlugin;
import me.devin.api.clients.ServerClient;
import me.devin.api.models.ServerUpdateRequest;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class HeartbeatTask extends BukkitRunnable {
    private final ServerClient serverClient;
    private final PlayerSessionTracker playerSessionTracker;

    public HeartbeatTask(ServerClient serverClient, PlayerSessionTracker playerSessionTracker) {
        this.serverClient = serverClient;
        this.playerSessionTracker = playerSessionTracker;
    }

    @Override
    public void run() {
        serverClient.update();
        ObjectMapper mapper = new ObjectMapper();

        Map<UUID, ServerUpdateRequest.PlayerSession> sessions = playerSessionTracker.getSessionCache();
        try {
            String json = mapper.writeValueAsString(sessions);
            ServerTrackerPlugin.getInstance().getLogger().info("Sending heartbeat. Session Cache Contents: " + json);
        } catch (JsonProcessingException e) {
            ServerTrackerPlugin.getInstance().getLogger().severe("Failed to serialize session cache: " + e.getMessage());
        }
    }
}