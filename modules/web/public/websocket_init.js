$(document).ready(function () {
    console.debug("WEB SOCKET INITIALISATION ...");
    ws = new WebSocket('ws://localhost:9000/socketRegistration');
    ws.onmessage = function (message) {
        console.debug("Message received from server: ");
        console.log(message);
    };


});