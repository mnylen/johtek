package fi.helsinki.cs.mnylen;

import java.util.ArrayList;

import static fi.helsinki.cs.mnylen.Helpers.*;

public abstract class ChessPieceMoves {
    protected final static ChessPieceMoveSet[] WHITE_PAWN_MOVES = new ChessPieceMoveSet[36];
    protected final static ChessPieceMoveSet[] BLACK_PAWN_MOVES = new ChessPieceMoveSet[36];
    protected final static ChessPieceMoveSet[] KNIGHT_MOVES = new ChessPieceMoveSet[36];
    protected final static ChessPieceMoveSet[] KING_MOVES = new ChessPieceMoveSet[36];
    protected final static ChessPieceMoveSet[] ROOK_MOVES_RIGHT = new ChessPieceMoveSet[36];
    protected final static ChessPieceMoveSet[] ROOK_MOVES_LEFT = new ChessPieceMoveSet[36];
    protected final static ChessPieceMoveSet[] ROOK_MOVES_UP = new ChessPieceMoveSet[36];
    protected final static ChessPieceMoveSet[] ROOK_MOVES_DOWN = new ChessPieceMoveSet[36];
    protected final static ChessPieceMoveSet[] QUEEN_MOVES = new ChessPieceMoveSet[36];
    protected final static ChessPieceMoveSet[] QUEEN_MOVES_RIGHT = ROOK_MOVES_RIGHT;
    protected final static ChessPieceMoveSet[] QUEEN_MOVES_LEFT = ROOK_MOVES_LEFT;
    protected final static ChessPieceMoveSet[] QUEEN_MOVES_UP = ROOK_MOVES_UP;
    protected final static ChessPieceMoveSet[] QUEEN_MOVES_DOWN = ROOK_MOVES_DOWN;
    protected final static ChessPieceMoveSet[] QUEEN_MOVES_UP_RIGHT = new ChessPieceMoveSet[36];
    protected final static ChessPieceMoveSet[] QUEEN_MOVES_DOWN_RIGHT = new ChessPieceMoveSet[36];
    protected final static ChessPieceMoveSet[] QUEEN_MOVES_UP_LEFT = new ChessPieceMoveSet[36];
    protected final static ChessPieceMoveSet[] QUEEN_MOVES_DOWN_LEFT = new ChessPieceMoveSet[36];

    static {
        /* White pawn moves */
        setPawnMoves(WHITE_PAWN_MOVES, -1);

        /* Black pawn moves */
        setPawnMoves(BLACK_PAWN_MOVES, +1);

        /* Knight moves */
        setKnightMoves(KNIGHT_MOVES);

        /* King moves */
        setKingMoves(KING_MOVES);

        /* Rook moves */
        setRookMoves();

        /* Queen moves */
        setQueenMoves();
    }

    private static void setPawnMoves(ChessPieceMoveSet[] moveSets, int direction) {
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
            }
        }
    }

    private static void setKnightMoves(ChessPieceMoveSet[] moveSets) {
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

                for (byte move : possibleMoves) {
                    if (move > 0 && move < moveSets.length) {
                        moves.add(move);
                    }
                }

                moveSets[index] = moves;
            }
        }
    }

    private static void setKingMoves(ChessPieceMoveSet[] moveSets) {
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

                for (byte move : possibleMoves) {
                    if (move > 0 && move < moveSets.length) {
                        moves.add(move);
                    }
                }

                moveSets[index] = moves;
            }
        }
    }

    private static void setRookMoves() {
        for (byte x = 0; x < Board.COLUMNS; x++) {
            for (byte y = 0; y < Board.ROWS; y++) {
                byte index = index(x,y);
                ChessPieceMoveSet rightMoves = ROOK_MOVES_RIGHT[index] = new ChessPieceMoveSet();
                setRookHorizontalMoves(rightMoves, index, x, y, +1);

                ChessPieceMoveSet leftMoves = ROOK_MOVES_LEFT[index] = new ChessPieceMoveSet();
                setRookHorizontalMoves(leftMoves, index, x, y, -1);

                ChessPieceMoveSet upMoves = ROOK_MOVES_UP[index] = new ChessPieceMoveSet();
                setRookVerticalMoves(upMoves, index, x, y, -1);

                ChessPieceMoveSet downMoves = ROOK_MOVES_DOWN[index] = new ChessPieceMoveSet();
                setRookVerticalMoves(downMoves, index, x, y, +1);
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

    private static void setQueenMoves() {
        for (byte x = 0; x < Board.COLUMNS; x++) {
            for (byte y = 0; y < Board.COLUMNS; y++) {
                byte index = index(x,y);
                setQueenDiagonalMovement(index, x, y, +1, +1);
                setQueenDiagonalMovement(index, x, y, +1, -1);
                setQueenDiagonalMovement(index, x, y, -1, +1);
                setQueenDiagonalMovement(index, x, y, -1, -1);
            }
        }
    }

    private static void setQueenDiagonalMovement(byte queenIndex, byte queenX, byte queenY, int directionX, int directionY) {
        byte moveXKill = (directionX == -1) ? -1 : (byte)(Board.COLUMNS);
        byte moveYKill = (directionY == -1) ? -1 : (byte)(Board.ROWS);

        ChessPieceMoveSet moves = null;
        if (directionX == 1 && directionY == 1) {
            moves = QUEEN_MOVES_DOWN_RIGHT[queenIndex] = new ChessPieceMoveSet();
        } else if (directionX == 1 && directionY == -1) {
            moves = QUEEN_MOVES_UP_RIGHT[queenIndex] = new ChessPieceMoveSet();
        } else if (directionX == -1 && directionY == 1) {
            moves = QUEEN_MOVES_DOWN_LEFT[queenIndex] = new ChessPieceMoveSet();
        } else if (directionX == -1 && directionY == -1) {
            moves = QUEEN_MOVES_UP_LEFT[queenIndex] = new ChessPieceMoveSet();
        }

        byte moveX = (byte)(queenX + directionX);
        byte moveY = (byte)(queenY + directionY);

        while (moveX != moveXKill && moveY != moveYKill) {
            moves.add(index(moveX, moveY));
            moveX += directionX;
            moveY += directionY;
        }
    }
}
