package me.devin.events.paper.registrar;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.devin.ServerTrackerPlugin;
import me.devin.events.paper.EventJoin;
import me.devin.events.paper.EventQuit;
import org.bukkit.plugin.PluginManager;

public class EventRegistrar {
    private final ServerTrackerPlugin plugin;
    private final Injector injector;

    @Inject
    public EventRegistrar(ServerTrackerPlugin plugin, Injector injector) {
        this.plugin = plugin;
        this.injector = injector;
    }

    public void registerEvents() {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        // Get instances with Guice
        EventJoin eventJoin = injector.getInstance(EventJoin.class);
        EventQuit eventQuit = injector.getInstance(EventQuit.class);

        // Register the event listener with the plugin manager
        pluginManager.registerEvents(eventJoin, plugin);
        pluginManager.registerEvents(eventQuit, plugin);
    }
}