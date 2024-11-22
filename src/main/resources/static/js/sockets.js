const stompClient = new StompJs.Client({
    brokerURL: window.location.origin + '/websocket'
});

stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    stompClient.subscribe("topic/test", (message) => {
        if (message.body) {
            console.log(message.body)
        } else {
            console.log("empty message delivered")
        }
    })
}

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

stompClient.activate();