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
            promotionRow = 1;
        }

        ChessPosition positionCheck = new ChessPosition(myPosition.getRow() + rowUpdate, myPosition.getColumn());
        if (board.getPiece(positionCheck) == null) {
            addMoves(moveList, myPosition, positionCheck.copy(), promotionRow);

            positionCheck.updatePosition(rowUpdate, 0);
            if (myPosition.getRow() == startRow && board.getPiece(positionCheck) == null) {
                addMoves(moveList, myPosition, positionCheck.copy(), promotionRow);
            }
            positionCheck.updatePosition(-rowUpdate, 0);

        }
        positionCheck.updatePosition(0, 1);
        if (positionCheck.positionWithinBoard()) {
            if (board.getPiece(positionCheck) != null && board.getPiece(positionCheck).getTeamColor() != color) {
                addMoves(moveList, myPosition, positionCheck.copy(), promotionRow);
            }
        }

        positionCheck.updatePosition(0, -2);

        if (positionCheck.positionWithinBoard()) {
            if (board.getPiece(positionCheck) != null && board.getPiece(positionCheck).getTeamColor() != color) {
                addMoves(moveList, myPosition, positionCheck.copy(), promotionRow);
            }
        }
        positionCheck.updatePosition(0, 1);

        return moveList;
    }

    private static void addMoves(List<ChessMove> moveList, ChessPosition start, ChessPosition end, int promotionRow) {
        if (end.getRow() == promotionRow) {
            moveList.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
            moveList.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
            moveList.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
            moveList.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
        } else {
            moveList.add(new ChessMove(start, end, null));
        }
    }

}
