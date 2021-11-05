var stompClient = null;

$(document).ready(function() {
    connect();

    $("#sendBro").click(function() {
        const time = new Date().toTimeString().split(' ')[0];
        fetch('/username').then(res => res.text())
            .then((res) => {
                sendMessage('Bro!', res, time)
            });
    });

    $("#sendSis").click(function() {
        const time = new Date().toTimeString().split(' ')[0];
        fetch('/username').then(res => res.text())
            .then((res) => {
                sendMessage('Sis!', res, time)
            });
    });
});

function connect() {
    var socket = new SockJS('/our-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function (message) {
            const json = JSON.parse(message.body);
            showMessage(json.text, json.sendBy, json.time);
        });
    });
}

function showMessage(text, sendBy, time) {
    $("#wsMessageText").html(text);
    $("#wsMessageInfo").html('Send by ' + sendBy + ' at ' + time);
}

function sendMessage(text, sendBy, time) {
    stompClient.send("/ws/message", {}, JSON.stringify(
        {
            'text': text,
            'sendBy': sendBy,
            'time': time,
        }
    ));
}