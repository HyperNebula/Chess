package dataaccess;

import model.DataModel.*;

public interface UserDAO {
    UserData getUser(String username);
    void createUser(UserData u);
    void deleteAll();
}
