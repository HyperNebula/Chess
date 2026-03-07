package dataaccess;

import model.DataModel.*;
import model.RequestModal.*;
import model.ResultModal.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDAOMySQLTests {

    private static UserDAO sharedUserDAO;

    @BeforeAll
    public static void init() throws DataAccessException {
        sharedUserDAO = new UserDAOMySQL();

        sharedUserDAO.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("createUser Success")
    public void createUserSuccess() throws DataAccessException {
        UserData newUser = new UserData("Bob", "password", "email");

        sharedUserDAO.createUser(newUser);

        UserData retrievedUser = sharedUserDAO.getUser("Bob");
        assertNotNull(retrievedUser);
        assertEquals(newUser.username(), retrievedUser.username());
        assertEquals(newUser.password(), retrievedUser.password());
        assertEquals(newUser.email(), retrievedUser.email());
    }

    @Test
    @Order(2)
    @DisplayName("createUser Failure")
    public void createUserFailure() throws DataAccessException {
        UserData newUser2 = new UserData("Bob", "password", "email");

        Assertions.assertThrows(DataAccessException.class, () -> sharedUserDAO.createUser(newUser2));
    }

    @Test
    @Order(3)
    @DisplayName("getUser Success")
    public void getUserSuccess() throws DataAccessException {
        UserData actualUser = sharedUserDAO.getUser("Bob");

        assertNotNull(actualUser);
        assertEquals("Bob", actualUser.username());
        assertEquals("password", actualUser.password());
        assertEquals("email", actualUser.email());
    }

    @Test
    @Order(4)
    @DisplayName("getUser Failure")
    public void getUserFailure() throws DataAccessException {
        UserData missingUser = sharedUserDAO.getUser("nonExistentUser");

        assertNull(missingUser);
    }

    @Test
    @Order(5)
    @DisplayName("Delete Success")
    public void deleteSuccess() throws DataAccessException {
        assertDoesNotThrow(() -> sharedUserDAO.deleteAll());

        assertNull(sharedUserDAO.getUser("Bob"));
    }
}
