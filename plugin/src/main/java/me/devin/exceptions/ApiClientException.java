package me.devin.exceptions;

import me.devin.ServerTrackerPlugin;

public class ApiClientException extends RuntimeException {
    public ApiClientException(String message, Throwable cause) {
        super(message, cause);
        ServerTrackerPlugin.getInstance().getLogger().severe("API Client Exception: " + message + " - Cause: " + (cause != null ? cause.getMessage() : "No cause"));
    }
}

