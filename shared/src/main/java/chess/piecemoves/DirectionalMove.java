package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.List;
import java.util.ArrayList;

public class DirectionalMove {

    public static List<ChessMove> generateMoveList(int[][] updateArray, ChessBoard board, ChessGame.TeamColor color, ChessPosition myPosition) {

        List<ChessMove> moveList = new ArrayList<>();

        for (int[] updatePos: updateArray) {

            ChessPosition positionCheck = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
            while (true) {
                positionCheck.updatePosition(updatePos[0], updatePos[1]);

                if (positionCheck.getRow() > 8 || positionCheck.getColumn() > 8 || positionCheck.getRow() < 1 || positionCheck.getColumn() < 1) {
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

}
