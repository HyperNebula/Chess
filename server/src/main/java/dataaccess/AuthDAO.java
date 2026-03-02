package dataaccess;

import model.DataModel.*;

public interface AuthDAO {
    AuthData getAuth(String authToken);
    void createAuth(AuthData auth);
    void deleteAuth(AuthData auth);
    void deleteAll();
}
