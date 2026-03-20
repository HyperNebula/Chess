package client;

import chess.ChessGame;
import model.DataModel;
import model.RequestModal.*;
import model.ResultModal.*;
import static ui.EscapeSequences.*;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;


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

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            System.out.println(SET_TEXT_COLOR_GREEN + "\tSuccessfully logged out." + RESET_TEXT_COLOR);
        } else if (httpResponse.statusCode() == 401) {
            System.out.println(SET_TEXT_COLOR_RED  + "\tNot authorized. Try logging in again." + RESET_TEXT_COLOR);
        } else {
            System.out.println(SET_TEXT_COLOR_RED  + "Error: received status code " + httpResponse.statusCode() + RESET_TEXT_COLOR);
        }
    }

    public static void createGame(String[] input) throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(mainURL + "/game"))
                .header("authorization", ClientMain.authToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{ \"gameName\" : \"" + input[1] + "\" }"))
                .timeout(java.time.Duration.ofMillis(5000))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            System.out.println(SET_TEXT_COLOR_GREEN + "\tSuccessfully created game." + RESET_TEXT_COLOR);
        } else if (httpResponse.statusCode() == 401) {
            System.out.println(SET_TEXT_COLOR_RED  + "\tNot authorized. Try logging in again." + RESET_TEXT_COLOR);
        } else {
            System.out.println(SET_TEXT_COLOR_RED  + "Error: received status code " + httpResponse.statusCode() + RESET_TEXT_COLOR);
        }
    }

    public static List<DataModel.GameData> listGames() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(mainURL + "/game"))
                .header("authorization", ClientMain.authToken)
                .GET()
                .timeout(java.time.Duration.ofMillis(5000))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            GamesResult gamesResult = new Gson().fromJson(httpResponse.body(), GamesResult.class);
            return gamesResult.games();
        } else if (httpResponse.statusCode() == 401) {
            System.out.println(SET_TEXT_COLOR_RED  + "\tNot authorized. Try logging in again." + RESET_TEXT_COLOR);
        } else {
            System.out.println(SET_TEXT_COLOR_RED  + "Error: received status code " + httpResponse.statusCode() + RESET_TEXT_COLOR);
        }
        return null;
    }

    public static ChessGame joinGame(String[] input) throws Exception{
        String teamColor = input[2];
        JoinRequest joinRequest;

        ClientMain.listOfGames = listGames();

        if  (ClientMain.listOfGames == null) {
            return null;
        }
        else if (ClientMain.listOfGames.isEmpty()) {
            System.out.println(SET_TEXT_COLOR_RED  + "\tNo games exist. Create a game first and then join it." + RESET_TEXT_COLOR);
            return null;
        }

        int tempGameID = Integer.parseInt(input[1]);

        int realGameID = ClientMain.listOfGames.get(tempGameID-1).gameID();

        if (Objects.equals(teamColor, "white")) {
            if (Objects.equals(ClientMain.listOfGames.get(tempGameID - 1).whiteUsername(), ClientMain.username)) {
                System.out.println(SET_TEXT_COLOR_GREEN  + "\tSuccessfully joined the game." + RESET_TEXT_COLOR);
                return ClientMain.listOfGames.get(tempGameID-1).game();
            } else {
                joinRequest = new JoinRequest(ChessGame.TeamColor.WHITE, realGameID);
            }
        } else {
            if (Objects.equals(ClientMain.listOfGames.get(tempGameID - 1).blackUsername(), ClientMain.username)) {
                System.out.println(SET_TEXT_COLOR_GREEN  + "\tSuccessfully joined the game." + RESET_TEXT_COLOR);
                return ClientMain.listOfGames.get(tempGameID-1).game();
            } else {
                joinRequest = new JoinRequest(ChessGame.TeamColor.BLACK, realGameID);
            }
        }

        String tempJSONBody = new Gson().toJson(joinRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(mainURL + "/game"))
                .header("authorization", ClientMain.authToken)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(tempJSONBody))
                .timeout(java.time.Duration.ofMillis(5000))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            System.out.println(SET_TEXT_COLOR_GREEN  + "\tSuccessfully joined the game." + RESET_TEXT_COLOR);
            return ClientMain.listOfGames.get(tempGameID-1).game();
        } else if (httpResponse.statusCode() == 401) {
            System.out.println(SET_TEXT_COLOR_RED  + "\tNot authorized. Try logging in again." + RESET_TEXT_COLOR);
        } else if (httpResponse.statusCode() == 403) {
            System.out.println(SET_TEXT_COLOR_RED  + "\tThat game spot is full. Join a different game or as a different side." + RESET_TEXT_COLOR);
        } else {
            System.out.println(SET_TEXT_COLOR_RED  + "Error: received status code " + httpResponse.statusCode() + RESET_TEXT_COLOR);
        }
        return null;
    }

    public static ChessGame observeGame(String[] input) throws Exception {
        ClientMain.listOfGames = listGames();

        if  (ClientMain.listOfGames == null) {
            return null;
        }
        else if (ClientMain.listOfGames.isEmpty()) {
            System.out.println(SET_TEXT_COLOR_RED  + "\tNo games exist. Create a game first and then observe it." + RESET_TEXT_COLOR);
            return null;
        }

        int tempGameID = Integer.parseInt(input[1]);

        return ClientMain.listOfGames.get(tempGameID-1).game();
    }
}
