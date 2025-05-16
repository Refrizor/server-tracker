package me.devin.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import me.devin.config.ConfigFile;
import me.devin.config.ConfigurationManager;
import me.devin.redis.JedisBuilder;
import me.devin.redis.RedisProvider;
import redis.clients.jedis.JedisPool;

public class RedisModule extends AbstractModule {
    @Override
    protected void configure() {
        // Bind RedisProvider as an eager singleton
        bind(RedisProvider.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    public JedisBuilder provideJedisBuilder(ConfigurationManager configurationManager) {
        // Retrieve configuration properties
        String redisAddress = configurationManager.getConfig(ConfigFile.MAIN_CONFIG).getString("redis.address");
        String redisPassword = configurationManager.getConfig(ConfigFile.MAIN_CONFIG).getString("redis.password");
        int redisPort = configurationManager.getConfig(ConfigFile.MAIN_CONFIG).getInt("redis.port");

        // Create and configure JedisBuilder
        JedisBuilder jedisBuilder = new JedisBuilder(redisAddress, redisPort);
        if (redisPassword != null && !redisPassword.isEmpty()) {
            jedisBuilder.setPassword(redisPassword);
        }

        return jedisBuilder;
    }

    @Provides
    @Singleton
    public JedisPool provideJedisPool(JedisBuilder jedisBuilder) {
        // Build and return the JedisPool
        return jedisBuilder.build();
    }
}