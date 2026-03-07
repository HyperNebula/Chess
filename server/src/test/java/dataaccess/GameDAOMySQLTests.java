package dataaccess;

import chess.ChessGame;
import model.DataModel.GameData;
import org.junit.jupiter.api.*;
import service.AlreadyTakenException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameDAOMySQLTests {

    private static GameDAO sharedGameDAO;

    @BeforeAll
    public static void init() throws DataAccessException {
        sharedGameDAO = new GameDAOMySQL();

        sharedGameDAO.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("createGame Success")
    public void createGameSuccess() throws DataAccessException {
        GameData newGame = new GameData(0, null, null, "Game1", new ChessGame());

        int generatedId = sharedGameDAO.createGame(newGame);

        GameData retrievedGame = sharedGameDAO.getGame(generatedId);
        assertNotNull(retrievedGame);
        assertEquals("Game1", retrievedGame.gameName());
        assertNotNull(retrievedGame.game());
    }

    @Test
    @Order(2)
    @DisplayName("createGame Failure")
    public void createGameFailure() {
        GameData newGame = new GameData(0, null, null, null, new ChessGame());

        assertThrows(DataAccessException.class, () -> sharedGameDAO.createGame(newGame));
    }

    @Test
    @Order(3)
    @DisplayName("getGame Success")
    public void getGameSuccess() throws DataAccessException {
        GameData newGame = new GameData(0, "white", "black", "Game2", new ChessGame());

        int generatedId = sharedGameDAO.createGame(newGame);
        GameData retrievedGame = sharedGameDAO.getGame(generatedId);

        assertNotNull(retrievedGame);
        assertEquals(generatedId, retrievedGame.gameID());
        assertEquals("white", retrievedGame.whiteUsername());
        assertEquals("black", retrievedGame.blackUsername());
        assertEquals("Game2", retrievedGame.gameName());
    }

    @Test
    @Order(4)
    @DisplayName("getGame Failure")
    public void getGameFailure() throws DataAccessException {
        GameData missingGame = sharedGameDAO.getGame(999999);

        assertNull(missingGame);
    }

    @Test
    @Order(5)
    @DisplayName("listGames Success")
    public void listGamesSuccess() throws DataAccessException {
        sharedGameDAO.createGame(new GameData(0, null, null, "Game3", new ChessGame()));
        sharedGameDAO.createGame(new GameData(0, null, null, "Game4", new ChessGame()));

        List<GameData> games = sharedGameDAO.listGames();

        assertNotNull(games);
        assertEquals(4, games.size());
    }

    @Test
    @Order(6)
    @DisplayName("listGames Failure")
    public void listGamesFailure() throws DataAccessException {
        sharedGameDAO.deleteAll();
        List<GameData> games = sharedGameDAO.listGames();

        assertNotNull(games);
        assertTrue(games.isEmpty());
    }

    @Test
    @Order(7)
    @DisplayName("joinGame Success")
    public void joinGameSuccess() throws Exception {
        GameData newGame = new GameData(0, null, null, "Game5", new ChessGame());
        int gameId = sharedGameDAO.createGame(newGame);

        GameData gameToJoin = sharedGameDAO.getGame(gameId);

        assertDoesNotThrow(() -> sharedGameDAO.joinGame(gameToJoin, ChessGame.TeamColor.WHITE, "Bob"));

        GameData updatedGame = sharedGameDAO.getGame(gameId);
        assertEquals("Bob", updatedGame.whiteUsername());
        assertNull(updatedGame.blackUsername());
    }

    @Test
    @Order(8)
    @DisplayName("joinGame Failure")
    public void joinGameFailure() throws DataAccessException {
        GameData gameToJoin = sharedGameDAO.getGame(1);

        assertThrows(AlreadyTakenException.class, () ->
                sharedGameDAO.joinGame(gameToJoin, ChessGame.TeamColor.WHITE, "newPlayer")
        );
    }

    @Test
    @Order(9)
    @DisplayName("Delete Success")
    public void deleteSuccess() throws DataAccessException {
        assertDoesNotThrow(() -> sharedGameDAO.deleteAll());

        assertTrue(sharedGameDAO.listGames().isEmpty());
    }
}
