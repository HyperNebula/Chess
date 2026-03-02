package server;

import dataaccess.*;
import io.javalin.*;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        UserDAO sharedUserDAO = new UserDAOMemory();
        AuthDAO sharedAuthDAO = new AuthDAOMemory();
        GameDAO sharedGameDAO = new GameDAOMemory();

        UserService sharedUserService = new UserService(sharedUserDAO, sharedAuthDAO);
        GameService sharedGameService = new GameService(sharedGameDAO, sharedAuthDAO);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
