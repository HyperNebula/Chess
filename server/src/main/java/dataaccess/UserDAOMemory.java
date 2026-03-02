package dataaccess;

import model.DataModel.*;

import java.util.Collection;
import java.util.LinkedHashSet;

public class UserDAOMemory implements UserDAO {

    private Collection<UserData> userDataStorage = new LinkedHashSet<>();

    public UserData getUser(String username) {
        for (UserData user : userDataStorage) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void createUser(UserData u) {
        userDataStorage.add(u);
    }

    public void deleteAll() {
        userDataStorage = new LinkedHashSet<>();
    }

}
