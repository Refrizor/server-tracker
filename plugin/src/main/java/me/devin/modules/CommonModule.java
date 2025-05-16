package me.devin.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.devin.api.ApiKeyInterceptor;
import me.devin.config.ConfigFile;
import me.devin.config.ConfigurationManager;
import okhttp3.OkHttpClient;

public class CommonModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ApiKeyInterceptor.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    @Named("ApiKey")
    public String provideApiKey(ConfigurationManager configManager) {
        String apiKey = configManager.getConfig(ConfigFile.MAIN_CONFIG).getString("api.key");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Missing required property: api.key. Please check config.yml.");
        }
        return apiKey;
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(ApiKeyInterceptor apiKeyInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(apiKeyInterceptor)
                .build();
    }
}