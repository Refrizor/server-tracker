import express from 'express';
import serverRoutes from './routes/serverRoutes';

export function createServer() {
    const app = express();

    // Middleware
    app.use(express.json());

    // Routes
    app.use('/', serverRoutes);

    return app;
}