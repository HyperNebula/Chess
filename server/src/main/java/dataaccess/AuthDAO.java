package dataaccess;

import model.DataModel.*;

public interface AuthDAO {
    public AuthData getAuth(String authToken);
    public void createAuth(AuthData auth);
    public void deleteAuth(AuthData auth) throws DataAccessException;
    public void deleteAll();
}
