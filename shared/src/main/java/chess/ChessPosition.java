package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int row;
    private int col;

    public ChessPosition(int row, int col) {

        this.row = row;
        this.col = col;

    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {

        return row;

    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {

        return col;

    }

    public void updatePosition(int rowi, int coli) {
        this.row += rowi;
        this.col += coli;
    }

    public boolean positionWithinBoard() {
        return (0 < row) && (row < 9) && (0 < col) && (col < 9);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return getRow() == that.getRow() && col == that.col;
    }

    public ChessPosition copy() {
        return new ChessPosition(row, col);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), col);
    }

    @Override
    public String toString() {
        return "[" +
                "row=" + row +
                ", col=" + col +
                ']';
    }
}
