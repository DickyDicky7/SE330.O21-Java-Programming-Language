package com.mygdx.game.chess.engine.pieces;

import static com.mygdx.game.chess.engine.board.Move.MajorMove;

import com.google.common.collect.ImmutableList;
import com.mygdx.game.chess.engine.League;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.board.MoveTransition;

public abstract class Piece {

    private final PieceType pieceType;
    private final int piecePosition;
    private final League league;
    private final int hashCode;
    private final boolean isFirstMove;

    public Piece(final PieceType pieceType, final int piecePosition, final League league, final boolean isFirstMove) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.league = league;
        this.isFirstMove = isFirstMove;
        this.hashCode = this.generateHashCode();
    }

    private int generateHashCode() {
        int result = this.pieceType.hashCode();
        result = 31 * result + this.league.hashCode();
        result = 31 * result + this.piecePosition;
        result = 31 * result + (this.isFirstMove ? 1 : 0);
        return result;
    }

    //prior to JDK 7, a manual hashCode is needed
    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(final Object object) {

        if (this == object) {
            return true;
        }

        if (!(object instanceof Piece otherPiece)) {
            return false;
        }

        return this.piecePosition == otherPiece.getPiecePosition() && this.pieceType == otherPiece.getPieceType() &&
                this.league == otherPiece.getLeague() && this.isFirstMove == otherPiece.isFirstMove();
    }

    public final boolean isFirstMove() {
        return this.isFirstMove;
    }

    public abstract ImmutableList<Move> calculateLegalMoves(final Board board);

    protected final boolean isLegalMove(final Board board, final int candidateDestinationCoordinate) {
        try {
            //make a move, if the move is safe, return true, else false
            final MoveTransition moveTransition = board.currentPlayer().makeMove(new MajorMove(board, this, candidateDestinationCoordinate));
            return moveTransition.moveStatus().isFinished();
        } catch (final RuntimeException e) {
            //for catching null board at the beginning of the game
            return true;
        }
    }

    public abstract Piece movedPiece(final Move move);

    public final League getLeague() {
        return this.league;
    }

    public final int getPiecePosition() {
        return this.piecePosition;
    }

    public final PieceType getPieceType() {
        return this.pieceType;
    }

    public final int getPieceValue() {
        return this.pieceType.getPieceValue();
    }
}