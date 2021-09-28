var express = require('express');
var router = express.Router();

const controller = require('./app_controller');

router.post('/test',controller.test);

module.exports = router;
