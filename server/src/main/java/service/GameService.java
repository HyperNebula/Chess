package service;

import chess.ChessGame;
import dataaccess.*;
import model.DataModel.*;
import model.RequestModal.*;
import model.ResultModal.*;

public class GameService {

    private GameDAO gameDB;
    private AuthDAO authDB;

    public GameService(GameDAO gameDB, AuthDAO authDB) {
        this.gameDB = gameDB;
        this.authDB = authDB;
    }

    public GamesResult listGames(GamesRequest gamesRequest) throws DataAccessException {
        AuthData tempAuthData = authDB.getAuth(gamesRequest.authToken());

        if (tempAuthData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        return new GamesResult(gameDB.listGames());
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        if (createGameRequest.gameName() == null) {
            throw new DataAccessException("Error: bad request");
        }

        AuthData tempAuthData = authDB.getAuth(createGameRequest.authToken());

        if (tempAuthData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        int gameID = gameDB.createGame(new GameData(0, null, null, createGameRequest.gameName(), new ChessGame()));

        return new CreateGameResult(gameID);
    }

    public JoinResult joinGame(String authToken, JoinRequest joinRequest) throws DataAccessException {
        AuthData tempAuthData = authDB.getAuth(authToken);

        if (tempAuthData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        GameData tempOldGame = gameDB.getGame(joinRequest.gameID());

        if (tempOldGame == null) {
            throw new DataAccessException("Error: bad request");
        }

        gameDB.joinGame(tempOldGame, joinRequest.playerColor(), tempAuthData.username());

        return new JoinResult(true);

    }

    public void deleteAllGames() throws DataAccessException {
        gameDB.deleteAll();
    }

}
