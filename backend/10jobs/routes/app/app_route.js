var express = require('express');
var router = express.Router();

const controller = require('./app_controller');

router.post('/test',controller.test);

//router.post('/status',controller.getStatus);

//router.post('/control',controller.control);

//router.post('/patrolStatus', controller.getPatrolStatus);

module.exports = router;
