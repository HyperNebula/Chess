package dataaccess;

import chess.ChessGame;
import model.DataModel;

import java.util.List;

public class GameDAOMySQL implements GameDAO {

    public DataModel.GameData getGame(int gameID) {
        return null;
    }

    public List<DataModel.GameData> listGames() {
        return List.of();
    }

    public void joinGame(DataModel.GameData game, ChessGame.TeamColor color, String username) throws DataAccessException {

    }

    public void createGame(DataModel.GameData game) {

    }

    public void deleteAll() {

    }
}
