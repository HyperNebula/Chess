package client;

import model.RequestModal.*;
import model.ResultModal.*;
import static ui.EscapeSequences.*;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class WebClient {
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    private static final String mainURL = "http://localhost:8080";

    public static boolean register(String[] input) throws Exception{
        RegisterRequest registerRequest = new RegisterRequest(input[1], input[2], input[3]);

        String tempJSONBody = new Gson().toJson(registerRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(mainURL + "/user"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(tempJSONBody))
                .timeout(java.time.Duration.ofMillis(5000))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            RegisterResult registerResult = new Gson().fromJson(httpResponse.body(), RegisterResult.class);
            ClientMain.username = registerResult.username();
            ClientMain.authToken = registerResult.authToken();
            return true;
        } else if (httpResponse.statusCode() == 403) {
            System.out.println(SET_TEXT_COLOR_RED  + "\tUsername '" + input[1] + "' already taken. Try again with another username." + RESET_TEXT_COLOR);
            return false;
        } else {
            System.out.println(SET_TEXT_COLOR_RED  + "Error: received status code " + httpResponse.statusCode() + RESET_TEXT_COLOR);
            return false;
        }
    }

    public static boolean login(String[] input) throws Exception {
        LoginRequest loginRequest = new LoginRequest(input[1], input[2]);

        String tempJSONBody = new Gson().toJson(loginRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(mainURL + "/session"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(tempJSONBody))
                .timeout(java.time.Duration.ofMillis(5000))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            LoginResult loginResult = new Gson().fromJson(httpResponse.body(), LoginResult.class);
            ClientMain.username = loginResult.username();
            ClientMain.authToken = loginResult.authToken();
            return true;
        } else if (httpResponse.statusCode() == 401) {
            System.out.println(SET_TEXT_COLOR_RED  + "\tIncorrect username or password. Try again." + RESET_TEXT_COLOR);
            return false;
        } else {
            System.out.println(SET_TEXT_COLOR_RED  + "Error: received status code " + httpResponse.statusCode() + RESET_TEXT_COLOR);
            return false;
        }
    }

    public static void logout() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(mainURL + "/session"))
                .header("authorization", ClientMain.authToken)
                .DELETE()
                .timeout(java.time.Duration.ofMillis(5000))
                .build();

        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(SET_TEXT_COLOR_GREEN + "\tSuccessfully logged out." + RESET_TEXT_COLOR);
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
