package server;

import dataaccess.*;
import io.javalin.*;
import service.GameService;
import service.UserService;

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
                .exception(Exception.class, sharedServerHandler::exceptionHandler);
    }

    public int run(int desiredPort) {
        httpHandler.start(desiredPort);
        return httpHandler.port();
    }

    public void stop() {
        httpHandler.stop();
    }
}
