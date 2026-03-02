package server;

import dataaccess.*;
import io.javalin.*;
import service.*;

public class Server {

    private final Javalin httpHandler;

    public Server() {
        UserDAO sharedUserDAO = new UserDAOMemory();
        AuthDAO sharedAuthDAO = new AuthDAOMemory();
        GameDAO sharedGameDAO = new GameDAOMemory();

        UserService sharedUserService = new UserService(sharedUserDAO, sharedAuthDAO);
        GameService sharedGameService = new GameService(sharedGameDAO, sharedAuthDAO);

        ServerHandler sharedServerHandler = new ServerHandler(sharedUserService, sharedGameService);

        httpHandler = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", sharedServerHandler::handleClear)
                .post("/user", sharedServerHandler::handleRegister)
                .post("/session", sharedServerHandler::handleLogin)
                .delete("/session", sharedServerHandler::handleLogout)
                .get("/game", sharedServerHandler::handleGames)
                .post("/game", sharedServerHandler::handleCreateGame)
                .put("/game", sharedServerHandler::handleJoinGame)
                .exception(AlreadyTakenException.class, sharedServerHandler::exceptionATHandler)
                .exception(UnauthorizedException.class, sharedServerHandler::exceptionUAHandler)
                .exception(DataAccessException.class, sharedServerHandler::exceptionDAHandler);
    }

    public int run(int desiredPort) {
        httpHandler.start(desiredPort);
        return httpHandler.port();
    }

    public void stop() {
        httpHandler.stop();
    }
}
