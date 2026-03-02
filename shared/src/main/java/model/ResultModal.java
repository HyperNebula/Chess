package model;

import java.util.List;

public class ResultModal {

    public record LoginResult(String username, String authToken) {}

    public record RegisterResult(String username, String authToken) {}

    public record LogoutResult(Boolean success) {}

    public record GamesResult(List<DataModel.GameData> gameDataList) {}

    public record CreateGameResult(int gameID) {}

    public record JoinResult(Boolean success) {}

}
