package service;

import chess.ChessGame;
import dataaccess.*;
import model.DataModel.*;
import model.RequestModal.*;
import model.ResultModal.*;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTests {

    private static GameService sharedGameService;
    private static UserService sharedUserService;

    @BeforeEach
    public void setup() {
        GameDAO sharedGameDAO = new GameDAOMemory();
        AuthDAO sharedAuthDAO = new AuthDAOMemory();
        UserDAO sharedUserDAO = new UserDAOMemory();

        sharedGameService = new GameService(sharedGameDAO, sharedAuthDAO);
        sharedUserService = new UserService(sharedUserDAO, sharedAuthDAO);
    }

    @Test
    @Order(1)
    @DisplayName("ListGames Success")
    public void listGamesSuccess() {
        RegisterResult registerResult = sharedUserService.register(new RegisterRequest("Bob", "password", "email"));

        Assertions.assertTrue(sharedGameService.listGames(new GamesRequest(registerResult.authToken())).games().isEmpty());

        CreateGameResult createGameResult = sharedGameService.createGame(new CreateGameRequest(registerResult.authToken(), "Game"));

        Assertions.assertEquals(1, sharedGameService.listGames(new GamesRequest(registerResult.authToken())).games().size());
        Assertions.assertEquals("Game", sharedGameService.listGames(new GamesRequest(registerResult.authToken())).games().getFirst().gameName());
        Assertions.assertEquals(1, sharedGameService.listGames(new GamesRequest(registerResult.authToken())).games().getFirst().gameID());
        Assertions.assertEquals(1, createGameResult.gameID());
    }

    @Test
    @Order(2)
    @DisplayName("ListGames Failure")
    public void listGamesFailure() {
        Assertions.assertThrows(UnauthorizedException.class, () -> sharedGameService.listGames(new GamesRequest("123")));
    }

    @Test
    @Order(3)
    @DisplayName("CreateGames Success")
    public void createGamesSuccess() {
        RegisterResult registerResult = sharedUserService.register(new RegisterRequest("Bob", "password", "email"));

        CreateGameResult createGameResult = sharedGameService.createGame(new CreateGameRequest(registerResult.authToken(), "Game"));
        CreateGameResult createGameResult2 = sharedGameService.createGame(new CreateGameRequest(registerResult.authToken(), "Game2"));

        Assertions.assertEquals("Game", sharedGameService.listGames(new GamesRequest(registerResult.authToken())).games().getFirst().gameName());
        Assertions.assertEquals(1, sharedGameService.listGames(new GamesRequest(registerResult.authToken())).games().getFirst().gameID());
        Assertions.assertEquals(1, createGameResult.gameID());

        Assertions.assertEquals("Game2", sharedGameService.listGames(new GamesRequest(registerResult.authToken())).games().get(1).gameName());
        Assertions.assertEquals(2, sharedGameService.listGames(new GamesRequest(registerResult.authToken())).games().get(1).gameID());
        Assertions.assertEquals(2, createGameResult2.gameID());
    }

    @Test
    @Order(4)
    @DisplayName("CreateGames Failure")
    public void createGamesFailure() {
        Assertions.assertThrows(UnauthorizedException.class, () -> sharedGameService.createGame(new CreateGameRequest("123", "Game")));
    }

    @Test
    @Order(5)
    @DisplayName("Join Success")
    public void joinSuccess() throws DataAccessException {
        RegisterResult registerResult = sharedUserService.register(new RegisterRequest("Bob", "password", "email"));

        CreateGameResult createGameResult = sharedGameService.createGame(new CreateGameRequest(registerResult.authToken(), "Game"));

        JoinResult joinResult = sharedGameService.joinGame(registerResult.authToken(), new JoinRequest(ChessGame.TeamColor.WHITE, createGameResult.gameID()));

        GameData testGameData = sharedGameService.listGames(new GamesRequest(registerResult.authToken())).games().get(createGameResult.gameID() - 1);

        Assertions.assertTrue(joinResult.success());
        Assertions.assertEquals(new GameData(createGameResult.gameID(), "Bob", null, "Game", new ChessGame()), testGameData);
    }

    @Test
    @Order(6)
    @DisplayName("Join Failure")
    public void JoinFailure() throws DataAccessException {
        Assertions.assertThrows(UnauthorizedException.class, () -> sharedGameService.joinGame("123", new JoinRequest(ChessGame.TeamColor.WHITE, 0)));

        RegisterResult registerResult = sharedUserService.register(new RegisterRequest("Bob", "password", "email"));

        Assertions.assertThrows(DataAccessException.class, () -> sharedGameService.joinGame(registerResult.authToken(), new JoinRequest(ChessGame.TeamColor.WHITE, 0)));

        CreateGameResult createGameResult = sharedGameService.createGame(new CreateGameRequest(registerResult.authToken(), "game"));
        sharedGameService.joinGame(registerResult.authToken(), new JoinRequest(ChessGame.TeamColor.WHITE, createGameResult.gameID()));

        sharedUserService.logout(new LogoutRequest(registerResult.authToken()));
        RegisterResult registerResult2 = sharedUserService.register(new RegisterRequest("Bob2", "password", "email"));

        Assertions.assertThrows(AlreadyTakenException.class, () -> sharedGameService.joinGame(registerResult2.authToken(), new JoinRequest(ChessGame.TeamColor.WHITE, createGameResult.gameID())));
        sharedGameService.joinGame(registerResult2.authToken(), new JoinRequest(ChessGame.TeamColor.BLACK, createGameResult.gameID()));

    }

}