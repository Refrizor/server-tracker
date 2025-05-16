package me.devin.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.devin.config.ConfigFile;
import me.devin.config.ConfigurationManager;

import java.io.IOException;
import java.nio.file.Path;

public class ConfigModule extends AbstractModule {
    private final Path dataDirectory;

    public ConfigModule(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    @Override
    protected void configure() {
        ConfigurationManager configManager = new ConfigurationManager(dataDirectory);

        try {
            for (ConfigFile configFile : ConfigFile.values()) {
                configManager.loadConfig(configFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configurations", e);
        }

        bind(ConfigurationManager.class).toInstance(configManager);
    }

    @Provides
    @Named("BaseApiUrl")
    @Singleton
    public String provideBaseApiUrl(ConfigurationManager configurationManager) {
        String baseUrl = configurationManager.getConfig(ConfigFile.MAIN_CONFIG).getString("api.url");

        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalStateException("Missing required property: api.url");
        }

        return baseUrl;
    }
}