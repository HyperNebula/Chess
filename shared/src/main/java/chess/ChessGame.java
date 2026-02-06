package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor currentTeam;
    private ChessBoard gameBoard;

    public ChessGame() {

        currentTeam = TeamColor.WHITE;

        gameBoard = new ChessBoard();
        gameBoard.resetBoard();

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {

        currentTeam = team;

    }

    public void swapTeamTurn() {
        if (currentTeam == TeamColor.BLACK) {
            currentTeam = TeamColor.WHITE;
        } else {
            currentTeam = TeamColor.BLACK;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    private void moveAndRemovePiece(ChessMove move, ChessBoard board) {
        if (move.getPromotionPiece() == null) {
            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        } else {
            board.addPiece(move.getEndPosition(), new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece()));
        }
        board.removePiece(move.getStartPosition());
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (gameBoard.getPiece(startPosition) == null) {
            return null;
        }

        return gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition);

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (validMoves(move.getStartPosition()) == null) {
            throw new InvalidMoveException("Invalid move (no piece there): " + move);
        }
        if (gameBoard.getPiece(move.getStartPosition()).getTeamColor() != currentTeam) {
            throw new InvalidMoveException("Invalid move (not your turn): " + move);
        }
        if (validMoves(move.getStartPosition()).contains(move)) {
            moveAndRemovePiece(move, gameBoard);

            swapTeamTurn();
        } else {
            throw new InvalidMoveException("Invalid move: " + move);
        }
    }

    private Set<ChessPosition> attackPositionsSetCalculator(TeamColor currentTeam, ChessBoard board) {
        Set<ChessMove> tempAttackMoveSet = new HashSet<>();
        Set<ChessPosition> attackSet = new HashSet<>();

        List<ChessPosition> enemyPiecePositions;
        if (currentTeam == TeamColor.WHITE) {
            enemyPiecePositions = board.getTeamPosList(TeamColor.BLACK);
        } else {
            enemyPiecePositions = board.getTeamPosList(TeamColor.WHITE);
        }

        for (ChessPosition enemyPosition : enemyPiecePositions) {
            ChessPiece piece = board.getPiece(enemyPosition);

            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {

                ChessPosition attackLeft;
                ChessPosition attackRight;
                if (currentTeam == TeamColor.WHITE) {
                    attackLeft = new ChessPosition(enemyPosition.getRow() - 1, enemyPosition.getColumn() + 1);
                    attackRight = new ChessPosition(enemyPosition.getRow() - 1, enemyPosition.getColumn() - 1);
                } else {
                    attackLeft = new ChessPosition(enemyPosition.getRow() + 1, enemyPosition.getColumn() + 1);
                    attackRight = new ChessPosition(enemyPosition.getRow() + 1, enemyPosition.getColumn() - 1);
                }
                if (attackLeft.positionWithinBoard()) {
                    attackSet.add(attackLeft);
                }
                if (attackRight.positionWithinBoard()) {
                    attackSet.add(attackRight);
                }

            } else {

                tempAttackMoveSet.addAll(piece.pieceMoves(board, enemyPosition));

            }

        }

        for (ChessMove move : tempAttackMoveSet) {
            attackSet.add(move.getEndPosition());
        }

        return attackSet;

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        return attackPositionsSetCalculator(teamColor, gameBoard).contains(gameBoard.getKingPos(teamColor));

    }

    private boolean mateChecker(TeamColor teamColor) {
        for (ChessPosition piecePosition : gameBoard.getTeamPosList(teamColor)) {
            for (ChessMove possibleMove : gameBoard.getPiece(piecePosition).pieceMoves(gameBoard, piecePosition)) {

                ChessBoard gameBoardCopy = new ChessBoard(gameBoard);

                moveAndRemovePiece(possibleMove, gameBoardCopy);

                if (!(attackPositionsSetCalculator(teamColor, gameBoardCopy).contains(gameBoardCopy.getKingPos(teamColor)))) {
                    return false;
                }

            }
        }
        return true;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return mateChecker(teamColor);
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {

            return mateChecker(teamColor);
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {

        return gameBoard;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return currentTeam == chessGame.currentTeam && Objects.equals(gameBoard, chessGame.gameBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentTeam, gameBoard);
    }

}
