package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import io.javalin.websocket.*;
import model.DataModel;
import org.jetbrains.annotations.NotNull;
import com.google.gson.Gson;

import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.commands.MakeMoveCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import static websocket.messages.ServerMessage.ServerMessageType.*;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private ConnectionManager connections = new ConnectionManager();

    private UserService sharedUserService;
    private GameService sharedGameService;

    public WebSocketHandler(UserService sharedUserService, GameService sharedGameService) {
        this.sharedUserService = sharedUserService;
        this.sharedGameService = sharedGameService;
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext ctx) {
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) {
        ctx.closeSession();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) {
        try {

            UserGameCommand userCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            DataModel.AuthData auth = sharedUserService.getAuth(userCommand.getAuthToken());

            if (auth == null) {
                sendError(ctx, "Not Authorized");
                return;
            }

            switch (userCommand.getCommandType()) {
                case CONNECT:
                    connections.add(userCommand.getGameID(), auth.username(), ctx.session);

                    LoadGameMessage loadGameMessage = new LoadGameMessage(LOAD_GAME, sharedGameService.getGame(userCommand.getGameID()).game());
                    ctx.send(new Gson().toJson(loadGameMessage));

                    NotificationMessage connectMsg = new NotificationMessage(NOTIFICATION, auth.username() + " joined the game");
                    connections.broadcast(userCommand.getGameID(), ctx.session, connectMsg);

                    break;
                case LEAVE:
                    connections.remove(userCommand.getGameID(), auth.username(), ctx.session);
                    sharedGameService.leaveGame(userCommand.getGameID(), auth.username());

                    NotificationMessage leaveMsg = new NotificationMessage(NOTIFICATION, auth.username() + " left the game");
                    connections.broadcast(userCommand.getGameID(), ctx.session, leaveMsg);
                case RESIGN:
                    break;
                case MAKE_MOVE:
                    MakeMoveCommand moveCommand = new Gson().fromJson(ctx.message(), MakeMoveCommand.class);

                    ChessGame tempGame = sharedGameService.getGame(userCommand.getGameID()).game();

                    try {
                        tempGame.makeMove(moveCommand.getChessMove());
                    } catch (InvalidMoveException ex) {
                        sendError(ctx, ex.getMessage());
                    }

                    //UPDATE GAME in DB

                    break;
            }

        } catch (Exception ex) {
            sendError(ctx, ex.getMessage());
        }
    }

    private void sendError(WsContext ctx, String message) {
        ctx.send("Error: " + message);
    }
}
