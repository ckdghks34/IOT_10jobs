// WebClient에서 WebSocket 서버로 통신을 연결하고 서버에서 온 데이터를 웹페이지에 보여줄 수 있도록 해주는 노드입니다.

// 노드 로직 순서
// 1. 서버에서 온 메시지를 웹페이지에 전달
// 2. 버튼 클릭시 호출되는 함수

const socket = io();

socket.on('disconnect', function()  {
    console.log('disconnected form server_client.');
});


// 로직 1. 서버에서 온 메시지를 웹페이지에 전달
socket.on('sendTimeToWeb', function(message) {
    console.log('sendTimeToWeb', message);
    document.querySelector('#tTime').value = message;
});

socket.on('sendWeatherToWeb', function(message) {
    console.log('sendWeatherToWeb', message);
    document.querySelector('#tWeather').value = message;
});

socket.on('sendTemperatureToWeb', function(message) {
    console.log('sendTemperatureToWeb', message);
    document.querySelector('#tTemperature').value = message;
});

socket.on('sendAirConditionerToWeb', function(message) {
    console.log('sendAirConditionerToWeb', message);
    document.querySelector('#tAirConditioner').value = message;
});

// 로직 2. 버튼 클릭시 호출되는 함수

function btn_send_on() {

    console.log('btn_send_on');

    let data = 1;

    socket.emit('sendAirConditionerOn', data);
};

function btn_send_off() {

    console.log('btn_send_off');

    let data = 2;

    socket.emit('sendAirConditionerOff', data);
};

// function btn_turn_left() {

//     console.log('btn_left');

//     let data = 1;

//     socket.emit('turnleftToServer', data);
// };

// function btn_go_straight() {

//     console.log('btn_go_straight');

//     let data = 2;

//     socket.emit('gostraightToServer', data);
// };

// function btn_turn_right() {

//     console.log('btn_turn_right');

//     let data = 3;

//     socket.emit('turnrightToServer', data);
// };