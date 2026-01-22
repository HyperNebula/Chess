package chess.piecemoves;

import chess.*;

import java.util.List;

public class BishopMove {

    public static List<ChessMove> bishopMoveSet(ChessBoard board, ChessGame.TeamColor color, ChessPosition myPosition) {

        int[][] updateArray = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        return MoveListCalculator.generateMoveListQRB(updateArray, board, color, myPosition);

    }

}
