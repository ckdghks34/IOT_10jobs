var mysql = require('mysql');
var pool = mysql.createPool({
	host: '127.0.0.1',
	port: 3306,
	user: '10jobs',
	password: 'jobs',
	database: '10jobs',
	connectionLimit : 1000000,
	queueLimit: 100,
	multipleStatements : true,
});

module.exports = pool;
