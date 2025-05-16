package me.devin.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader implements ConfigLoader<PropertiesConfig> {
    @Override
    public PropertiesConfig load(InputStream input) throws IOException {
        Properties properties = new Properties();
        properties.load(input);
        return new PropertiesConfig(properties);
    }
}