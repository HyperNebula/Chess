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
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    private static String mainUrl = "http://localhost:8080";


    public WebClient(int port) {
        mainUrl = "http://localhost:" + port;
    }

    public static void clear() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(mainUrl + "/db"))
                .DELETE()
                .timeout(java.time.Duration.ofMillis(5000))
                .build();

        HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static RegisterResult register(String[] input) throws Exception{
        RegisterRequest registerRequest = new RegisterRequest(input[1], input[2], input[3]);

        String tempJSONBody = new Gson().toJson(registerRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(mainUrl + "/user"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(tempJSONBody))
                .timeout(java.time.Duration.ofMillis(5000))
                .build();

        HttpResponse<String> httpResponse = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            return new Gson().fromJson(httpResponse.body(), RegisterResult.class);
        } else if (httpResponse.statusCode() == 403) {
            System.out.println(SET_TEXT_COLOR_RED  + "\tUsername '" + input[1]
                    + "' already taken. Try again with another username." + RESET_TEXT_COLOR);
            return null;
        } else {
            System.out.println(SET_TEXT_COLOR_RED  + "Error: received status code "
                    + httpResponse.statusCode() + RESET_TEXT_COLOR);
            return null;
        }
    }

    public static LoginResult login(String[] input) throws Exception {
        LoginRequest loginRequest = new LoginRequest(input[1], input[2]);

        String tempJSONBody = new Gson().toJson(loginRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(mainUrl + "/session"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(tempJSONBody))
                .timeout(java.time.Duration.ofMillis(5000))
                .build();

        HttpResponse<String> httpResponse = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            return new Gson().fromJson(httpResponse.body(), LoginResult.class);
        } else if (httpResponse.statusCode() == 401) {
            System.out.println(SET_TEXT_COLOR_RED  + "\tIncorrect username or password. Try again."
                    + RESET_TEXT_COLOR);
            return null;
        } else {
            System.out.println(SET_TEXT_COLOR_RED  + "Error: received status code " + httpResponse.statusCode()
                    + RESET_TEXT_COLOR);
            return null;
        }
    }

    public static void logout(String authToken) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(mainUrl + "/session"))
                .header("authorization", authToken)
                .DELETE()
                .timeout(java.time.Duration.ofMillis(5000))
                .build();

        HttpResponse<String> httpResponse = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            System.out.println(SET_TEXT_COLOR_GREEN + "\tSuccessfully logged out." + RESET_TEXT_COLOR);
        } else if (httpResponse.statusCode() == 401) {
            System.out.println(SET_TEXT_COLOR_RED  + "\tNot authorized. Try logging in again." + RESET_TEXT_COLOR);
        } else {
            System.out.println(SET_TEXT_COLOR_RED  + "Error: received status code " + httpResponse.statusCode()
                    + RESET_TEXT_COLOR);
        }
    }

    public static void createGame(String authToken, String[] input) throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(mainUrl + "/game"))
                .header("authorization", authToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{ \"gameName\" : \"" + input[1] + "\" }"))
                .timeout(java.time.Duration.ofMillis(5000))
                .build();

        HttpResponse<String> httpResponse = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            System.out.println(SET_TEXT_COLOR_GREEN + "\tSuccessfully created game." + RESET_TEXT_COLOR);
        } else if (httpResponse.statusCode() == 401) {
            System.out.println(SET_TEXT_COLOR_RED  + "\tNot authorized. Try logging in again." + RESET_TEXT_COLOR);
        } else {
            System.out.println(SET_TEXT_COLOR_RED  + "Error: received status code " + httpResponse.statusCode()
                    + RESET_TEXT_COLOR);
        }
    }

    public static List<DataModel.GameData> listGames(String authToken) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(mainUrl + "/game"))
                .header("authorization", authToken)
                .GET()
                .timeout(java.time.Duration.ofMillis(5000))
                .build();

        HttpResponse<String> httpResponse = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            GamesResult gamesResult = new Gson().fromJson(httpResponse.body(), GamesResult.class);
            return gamesResult.games();
        } else if (httpResponse.statusCode() == 401) {
            System.out.println(SET_TEXT_COLOR_RED  + "\tNot authorized. Try logging in again." + RESET_TEXT_COLOR);
        } else {
            System.out.println(SET_TEXT_COLOR_RED  + "Error: received status code " + httpResponse.statusCode()
                    + RESET_TEXT_COLOR);
        }
        return null;
    }

    public static ChessGame joinGame(String authToken, String username, String[] input) throws Exception{
        String teamColor = input[2];
        JoinRequest joinRequest;

        List<DataModel.GameData> gameList = listGames(authToken);

        if  (gameList == null) {
            return null;
        }
        else if (gameList.isEmpty()) {
            System.out.println(SET_TEXT_COLOR_RED  + "\tNo games exist. Create a game first and then join it."
                    + RESET_TEXT_COLOR);
            return null;
        }

        int tempGameID = Integer.parseInt(input[1]);

        if (tempGameID > gameList.size() || tempGameID < 1) {
            System.out.println(SET_TEXT_COLOR_RED  + "\tID does not correspond with a game." + RESET_TEXT_COLOR);
            return null;
        }

        int realGameID = gameList.get(tempGameID-1).gameID();

        if (Objects.equals(teamColor, "white")) {
            if (Objects.equals(gameList.get(tempGameID - 1).whiteUsername(), username)) {
                System.out.println(SET_TEXT_COLOR_GREEN  + "\tSuccessfully joined the game." + RESET_TEXT_COLOR);
                return gameList.get(tempGameID-1).game();
            } else {
                joinRequest = new JoinRequest(ChessGame.TeamColor.WHITE, realGameID);
            }
        } else {
            if (Objects.equals(gameList.get(tempGameID - 1).blackUsername(), username)) {
                System.out.println(SET_TEXT_COLOR_GREEN  + "\tSuccessfully joined the game." + RESET_TEXT_COLOR);
                return gameList.get(tempGameID-1).game();
            } else {
                joinRequest = new JoinRequest(ChessGame.TeamColor.BLACK, realGameID);
            }
        }

        String tempJSONBody = new Gson().toJson(joinRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(mainUrl + "/game"))
                .header("authorization", authToken)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(tempJSONBody))
                .timeout(java.time.Duration.ofMillis(5000))
                .build();

        HttpResponse<String> httpResponse = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            System.out.println(SET_TEXT_COLOR_GREEN  + "\tSuccessfully joined the game." + RESET_TEXT_COLOR);
            return gameList.get(tempGameID-1).game();
        } else if (httpResponse.statusCode() == 401) {
            System.out.println(SET_TEXT_COLOR_RED  + "\tNot authorized. Try logging in again." + RESET_TEXT_COLOR);
        } else if (httpResponse.statusCode() == 403) {
            System.out.println(SET_TEXT_COLOR_RED
                    + "\tThat game spot is full. Join a different game or as a different side." + RESET_TEXT_COLOR);
        } else {
            System.out.println(SET_TEXT_COLOR_RED  + "Error: received status code " + httpResponse.statusCode()
                    + RESET_TEXT_COLOR);
        }
        return null;
    }

    public static ChessGame observeGame(String authToken, String[] input) throws Exception {
        List<DataModel.GameData> gameList = listGames(authToken);

        if  (gameList == null) {
            return null;
        }
        else if (gameList.isEmpty()) {
            System.out.println(SET_TEXT_COLOR_RED  + "\tNo games exist. Create a game first and then observe it."
                    + RESET_TEXT_COLOR);
            return null;
        }

        int tempGameID = Integer.parseInt(input[1]);

        if (tempGameID > gameList.size() || tempGameID < 1) {
            System.out.println(SET_TEXT_COLOR_RED  + "\tID does not correspond with a game." + RESET_TEXT_COLOR);
            return null;
        }

        return gameList.get(tempGameID-1).game();
    }
}
