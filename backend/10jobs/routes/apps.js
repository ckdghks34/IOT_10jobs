var express = require('express');
var router = express.Router();

const app = require('./app');

router.use('/',app);


// Get Notifications.
// DB에서 받아옴.
//router.post('/',function(req,res,next){
//	// example	
//	res.send('get Notifications.');
//});

// 


module.exports = router;
