package client;

import chess.*;
import model.DataModel;
import model.ResultModal.*;

import static client.WebClient.*;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ClientMain {
    private static boolean loggedIn = false;
    private static boolean playing = false;

    public static String username;
    private static String authToken;

    public static List<DataModel.GameData> listOfGames;
    public static ChessGame game;
    public static int realGameID;

    public static String teamColor = "white";

    private static WebSocketClient webSocketClient;
    private static ServerNotificationHandler serverNotificationHandler = new ServerNotificationHandler() {
        @Override
        public void notify(String message) {
            System.out.println("\t" + message);
            System.out.print(SET_TEXT_COLOR_BLUE + "[PLAYING]" + RESET_TEXT_COLOR + " >>> ");
        }
    };

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        webSocketClient = new WebSocketClient("http://localhost:8080", serverNotificationHandler);

        System.out.println("♕ Welcome to CS240 Chess Client. Type '" + SET_TEXT_COLOR_GREEN + "help"
                + RESET_TEXT_COLOR + "' for a list of commands.");
        while (true) {
            if (!loggedIn && !playing) {
                loggedOutState(scanner);
            } else if (loggedIn && !playing) {
                loggedInState(scanner);
            } else if (loggedIn && playing) {
                playingState(scanner);
            } else {
                System.out.println("Error. Unknown state. Relaunch the program.");
                System.exit(1);
            }
        }
    }

    private static void loggedOutState(Scanner scanner) throws Exception {
        System.out.print(SET_TEXT_COLOR_BLUE + "[LOGGED_OUT]" + RESET_TEXT_COLOR + " >>> ");
        String[] input = scanner.nextLine().toLowerCase().split(" ");

        switch (input[0]) {
            case "quit":
                System.out.println("\tExiting...");
                System.exit(0);
                return;
            case "help":
                System.out.println(SET_TEXT_COLOR_YELLOW + "\tregister <USERNAME> <PASSWORD> <EMAIL>"
                        + RESET_TEXT_COLOR + " - Create an account");
                System.out.println(SET_TEXT_COLOR_YELLOW + "\tlogin <USERNAME> <PASSWORD>" + RESET_TEXT_COLOR
                        + " - Login to a created account");
                System.out.println(SET_TEXT_COLOR_YELLOW + "\thelp" + RESET_TEXT_COLOR
                        + " - Displays possible commands");
                System.out.println(SET_TEXT_COLOR_YELLOW + "\tquit" + RESET_TEXT_COLOR
                        + " - Exits the program.");
                break;
            case "login":
                if (input.length != 3) {
                    System.out.println("\tProper usage is: " + SET_TEXT_COLOR_YELLOW + "login <USERNAME> <PASSWORD>"
                            + RESET_TEXT_COLOR);
                } else {
                    LoginResult loginResult = login(input);
                    if (loginResult != null) {
                        ClientMain.username = loginResult.username();
                        ClientMain.authToken = loginResult.authToken();
                        loggedIn = true;
                        System.out.println("♕ Logged in as " + username + ". Type '" + SET_TEXT_COLOR_GREEN
                                + "help" + RESET_TEXT_COLOR + "' for a list of available commands.");
                    }
                }
                break;
            case "register":
                if (input.length != 4) {
                    System.out.println("\tProper usage is: " + SET_TEXT_COLOR_YELLOW
                            + "register <USERNAME> <PASSWORD> <EMAIL>" + RESET_TEXT_COLOR);
                } else {
                    RegisterResult registerResult = register(input);
                    if (registerResult != null) {
                        ClientMain.username = registerResult.username();
                        ClientMain.authToken = registerResult.authToken();
                        loggedIn = true;
                        System.out.println("♕ Created account and logged in as " + username
                                + ". Type '" + SET_TEXT_COLOR_GREEN + "help" + RESET_TEXT_COLOR
                                + "' for a list of available commands.");
                    }
                }
                break;
            default:
                System.out.println("\tNot a valid command. Type '" + SET_TEXT_COLOR_GREEN + "help"
                        + RESET_TEXT_COLOR + "' for a list of commands.");
                break;
        }
    }

    private static void loggedInState(Scanner scanner) throws Exception {
        System.out.print(SET_TEXT_COLOR_BLUE + "[LOGGED_IN]" + RESET_TEXT_COLOR + " >>> ");
        String[] input = scanner.nextLine().toLowerCase().split(" ");

        switch (input[0]) {
            case "quit":
                System.out.println("\tExiting...");
                System.exit(0);
                break;
            case "help":
                System.out.println(SET_TEXT_COLOR_YELLOW + "\tcreate <NAME>" + RESET_TEXT_COLOR
                        + " - Create a game");
                System.out.println(SET_TEXT_COLOR_YELLOW + "\tlist" + RESET_TEXT_COLOR
                        + " - List all created games");
                System.out.println(SET_TEXT_COLOR_YELLOW + "\tjoin <ID> [WHITE|BLACK]"
                        + RESET_TEXT_COLOR + " - Join a game based on the ID");
                System.out.println(SET_TEXT_COLOR_YELLOW + "\tobserve <ID>" + RESET_TEXT_COLOR
                        + " - Observe a game based on the ID");
                System.out.println(SET_TEXT_COLOR_YELLOW + "\tlogout" + RESET_TEXT_COLOR
                        + " - Logout of the client");
                System.out.println(SET_TEXT_COLOR_YELLOW + "\thelp" + RESET_TEXT_COLOR
                        + " - Displays possible commands");
                System.out.println(SET_TEXT_COLOR_YELLOW + "\tquit" + RESET_TEXT_COLOR
                        + " - Exits the program.");
                break;
            case "logout":
                logout(authToken);
                loggedIn = false;
                playing = false;
                break;
            case "list":
                List<DataModel.GameData> tempGameList = listGames(authToken);

                listOfGames = tempGameList;

                if (tempGameList == null) {
                    System.out.print(SET_TEXT_COLOR_BLUE + "[LOGGED_IN]" + RESET_TEXT_COLOR + " >>> ");
                    break;
                } else if (tempGameList.isEmpty()) {
                    System.out.println("\tNo games created. Used the command '" + SET_TEXT_COLOR_GREEN
                            + "create" + RESET_TEXT_COLOR + "' to create a new game." );
                    break;
                } else {
                    System.out.println("\tCreated games and their ID:");
                    for (int i = 0; i < tempGameList.size(); i++) {
                        System.out.println(SET_TEXT_COLOR_YELLOW + "\t" + (i + 1) + ": "
                                + tempGameList.get(i).gameName() + RESET_TEXT_COLOR);
                        System.out.println("\t\tPlayers in the game - White: " + SET_TEXT_COLOR_YELLOW
                                + (tempGameList.get(i).whiteUsername() != null ? tempGameList.get(i).whiteUsername() : "None")
                                + RESET_TEXT_COLOR + " Black: " + SET_TEXT_COLOR_YELLOW
                                + (tempGameList.get(i).blackUsername() != null ? tempGameList.get(i).blackUsername() : "None")
                                + RESET_TEXT_COLOR);
                    }
                    break;
                }
            case "join":
                if (input.length != 3
                        || (!Objects.equals(input[2], "white") && !Objects.equals(input[2], "black"))
                        || !isInteger(input[1])) {
                    System.out.println("\tProper usage is: " + SET_TEXT_COLOR_YELLOW + "join <ID> [WHITE|BLACK]"
                            + RESET_TEXT_COLOR);
                } else {
                    model.DataModel.GameData tempGameData = joinGame(authToken, username, input);
                    if (tempGameData != null) {
                        game = tempGameData.game();
                        teamColor = input[2];
                        realGameID = tempGameData.gameID();
                        playing = true;

                        webSocketClient = new WebSocketClient("http://localhost:8080", serverNotificationHandler);
                        webSocketClient.connect(authToken, realGameID);
                    } else {
                        System.out.println("\tNot a proper game ID");
                        System.out.print(SET_TEXT_COLOR_BLUE + "[LOGGED_IN]" + RESET_TEXT_COLOR + " >>> ");
                    }
                }
                break;
            case "observe":
                if (input.length != 2 || !isInteger(input[1])){
                    System.out.println("\tProper usage is: " + SET_TEXT_COLOR_YELLOW + "observe <ID>"
                            + RESET_TEXT_COLOR);
                } else {
                    model.DataModel.GameData tempGameData = observeGame(authToken, input);
                    if (tempGameData != null) {
                        game = tempGameData.game();
                        playing = true;
                        teamColor = "white";
                        realGameID = tempGameData.gameID();

                        webSocketClient = new WebSocketClient("http://localhost:8080", serverNotificationHandler);
                        webSocketClient.connect(authToken, realGameID);
                    } else {
                        System.out.println("\tNot a proper game ID");
                        System.out.print(SET_TEXT_COLOR_BLUE + "[LOGGED_IN]" + RESET_TEXT_COLOR + " >>> ");
                    }
                }
                break;
            case "create":
                if (input.length != 2){
                    System.out.println("\tProper usage is: " + SET_TEXT_COLOR_YELLOW + "create <NAME>"
                            + RESET_TEXT_COLOR);
                } else {
                    createGame(authToken, input);
                }
                break;
            default:
                System.out.println("\tNot a valid command. Type '" + SET_TEXT_COLOR_GREEN + "help"
                        + RESET_TEXT_COLOR + "' for a list of commands.");
                break;
        }
    }

    private static void playingState(Scanner scanner) throws Exception {
        String[] input = scanner.nextLine().toLowerCase().split(" ");
        switch (input[0]) {
            case "quit":
                System.out.println("\tExiting...");
                System.exit(0);
                break;
            case "help":
                System.out.println(SET_TEXT_COLOR_YELLOW + "\tmove <PIECE> <TARGET SQUARE> [PROMOTION PIECE (q,r,b,n)]" + RESET_TEXT_COLOR
                        + " - move a piece to a square on the board. Add promotion piece (q, r, b, n) if promoting a pawn.");
                System.out.println(SET_TEXT_COLOR_YELLOW + "\tredraw" + RESET_TEXT_COLOR
                        + " - redraw the chess board in it's current state");
                System.out.println(SET_TEXT_COLOR_YELLOW + "\tleave" + RESET_TEXT_COLOR
                        + " - leave the current game");
                System.out.println(SET_TEXT_COLOR_YELLOW + "\tresign" + RESET_TEXT_COLOR
                        + " - forfeit the game");
                System.out.println(SET_TEXT_COLOR_YELLOW + "\thighlight <PIECE>" + RESET_TEXT_COLOR
                        + " - highlights all legal moves a piece can make");
                System.out.println(SET_TEXT_COLOR_YELLOW + "\thelp" + RESET_TEXT_COLOR
                        + " - Displays possible commands");
                System.out.println(SET_TEXT_COLOR_YELLOW + "\tquit" + RESET_TEXT_COLOR
                        + " - Exits the program.");
                System.out.print(SET_TEXT_COLOR_BLUE + "[PLAYING]" + RESET_TEXT_COLOR + " >>> ");
                break;
            case "redraw":
                BoardPrinter.printBoard();
                break;
            case "leave":
                loggedIn = true;
                playing = false;

                webSocketClient.leave(authToken, realGameID);

                break;
            case "resign":
                System.out.print(SET_TEXT_COLOR_RED + "\tAre you sure you want to resign? (type 'yes' or 'no') "  + RESET_TEXT_COLOR);

                String input2;
                while (true) {
                    input2 = scanner.nextLine().trim();
                    if (input2.equalsIgnoreCase("yes") || input2.equalsIgnoreCase("no")) {
                        break;
                    }
                    System.out.print(SET_TEXT_COLOR_RED + "\tPlease only type 'yes' or 'no' " + RESET_TEXT_COLOR);
                }
                if (input2.equalsIgnoreCase("yes")) {
                    loggedIn = true;
                    playing = false;

                    webSocketClient.resign(authToken, realGameID);
                } else {
                    System.out.print(SET_TEXT_COLOR_BLUE + "[PLAYING]" + RESET_TEXT_COLOR + " >>> ");
                }
                break;
            case "move":
                if ((input.length != 3 && input.length != 4) || input[1].length() != 2 || input[2].length() != 2) {
                    System.out.println("\tProper usage is: " + SET_TEXT_COLOR_YELLOW + "move <PIECE> <TARGET SQUARE> [PROMOTION PIECE (q,r,b,n)]"
                            + RESET_TEXT_COLOR);
                } else {
                    if ((Objects.equals(teamColor, "white") && game.getTeamTurn() == ChessGame.TeamColor.WHITE) ||
                            (Objects.equals(teamColor, "black") && game.getTeamTurn() == ChessGame.TeamColor.BLACK)) {
                        ChessPosition parsedPos1 = parsePosition(input[1]);

                        if (parsedPos1 == null) {
                            System.out.println("\tProper usage is: " + SET_TEXT_COLOR_YELLOW + "move <PIECE> <TARGET SQUARE> [PROMOTION PIECE (q,r,b,n)]"
                                    + RESET_TEXT_COLOR);
                            break;
                        }

                        ChessPosition parsedPos2 = parsePosition(input[2]);

                        if (parsedPos2 == null) {
                            System.out.println("\tProper usage is: " + SET_TEXT_COLOR_YELLOW + "move <PIECE> <TARGET SQUARE> [PROMOTION PIECE (q,r,b,n)]"
                                    + RESET_TEXT_COLOR);
                            break;
                        }

                        ChessPiece.PieceType promotionPiece = null;
                        if (input.length == 4) {
                            switch (input[3].toLowerCase()) {
                                case "q":
                                    promotionPiece = ChessPiece.PieceType.QUEEN;
                                    break;
                                case "r":
                                    promotionPiece = ChessPiece.PieceType.ROOK;
                                    break;
                                case "b":
                                    promotionPiece = ChessPiece.PieceType.BISHOP;
                                    break;
                                case "n":
                                    promotionPiece = ChessPiece.PieceType.KNIGHT;
                                    break;
                                default:
                                    System.out.println("\tInvalid promotion piece. Valid options: q, r, b, n.");
                                    break;
                            }
                            if (promotionPiece == null) {
                                break;
                            }
                        }

                        webSocketClient.makeMove(authToken, realGameID, new ChessMove(parsedPos1, parsedPos2, promotionPiece));

                    } else {
                        System.out.println("\tIt is not your turn.");
                        break;
                    }
                }
                break;
            case "highlight":
                if (input.length != 2 || input[1].length() != 2) {
                    System.out.println("\tProper usage is: " + SET_TEXT_COLOR_YELLOW + "highlight <PIECE>"
                            + RESET_TEXT_COLOR);
                } else {
                    ChessPosition parsedPos = parsePosition(input[1]);

                    if (parsedPos == null) {
                        System.out.println("\tProper usage is: " + SET_TEXT_COLOR_YELLOW + "highlight <PIECE>"
                                + RESET_TEXT_COLOR);
                        break;
                    }

                    BoardPrinter.printValidMoves(parsedPos.getRow(), parsedPos.getColumn());
                }
                break;
            default:
                System.out.println("\tNot a valid command. Type '" + SET_TEXT_COLOR_GREEN + "help"
                        + RESET_TEXT_COLOR + "' for a list of commands.");
                System.out.print(SET_TEXT_COLOR_BLUE + "[PLAYING]" + RESET_TEXT_COLOR + " >>> ");
                break;
        }
    }

    private static ChessPosition parsePosition(String coords) {
        char pieceLetter = coords.charAt(0);
        int pieceLetterPos = "abcdefgh".indexOf(pieceLetter) + 1;
        if (pieceLetterPos == 0) {
            return null;
        }

        char pieceNumber = coords.charAt(1);
        if (!Character.isDigit(pieceNumber)) {
            return null;
        }
        int pieceNumberPos = Character.getNumericValue(pieceNumber);

        return new ChessPosition(pieceNumberPos, pieceLetterPos);
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
