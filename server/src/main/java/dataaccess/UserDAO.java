package dataaccess;

import model.DataModel.*;

public interface UserDAO {
    public UserData getUser(String username);
    public void createUser(UserData u);
    public void deleteAll();
}
