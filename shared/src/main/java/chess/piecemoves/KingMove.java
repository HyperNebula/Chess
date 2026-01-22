package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.List;

public class KingMove {

    public static List<ChessMove> kingMoveSet(ChessBoard board, ChessGame.TeamColor color, ChessPosition myPosition) {

        List<ChessMove> moveList = new ArrayList<>();

        int[][] directions = {{1,0}, {1,1}, {0,1}, {-1,1}, {-1,0}, {-1,-1}, {0,-1}, {1,-1}};

        for (int[] moveDirection : directions) {
            ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + moveDirection[0], myPosition.getColumn() + moveDirection[1]);

            if (nextPosition.positionWithinBoard() && (board.getPiece(nextPosition) == null || board.getPiece(nextPosition).getTeamColor() != color)) {
                moveList.add(new ChessMove(myPosition, nextPosition, null));
            }
        }

        return moveList;

    }

}
