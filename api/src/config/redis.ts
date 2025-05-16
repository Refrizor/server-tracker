import { redis as redisConfig } from './config';
import {createClient} from "redis";

const client = createClient({
    socket: {
        host: redisConfig.host ?? 'localhost',
        port: Number(redisConfig.port ?? 6379),
    },
    password: redisConfig.password ?? undefined,
});

client.on('error', (err) => {
    console.error('Redis error:', err);
});

export async function connectRedis() {
    await client.connect();
    console.log('Redis connected');
}

export default client;