package dataaccess;

import model.DataModel.*;

import java.util.ArrayList;
import java.util.List;

public interface AuthDAO {
    public AuthData getAuth(String authToken);
    public void createAuth(String authToken, String username);
    public void deleteAuth(AuthData auth);
    public void deleteAll();
}
