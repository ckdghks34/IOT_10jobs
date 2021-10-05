var express = require('express');
var router = express.Router();

const app = require('./app');

router.use('/',app);

module.exports = router;
