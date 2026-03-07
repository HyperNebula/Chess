package dataaccess;

import model.DataModel.*;

import java.sql.*;

public class AuthDAOMySQL implements AuthDAO {

    public AuthDAOMySQL() throws DataAccessException {
        setupDatabase();
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException(String.format("Error: Unable to read data: %s", ex.getMessage()), ex);
        }
        return null;
    }

    public void createAuth(AuthData auth) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, auth.authToken());
                ps.setString(2, auth.username());

                ps.executeUpdate();
            }
        } catch (Exception ex) {
            throw new DataAccessException(String.format("Error: Unable to read data: %s", ex.getMessage()), ex);
        }
    }

    public void deleteAuth(AuthData auth) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "DELETE FROM auth WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, auth.authToken());
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            throw new DataAccessException(String.format("Error: Unable to read data: %s", ex.getMessage()), ex);
        }
    }

    public void deleteAll() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE auth";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            throw new DataAccessException(String.format("Error: Unable to read data: %s", ex.getMessage()), ex);
        }
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        String authToken = rs.getString("authToken");
        String username = rs.getString("username");
        return new AuthData(authToken, username);
    }

    private void setupDatabase() throws DataAccessException {
        String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };

        DatabaseManager.configureDatabase(createStatements);
    }
}
