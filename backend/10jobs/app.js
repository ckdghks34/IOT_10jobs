var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
var rosRouter = require('./routes/ros');
var appRouter = require('./routes/apps');
var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'pug');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/ros',rosRouter);
app.use('/apps',appRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});


// WebSocket 서버, WebClient 통신 규약 정의
const server = require('http').createServer(app);
const io = require('socket.io')(server)


var fs = require('fs');

const port = process.env.port || 12001;

const publicPath = path.join(__dirname,"/public");
const picPath = path.join(__dirname,"/public/images/client");


server.listen(port,() =>{
	console.log(`listening on * : ${port}`);
});

const roomName ='team';

io.on('connection', socket =>{
	socket.join(roomName);

	// 사용자의 메시지 수신시 WebClient로 메시지 전달
	socket.on('safety_status',(message) =>{
		socket.to(roomName).emit('sendSafetyStatus',message);
	});

	socket.on('PatrolStatus', (message) => {
        	socket.to(roomName).emit('sendPatrolStatus', message);
    	});

    	socket.on('PatrolOnToServer', (data) => {
        	socket.to(roomName).emit('patrolOn', data);
        	console.log('Patrol On!');
    	});

    	socket.on('PatrolOffToServer', (data) => {
        	socket.to(roomName).emit('patrolOff', data);
    	});

    	socket.on('turnleftToServer', (data) => {
        	socket.to(roomName).emit('turnleft', data);
    	});

    	socket.on('gostraightToServer', (data) => {
        	socket.to(roomName).emit('gostraight', data);
    	});

    	socket.on('turnrightToServer', (data) => {
        	socket.to(roomName).emit('turnright', data);
    	});

    	socket.on('disconnect', () => {
        	console.log('disconnected from server');
    	});
	
	// 전달받은 이미지를 jpg 파일로 저장
	socket.on('streaming', (message) => {
		socket.to(roomName).emit('sendStreaming', message);

		buffer = Buffer.from(message, "base64");
		fs.writeFileSync(path.join(picPath, "../client/cam.jpg"),buffer);
	});
})

//

module.exports = app;
