package me.devin.config;

import java.util.Properties;

public class PropertiesConfig implements Config {
    private final Properties properties;

    public PropertiesConfig(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Object get(String key) {
        // For properties files, we assume keys are flat.
        return properties.getProperty(key);
    }
}
