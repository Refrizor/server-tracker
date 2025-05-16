package me.devin.config;

import java.util.List;
import java.util.stream.Collectors;

public interface Config {
    /**
     * Retrieves the raw value associated with the given key.
     * For YAML implementations, keys may use dot notation (e.g. "database.host").
     *
     * @param key the configuration key
     * @return the raw value (or null if not found)
     */
    Object get(String key);

    /**
     * Retrieves the value associated with the given key and converts it to the specified type.
     *
     * @param key   the configuration key
     * @param clazz the target class
     * @param <T>   the target type
     * @return the converted value
     */
    default <T> T get(String key, Class<T> clazz) {
        Object value = get(key);
        return ConversionUtils.convert(value, clazz);
    }

    // Convenience methods:
    default String getString(String key) {
        return get(key, String.class);
    }

    default Integer getInt(String key) {
        return get(key, Integer.class);
    }

    default Double getDouble(String key) {
        return get(key, Double.class);
    }

    default Boolean getBoolean(String key) {
        return get(key, Boolean.class);
    }

    default List<String> getStringList(String key) {
        Object value = get(key);
        if (value instanceof List<?>) {
            return ((List<?>) value).stream()
                    .filter(item -> item instanceof String)
                    .map(item -> (String) item)
                    .collect(Collectors.toList());
        }
        return null;
    }
}