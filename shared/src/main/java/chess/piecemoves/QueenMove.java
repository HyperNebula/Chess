package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.List;

public class QueenMove {

    public static List<ChessMove> queenMoveSet(ChessBoard board, ChessGame.TeamColor color, ChessPosition myPosition) {

        int[][] updateArray = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {0, 1}, {1, 0}, {-1, 0}, {0, -1}};

        return MoveListCalculator.generateMoveListQRB(updateArray, board, color, myPosition);

    }

}
