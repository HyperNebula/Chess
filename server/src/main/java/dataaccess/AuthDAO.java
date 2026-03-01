package dataaccess;

import model.DataModel.*;

public interface AuthDAO {
    public AuthData getAuth(String authToken);
    public void createAuth(String authToken, String username);
    public void deleteAuth(AuthData auth);
    public void deleteAll();
}
