const stompClient = new StompJs.Client({
    brokerURL: window.location.origin + '/websocket'
});

stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
}

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

stompClient.activate();