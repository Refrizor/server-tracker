package me.devin.config;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@SuppressWarnings("unchecked")
public class YamlLoader implements ConfigLoader<YamlConfig> {
    private final Yaml yaml = new Yaml();

    @Override
    public YamlConfig load(InputStream input) throws IOException {
        Object loaded = yaml.load(input);

        if (loaded instanceof Map) {
            return new YamlConfig((Map<String, Object>) loaded);
        }
        throw new IOException("Invalid YAML format: expected a mapping at the root.");
    }
}