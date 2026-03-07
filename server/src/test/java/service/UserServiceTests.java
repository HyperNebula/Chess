package service;

import dataaccess.*;
import model.DataModel.*;
import model.RequestModal.*;
import model.ResultModal.*;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTests {

    private static UserDAO sharedUserDAO;
    private static AuthDAO sharedAuthDAO;

    private static UserService sharedUserService;

    @BeforeAll
    public static void init() throws DataAccessException {
        sharedUserDAO = new UserDAOMySQL();
        sharedAuthDAO = new AuthDAOMySQL();

        sharedUserDAO.deleteAll();
        sharedAuthDAO.deleteAll();

        sharedUserService = new UserService(sharedUserDAO, sharedAuthDAO);
    }

    @Test
    @Order(1)
    @DisplayName("Register Success")
    public void registerSuccess() throws DataAccessException {
        RegisterResult result = sharedUserService.register(new RegisterRequest("Bob", "password", "email"));
        sharedUserService.register(new RegisterRequest("Bob2", "password", "email"));

        Assertions.assertEquals("Bob", result.username());
        Assertions.assertEquals("Bob", sharedAuthDAO.getAuth(result.authToken()).username());

        Assertions.assertEquals(new UserData("Bob2", "password", "email"), sharedUserDAO.getUser("Bob2"));

    }

    @Test
    @Order(2)
    @DisplayName("Register Failure")
    public void registerFailure() throws DataAccessException {
        sharedUserService.register(new RegisterRequest("Bob3", "password", "email"));

        Assertions.assertThrows(AlreadyTakenException.class,
                () -> sharedUserService.register(new RegisterRequest("Bob3", "password2", "email2")));
    }

    @Test
    @Order(3)
    @DisplayName("Login Success")
    public void loginSuccess() throws DataAccessException {
        LoginResult logresult = sharedUserService.login(new LoginRequest("Bob", "password"));

        Assertions.assertEquals("Bob", logresult.username());
        Assertions.assertEquals("Bob", sharedAuthDAO.getAuth(logresult.authToken()).username());
    }

    @Test
    @Order(4)
    @DisplayName("Login Failure")
    public void loginFailure() throws DataAccessException {
        Assertions.assertThrows(UnauthorizedException.class, () -> sharedUserService.login(new LoginRequest("Bob1", "password")));
        Assertions.assertThrows(UnauthorizedException.class, () -> sharedUserService.login(new LoginRequest("Bob", "password2")));
    }

    @Test
    @Order(5)
    @DisplayName("Logout Success")
    public void logoutSuccess() throws DataAccessException {
        LoginResult logresult = sharedUserService.login(new LoginRequest("Bob", "password"));
        LogoutResult logoutresult = sharedUserService.logout(new LogoutRequest(logresult.authToken()));

        Assertions.assertTrue(logoutresult.success());
        Assertions.assertNull(sharedAuthDAO.getAuth(logresult.authToken()));
    }

    @Test
    @Order(6)
    @DisplayName("Logout Failure")
    public void logoutFailure() throws DataAccessException {
        sharedUserService.login(new LoginRequest("Bob", "password"));

        Assertions.assertThrows(UnauthorizedException.class, () -> sharedUserService.logout(new LogoutRequest("STRING")));
    }

}
