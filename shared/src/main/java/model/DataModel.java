package model;

import chess.ChessGame;

public class DataModel {

    record UserData(String username, String password, String email) {};

    record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {};

    record AuthData(String authToken, String username) {};

}
