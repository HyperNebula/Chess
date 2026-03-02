package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.RequestModal.*;
import model.ResultModal.*;
import service.*;

import java.util.Map;

public class ServerHandler {

    private UserService sharedUserService;
    private GameService sharedGameService;

    public ServerHandler(UserService sharedUserService, GameService sharedGameService) {
        this.sharedUserService = sharedUserService;
        this.sharedGameService = sharedGameService;
    }

    public void handleClear(Context ctx) {
        sharedUserService.deleteAllUsers();
        sharedGameService.deleteAllGames();

        ctx.status(200);
    }

    public void handleRegister(Context ctx) throws DataAccessException {
        RegisterRequest registerRequest = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult registerResult = sharedUserService.register(registerRequest);

        ctx.status(200).result(new Gson().toJson(registerResult));
    }

    public void handleLogin(Context ctx) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(ctx.body(), LoginRequest.class);
        LoginResult loginResult = sharedUserService.login(loginRequest);

        ctx.status(200).result(new Gson().toJson(loginResult));
    }

    public void handleLogout(Context ctx) {
        LogoutRequest logoutRequest = new LogoutRequest(ctx.header("authorization"));
        LogoutResult logoutResult = sharedUserService.logout(logoutRequest);

        ctx.status(200);
    }

    public void handleGames(Context ctx) {
        GamesRequest gamesRequest = new GamesRequest(ctx.header("authorization"));
        GamesResult gamesResult = sharedGameService.listGames(gamesRequest);

        ctx.status(200).result(new Gson().toJson(gamesResult));
    }

    public void handleCreateGame(Context ctx) throws DataAccessException {
        CreateGameRequest createGameRequest = new CreateGameRequest(ctx.header("authorization"), (String) new Gson().fromJson(ctx.body(), Map.class).get("gameName"));
        CreateGameResult createGameResult = sharedGameService.createGame(createGameRequest);

        ctx.status(200).result(new Gson().toJson(createGameResult));
    }

    public void handleJoinGame(Context ctx) throws DataAccessException {
        JoinRequest joinRequest = new Gson().fromJson(ctx.body(), JoinRequest.class);
        JoinResult joinResult = sharedGameService.joinGame(ctx.header("authorization"), joinRequest);

        ctx.status(200);
    }

    public void exceptionATHandler(AlreadyTakenException ex, Context ctx) {
        ctx.status(403);
        ctx.result(new Gson().toJson(Map.of("message", ex.getMessage())));
    }

    public void exceptionUAHandler(UnauthorizedException ex, Context ctx) {
        ctx.status(401);
        ctx.result(new Gson().toJson(Map.of("message", ex.getMessage())));
    }

    public void exceptionDAHandler(DataAccessException ex, Context ctx) {
        ctx.status(400);
        ctx.result(new Gson().toJson(Map.of("message", ex.getMessage())));
    }

}
