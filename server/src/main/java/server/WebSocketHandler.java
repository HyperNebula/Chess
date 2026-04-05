package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import io.javalin.websocket.*;
import model.DataModel;
import org.jetbrains.annotations.NotNull;
import com.google.gson.Gson;

import service.GameService;
import service.UserService;
import websocket.commands.*;
import websocket.messages.*;

import java.util.Objects;

import static websocket.messages.ServerMessage.ServerMessageType.*;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private ConnectionManager connections = new ConnectionManager();

    private UserService sharedUserService;
    private GameService sharedGameService;

    private final Gson gson = new Gson();

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

            UserGameCommand userCommand = gson.fromJson(ctx.message(), UserGameCommand.class);
            DataModel.AuthData auth = sharedUserService.getAuth(userCommand.getAuthToken());

            if (auth == null) {
                sendError(ctx, "Not Authorized");
                return;
            }

            switch (userCommand.getCommandType()) {
                case CONNECT:
                    connections.add(userCommand.getGameID(), auth.username(), ctx.session);


                    LoadGameMessage loadGameMessage = new LoadGameMessage(LOAD_GAME, sharedGameService.getGame(userCommand.getGameID()).game());
                    ctx.send(gson.toJson(loadGameMessage));

                    NotificationMessage connectMsg = new NotificationMessage(NOTIFICATION, auth.username() + " joined the game");
                    connections.broadcast(userCommand.getGameID(), ctx.session, connectMsg);

                    break;
                case LEAVE:
                    connections.remove(userCommand.getGameID(), auth.username(), ctx.session);
                    sharedGameService.leaveGame(userCommand.getGameID(), auth.username());

                    NotificationMessage leaveMsg = new NotificationMessage(NOTIFICATION, auth.username() + " left the game");
                    connections.broadcast(userCommand.getGameID(), ctx.session, leaveMsg);

                    break;
                case RESIGN:
                    DataModel.GameData tempResignGame = sharedGameService.getGame(userCommand.getGameID());
                    if (!Objects.equals(auth.username(), tempResignGame.whiteUsername())
                            && !Objects.equals(auth.username(), tempResignGame.blackUsername())) {
                        sendError(ctx, "You are an observer.");
                        break;
                    }

                    sharedGameService.deleteGame(userCommand.getGameID());

                    NotificationMessage resignMsg = new NotificationMessage(NOTIFICATION, auth.username() + " resigned from the game");
                    connections.broadcast(userCommand.getGameID(), null, resignMsg);

                    break;
                case MAKE_MOVE:
                    MakeMoveCommand moveCommand = gson.fromJson(ctx.message(), MakeMoveCommand.class);

                    DataModel.GameData tempGame = sharedGameService.getGame(userCommand.getGameID());

                    if (tempGame.game().getTeamTurn() == ChessGame.TeamColor.WHITE) {
                        if (!Objects.equals(auth.username(), tempGame.whiteUsername())) {
                            sendError(ctx, "Not your turn.");
                            break;
                        }
                    } else {
                        if (!Objects.equals(auth.username(), tempGame.blackUsername())) {
                            sendError(ctx, "Not your turn.");
                            break;
                        }
                    }

                    try {
                        tempGame.game().makeMove(moveCommand.getChessMove());
                    } catch (InvalidMoveException ex) {
                        sendError(ctx, ex.getMessage());
                        break;
                    }

                    LoadGameMessage loadGameMsg = new LoadGameMessage(LOAD_GAME, tempGame.game());
                    connections.broadcast(userCommand.getGameID(), null, loadGameMsg);

                    NotificationMessage moveMsg = new NotificationMessage(NOTIFICATION, auth.username() + " made a move.");
                    connections.broadcast(userCommand.getGameID(), ctx.session, moveMsg);

                    if (tempGame.game().checker()) {
                        NotificationMessage stateMsg = new NotificationMessage(NOTIFICATION,
                                auth.username() + " made a move that resulted in a check, checkmate, or stalemate.");
                        connections.broadcast(userCommand.getGameID(), null, stateMsg);
                    }

                    sharedGameService.updateGame(tempGame);

                    break;
            }

        } catch (Exception ex) {
            sendError(ctx, ex.getMessage());
        }
    }

    private void sendError(WsContext ctx, String message) {
        ErrorMessage error = new ErrorMessage(ERROR, message);
        ctx.send(gson.toJson(error));
    }
}
