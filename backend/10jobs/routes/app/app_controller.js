const pool = require('../../config/db_connect');


function test(req,res){
//	pool.getConnection((err,connection) => {
//		var sql = ``;
//		var data = [];
//		connection.query(sql,data,(err,result)=> {
//			if (results){
//				res.json({
//					err : err,
//					res : results,
//					status : 200
//			}
//			else{
//				res.json({
//					err:err,
//					res:false,
//					status : 404
//			}
//		})
//	})

	console.log('call test');
	console.log(req.body);
	res.json({
		'msg' : 'test'
	});
}

module.exports = {
	test,
}


