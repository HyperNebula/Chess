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
    private static String authToken;

    @BeforeAll
    public static void init() throws DataAccessException {
        GameDAO sharedGameDAO = new GameDAOMySQL();
        AuthDAO sharedAuthDAO = new AuthDAOMySQL();
        UserDAO sharedUserDAO = new UserDAOMySQL();

        sharedUserDAO.deleteAll();
        sharedAuthDAO.deleteAll();
        sharedGameDAO.deleteAll();

        sharedGameService = new GameService(sharedGameDAO, sharedAuthDAO);
        sharedUserService = new UserService(sharedUserDAO, sharedAuthDAO);

        RegisterResult registerResult = sharedUserService.register(new RegisterRequest("Bob", "password", "email"));
        authToken = registerResult.authToken();
    }

    @Test
    @Order(1)
    @DisplayName("ListGames Success")
    public void listGamesSuccess() throws DataAccessException {
        Assertions.assertTrue(sharedGameService.listGames(new GamesRequest(authToken)).games().isEmpty());

        CreateGameResult createGameResult = sharedGameService.createGame(new CreateGameRequest(authToken, "Game"));

        Assertions.assertEquals(1,
                sharedGameService.listGames(new GamesRequest(authToken)).games().size());
        Assertions.assertEquals("Game",
                sharedGameService.listGames(new GamesRequest(authToken)).games().getFirst().gameName());
        Assertions.assertEquals(1,
                sharedGameService.listGames(new GamesRequest(authToken)).games().getFirst().gameID());
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
    public void createGamesSuccess() throws DataAccessException {
        CreateGameResult createGameResult = sharedGameService.createGame(new CreateGameRequest(authToken, "Game2"));
        CreateGameResult createGameResult2 = sharedGameService.createGame(new CreateGameRequest(authToken, "Game3"));

        Assertions.assertEquals("Game",
                sharedGameService.listGames(new GamesRequest(authToken)).games().getFirst().gameName());
        Assertions.assertEquals(1,
                sharedGameService.listGames(new GamesRequest(authToken)).games().getFirst().gameID());
        Assertions.assertEquals(2, createGameResult.gameID());

        Assertions.assertEquals("Game2",
                sharedGameService.listGames(new GamesRequest(authToken)).games().get(1).gameName());
        Assertions.assertEquals(2,
                sharedGameService.listGames(new GamesRequest(authToken)).games().get(1).gameID());
        Assertions.assertEquals(3, createGameResult2.gameID());
    }

    @Test
    @Order(4)
    @DisplayName("CreateGames Failure")
    public void createGamesFailure() {
        Assertions.assertThrows(UnauthorizedException.class,
                () -> sharedGameService.createGame(new CreateGameRequest("123", "Game")));
    }

    @Test
    @Order(5)
    @DisplayName("Join Success")
    public void joinSuccess() throws DataAccessException {
        CreateGameResult createGameResult = sharedGameService.createGame(new CreateGameRequest(authToken, "Game4"));

        JoinResult joinResult = sharedGameService.joinGame(authToken,
                new JoinRequest(ChessGame.TeamColor.WHITE, createGameResult.gameID()));

        GameData testGameData =
                sharedGameService.listGames(new GamesRequest(authToken)).games().get(createGameResult.gameID() - 1);

        Assertions.assertTrue(joinResult.success());
        Assertions.assertEquals(new GameData(createGameResult.gameID(), "Bob", null, "Game4", new ChessGame()), testGameData);
    }

    @Test
    @Order(6)
    @DisplayName("Join Failure")
    public void joinFailure() throws DataAccessException {
        Assertions.assertThrows(UnauthorizedException.class,
                () -> sharedGameService.joinGame("123", new JoinRequest(ChessGame.TeamColor.WHITE, 0)));

        Assertions.assertThrows(DataAccessException.class,
                () -> sharedGameService.joinGame(authToken,
                        new JoinRequest(ChessGame.TeamColor.WHITE, 0)));

        CreateGameResult createGameResult = sharedGameService.createGame(new CreateGameRequest(authToken, "game5"));
        sharedGameService.joinGame(authToken, new JoinRequest(ChessGame.TeamColor.WHITE, createGameResult.gameID()));

        //sharedUserService.logout(new LogoutRequest(authToken));
        RegisterResult registerResult2 = sharedUserService.register(new RegisterRequest("Bob2", "password", "email"));

        Assertions.assertThrows(AlreadyTakenException.class,
                () -> sharedGameService.joinGame(registerResult2.authToken(),
                        new JoinRequest(ChessGame.TeamColor.WHITE, createGameResult.gameID())));
        sharedGameService.joinGame(registerResult2.authToken(), new JoinRequest(ChessGame.TeamColor.BLACK, createGameResult.gameID()));
    }

    @Test
    @Order(7)
    @DisplayName("Delete Success")
    public void deleteSuccess() throws DataAccessException {
        sharedGameService.createGame(new CreateGameRequest(authToken, "Game6"));

        Assertions.assertNotEquals(0,
                sharedGameService.listGames(new GamesRequest(authToken)).games().size());

        sharedGameService.deleteAllGames();

        Assertions.assertTrue(sharedGameService.listGames(new GamesRequest(authToken)).games().isEmpty());
    }

    @Test
    @Order(8)
    @DisplayName("Delete Failure")
    public void deleteFailure() throws DataAccessException {
        sharedGameService.deleteAllGames();

        Assertions.assertTrue(sharedGameService.listGames(new GamesRequest(authToken)).games().isEmpty());
    }

}

