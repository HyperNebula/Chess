package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.List;

public class KnightMove {

    public static List<ChessMove> knightMoveSet(ChessBoard board, ChessGame.TeamColor color, ChessPosition myPosition) {

        int[][] directions = {{2,1}, {2,-1}, {-2,1}, {-2,-1}, {1,2}, {-1,2}, {1,-2}, {-1,-2}};

        return MoveListCalculator.generateMoveListKK(directions, board, color, myPosition);

    }

}
