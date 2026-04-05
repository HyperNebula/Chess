package client;

import chess.ChessMove;

import com.google.gson.Gson;
import websocket.messages.*;
import websocket.commands.*;

import jakarta.websocket.*;

import java.io.Console;
import java.net.URI;

import static websocket.commands.UserGameCommand.CommandType.*;

public class WebSocketClient extends Endpoint {

    Session session;
    ServerNotificationHandler serverNotificationHandler;

    public WebSocketClient(String url, ServerNotificationHandler serverNotificationHandler) throws Exception {

        URI socketURI = new URI(url.replace("http", "ws") + "/ws");
        this.serverNotificationHandler = serverNotificationHandler;

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, socketURI);

        this.session.addMessageHandler((MessageHandler.Whole<String>) message -> {
            ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);

            switch (serverMessage.getServerMessageType()) {
                case LOAD_GAME:
                    LoadGameMessage loadGame = new Gson().fromJson(message, LoadGameMessage.class);
                    ClientMain.game = loadGame.getGame();

                    System.out.println("Loaded game from " + loadGame.getGame());
                    BoardPrinter.printBoard();

                    serverNotificationHandler.notify("Game loaded/updated.");
                    break;
                case NOTIFICATION:
                    NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
                    serverNotificationHandler.notify("Notification: " + notification.getMessage());
                    break;
                case ERROR:
                    ErrorMessage error = new Gson().fromJson(message, ErrorMessage.class);
                    serverNotificationHandler.notify("Error: " + error.getMessage());
                    break;
            }
        });

    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, int gameID) throws Exception {
        UserGameCommand command = new UserGameCommand(CONNECT, authToken, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws Exception {
        MakeMoveCommand command = new MakeMoveCommand(MAKE_MOVE, authToken, gameID, move);
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public void leave(String authToken, int gameID) throws Exception {
        UserGameCommand command = new UserGameCommand(LEAVE, authToken, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
        this.session.close();
    }

    public void resign(String authToken, int gameID) throws Exception {
        UserGameCommand command = new UserGameCommand(RESIGN, authToken, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }

}
