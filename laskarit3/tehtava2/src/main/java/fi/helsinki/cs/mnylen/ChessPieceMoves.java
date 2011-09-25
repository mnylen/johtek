package fi.helsinki.cs.mnylen;

import java.util.ArrayList;

import static fi.helsinki.cs.mnylen.Helpers.*;

public abstract class ChessPieceMoves {
    protected final static ChessPieceMoveSet[] WHITE_PAWN_MOVES = new ChessPieceMoveSet[36];
    protected final static byte[] WHITE_PAWN_TOTAL_MOVES = new byte[36];

    protected final static ChessPieceMoveSet[] BLACK_PAWN_MOVES = new ChessPieceMoveSet[36];
    protected final static byte[] BLACK_PAWN_TOTAL_MOVES = new byte[36];

    protected final static ChessPieceMoveSet[] KNIGHT_MOVES = new ChessPieceMoveSet[36];
    protected final static byte[] KNIGHT_TOTAL_MOVES = new byte[36];

    protected final static ChessPieceMoveSet[] KING_MOVES = new ChessPieceMoveSet[36];
    protected final static byte[] KING_TOTAL_MOVES = new byte[36];

    protected final static ChessPieceMoveSet[] ROOK_MOVES = new ChessPieceMoveSet[36];
    protected final static byte[] ROOK_TOTAL_MOVES = new byte[36];

    protected final static ChessPieceMoveSet[] QUEEN_MOVES = new ChessPieceMoveSet[36];
    protected final static byte[] QUEEN_TOTAL_MOVES = new byte[36];

    static {
        /* White pawn moves */
        setPawnMoves(WHITE_PAWN_MOVES, WHITE_PAWN_TOTAL_MOVES, -1);

        /* Black pawn moves */
        setPawnMoves(BLACK_PAWN_MOVES, BLACK_PAWN_TOTAL_MOVES, +1);

        /* Knight moves */
        setKnightMoves(KNIGHT_MOVES, KNIGHT_TOTAL_MOVES);

        /* King moves */
        setKingMoves(KING_MOVES, KING_TOTAL_MOVES);

        /* Rook moves */
        setRookMoves(ROOK_MOVES, ROOK_TOTAL_MOVES);

        /* Queen moves */
        setQueenMoves(QUEEN_MOVES, QUEEN_TOTAL_MOVES);
    }

    private static void setPawnMoves(ChessPieceMoveSet[] moveSets, byte[] totalMoves, int direction) {
        byte yStart = (direction== -1) ? (byte)(Board.ROWS - 1) : (byte)0;
        byte yKill = (direction == -1) ? (byte)-1 : (byte)(Board.ROWS);

        for (byte x = 0; x < Board.COLUMNS; x++) {
            for (byte y = yStart; y != yKill; y += direction) {
                byte index = index(x, y);

                ChessPieceMoveSet moves = new ChessPieceMoveSet();

                if (y > 0 && y < (Board.ROWS-1)) {
                    if (x > 0 && x < Board.COLUMNS) {
                        moves.add(index(index, +1, direction));
                        moves.add(index(index, -1, direction));
                    }

                    moves.add(index(index, +0, direction));
                }

                moveSets[index] = moves;
                totalMoves[index] = (byte)moves.size();
            }
        }
    }

    private static void setKnightMoves(ChessPieceMoveSet[] moveSets, byte[] totalMoves) {
        for (byte x = 0; x < Board.COLUMNS; x++) {
            for (byte y = 0; y < Board.ROWS; y++) {
                byte index = index(x,y);
                ChessPieceMoveSet moves = new ChessPieceMoveSet();
                ArrayList<Byte> possibleMoves = new ArrayList<Byte>();
                possibleMoves.add(index(index, +1, -2));
                possibleMoves.add(index(index, +2, -1));
                possibleMoves.add(index(index, +2, -1));
                possibleMoves.add(index(index, +2, +1));
                possibleMoves.add(index(index, +1, +2));
                possibleMoves.add(index(index, -1, +2));
                possibleMoves.add(index(index, -2, +1));
                possibleMoves.add(index(index, -2, -1));
                possibleMoves.add(index(index, -1, -2));

                int counter = 0;
                for (byte move : possibleMoves) {
                    if (move > 0 && move < moveSets.length) {
                        counter++;
                        moves.add(move);
                    }
                }

                moveSets[index] = moves;
                totalMoves[index] = (byte)counter;
            }
        }
    }

