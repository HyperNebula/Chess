package dataaccess;

import model.DataModel.*;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;
    void createUser(UserData u) throws DataAccessException;
    void deleteAll() throws DataAccessException;
}
