package me.devin.config;

import java.util.Map;

@SuppressWarnings("unchecked")
public class YamlConfig implements Config {
    private final Map<String, Object> data;

    public YamlConfig(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public Object get(String key) {
        String[] parts = key.split("\\.");
        Object current = data;
        for (String part : parts) {
            if (current instanceof Map) {
                current = ((Map<String, Object>) current).get(part);
            } else {
                return null;
            }
        }
        return current;
    }
}
