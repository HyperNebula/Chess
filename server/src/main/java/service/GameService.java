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

    public GamesResult listGames(GamesRequest gamesRequest) {
        AuthData tempAuthData = authDB.getAuth(gamesRequest.authToken());

        if (tempAuthData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        return new GamesResult(gameDB.listGames());
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) {
        AuthData tempAuthData = authDB.getAuth(createGameRequest.authToken());

        if (tempAuthData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        int tempGameID = gameDB.listGames().size() + 1;
        gameDB.createGame(new GameData(tempGameID, null, null, createGameRequest.gameName(), new ChessGame()));

        return new CreateGameResult(tempGameID);
    }

    public JoinResult joinGame(String authToken, JoinRequest joinRequest) throws DataAccessException {
        AuthData tempAuthData = authDB.getAuth(authToken);

        if (tempAuthData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        GameData tempOldGame = gameDB.getGame(joinRequest.gameID());

        gameDB.joinGame(tempOldGame, joinRequest.playerColor(), tempAuthData.username());

        return new JoinResult(true);

    }

    public void deleteAllGames() {
        gameDB.deleteAll();
    }

}
