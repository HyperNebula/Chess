package client;

import chess.*;

import java.util.List;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ClientMain {
    private static boolean loggedIn = false;

    public static void main(String[] args) {
        // var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        // System.out.println("♕ 240 Chess Client: " + piece);
        Scanner scanner = new Scanner(System.in);

        System.out.println("♕ Welcome to CS240 Chess Client. Type '" + SET_TEXT_COLOR_GREEN + "help" + RESET_TEXT_COLOR + "' for a list of commands.");
        while (!loggedIn) {
            System.out.print(SET_TEXT_COLOR_BLUE + "[LOGGED_OUT]" + RESET_TEXT_COLOR + " >>> ");
            String[] input = scanner.nextLine().toLowerCase().split(" ");

            switch (input[0]) {
                case "quit":
                    System.out.println("\tExiting...");
                    System.exit(0);
                    break;
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
                    }
                    break;
                case "register":
                    if (input.length != 4) {
                        System.out.println("\tProper usage is: " + SET_TEXT_COLOR_YELLOW + "register <USERNAME> <PASSWORD> <EMAIL>" + RESET_TEXT_COLOR);
                    } else {
                        register(input);
                    }
                    break;
                default:
                    System.out.println("\tNot a valid command. Type '" + SET_TEXT_COLOR_GREEN + "help" + RESET_TEXT_COLOR + "' for a list of commands.");
                    break;
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
}
