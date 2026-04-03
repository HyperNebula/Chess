package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.DataModel.*;
import service.AlreadyTakenException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class GameDAOMySQL implements GameDAO {

    public GameDAOMySQL() throws DataAccessException {
        setupDatabase();
    }

    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()), ex);
        }
        return null;
    }

    public List<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()), ex);
        }
        return result;
    }

    public void joinGame(GameData game, ChessGame.TeamColor color, String username) throws DataAccessException {
        if (color == ChessGame.TeamColor.WHITE) {
            if (game.whiteUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }

            try (Connection conn = DatabaseManager.getConnection()) {
                String statement = "UPDATE games SET whiteUsername = ? WHERE gameID = ?";
                try (PreparedStatement ps = conn.prepareStatement(statement)) {
                    ps.setString(1, username);
                    ps.setInt(2, game.gameID());

                    ps.executeUpdate();
                }
            } catch (Exception ex) {
                throw new DataAccessException(String.format("Error: Unable to read data: %s", ex.getMessage()), ex);
            }
        } else {
            if (game.blackUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }

            try (Connection conn = DatabaseManager.getConnection()) {
                String statement = "UPDATE games SET blackUsername = ? WHERE gameID = ?";
                try (PreparedStatement ps = conn.prepareStatement(statement)) {
                    ps.setString(1, username);
                    ps.setInt(2, game.gameID());

                    ps.executeUpdate();
                }
            } catch (Exception ex) {
                throw new DataAccessException(String.format("Error: Unable to read data: %s", ex.getMessage()), ex);
            }
        }
    }

    public void leaveGame(int gameID, String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {

            String whiteStatement = "UPDATE games SET whiteUsername = NULL WHERE gameID = ? AND whiteUsername = ?";
            try (PreparedStatement ps = conn.prepareStatement(whiteStatement)) {
                ps.setInt(1, gameID);
                ps.setString(2, username);
                ps.executeUpdate();
            }

            String blackStatement = "UPDATE games SET blackUsername = NULL WHERE gameID = ? AND blackUsername = ?";
            try (PreparedStatement ps = conn.prepareStatement(blackStatement)) {
                ps.setInt(1, gameID);
                ps.setString(2, username);
                ps.executeUpdate();
            }

        } catch (Exception ex) {
            throw new DataAccessException(String.format("Error: Unable to update data: %s", ex.getMessage()), ex);
        }
    }

    public int createGame(GameData game) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                ps.setString(1, game.whiteUsername());
                ps.setString(2, game.blackUsername());
                ps.setString(3, game.gameName());

                String gameJson = new Gson().toJson(game.game());
                ps.setString(4, gameJson);

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (Exception ex) {
            throw new DataAccessException(String.format("Error: Unable to read data: %s", ex.getMessage()), ex);
        }
    }

    public void deleteAll() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE games";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            throw new DataAccessException(String.format("Error: Unable to read data: %s", ex.getMessage()), ex);
        }
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        int gameID = rs.getInt("gameID");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        var gameJson = rs.getString("game");
        ChessGame game = new Gson().fromJson(gameJson, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    private void setupDatabase() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS games (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };

        DatabaseManager.configureDatabase(createStatements);
    }


}
