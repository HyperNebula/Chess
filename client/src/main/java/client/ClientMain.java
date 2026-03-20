package client;

import chess.*;
import model.DataModel;

import static client.WebClient.*;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ClientMain {
    private static boolean loggedIn = false;
    private static boolean playing = false;

    public static String username;
    public static String authToken;

    public static List<DataModel.GameData> listOfGames;
    private static ChessGame game;

    public static void main(String[] args) throws Exception {
        // var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        // System.out.println("♕ 240 Chess Client: " + piece);
        Scanner scanner = new Scanner(System.in);

        System.out.println("♕ Welcome to CS240 Chess Client. Type '" + SET_TEXT_COLOR_GREEN + "help" + RESET_TEXT_COLOR + "' for a list of commands.");
        while (true) {
            if (!loggedIn && !playing) {
                System.out.print(SET_TEXT_COLOR_BLUE + "[LOGGED_OUT]" + RESET_TEXT_COLOR + " >>> ");
                String[] input = scanner.nextLine().toLowerCase().split(" ");

                switch (input[0]) {
                    case "quit":
                        System.out.println("\tExiting...");
                        System.exit(0);
                        return;
                    case "help":
                        System.out.println(SET_TEXT_COLOR_YELLOW + "\tregister <USERNAME> <PASSWORD> <EMAIL>" + RESET_TEXT_COLOR + " - Create an account");
                        System.out.println(SET_TEXT_COLOR_YELLOW + "\tlogin <USERNAME> <PASSWORD>" + RESET_TEXT_COLOR + " - Login to a created account");
                        System.out.println(SET_TEXT_COLOR_YELLOW + "\thelp" + RESET_TEXT_COLOR + " - Displays possible commands");
                        System.out.println(SET_TEXT_COLOR_YELLOW + "\tquit" + RESET_TEXT_COLOR + " - Exits the program.");
                        break;
                    case "login":
                        if (input.length != 3) {
                            System.out.println("\tProper usage is: " + SET_TEXT_COLOR_YELLOW + "login <USERNAME> <PASSWORD>" + RESET_TEXT_COLOR);
                        } else {
                            if (login(input)) {
                                loggedIn = true;
                                System.out.println("♕ Logged in as " + username + ". Type '" + SET_TEXT_COLOR_GREEN + "help" + RESET_TEXT_COLOR + "' for a list of available commands.");
                            }
                        }
                        break;
                    case "register":
                        if (input.length != 4) {
                            System.out.println("\tProper usage is: " + SET_TEXT_COLOR_YELLOW + "register <USERNAME> <PASSWORD> <EMAIL>" + RESET_TEXT_COLOR);
                        } else {
                            if (register(input)) {
                                loggedIn = true;
                                System.out.println("♕ Created account and logged in as " + username + ". Type '" + SET_TEXT_COLOR_GREEN + "help" + RESET_TEXT_COLOR + "' for a list of available commands.");
                            }
                        }
                        break;
                    default:
                        System.out.println("\tNot a valid command. Type '" + SET_TEXT_COLOR_GREEN + "help" + RESET_TEXT_COLOR + "' for a list of commands.");
                        break;
                }
            } else if (loggedIn && !playing) {
                System.out.print(SET_TEXT_COLOR_BLUE + "[LOGGED_IN]" + RESET_TEXT_COLOR + " >>> ");
                String[] input = scanner.nextLine().toLowerCase().split(" ");

                switch (input[0]) {
                    case "quit":
                        System.out.println("\tExiting...");
                        System.exit(0);
                        break;
                    case "help":
                        System.out.println(SET_TEXT_COLOR_YELLOW + "\tcreate <NAME>" + RESET_TEXT_COLOR + " - Create a game");
                        System.out.println(SET_TEXT_COLOR_YELLOW + "\tlist" + RESET_TEXT_COLOR + " - List all created games");
                        System.out.println(SET_TEXT_COLOR_YELLOW + "\tjoin <ID> [WHITE|BLACK]" + RESET_TEXT_COLOR + " - Join a game based on the ID");
                        System.out.println(SET_TEXT_COLOR_YELLOW + "\tobserve <ID>" + RESET_TEXT_COLOR + " - Observe a game based on the ID");
                        System.out.println(SET_TEXT_COLOR_YELLOW + "\tlogout" + RESET_TEXT_COLOR + " - Logout of the client");
                        System.out.println(SET_TEXT_COLOR_YELLOW + "\thelp" + RESET_TEXT_COLOR + " - Displays possible commands");
                        System.out.println(SET_TEXT_COLOR_YELLOW + "\tquit" + RESET_TEXT_COLOR + " - Exits the program.");
                        break;
                    case "logout":
                        logout();
                        loggedIn = false;
                        playing = false;
                        break;
                    case "list":
                        List<DataModel.GameData> tempGameList = listGames();

                        listOfGames = tempGameList;

                        if (tempGameList == null) {
                            break;
                        } else if (tempGameList.isEmpty()) {
                            System.out.println("\tNo games created. Used the command '" + SET_TEXT_COLOR_GREEN + "create" + RESET_TEXT_COLOR + "' to create a new game." );
                            break;
                        } else {
                            System.out.println("\tCreated games and their ID:");
                            for (int i = 0; i < tempGameList.size(); i++) {
                                System.out.println(SET_TEXT_COLOR_YELLOW + "\t" + (i + 1) + ": " + tempGameList.get(i).gameName() + RESET_TEXT_COLOR);
                                System.out.println("\t\tPlayers in the game - White: " + SET_TEXT_COLOR_YELLOW
                                        + (tempGameList.get(i).whiteUsername() != null ? tempGameList.get(i).whiteUsername() : "None")
                                        + RESET_TEXT_COLOR + " Black: " + SET_TEXT_COLOR_YELLOW
                                        + (tempGameList.get(i).blackUsername() != null ? tempGameList.get(i).blackUsername() : "None") + RESET_TEXT_COLOR);
                            }
                            break;
                        }
                    case "join":
                        if (input.length != 3 && (Objects.equals(input[2], "white") || Objects.equals(input[2], "black")) && isInteger(input[1])) {
                            System.out.println("\tProper usage is: " + SET_TEXT_COLOR_YELLOW + "join <ID> [WHITE|BLACK]" + RESET_TEXT_COLOR);
                        } else {
                            game = joinGame(input);
                            if (game != null) {
                                playing = true;
                            }
                        }
                        break;
                    case "observe":
                        if (input.length != 2 && isInteger(input[1])){
                            System.out.println("\tProper usage is: " + SET_TEXT_COLOR_YELLOW + "observe <ID>" + RESET_TEXT_COLOR);
                        } else {
                            game = observeGame(input);
                            if (game != null) {
                                playing = true;
                            }
                        }
                        break;
                    case "create":
                        if (input.length != 2){
                            System.out.println("\tProper usage is: " + SET_TEXT_COLOR_YELLOW + "create <NAME>" + RESET_TEXT_COLOR);
                        } else {
                            createGame(input);
                        }
                        break;
                    default:
                        System.out.println("\tNot a valid command. Type '" + SET_TEXT_COLOR_GREEN + "help" + RESET_TEXT_COLOR + "' for a list of commands.");
                        break;
                }
            } else if (loggedIn && playing) {
                System.out.print(SET_TEXT_COLOR_BLUE + "[PLAYING]" + RESET_TEXT_COLOR + " >>> ");
                String[] input = scanner.nextLine().toLowerCase().split(" ");
            } else {
                System.out.println("Error. Unknown state. Relaunch the program.");
            }
        }
    }



    public static boolean isInteger(String str) {
        if (str == null || str.isBlank()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
