package dataaccess;

import model.DataModel.*;

import java.sql.*;

public class UserDAOMySQL implements UserDAO {

    public UserDAOMySQL() throws DataAccessException {
        setupDatabase();
    }

    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username, password, email FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException(String.format("Error: Unable to read data: %s", ex.getMessage()), ex);
        }
        return null;
    }


    public void createUser(UserData u) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, u.username());
                ps.setString(2, u.password());
                ps.setString(3, u.email());

                ps.executeUpdate();
            }
        } catch (Exception ex) {
            throw new DataAccessException(String.format("Error: Unable to read data: %s", ex.getMessage()), ex);
        }
    }


    public void deleteAll() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE users";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            throw new DataAccessException(String.format("Error: Unable to read data: %s", ex.getMessage()), ex);
        }
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        return new UserData(username, password, email);
    }

    private void setupDatabase() throws DataAccessException {
        String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };

        DatabaseManager.configureDatabase(createStatements);
    }
}
