package dataaccess;

import chess.ChessGame;
import model.DataModel.*;
import service.AlreadyTakenException;

import java.util.ArrayList;
import java.util.List;

public class GameDAOMemory implements GameDAO{

    private List<GameData> gameDataStorage = new ArrayList<>();

    public GameData getGame(int gameID) {
        for (GameData game : gameDataStorage) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    public List<GameData> listGames() {
        return gameDataStorage;
    }

    public void joinGame(GameData game, ChessGame.TeamColor color, String username) throws DataAccessException {
        if (!gameDataStorage.contains(game)) {
            throw new DataAccessException("Error: bad request");
        }

        if (color == null) {
            throw new DataAccessException("Error: bad request");
        }

        if (color == ChessGame.TeamColor.WHITE) {
            if (game.whiteUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }

            gameDataStorage.set(gameDataStorage.indexOf(game),
                    new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game()));

        } else {
            if (game.blackUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }

            gameDataStorage.set(gameDataStorage.indexOf(game),
                    new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game()));
        }
    }

    public void updateGame(GameData gameData, ChessGame newGame) throws DataAccessException {
        if (!gameDataStorage.contains(gameData)) {
            throw new DataAccessException("Error: bad request");
        }
        gameDataStorage.set(gameDataStorage.indexOf(gameData),
                new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(),
                        newGame));
    }

    public void createGame(GameData game) {
        gameDataStorage.add(game);
    }

    public void deleteGame(GameData game) throws DataAccessException {
        if (!gameDataStorage.contains(game)) {
            throw new DataAccessException("Error: bad request");
        }
        gameDataStorage.remove(game);
    }

    public void deleteAll() {
        gameDataStorage = new ArrayList<>();
    }

}
