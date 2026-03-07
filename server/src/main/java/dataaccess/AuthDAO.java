package dataaccess;

import model.DataModel.*;

public interface AuthDAO {
    AuthData getAuth(String authToken) throws DataAccessException;
    void createAuth(AuthData auth) throws DataAccessException;
    void deleteAuth(AuthData auth) throws DataAccessException;
    void deleteAll() throws DataAccessException;
}
