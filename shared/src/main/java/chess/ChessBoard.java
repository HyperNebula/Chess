package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] chessBoard;

    public ChessBoard() {
        chessBoard = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {

        chessBoard[position.getRow() - 1][position.getColumn() - 1] = piece;

    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

        return chessBoard[position.getRow() - 1][position.getColumn() - 1];

    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        chessBoard = new ChessPiece[8][8];

        setupRow(0, ChessGame.TeamColor.WHITE);
        setupRow(7, ChessGame.TeamColor.BLACK);

        for (int col = 0; col < 8; col++) {
            chessBoard[1][col] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            chessBoard[6][col] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }

    }

    private void setupRow(int row, ChessGame.TeamColor color) {
        chessBoard[row][0] = new ChessPiece(color, ChessPiece.PieceType.ROOK);
        chessBoard[row][1] = new ChessPiece(color, ChessPiece.PieceType.KNIGHT);
        chessBoard[row][2] = new ChessPiece(color, ChessPiece.PieceType.BISHOP);
        chessBoard[row][3] = new ChessPiece(color, ChessPiece.PieceType.QUEEN);
        chessBoard[row][4] = new ChessPiece(color, ChessPiece.PieceType.KING);
        chessBoard[row][5] = new ChessPiece(color, ChessPiece.PieceType.BISHOP);
        chessBoard[row][6] = new ChessPiece(color, ChessPiece.PieceType.KNIGHT);
        chessBoard[row][7] = new ChessPiece(color, ChessPiece.PieceType.ROOK);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(chessBoard, that.chessBoard);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(chessBoard);
    }
}
