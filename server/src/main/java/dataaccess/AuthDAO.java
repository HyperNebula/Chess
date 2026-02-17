package dataaccess;

import model.DataModel.*;

import java.util.ArrayList;
import java.util.List;

public class AuthDAO {

    private List<AuthData> authDataStorage = new ArrayList<>();

    public AuthData getAuth(String authToken) {
        for (AuthData auth : authDataStorage) {
            if (auth.authToken().equals(authToken)) {
                return auth;
            }
        }
        return null;
    }

    public void createAuth(String authToken, String username) {
        authDataStorage.add(new AuthData(authToken, username));
    }

    public void deleteAuth(AuthData auth) {
        authDataStorage.remove(auth);
    }

    public void deleteAll() {
        authDataStorage = new ArrayList<>();
    }

}
