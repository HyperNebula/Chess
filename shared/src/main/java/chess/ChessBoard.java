package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] chessBoard;
    private List<ChessPosition> whiteTeamPosList;
    private List<ChessPosition> blackTeamPosList;

    private ChessPosition whiteKingPosition;
    private ChessPosition blackKingPosition;

    public ChessBoard() {

        chessBoard = new ChessPiece[8][8];

        whiteTeamPosList = new ArrayList<>();
        blackTeamPosList = new ArrayList<>();

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {

        chessBoard[position.getRow() - 1][position.getColumn() - 1] = piece;

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            whiteTeamPosList.add(position);
            blackTeamPosList.remove(position);

            if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                whiteKingPosition = position;
            }
        } else {
            blackTeamPosList.add(position);
            whiteTeamPosList.remove(position);

            if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                blackKingPosition = position;
            }
        }

    }

    public void removePiece(ChessPosition position) {

        chessBoard[position.getRow() - 1][position.getColumn() - 1] = null;

        whiteTeamPosList.remove(position);
        blackTeamPosList.remove(position);
    }

    public List<ChessPosition> getTeamPosList(ChessGame.TeamColor team) {
        if (team == ChessGame.TeamColor.WHITE) {
            return whiteTeamPosList;
        } else {
            return blackTeamPosList;
        }
    }

    public ChessPosition getKingPos(ChessGame.TeamColor team) {
        if (team == ChessGame.TeamColor.WHITE) {
            return whiteKingPosition;
        } else {
            return blackKingPosition;
        }
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

        setupRow(1, ChessGame.TeamColor.WHITE);
        setupRow(8, ChessGame.TeamColor.BLACK);

        for (int col = 1; col < 9; col++) {
            addPiece(new ChessPosition(2, col), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, col), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }

    }

    private void setupRow(int row, ChessGame.TeamColor color) {
        addPiece(new ChessPosition(row, 1), new ChessPiece(color, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(row, 2), new ChessPiece(color, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(row, 3), new ChessPiece(color, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(row, 4), new ChessPiece(color, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(row, 5), new ChessPiece(color, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(row, 6), new ChessPiece(color, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(row, 7), new ChessPiece(color, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(row, 8), new ChessPiece(color, ChessPiece.PieceType.ROOK));
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
