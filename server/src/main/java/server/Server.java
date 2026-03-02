package server;

import dataaccess.AuthDAO;
import dataaccess.AuthDAOMemory;
import dataaccess.UserDAO;
import dataaccess.UserDAOMemory;
import io.javalin.*;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        UserDAO sharedUserDAO = new UserDAOMemory();
        AuthDAO sharedAuthDAO = new AuthDAOMemory();

        UserService sharedUserService = new UserService(sharedUserDAO, sharedAuthDAO);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
