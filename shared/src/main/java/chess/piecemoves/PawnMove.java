package chess.piecemoves;

import chess.*;

import java.util.List;
import java.util.ArrayList;

public class PawnMove {

    public static List<ChessMove> pawnMoveSet(ChessBoard board, ChessGame.TeamColor color, ChessPosition myPosition)  {

        List<ChessMove> moveList = new ArrayList<>();
        ChessPiece.PieceType[] promotionPieces = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.BISHOP};

        int rowUpdate;
        int startRow;
        int promotionRow;

        if (color == ChessGame.TeamColor.WHITE) {
            rowUpdate = 1;
            startRow = 2;
            promotionRow = 8;
        } else {
            rowUpdate = -1;
            startRow = 7;
        }

        ChessPosition positionCheck = new ChessPosition(myPosition.getRow() + rowUpdate, myPosition.getColumn());
        if (board.getPiece(positionCheck) == null) {
            if
            moveList.add(new ChessMove(myPosition, new ChessPosition(positionCheck.getRow(), positionCheck.getColumn()), promotionPiece));

            positionCheck.updatePosition(rowUpdate, 0);
            if (myPosition.getRow() == startRow && board.getPiece(positionCheck) == null) {
                moveList.add(new ChessMove(myPosition, new ChessPosition(positionCheck.getRow(), positionCheck.getColumn()), promotionPiece));
            }
            positionCheck.updatePosition(-rowUpdate, 0);

        }
        positionCheck.updatePosition(0, 1);
        if (positionCheck.positionWithinBoard()) {
            if (board.getPiece(positionCheck) != null && board.getPiece(positionCheck).getTeamColor() != color) {
                moveList.add(new ChessMove(myPosition, new ChessPosition(positionCheck.getRow(), positionCheck.getColumn()), promotionPiece));
            }
        }

        positionCheck.updatePosition(0, -2);

        if (positionCheck.positionWithinBoard()) {
            if (board.getPiece(positionCheck) != null && board.getPiece(positionCheck).getTeamColor() != color) {
                moveList.add(new ChessMove(myPosition, new ChessPosition(positionCheck.getRow(), positionCheck.getColumn()), promotionPiece));
            }
        }
        positionCheck.updatePosition(0, 1);

        return moveList;
    }

}
