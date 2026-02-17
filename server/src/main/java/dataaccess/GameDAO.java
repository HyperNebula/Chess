package dataaccess;

import chess.ChessGame;
import model.DataModel.*;

import java.util.ArrayList;
import java.util.List;

public class GameDAO {

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

    public void joinGame(GameData game, ChessGame.TeamColor color, String username) {
        if (color == ChessGame.TeamColor.WHITE) {
            gameDataStorage.set(gameDataStorage.indexOf(game), new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game()));
        } else {
            gameDataStorage.set(gameDataStorage.indexOf(game), new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game()));
        }
    }

    public void updateGame(GameData gameData, ChessGame newGame) {
        gameDataStorage.set(gameDataStorage.indexOf(gameData), new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), newGame));
    }

    public void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        gameDataStorage.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
    }

    public void deleteGame(GameData game) {
        gameDataStorage.remove(game);
    }

    public void deleteAll() {
        gameDataStorage = new ArrayList<>();
    }

}
