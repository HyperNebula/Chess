package client;

import chess.*;

import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ClientMain {
    private static boolean loggedIn = false;
    private static boolean playing = false;

    private static String username;
    private static String authToken;

    public static void main(String[] args) {
        // var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        // System.out.println("♕ 240 Chess Client: " + piece);
        Scanner scanner = new Scanner(System.in);

        System.out.println("♕ Welcome to CS240 Chess Client. Type '" + SET_TEXT_COLOR_GREEN + "help" + RESET_TEXT_COLOR + "' for a list of commands.");
        while (true) {
            if (!loggedIn) {
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
                            login(input);
                            System.out.println("♕ Logged in as " + username + ". Type '" + SET_TEXT_COLOR_GREEN + "help" + RESET_TEXT_COLOR + "' for a list of available commands.");
                        }
                        break;
                    case "register":
                        if (input.length != 4) {
                            System.out.println("\tProper usage is: " + SET_TEXT_COLOR_YELLOW + "register <USERNAME> <PASSWORD> <EMAIL>" + RESET_TEXT_COLOR);
                        } else {
                            register(input);
                            System.out.println("♕ Created account and logged in as " + username + ". Type '" + SET_TEXT_COLOR_GREEN + "help" + RESET_TEXT_COLOR + "' for a list of available commands.");
                        }
                        break;
                    default:
                        System.out.println("\tNot a valid command. Type '" + SET_TEXT_COLOR_GREEN + "help" + RESET_TEXT_COLOR + "' for a list of commands.");
                        break;
                }
            }

            if (loggedIn && !playing) {
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
                        listGames();
                        break;
                    case "join":
                        if (input.length != 3 && (Objects.equals(input[2], "WHITE") || Objects.equals(input[2], "BLACK"))) {
                            System.out.println("\tProper usage is: " + SET_TEXT_COLOR_YELLOW + "join <ID> [WHITE|BLACK]" + RESET_TEXT_COLOR);
                        } else {
                            joinGame(input);
                        }
                        break;
                    case "observe":
                        if (input.length != 2){
                            System.out.println("\tProper usage is: " + SET_TEXT_COLOR_YELLOW + "observe <ID>" + RESET_TEXT_COLOR);
                        } else {
                            observeGame(input);
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
            }
        }
    }

    private static void login(String[] input) {
        String username = input[1];
        String password = input[2];

        loggedIn = true;
    }

    private static void register(String[] input) {
        String username = input[1];
        String password = input[2];
        String email = input[3];

        loggedIn = true;
    }

    private static void logout() {

    }

    private static void createGame(String[] input) {
        String name = input[1];

    }

    private static void listGames() {

    }

    private static void joinGame(String[] input) {
        String ID = input[1];
        String teamColor = input[2];

    }

    private static void observeGame(String[] input) {
        String ID = input[1];

    }
}
