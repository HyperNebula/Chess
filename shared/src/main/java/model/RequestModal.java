package model;

import chess.ChessGame;

public class RequestModal {

    public record LoginRequest(String username, String password) {}

    public record RegisterRequest(String username, String password, String email) {}

    public record LogoutRequest(String authToken) {}

    public record GamesRequest(String authToken) {}

    public record CreateGameRequest(String authToken, String gameName) {}

    public record JoinRequest(ChessGame.TeamColor playerColor, int gameID) {}

}
