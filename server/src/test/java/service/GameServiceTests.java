package service;

import dataaccess.*;
import model.DataModel.*;
import model.RequestModal.*;
import model.ResultModal.*;
import org.junit.jupiter.api.*;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTests {

    private static GameDAO sharedGameDAO;
    private static AuthDAO sharedAuthDAO;
    private static UserDAO sharedUserDAO;

    private static GameService sharedGameService;
    private static UserService sharedUserService;

    @BeforeAll
    public static void init() {
        System.out.print("Hello");
    }

    @BeforeEach
    public void setup() {
        sharedGameDAO = new GameDAOMemory();
        sharedAuthDAO = new AuthDAOMemory();
        sharedUserDAO = new UserDAOMemory();

        sharedGameService = new GameService(sharedGameDAO, sharedAuthDAO);
        sharedUserService = new UserService(sharedUserDAO, sharedAuthDAO);
    }

    @Test
    @Order(1)
    @DisplayName("ListGames Success")
    public void listGamesSuccess() {
        RegisterResult registerResult = sharedUserService.register(new RegisterRequest("Bob", "password", "email"));

        Assertions.assertTrue(sharedGameService.listGames(new GamesRequest(registerResult.authToken())).gameDataList().isEmpty());

        CreateGameResult createGameResult = sharedGameService.createGame(new CreateGameRequest(registerResult.authToken(), "Game"));

        Assertions.assertEquals(1, sharedGameService.listGames(new GamesRequest(registerResult.authToken())).gameDataList().size());
        Assertions.assertEquals("Game", sharedGameService.listGames(new GamesRequest(registerResult.authToken())).gameDataList().get(0).gameName());
        Assertions.assertEquals(1, sharedGameService.listGames(new GamesRequest(registerResult.authToken())).gameDataList().get(0).gameID());
        Assertions.assertEquals(1, createGameResult.gameID());
    };

    @Test
    @Order(2)
    @DisplayName("ListGames Failure")
    public void listGamesFailure() {
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            sharedGameService.createGame(new CreateGameRequest("123", "Game"));
        });
    };

}