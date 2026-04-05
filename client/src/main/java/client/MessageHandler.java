package client;

import websocket.messages.*;

public interface MessageHandler {
    void notify(String message);
}