    private static void setKingMoves(ChessPieceMoveSet[] moveSets, byte[] totalMoves) {
        for (byte x = 0; x < Board.COLUMNS; x++) {
            for (byte y = 0; y < Board.ROWS; y++) {
                byte index = index(x,y);

                ChessPieceMoveSet moves = new ChessPieceMoveSet();

                ArrayList<Byte> possibleMoves = new ArrayList<Byte>();
                possibleMoves.add(index(index, +0, -1));
                possibleMoves.add(index(index, +1, -1));
                possibleMoves.add(index(index, +1, -0));
                possibleMoves.add(index(index, +1, +1));
                possibleMoves.add(index(index, +0, +1));
                possibleMoves.add(index(index, -1, +1));
                possibleMoves.add(index(index, -1, +0));
                possibleMoves.add(index(index, -1, -1));

                int counter = 0;
                for (byte move : possibleMoves) {
                    if (move > 0 && move < moveSets.length) {
                        counter++;
                        moves.add(move);
                    }
                }

                moveSets[index] = moves;
                totalMoves[index] = (byte)counter;
            }
        }
    }

    private static void setRookMoves(ChessPieceMoveSet[] moveSets, byte[] totalMoves) {
        for (byte x = 0; x < Board.COLUMNS; x++) {
            for (byte y = 0; y < Board.ROWS; y++) {
                byte index = index(x,y);
                byte counter = 0;
                ChessPieceMoveSet moves = moveSets[index] = new ChessPieceMoveSet();
                counter += setRookHorizontalMoves(moves, index, x, y, +1);
                counter += setRookHorizontalMoves(moves, index, x, y, -1);
                counter += setRookVerticalMoves(moves, index, x, y, +1);
                counter += setRookVerticalMoves(moves, index, x, y, -1);

                totalMoves[index] = counter;
            }
        }
    }

    private static byte setRookHorizontalMoves(ChessPieceMoveSet moves, byte rookIndex, byte rookX, byte rookY, int direction) {
        byte moveXStart = (direction == -1) ? (byte)(rookX - 1) : (byte)(rookX + 1);
        byte moveXKill = (direction == -1) ? -1 : (byte)(Board.COLUMNS);

        byte counter = 0;
        for (byte moveX = moveXStart; moveX != moveXKill; moveX += direction) {
            moves.add(index(moveX, rookY));
            counter++;
        }

        return counter;
    }

    private static byte setRookVerticalMoves(ChessPieceMoveSet moves, byte rookIndex, byte rookX, byte rookY, int direction) {
        byte moveYStart = (direction == -1) ? (byte)(rookY - 1) : (byte)(rookY + 1);
        byte moveYKill = (direction == -1) ? -1 : (byte)(Board.ROWS);

        byte counter = 0;
        for (byte moveY = moveYStart; moveY != moveYKill; moveY += direction) {
            moves.add(index(rookX, moveY));
            counter++;
        }

        return counter;
    }

    private static void setQueenMoves(ChessPieceMoveSet[] moveSets, byte[] totalMoves) {
        setRookMoves(moveSets, totalMoves);

        for (byte x = 0; x < Board.COLUMNS; x++) {
            for (byte y = 0; y < Board.COLUMNS; y++) {
                byte index = index(x,y);
                ChessPieceMoveSet moves = moveSets[index];
                byte counter = totalMoves[index];
                
                counter += setQueenDiagonalMovement(moves, index, x, y, +1, +1);
                counter += setQueenDiagonalMovement(moves, index, x, y, +1, -1);
                counter += setQueenDiagonalMovement(moves, index, x, y, -1, +1);
                counter += setQueenDiagonalMovement(moves, index, x, y, -1, -1);

                totalMoves[index] = counter;
            }
        }
    }

    private static byte setQueenDiagonalMovement(ChessPieceMoveSet moves, byte queenIndex, byte queenX, byte queenY, int directionX, int directionY) {
        byte moveXKill = (directionX == -1) ? -1 : (byte)(Board.COLUMNS);
        byte moveYKill = (directionY == -1) ? -1 : (byte)(Board.ROWS);

        byte counter = 0;
        byte moveX = (byte)(queenX + directionX);
        byte moveY = (byte)(queenY + directionY);

        while (moveX != moveXKill && moveY != moveYKill) {
            moves.add(index(moveX, moveY));
            moveX += directionX;
            moveY += directionY;
            counter++;
        }

        return counter;
    }
}
