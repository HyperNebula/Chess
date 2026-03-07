package dataaccess;

import model.DataModel.AuthData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthDAOMySQLTests {

    private static AuthDAO sharedAuthDAO;

    @BeforeAll
    public static void init() throws DataAccessException {
        sharedAuthDAO = new AuthDAOMySQL();

        sharedAuthDAO.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("createAuth Success")
    public void createAuthSuccess() throws DataAccessException {
        AuthData newAuth = new AuthData("token", "Bob");

        sharedAuthDAO.createAuth(newAuth);

        AuthData retrievedAuth = sharedAuthDAO.getAuth("token");
        assertNotNull(retrievedAuth);
        assertEquals(newAuth.username(), retrievedAuth.username());
        assertEquals(newAuth.authToken(), retrievedAuth.authToken());
    }

    @Test
    @Order(2)
    @DisplayName("createAuth Failure")
    public void createAuthFailure() throws DataAccessException {
        AuthData newAuth = new AuthData("token", "Bob2");

        assertThrows(DataAccessException.class, () -> sharedAuthDAO.createAuth(newAuth));
    }

    @Test
    @Order(3)
    @DisplayName("getAuth Success")
    public void getAuthSuccess() throws DataAccessException {
        AuthData retrievedAuth = sharedAuthDAO.getAuth("token");

        assertNotNull(retrievedAuth);
        assertEquals("Bob", retrievedAuth.username());
        assertEquals("token", retrievedAuth.authToken());
    }

    @Test
    @Order(4)
    @DisplayName("getAuth Failure")
    public void getAuthFailure() throws DataAccessException {
        AuthData retrievedAuth = sharedAuthDAO.getAuth("token2");

        assertNull(retrievedAuth);
    }

    @Test
    @Order(5)
    @DisplayName("deleteAuth Success")
    public void deleteAuthSuccess() throws DataAccessException {
        AuthData newAuth = new AuthData("token", "Bob");

        sharedAuthDAO.deleteAuth(newAuth);

        assertNull(sharedAuthDAO.getAuth("token"));
    }

    @Test
    @Order(6)
    @DisplayName("deleteAuth Failure")
    public void deleteAuthFailure() throws DataAccessException {
        AuthData newAuth = new AuthData("token", "Bob");

        assertDoesNotThrow(() -> sharedAuthDAO.deleteAuth(newAuth));
    }

    @Test
    @Order(7)
    @DisplayName("Delete Success")
    public void deleteSuccess() throws DataAccessException {
        assertDoesNotThrow(() -> sharedAuthDAO.deleteAll());

        assertNull(sharedAuthDAO.getAuth("token"));
    }
}
