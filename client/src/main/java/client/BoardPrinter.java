package client;

import chess.*;

import java.util.Objects;

import static ui.EscapeSequences.*;

public class BoardPrinter {

    private static String choosePieceType(ChessPiece.PieceType pieceType) {
        return switch (pieceType) {
            case KING -> KING;
            case QUEEN -> QUEEN;
            case BISHOP -> BISHOP;
            case ROOK -> ROOK;
            case KNIGHT -> KNIGHT;
            case PAWN -> PAWN;
            default -> EMPTYC;
        };

    }

    private static String choosePieceColor(ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            return SET_TEXT_BOLD + SET_TEXT_COLOR_BLUE;
        } else {
            return SET_TEXT_BOLD + SET_TEXT_COLOR_RED;
        }
    }

    private static String chooseSquareColor(int indexX, int indexY) {
        if ((indexX + indexY) % 2 == 0) {
            return SET_BG_COLOR_WHITE;
        } else {
            return SET_BG_COLOR_BLACK;
        }
    }

    private static void printEndRow() {
        if (Objects.equals(ClientMain.teamColor, "white")) {
            System.out.println(SET_BG_COLOR_DARK_GREEN + "    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR);
        } else {
            System.out.println(SET_BG_COLOR_DARK_GREEN + "    h  g  f  e  d  c  b  a    " + RESET_BG_COLOR);
        }

    }

    public static void printBoard() {
        printEndRow();
        for (int row = 0; row < 8; row++) {
            int actualRow = row;
            if (Objects.equals(ClientMain.teamColor, "white")) {
                actualRow = 7 - row;
            }
            System.out.print(SET_BG_COLOR_DARK_GREEN + " " + (actualRow + 1) + " ");

            for (int col = 0; col < 8; col++) {
                int actualCol = col;
                if (Objects.equals(ClientMain.teamColor, "black")) {
                    actualCol = 7 - col;
                }

                ChessPiece tempPiece = ClientMain.game.getBoard().getPiece(new ChessPosition(actualRow + 1, actualCol + 1));
                if (tempPiece != null) {
                    System.out.print(chooseSquareColor(row, col)
                            + choosePieceColor(tempPiece.getTeamColor())
                            + choosePieceType(tempPiece.getPieceType()));
                } else {
                    System.out.print(chooseSquareColor(row, col) + EMPTYC);
                }
            }

            System.out.println(SET_BG_COLOR_DARK_GREEN + RESET_TEXT_BOLD_FAINT + RESET_TEXT_COLOR + " " + (actualRow + 1) + " " + RESET_BG_COLOR);
        }
        printEndRow();
    }

}
