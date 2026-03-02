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

    @BeforeEach
    public void setup() {
        sharedUserDAO = new UserDAOMemory();
        sharedAuthDAO = new AuthDAOMemory();

        sharedUserService = new UserService(sharedUserDAO, sharedAuthDAO);
    }

    @Test
    @Order(1)
    @DisplayName("Register Success")
    public void registerSuccess() {
        RegisterResult result = sharedUserService.register(new RegisterRequest("Bob", "password", "email"));
        sharedUserService.register(new RegisterRequest("Bob2", "password", "email"));

        Assertions.assertEquals("Bob", result.username());
        Assertions.assertEquals("Bob", sharedAuthDAO.getAuth(result.authToken()).username());

        Assertions.assertEquals(new UserData("Bob2", "password", "email"), sharedUserDAO.getUser("Bob2"));

    }

    @Test
    @Order(2)
    @DisplayName("Register Failure")
    public void registerFailure() {
        sharedUserService.register(new RegisterRequest("Bob", "password", "email"));

        Assertions.assertThrows(AlreadyTakenException.class, () -> sharedUserService.register(new RegisterRequest("Bob", "password2", "email2")));
    }

    @Test
    @Order(3)
    @DisplayName("Login Success")
    public void loginSuccess() {
        RegisterResult regresult = sharedUserService.register(new RegisterRequest("Bob", "password", "email"));
        LoginResult logresult = sharedUserService.login(new LoginRequest("Bob", "password"));

        Assertions.assertEquals("Bob", logresult.username());
        Assertions.assertEquals("Bob", sharedAuthDAO.getAuth(regresult.authToken()).username());
        Assertions.assertEquals("Bob", sharedAuthDAO.getAuth(logresult.authToken()).username());

        Assertions.assertNotEquals(sharedAuthDAO.getAuth(regresult.authToken()).authToken(), sharedAuthDAO.getAuth(logresult.authToken()).authToken());

    }

    @Test
    @Order(4)
    @DisplayName("Login Failure")
    public void loginFailure() {
        sharedUserService.register(new RegisterRequest("Bob", "password", "email"));

        Assertions.assertThrows(UnauthorizedException.class, () -> sharedUserService.login(new LoginRequest("Bob1", "password")));
        Assertions.assertThrows(UnauthorizedException.class, () -> sharedUserService.login(new LoginRequest("Bob", "password2")));
    }

    @Test
    @Order(5)
    @DisplayName("Logout Success")
    public void logoutSuccess() {
        RegisterResult regresult = sharedUserService.register(new RegisterRequest("Bob", "password", "email"));
        LogoutResult logoutresult = sharedUserService.logout(new LogoutRequest(regresult.authToken()));

        Assertions.assertTrue(logoutresult.success());
        Assertions.assertNull(sharedAuthDAO.getAuth(regresult.authToken()));
    }

    @Test
    @Order(6)
    @DisplayName("Logout Failure")
    public void logoutFailure() {
        sharedUserService.register(new RegisterRequest("Bob", "password", "email"));

        Assertions.assertThrows(UnauthorizedException.class, () -> sharedUserService.logout(new LogoutRequest("STRING")));
    }

}
