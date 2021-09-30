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
app.set("views", path.join(__dirname, "views"));
app.set("view engine", "ejs");
app.engine("html",require("ejs").renderFile);

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

const publicPath = path.join(__dirname,"/public/images/client");
const picPath = path.join(__dirname,"/public/images/client");


server.listen(port,() =>{
	console.log(`listening on * : ${port}`);
	console.log(path.join(picPath,"/cam.jpg"));
});

const roomName ='team';

io.on('connection', socket =>{
	var ip = socket.request.headers["x-forwarded-for"] || socket.request.connection.remoteAddress;
	console.log(`클라이언트 연결 성공 - 클라이언트 IP : ${ip}, Socket ID : ${socket.id}`);

	socket.join(roomName);

	// 사용자의 메시지 수신시 WebClient로 메시지 전달
	socket.on('safety_status',(message) =>{
		socket.to(roomName).emit('sendSafetyStatus',message);
	});

	socket.on('PatrolStatus', (message) => {
        	socket.to(roomName).emit('sendPatrolStatus', message);
    	});

	// 터틀봇 상태 확인 소켓
	socket.on('BotStatus', (message) => {
		socket.to(roomName).emit('sendBotStatus', message);
	});

    	socket.on('PatrolOnToServer', (data) => {
        	socket.to(roomName).emit('patrolOn', data);
        	console.log('Patrol On!');
    	});

    	socket.on('PatrolOffToServer', (data) => {
        	socket.to(roomName).emit('patrolOff', data);
    	});

	// 맵 만들기 소켓
	socket.on('makeMapToServer', (data) => {
		socket.to(roomName).emit('makeMake', data);
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

	socket.on('gobackToServer', (data) => {
		socket.to(roomName).emit('goback', data);
	});

	// 물건을 찾아달라는 소켓
	socket.on('findWalletToServer', (data) => {
		socket.to(roomName).emit('findWallet', data);
	});

	socket.on('findRemoteToServer', (data) => {
		socket.to(roomName).emit('findRemote', data);
	});

	socket.on('findKeyToServer', (data) => {
		socket.to(roomName).emit('findKey', data);
	});

	socket.on('findBagToServer', (data) => {
		socket.to(roomName).emit('findBag', data);
	});

	// 물건 찾은 상태 보내는 소켓 -> found/not found로 보내면 될듯(PatrolStatus의 On/Off처럼)
	socket.on('WalletStatus', (message) => {
		socket.to(roomName).emit('sendWalletStatus', message);
	});

	socket.on('RemoteStatus', (message) => {
		socket.to(roomName).emit('sendRemoteStatus', message);
	});

	socket.on('KeyStatus', (mesasge) => {
		socket.to(roomName).emit('sendKeyStatus', message);
	});

	socket.on('BagStatus', (message) => {
		socket.to(roomName).emit('sendBagStatus', message);
	});

    	socket.on('disconnect', () => {
		console.log(`클라이언트 연결 해제 - 클라이언트 IP : ${ip}, Socket ID : ${socket.id}`);
    	});
	
	// 전달받은 이미지를 jpg 파일로 저장
	socket.on('streaming', (message) => {
		socket.to(roomName).emit('sendStreaming', message);

		buffer = Buffer.from(message, "base64");
		fs.writeFileSync(path.join(picPath, "/cam.jpg"),buffer);
	});
})

//

module.exports = app;
