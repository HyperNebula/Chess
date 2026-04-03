package dataaccess;

import chess.ChessGame;
import model.DataModel.*;

import java.util.List;

public interface GameDAO {
    GameData getGame(int gameID) throws DataAccessException;
    List<GameData> listGames() throws DataAccessException;
    void joinGame(GameData game, ChessGame.TeamColor color, String username) throws DataAccessException;
    //void updateGame(GameData gameData, ChessGame newGame) throws DataAccessException;
    int createGame(GameData game) throws DataAccessException;
    //void deleteGame(GameData game) throws DataAccessException;
    void deleteAll() throws DataAccessException;
    void leaveGame(int gameID, String username) throws DataAccessException;
}
