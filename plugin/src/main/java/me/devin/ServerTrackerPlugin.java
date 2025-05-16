package me.devin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import me.devin.api.clients.ServerClient;
import me.devin.api.models.ServerRegisterRequest;
import me.devin.config.ConfigFile;
import me.devin.config.ConfigurationManager;
import me.devin.environment.Environment;
import me.devin.events.EventDispatcher;
import me.devin.events.RedisSubscriber;
import me.devin.events.handlers.AsyncEventHandler;
import me.devin.events.handlers.UpdatePlayerCountHandler;
import me.devin.events.middleware.LoggingMiddleware;
import me.devin.events.middleware.ValidationMiddleware;
import me.devin.events.paper.registrar.EventRegistrar;
import me.devin.modules.AppModule;
import me.devin.redis.RedisProvider;
import me.devin.tasks.HeartbeatTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class ServerTrackerPlugin extends JavaPlugin {
    private static final String TYPE = "spigot";
    private static ServerTrackerPlugin serverTrackerPlugin;
    private Injector injector;
    private String serverName;
    private String apiUrl;
    private UUID serverId;
    private Environment environment;

    public static ServerTrackerPlugin getInstance() {
        return serverTrackerPlugin;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        serverTrackerPlugin = this;
        injector = Guice.createInjector(new AppModule(this.getDataFolder().toPath()));

        // Retrieve and set plugin-wide values
        ConfigurationManager configurationManager = injector.getInstance(ConfigurationManager.class);
        apiUrl = injector.getInstance(Key.get(String.class, Names.named("BaseApiUrl")));
        serverId = UUID.fromString(configurationManager.getConfig(ConfigFile.MAIN_CONFIG).getString("server.uuid"));
        serverName = configurationManager.getConfig(ConfigFile.MAIN_CONFIG).getString("server.name");
        environment = Environment.valueOf(configurationManager.getConfig(ConfigFile.MAIN_CONFIG).getString("server.environment"));

        // Get guice instance and register the server
        ServerClient serverClient = injector.getInstance(ServerClient.class);
        ServerRegisterRequest serverRegisterRequest = new ServerRegisterRequest(serverId, serverName, TYPE, environment.name(), Bukkit.getVersion(), getServer().getPort());
        serverClient.register(serverRegisterRequest);

        // Start the scheduler
        startHeartbeatScheduler(serverClient);

        // Initialize Redis
        initializeRedis();

        // Register join and quit events
        EventRegistrar eventRegistrar = new EventRegistrar(serverTrackerPlugin, injector);
        eventRegistrar.registerEvents();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        injector.getInstance(RedisProvider.class).shutdown();
    }

    /**
     * Starts a repeating non-blocking task to send heartbeats every 15 seconds.
     */
    private void startHeartbeatScheduler(ServerClient serverClient) {
        new HeartbeatTask(serverClient, injector.getInstance(PlayerSessionTracker.class)).runTaskTimerAsynchronously(this, 0L, 20 * 3); // Every *3 seconds
    }

    private void initializeRedis() {
        RedisProvider redisProvider = injector.getInstance(RedisProvider.class);
        EventDispatcher eventDispatcher = new EventDispatcher(redisProvider);

        eventDispatcher.registerHandler("global.playercount", new AsyncEventHandler<>(new UpdatePlayerCountHandler()));

        eventDispatcher.addMiddleware(new LoggingMiddleware());
        eventDispatcher.addMiddleware(new ValidationMiddleware());

        // Start Redis subscriber using RedisProvider
        RedisSubscriber subscriber = new RedisSubscriber(eventDispatcher, redisProvider);
        subscriber.subscribeToChannels("global.playercount");

        getLogger().info("Event system initialized successfully!");
    }

    public String getServerName() {
        return serverName;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public UUID getServerId() {
        return serverId;
    }

    public Environment getEnvironment() {
        return environment;
    }
}
