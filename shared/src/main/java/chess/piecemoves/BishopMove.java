package chess.piecemoves;

import chess.*;

import java.util.List;
import java.util.ArrayList;

public class BishopMove {

    public static List<ChessMove> bishopMoveSet(ChessBoard board, ChessGame.TeamColor color, ChessPosition myPosition) {

        int[][] updateArray = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        return DirectionalMove.generateMoveList(updateArray, board, color, myPosition);

    }

}
