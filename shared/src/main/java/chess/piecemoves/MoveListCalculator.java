package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.List;
import java.util.ArrayList;

public class MoveListCalculator {

    public static List<ChessMove> generateMoveListQRB(int[][] updateArray, ChessBoard board, ChessGame.TeamColor color, ChessPosition myPosition) {

        List<ChessMove> moveList = new ArrayList<>();

        for (int[] updatePos: updateArray) {

            ChessPosition positionCheck = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
            while (true) {
                positionCheck.updatePosition(updatePos[0], updatePos[1]);

                if (!positionCheck.positionWithinBoard()) {
                    break;
                }

                if (board.getPiece(positionCheck) == null) {
                    moveList.add(new ChessMove(myPosition, new ChessPosition(positionCheck.getRow(), positionCheck.getColumn()), null));
                } else {
                    if (board.getPiece(positionCheck).getTeamColor() != color) {
                        moveList.add(new ChessMove(myPosition, new ChessPosition(positionCheck.getRow(), positionCheck.getColumn()), null));
                    }
                    break;
                }
            }
        }

        return moveList;

    }

    public static List<ChessMove> generateMoveListKK(int[][] directions, ChessBoard board, ChessGame.TeamColor color, ChessPosition myPosition) {

        List<ChessMove> moveList = new ArrayList<>();

        for (int[] moveDirection : directions) {
            ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + moveDirection[0], myPosition.getColumn() + moveDirection[1]);

            if (nextPosition.positionWithinBoard() && (board.getPiece(nextPosition) == null || board.getPiece(nextPosition).getTeamColor() != color)) {
                moveList.add(new ChessMove(myPosition, nextPosition, null));
            }
        }

        return moveList;
    }

}
