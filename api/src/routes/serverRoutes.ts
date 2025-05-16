import { Router } from 'express';
import serverController from '../controllers/serverController';

const router = Router();

router.get('/servers/:uuid', serverController.getServer);
router.get('/servers', serverController.getAllServers);
// router.get('/servers/players', serverController.fetchPlayers);

router.post('/servers', serverController.registerServer);
router.patch('/servers/:uuid', serverController.updateServer);
// router.put('/servers/:uuid/playercount', serverController.updatePlayerCount);

export default router;
