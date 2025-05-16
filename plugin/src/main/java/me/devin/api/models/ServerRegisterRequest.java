package me.devin.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class ServerRegisterRequest {
    @JsonProperty("serverId") private UUID serverId;
    @JsonProperty("serverName") private String serverName;
    @JsonProperty("type") private String type;
    @JsonProperty("environment") private String environment;
    @JsonProperty("version") private String version;
    @JsonProperty("port") private int port;

    public ServerRegisterRequest(UUID serverId, String serverName, String type, String environment, String version, int port) {
        this.serverId = serverId;
        this.serverName = serverName;
        this.type = type;
        this.environment = environment;
        this.version = version;
        this.port = port;
    }
}
