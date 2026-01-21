package chess.piecemoves;

import chess.*;

import java.util.List;
import java.util.ArrayList;

public class PawnMove {

    public static List<ChessMove> pawnMoveSet(ChessBoard board, ChessGame.TeamColor color, ChessPosition myPosition, ChessPiece.PieceType promotionPiece)  {

        List<ChessMove> moveList = new ArrayList<>();
        int rowUpdate;

        if (color == ChessGame.TeamColor.WHITE) {
            rowUpdate = 1;
        } else {
            rowUpdate = -1;
        }

        ChessPosition positionCheck = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + rowUpdate);
        if (board.getPiece(positionCheck) == null) {
            moveList.add(new ChessMove(myPosition, new ChessPosition(positionCheck.getRow(), positionCheck.getColumn()), promotionPiece));

            positionCheck.updatePosition(0, rowUpdate);
            if (myPosition.getRow() == 2 || board.getPiece(positionCheck) == null) {
                moveList.add(new ChessMove(myPosition, new ChessPosition(positionCheck.getRow(), positionCheck.getColumn()), promotionPiece));
            }
            positionCheck.updatePosition(0, -rowUpdate);

        }
        if (myPosition.getRow() < 8) {
            positionCheck.updatePosition(1, 0);
            if (board.getPiece(positionCheck) != null && board.getPiece(positionCheck).getTeamColor() != color) {
                moveList.add(new ChessMove(myPosition, new ChessPosition(positionCheck.getRow(), positionCheck.getColumn()), promotionPiece));
            }
            positionCheck.updatePosition(-1, 0);
        }
        if (myPosition.getRow() > 0) {
            positionCheck.updatePosition(-1, 0);
            if (board.getPiece(positionCheck) != null && board.getPiece(positionCheck).getTeamColor() != color) {
                moveList.add(new ChessMove(myPosition, new ChessPosition(positionCheck.getRow(), positionCheck.getColumn()), promotionPiece));
            }
            positionCheck.updatePosition(1, 0);
        }

        return moveList;
    }

}
