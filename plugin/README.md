# ServerTracker Plugin

This is the Spigot plugin component of the ServerTracker system. It runs on a Minecraft server and is responsible for:

- Registering the server with the API on startup
- Sending periodic heartbeat updates with server/player data
- Tracking player sessions (join/quit events)
- Subscribing to Redis pub/sub messages (e.g., for global player count)

## Requirements

- Minecraft server (Spigot or Paper) version **1.21.5**
- Java 17+
- Active ServerTracker API instance
- Redis instance (for pub/sub)

## Key Functionality

- **Heartbeat updates**: Sent on a regular interval, containing `lastHeartbeat`, `playerCount`, and a list of player session data.
- **Session tracking**: Captures player `uuid`, `username`, `joinedAt`, and `vanished` status.
- **Redis pub/sub**: Subscribes to global player count messages and updates internal cache for use in UI (like a scoreboard).
- **Event-driven architecture**: Uses Guice to manage dependencies and modular components.
## Configuration

The plugin uses a YAML config file located at `plugin/src/main/resources/configs/config.yml`, containing API and Redis information,
as well as information on the specific server.
 
### Example:

```yaml
api:
  url: "http://localhost:3000"
  apiKey: "your-api-key" //optional

server:
  id: "00000000-0000-0000-0000-000000000000"
  name: "lobby"
  type: "spigot"
  environment: "PRODUCTION"
  ```

## Components

| Component                       | Description                                     |
|---------------------------------|-------------------------------------------------|
| `ServersApi`                    | Retrofit interface of endpoints                 |
| `ServerClient`                  | Handles Retrofit communication with the API     |
| `PlayerSessionTracker`          | Tracks online players and their sessions        |
| `HeartbeatTask`                 | Periodic heartbeat update task                  |
| `GlobalPlayerCountCache`        | Maintains live player count from Redis          |
| `EventJoin` / `EventQuit`       | Capture player login/logout and update sessions |
| `RedisSubscriber` / `Publisher` | Redis messaging infrastructure                  |
| `ConfigurationManager`          | Loads and validates configuration files         |

## Events & Pub/Sub
The plugin publishes and listens to Redis messages to stay in sync across servers.
* Publishes global player count updates
* Subscribes to updates from other servers (e.g., for displaying player totals)

## Building
To build the plugin:
1. Ensure your build system includes:
   * Spigot API 1.21.5
   * Jackson
   * retrofit2
   * Guice
2. Package it using your preferred method
3. As always, place the generated .jar in your `plugins/` directory

## Notes
* `vanished` field currently has no in-game impact, but is functional for calculating "visible" player counts; thus, it can be used for future integration with vanish plugins.
* The plugin assumes accurate system time for timestamp tracking.
