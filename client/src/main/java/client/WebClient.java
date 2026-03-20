package client;
import model.RequestModal.*;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WebClient {
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    private static final String mainURL = "http://localhost:8080";

    public static boolean login(String[] input) throws Exception {
        LoginRequest loginRequest = new LoginRequest(input[1], input[2]);

        String tempJSONBody = new Gson().toJson(loginRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(mainURL + "/session"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(tempJSONBody))
                //.timeout(java.time.Duration.ofMillis(5000))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            System.out.println(httpResponse.body());
        } else {
            System.out.println("Error: received status code " + httpResponse.statusCode());
        }

        return true;
    }

    public static boolean register(String[] input) {
        String username = input[1];
        String password = input[2];
        String email = input[3];

        return true;
    }

    public static void logout() {

    }

    public static void createGame(String[] input) {
        String name = input[1];

    }

    public static void listGames() {

    }

    public static void joinGame(String[] input) {
        String ID = input[1];
        String teamColor = input[2];

    }

    public static void observeGame(String[] input) {
        String ID = input[1];

    }
}
