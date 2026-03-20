package client;

import chess.*;

import java.util.Scanner;

public class ClientMain {
    private static boolean loggedIn = false;
    public static void main(String[] args) {
        // var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        // System.out.println("♕ 240 Chess Client: " + piece);
        Scanner scanner = new Scanner(System.in);

        System.out.println("♕ Welcome to CS240 Chess Client. Type 'help' for a list of commands.");
        while (!loggedIn) {
            System.out.print("[LOGGED_OUT] >>> ");
            String input = scanner.nextLine().toLowerCase();

            if (input.equals("help")) {
                System.out.println("register");
            } else {
                System.out.println("Not a valid command. Type 'help' for a list of commands.");
            }
        }
    }
}
