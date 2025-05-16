package me.devin.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationManager {
    private final Path dataDirectory;
    private final Map<ConfigFile, Config> configs = new HashMap<>();

    public ConfigurationManager(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
        // Ensure the data directory exists.
        try {
            if (!Files.exists(dataDirectory)) {
                Files.createDirectories(dataDirectory);
                System.out.println("Created data directory: " + dataDirectory);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create data directory: " + dataDirectory, e);
        }
    }

    /**
     * Loads the specified configuration file. If the file does not exist in the data directory,
     * it is copied from the default resource.
     *
     * @param configFile the configuration file to load
     * @param <T>        the type of configuration (a subclass of Config)
     * @return the loaded configuration
     * @throws IOException if an error occurs during loading
     */
    @SuppressWarnings("unchecked")
    public <T extends Config> T loadConfig(ConfigFile configFile) throws IOException {
        Path targetPath = dataDirectory.resolve(configFile.getRelativePath());

        if (!Files.exists(targetPath)) {
            copyDefaultFile(configFile, targetPath);
        }

        try (InputStream input = Files.newInputStream(targetPath)) {
            T config = (T) configFile.getLoader().load(input);
            configs.put(configFile, config);
            System.out.println("Loaded configuration: " + configFile.name());
            return config;
        }
    }

    private void copyDefaultFile(ConfigFile configFile, Path targetPath) throws IOException {
        String resourcePath = "/" + configFile.getResourcePath();
        System.out.println("Looking for default resource: " + resourcePath);
        try (InputStream input = getClass().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IllegalStateException("Default configuration resource not found: " + resourcePath);
            }
            if (targetPath.getParent() != null) {
                Files.createDirectories(targetPath.getParent());
            }
            Files.copy(input, targetPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Copied default configuration to: " + targetPath);
        }
    }

    /**
     * Retrieves a previously loaded configuration.
     *
     * @param configFile the configuration file
     * @param <T>        the type of configuration
     * @return the loaded configuration
     */
    @SuppressWarnings("unchecked")
    public <T extends Config> T getConfig(ConfigFile configFile) {
        T config = (T) configs.get(configFile);
        if (config == null) {
            throw new IllegalStateException("Configuration " + configFile.name() + " has not been loaded.");
        }
        return config;
    }
}