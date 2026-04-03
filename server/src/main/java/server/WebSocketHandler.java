package server;

import io.javalin.websocket.*;
import model.DataModel;
import org.jetbrains.annotations.NotNull;
import com.google.gson.Gson;

import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
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
                sendError(ctx);
                return;
            }

            switch (userCommand.getCommandType()) {
                case CONNECT:
                    connections.add(userCommand.getGameID(), auth.username(), ctx.session);

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
                    break;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendError(WsContext ctx) {
        ctx.send("Error");
    }
}
