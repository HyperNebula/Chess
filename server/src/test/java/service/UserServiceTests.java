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
    public static void init() {
        System.out.print("Hello");
    }

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
        Assertions.assertEquals(new UserData("Bob2", "password", "email"), sharedUserDAO.getUser("Bob2"));
    };

    @Test
    @Order(2)
    @DisplayName("Register Failure")
    public void registerFailure() {
        sharedUserService.register(new RegisterRequest("Bob", "password", "email"));
        //sharedUserService.register(new RegisterRequest("Bob", "password2", "email2"));
    };

}
