import {createPool, Pool, PoolOptions} from 'mysql2/promise';
import {mysqlConfig} from './config';

const poolOptions: PoolOptions = {
    host: mysqlConfig.host,
    user: mysqlConfig.user,
    password: mysqlConfig.password,
    database: mysqlConfig.name,
    waitForConnections: true,
    connectionLimit: 10,
    queueLimit: 0,
    connectTimeout: 10000, // 10 seconds for establishing a new connection
};

let pool: Pool;

try {
    pool = createPool(poolOptions);

    pool.on('enqueue', () => {
        console.log('Waiting for available connection slot');
    });
} catch (error) {
    console.error('Error creating MySQL connection pool:', error instanceof Error ? error.message : error);
    process.exit(1); // Exit process with failure
}

export default pool;