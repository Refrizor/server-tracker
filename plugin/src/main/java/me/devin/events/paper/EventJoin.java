package me.devin.events.paper;

import com.google.inject.Inject;
import me.devin.PlayerSessionTracker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class EventJoin implements Listener {
    private final PlayerSessionTracker sessionTracker;

    @Inject
    public EventJoin(PlayerSessionTracker sessionTracker) {
        this.sessionTracker = sessionTracker;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String username = player.getName();

        sessionTracker.onPlayerJoin(uuid, username, false);
    }
}