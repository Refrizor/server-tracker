package me.devin.config;

import java.io.IOException;
import java.io.InputStream;

public interface ConfigLoader<T extends Config> {
    T load(InputStream input) throws IOException;
}
