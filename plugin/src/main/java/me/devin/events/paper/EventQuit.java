package me.devin.events.paper;

import com.google.inject.Inject;
import me.devin.PlayerSessionTracker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class EventQuit implements Listener {
    private final PlayerSessionTracker sessionTracker;

    @Inject
    public EventQuit(PlayerSessionTracker sessionTracker) {
        this.sessionTracker = sessionTracker;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // Remove from list
        sessionTracker.onPlayerQuit(uuid);
    }
}