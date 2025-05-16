import dotenv from 'dotenv';
dotenv.config();

import { logger } from './utils/logger';
import { createServer } from './server';
import { startSchedulers } from './loaders/scheduler';
import {connectRedis} from "./config/redis";

const PORT: number = parseInt(process.env.API_PORT || '3000', 10);

(async () => {
    try {
        const app = createServer();

        app.listen(PORT, () => {
            logger.info(`Server running on port ${PORT}`);
        });

        await connectRedis();
        startSchedulers();
    } catch (error: any) {
        logger.error('Failed to start application:', error);
        process.exit(1);
    }
})();