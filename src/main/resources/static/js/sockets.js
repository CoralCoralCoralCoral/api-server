var sock = new SockJS(window.location.origin + '/websocket')

const stompClient = Stomp.over(sock)

stompClient.connect({}, (frame) => {
    console.log('Connected: ' + frame);
    stompClient.subscribe("/topic/test", function (message) {
        console.log(JSON.parse(message.body));
    })
})

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

// stompClient.activate();