package client;

import chess.ChessMove;

import com.google.gson.Gson;
import websocket.messages.*;
import websocket.commands.*;

import jakarta.websocket.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClient {

    Session session;
    MessageHandler messageHandler;

    public WebSocketClient(String url, MessageHandler messageHandler) throws Exception {

        URI socketURI = new URI(url.replace("http", "ws") + "/ws");
        this.messageHandler = messageHandler;

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, socketURI);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);

                switch (serverMessage.getServerMessageType()) {
                    case LOAD_GAME:
                        LoadGameMessage loadGame = new Gson().fromJson(message, LoadGameMessage.class);

                        messageHandler.notify("Game loaded/updated.");
                        break;
                    case NOTIFICATION:
                        NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
                        messageHandler.notify("Notification: " + notification.getMessage());
                        break;
                    case ERROR:
                        ErrorMessage error = new Gson().fromJson(message, ErrorMessage.class);
                        messageHandler.notify("Error: " + error.getMessage());
                        break;
                }
            }
        });

    }

    public void makeMove(ChessMove move) {
        return;
    }

}
