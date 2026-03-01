package dataaccess;

import chess.ChessGame;
import model.DataModel.*;

import java.util.List;

public interface GameDAO {
    public GameData getGame(int gameID);
    public List<GameData> listGames();
    public void joinGame(GameData game, ChessGame.TeamColor color, String username);
    public void updateGame(GameData gameData, ChessGame newGame);
    public void createGame(GameData game) ;
    public void deleteGame(GameData game);
    public void deleteAll();
}
