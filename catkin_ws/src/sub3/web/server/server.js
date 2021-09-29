// Websocket 서버 구동을 위한 서버 코드입니다.

// 노드 로직 순서

const path = require('path');
const express = require('express');

// client 경로의 폴더를 지정해줍니다.
const publicPath = path.join(__dirname, "/../client");
var app = express();

const picPath = path.join(__dirname, "/../client");

app.use(express.static(publicPath));

// 로직 1. WebSocket 서버, WebClient 통신 규약 정의
const server = require('http').createServer(app);
const io = require('socket.io')(server)


var fs = require('fs'); // required for file serving

// 로직 2. 포트번호 지정
const port = process.env.port || 12001

server.listen(port, () => {
    console.log(`listening on *:${port}`);
});

const roomName = 'team';

io.on('connection', socket => {
    socket.join(roomName);

    // 로직 3. 사용자의 메시지 수신시 WebClient로 메시지 전달
<<<<<<< HEAD
    socket.on('safety_status', (message) => {
        socket.to(roomName).emit('sendSafetyStatus', message);
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
=======
    socket.on('sendTime', (message) => {
        socket.to(roomName).emit('sendTimeToWeb', message);
    });

    socket.on('sendWeather', (message) => {
        socket.to(roomName).emit('sendWeatherToWeb', message);
    });

    socket.on('sendTemperature', (message) => {
        socket.to(roomName).emit('sendTemperatureToWeb', message);
    });

    socket.on('sendAirConditioner', (message) => {
        socket.to(roomName).emit('sendAirConditionerToWeb', message);
    });

    socket.on('sendAirConditionerOn', (data) => {
        socket.to(roomName).emit('sendAirConOn', data);
    });

    socket.on('sendAirConditionerOff', (data) => {
        socket.to(roomName).emit('sendAirConOff', data);
    });

    // socket.on('turnleftToServer', (data) => {
    //     socket.to(roomName).emit('turnleft', data);
    // });

    // socket.on('gostraightToServer', (data) => {
    //     socket.to(roomName).emit('gostraight', data);
    // });

    // socket.on('turnrightToServer', (data) => {
    //     socket.to(roomName).emit('turnright', data);
    // });
>>>>>>> 74e7b3a93a2a7244c2c89fcdae147e7d2c75dae9

    socket.on('disconnect', () => {
        console.log('disconnected from server');
    });

<<<<<<< HEAD
    // 전달받은 이미지를 jpg 파일로 저장
    socket.on('streaming', (message) => {
        socket.to(roomName).emit('sendStreaming', message);
        // console.log(message);
        buffer = Buffer.from(message, "base64");
        fs.writeFileSync(path.join(picPath, "/../client/cam.jpg"), buffer);
    });
=======
    // // 전달받은 이미지를 jpg 파일로 저장
    // socket.on('streaming', (message) => {
    //     socket.to(roomName).emit('sendStreaming', message);
    //     // console.log(message);
    //     buffer = Buffer.from(message, "base64");
    //     fs.writeFileSync(path.join(picPath, "/../client/cam.jpg"), buffer);
    // });
>>>>>>> 74e7b3a93a2a7244c2c89fcdae147e7d2c75dae9

})