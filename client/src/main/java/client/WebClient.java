package client;

public class WebClient {
    public static boolean login(String[] input) {
        String username = input[1];
        String password = input[2];

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
