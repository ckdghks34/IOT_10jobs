const socket = io();

socket.on('disconnect', function()  {
    console.log('disconnected form server_client.');
});

// 사용자의 맵정보 존재 여부 판별
let files = new Image;
let makeMapClass = document.getElementsByClassName("make-map")
files.src = 'createmap.png'
// if(!files.complete) {

// }



// 로직 1. 서버에서 온 메시지를 웹페이지에 전달
socket.on('sendSafetyStatus', function(message) {
    console.log('sendSafetyStatus', message);
    document.querySelector('#tSafetyStatus').value = message;
});

socket.on('sendPatrolStatus', function(message) {
    console.log('sendPatrolStatus', message);
    document.querySelector('#tPatrolStatus').value = message;
});

// 로직 2. 버튼 클릭시 호출되는 함수
function btn_turn_left() {

    console.log('btn_left');

    let data = 1;

    socket.emit('turnleftToServer', data);
};

function btn_go_straight() {

    console.log('btn_go_straight');

    let data = 2;

    socket.emit('gostraightToServer', data);
};

function btn_turn_right() {

    console.log('btn_turn_right');

    let data = 3;

    socket.emit('turnrightToServer', data);
};
