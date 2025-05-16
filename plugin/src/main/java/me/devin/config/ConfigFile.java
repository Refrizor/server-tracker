package me.devin.config;

public enum ConfigFile {
    MAIN_CONFIG("config.yml", "configs/config.yml", new YamlLoader());

    private final String relativePath;
    private final String resourcePath;
    private final ConfigLoader<? extends Config> loader;

    ConfigFile(String relativePath, String resourcePath, ConfigLoader<? extends Config> loader) {
        this.relativePath = relativePath;
        this.resourcePath = resourcePath;
        this.loader = loader;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public ConfigLoader<? extends Config> getLoader() {
        return loader;
    }
}