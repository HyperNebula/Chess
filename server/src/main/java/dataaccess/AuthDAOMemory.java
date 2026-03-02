package dataaccess;

import model.DataModel.*;

import java.util.ArrayList;
import java.util.List;

public class AuthDAOMemory implements AuthDAO {

    private static List<AuthData> authDataStorage = new ArrayList<>();

    public AuthData getAuth(String authToken) {
        for (AuthData auth : authDataStorage) {
            if (auth.authToken().equals(authToken)) {
                return auth;
            }
        }
        return null;
    }

    public void createAuth(AuthData auth) {
        authDataStorage.add(auth);
    }

    public void deleteAuth(AuthData auth) throws DataAccessException {
        if (!authDataStorage.contains(auth)) {
            throw new DataAccessException("Game " + auth + "does not exist");
        }
        authDataStorage.remove(auth);
    }

    public void deleteAll() {
        authDataStorage = new ArrayList<>();
    }

}
