package ai;

import java.util.ArrayList;

import static ai.Helpers.*;

public class Board {
    public static final int ROWS = 6;
    public static final int COLUMNS = 6;

    private final Square[] squares;
    private final byte piecesOnBoard;
    private final byte whitePiecesOnBoard;
    private final byte blackPiecesOnBoard;

    public Board(int[][] boardValues) {
        this.squares = new Square[ROWS*COLUMNS];

        byte piecesOnBoard = 0;
        byte whitePiecesOnBoard = 0;
        byte blackPiecesOnBoard = 0;

        for (byte x = 0; x < COLUMNS; x++) {
            for (byte y = 0; y < ROWS; y++) {
                byte squareValue = (byte)boardValues[x][y];
                byte index = index(x, y);
                Square sq = this.squares[index] = new Square(squareValue);

                if (!(sq.isEmpty())) {
                    piecesOnBoard++;

                    if (sq.getPiece().getColor() == ChessPieceColor.WHITE) {
                        whitePiecesOnBoard++;
                    } else {
                        blackPiecesOnBoard++;
                    }
                }
            }
        }

        this.piecesOnBoard = piecesOnBoard;
        this.whitePiecesOnBoard = whitePiecesOnBoard;
        this.blackPiecesOnBoard = blackPiecesOnBoard;
    }

    public byte getPiecesOnBoard() {
        return piecesOnBoard;
    }

    public byte getWhitePiecesOnBoard() {
        return whitePiecesOnBoard;
    }

    public byte getBlackPiecesOnBoard() {
        return blackPiecesOnBoard;
    }

    public byte size() {
        return (byte)this.squares.length;
    }

    public Square get(int index) {
        return this.squares[index];
    }

    public ArrayList<Byte> validMoves(int index) {
        Square square = this.get(index);
        if (square.isEmpty()) {
            return new ArrayList<Byte>();
        }
        
        ChessPiece pc = square.getPiece();
        ChessPieceColor opponentColor = (pc.getColor() == ChessPieceColor.WHITE) ? ChessPieceColor.BLACK : ChessPieceColor.WHITE;
        ArrayList<Byte> validMoves = new ArrayList<Byte>(24);
        
        switch (pc.getType()) {
            case KNIGHT:
                addValidMoves(ChessPieceMoves.KNIGHT_MOVES[index], pc, opponentColor, validMoves, false);
                break;

            case KING:
                addValidMoves(ChessPieceMoves.KING_MOVES[index], pc, opponentColor, validMoves, false);
                break;

            case PAWN:
                if (pc.getColor() == ChessPieceColor.WHITE) {
                    addValidMoves(ChessPieceMoves.WHITE_PAWN_MOVES[index], pc, opponentColor, validMoves, false);
                } else {
                    addValidMoves(ChessPieceMoves.BLACK_PAWN_MOVES[index], pc, opponentColor, validMoves, false);
                }
                break;

            case ROOK:
                addValidMoves(ChessPieceMoves.ROOK_MOVES_DOWN[index], pc, opponentColor, validMoves, true);
                addValidMoves(ChessPieceMoves.ROOK_MOVES_LEFT[index], pc, opponentColor, validMoves, true);
                addValidMoves(ChessPieceMoves.ROOK_MOVES_RIGHT[index], pc, opponentColor, validMoves, true);
                addValidMoves(ChessPieceMoves.ROOK_MOVES_UP[index], pc, opponentColor, validMoves, true);
                break;

            case QUEEN:
                addValidMoves(ChessPieceMoves.QUEEN_MOVES_DOWN[index], pc, opponentColor, validMoves, true);
                addValidMoves(ChessPieceMoves.QUEEN_MOVES_LEFT[index], pc, opponentColor, validMoves, true);
                addValidMoves(ChessPieceMoves.QUEEN_MOVES_RIGHT[index], pc, opponentColor, validMoves, true);
                addValidMoves(ChessPieceMoves.QUEEN_MOVES_UP[index], pc, opponentColor, validMoves, true);
                addValidMoves(ChessPieceMoves.QUEEN_MOVES_DOWN_LEFT[index], pc, opponentColor, validMoves, true);
                addValidMoves(ChessPieceMoves.QUEEN_MOVES_DOWN_RIGHT[index], pc, opponentColor, validMoves, true);
                addValidMoves(ChessPieceMoves.QUEEN_MOVES_UP_LEFT[index], pc, opponentColor, validMoves, true);
                addValidMoves(ChessPieceMoves.QUEEN_MOVES_UP_RIGHT[index], pc, opponentColor, validMoves, true);
                break;
        }

        return validMoves;
    }

    private void addValidMoves(ChessPieceMoveSet moveSet, ChessPiece pc, ChessPieceColor opponentColor, ArrayList<Byte> to, boolean breakIfNotAdded) {
        for (byte moveTo : moveSet) {
            Square sq = this.get(moveTo);
            if (sq.isEmpty()) {
                to.add(moveTo);
            } else if (sq.getPiece().getColor() == opponentColor) {
                /*
                    pc is attacking the piece in this square
                 */

                ChessPiece attacked = sq.getPiece();
                attacked.setAttackedValue(attacked.getAttackedValue() + pc.getActionValue());

            } else if (sq.getPiece().getColor() != opponentColor) {
                /*
                    pc is protecting the same colored piece in this square
                 */

                ChessPiece defended = sq.getPiece();
                defended.setDefendedValue(defended.getDefendedValue() + pc.getActionValue());
            } else {
                if (breakIfNotAdded) {
                    break;
                }
            }
        }
    }
}
