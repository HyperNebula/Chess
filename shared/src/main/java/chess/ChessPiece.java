package chess;

import java.util.Collection;
import java.util.Objects;

import chess.piecemoves.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {

        this.pieceColor = pieceColor;
        pieceType = type;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && getPieceType() == that.getPieceType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, getPieceType());
    }

    @Override
    public String toString() {
        return "" + pieceColor + pieceType;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {

        return pieceColor;

    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return pieceType;

    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (pieceType == PieceType.BISHOP) {
            return BishopMove.bishopMoveSet(board, pieceColor, myPosition);
        }
        if (pieceType == PieceType.QUEEN) {
        return QueenMove.queenMoveSet(board, pieceColor, myPosition);
        }
        if (pieceType == PieceType.ROOK) {
            return RookMove.rookMoveSet(board, pieceColor, myPosition);
        }
        if (pieceType == PieceType.PAWN) {
            return PawnMove.pawnMoveSet(board, pieceColor, myPosition);
        }
        if (pieceType == PieceType.KING) {
            return KingMove.kingMoveSet(board, pieceColor, myPosition);
        }
        return java.util.List.of();
    }
}
