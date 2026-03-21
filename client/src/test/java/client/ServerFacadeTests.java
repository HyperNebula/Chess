package client;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;
import server.Server;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() throws Exception {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        WebClient.clear();
    }

    @AfterAll
    static void stopServer() throws Exception {
        WebClient.clear();

        server.stop();
    }


    @Test
    void registerSuccess() throws Exception {
        var authData = WebClient.register(new String[]{"Register", "player1", "password", "p1@email.com"});
        Assertions.assertNotNull(authData);
        Assertions.assertTrue(authData.authToken().length() > 10);
        Assertions.assertEquals("player1", authData.username());
    }

    @Test
    void registerFailure() throws Exception {
        var authData = WebClient.register(new String[]{"Register", "player2", "password", "p1@email.com"});
        Assertions.assertNotNull(authData);
        Assertions.assertTrue(authData.authToken().length() > 10);
        Assertions.assertEquals("player2", authData.username());

        var authData2 = WebClient.register(new String[]{"Register", "player2", "password", "p1@email.com"});
        Assertions.assertNull(authData2);
    }

    @Test
    void loginSuccess() throws Exception {
        var authData = WebClient.login(new String[]{"login", "player1", "password"});
        Assertions.assertNotNull(authData);
        Assertions.assertTrue(authData.authToken().length() > 10);
        Assertions.assertEquals("player1", authData.username());
    }

    @Test
    void loginFailure() throws Exception {
        var authData2 = WebClient.login(new String[]{"login", "player2", "password2", "p1@email.com"});
        Assertions.assertNull(authData2);
    }

    @Test
    void logoutSuccess() throws Exception {
        var authData = WebClient.register(new String[]{"Register", "logoutUser", "password", "email"});

        Assertions.assertNotNull(authData);
        Assertions.assertDoesNotThrow(() -> WebClient.logout(authData.authToken()));

        var games = WebClient.listGames(authData.authToken());
        Assertions.assertNull(games);
    }

    @Test
    void logoutFailure() throws Exception {
        Assertions.assertDoesNotThrow(() -> WebClient.logout("fake-auth-token"));
    }

    @Test
    void createGameSuccess() throws Exception {
        var authData = WebClient.register(new String[]{"Register", "createGameUser", "password", "email"});

        Assertions.assertNotNull(authData);
        WebClient.createGame(authData.authToken(), new String[]{"create", "myAwesomeGame"});

        var games = WebClient.listGames(authData.authToken());
        Assertions.assertNotNull(games);
        boolean gameFound = games.stream().anyMatch(g -> g.gameName().equals("myAwesomeGame"));
        Assertions.assertTrue(gameFound);
    }

    @Test
    void createGameFailure() throws Exception {
        Assertions.assertDoesNotThrow(() ->
                WebClient.createGame("invalid-token", new String[]{"create", "ghostGame"})
        );

        var authData = WebClient.register(new String[]{"Register", "verifyUser", "password", "email"});

        Assertions.assertNotNull(authData);
        var games = WebClient.listGames(authData.authToken());

        if (games != null) {
            boolean gameFound = games.stream().anyMatch(g -> g.gameName().equals("ghostGame"));
            Assertions.assertFalse(gameFound);
        }
    }

    @Test
    void listGamesSuccess() throws Exception {
        var authData = WebClient.register(new String[]{"Register", "listGamesUser", "password", "email"});
        Assertions.assertNotNull(authData);
        WebClient.createGame(authData.authToken(), new String[]{"create", "listGame1"});
        WebClient.createGame(authData.authToken(), new String[]{"create", "listGame2"});

        var games = WebClient.listGames(authData.authToken());

        Assertions.assertNotNull(games);
        Assertions.assertTrue(games.size() >= 2);
    }

    @Test
    void listGamesFailure() throws Exception {
        var games = WebClient.listGames("bad-token-123");
        Assertions.assertNull(games);
    }

}
