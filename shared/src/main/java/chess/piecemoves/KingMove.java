package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.List;

public class KingMove {

    public static List<ChessMove> kingMoveSet(ChessBoard board, ChessGame.TeamColor color, ChessPosition myPosition) {

        int[][] directions = {{1,0}, {1,1}, {0,1}, {-1,1}, {-1,0}, {-1,-1}, {0,-1}, {1,-1}};

        return MoveListCalculator.generateMoveListKK(directions, board, color, myPosition);

    }

}
