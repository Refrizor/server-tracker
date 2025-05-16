# ServerTracker API

This is the backend component of **ServerTracker**, built with Node.js and TypeScript. It handles server registration, uptime tracking, and player session aggregation. It communicates with Redis for volatile state and MySQL for persistent storage.

---

## Requirements

- Node.js 18+
- MySQL
- Redis

---

## Setup

1. Install dependencies:

   ```bash
   npm install
   ```
2. Configure the environment variables. Environmental details are in `src/config.ts`
3. Build the project `npx tsc`
4. Start the API: `node dist/app.ts`
   * For development: `npx ts-node src/app.ts`

## Project Structure
```api/
├── src/
│   ├── config/        # DB and Redis config
│   ├── controllers/   # Route logic
│   ├── loaders/       # Scheduler
│   ├── repositories/  # DB interaction
│   ├── routes/        # Express route declarations
│   ├── services/      # Business logic
│   ├── utils/         # Logging, redis publisher, response formatting
│   └── app.ts         # Entry point
```
## API Endpoints
### Register server
**POST** `/servers`

Registers a server on boot

**Body:**

```json
{
  "serverId": "uuid",
  "serverName": "lobby",
  "type": "spigot",
  "environment": "PRODUCTION",
  "version": "1.21.5",
  "port": 25565
}
```

### Send Heartbeat/Update Server
**PATCH** `/servers/:uuid`

Updates server data (heartbeat, player count, player sessions).

**Body:**

```json
{
  "playerCount": 12,
  "lastHeartbeat": 1747360053,
  "playerList": [
    {
      "uuid": "player-uuid",
      "username": "Inferris",
      "joinedAt": 1747360025,
      "vanished": false
    }
  ]
}
```

### Get All Servers
**GET** `/servers`

Returns all tracked servers and their current status.

**Example response:**
```json
{
   "success": true,
   "server": {
      "serverId": "b3209ce1-023f-4c50-baaf-25949f3915b7",
      "serverName": "lobby",
      "type": "spigot",
      "environment": "PRODUCTION",
      "version": "1.21.5-78-d683970 (MC: 1.21.5)",
      "port": 25565,
      "lastStarted": 1747358739,
      "lastHeartbeat": 1747360053,
      "playerCount": 1,
      "playerList": [
         {
            "uuid": "e0db0830-243f-43d8-8154-b10b40b4bbed",
            "username": "Inferris",
            "joinedAt": 1747360025,
            "vanished": false
         }
      ],
      "status": "online"
   }
}
```

## Redis Keys Used
The scheduler (in `src/loaders/scheduler.ts`) periodically checks registered servers. If a server hasn’t sent a heartbeat within the configured threshold, it is marked offline and the global state is updated accordingly.

## Pub/Sub
Publishes to: `server.playercount`

Payload includes: `{ visible: number, actual: number }`

Spigot plugin subscribes to this topic to update live player data (e.g., lobby scoreboard)