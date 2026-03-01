package dataaccess;

import model.DataModel.*;

public interface UserDAO {
    public UserData getUser(String username);
    public void createUser(String username, String password, String email);
    public void deleteAll();
}
