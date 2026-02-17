package dataaccess;

import model.DataModel.*;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private List<UserData> userDataStorage = new ArrayList<>();

    public UserData getUser(String username) {
        for (UserData user : userDataStorage) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void createUser(String username, String password, String email) {
        userDataStorage.add(new UserData(username, password, email));
    }

    public void deleteAll() {
        userDataStorage = new ArrayList<>();
    }

}
