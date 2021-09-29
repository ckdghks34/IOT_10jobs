// WebSocket 서버, WebClient 통신 규약 정의
const server = require('http').createServer(app);
const io = require('socket.io')(server)


var fs = require('fs');

const port = process.env.port || 12001;

const publicPath = path.join(__dirname,"/public");
const picPath = path.join(__dirname,"/public/images/client");

const roomName ='team';
module.exports = {
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
}
