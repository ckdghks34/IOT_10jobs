var express = require('express');
var router = express.Router();

const appRouter = require('./app_route');

router.use('/',appRouter);

module.exports = router;
