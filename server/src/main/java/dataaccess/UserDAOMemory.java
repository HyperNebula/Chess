package dataaccess;

import model.DataModel.*;

import java.util.ArrayList;
import java.util.List;

public class UserDAOMemory implements UserDAO {

    private List<UserData> userDataStorage = new ArrayList<>();

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
        userDataStorage = new ArrayList<>();
    }

}
