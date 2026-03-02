package dataaccess;

import chess.ChessGame;
import model.DataModel.*;

import java.util.List;

public interface GameDAO {
    GameData getGame(int gameID);
    List<GameData> listGames();
    void joinGame(GameData game, ChessGame.TeamColor color, String username) throws DataAccessException;
    //void updateGame(GameData gameData, ChessGame newGame) throws DataAccessException;
    void createGame(GameData game) ;
    //void deleteGame(GameData game) throws DataAccessException;
    void deleteAll();
}
