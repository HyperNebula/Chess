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
    static void stopServer() {
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

}
