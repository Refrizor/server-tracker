package me.devin.modules;

import com.google.inject.AbstractModule;
import me.devin.ServerTrackerPlugin;
import org.bukkit.Server;

import java.nio.file.Path;

public class AppModule extends AbstractModule {
    private final Path dataDirectory;

    public AppModule(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    @Override
    protected void configure() {
        bind(ServerTrackerPlugin.class).toInstance(ServerTrackerPlugin.getInstance());
        bind(Server.class).toProvider(() -> ServerTrackerPlugin.getInstance().getServer());

        install(new ConfigModule(dataDirectory));
        install(new CommonModule());
        install(new ServersModule());
        install(new RedisModule());
    }
}
