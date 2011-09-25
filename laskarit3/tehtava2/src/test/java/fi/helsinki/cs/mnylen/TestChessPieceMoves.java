package fi.helsinki.cs.mnylen;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static fi.helsinki.cs.mnylen.Helpers.*;

public class TestChessPieceMoves {

    @Test
    public void testWhitePawnMoves() {
        for (byte x = 0; x < Board.COLUMNS; x++) {
            for (byte y = Board.ROWS - 1; y >= 0; y--) {
                byte index = index(x,y);

                ChessPieceMoveSet moves = ChessPieceMoves.WHITE_PAWN_MOVES[index];

                if (y == 0 || y == Board.ROWS - 1) {
                    assertThat(moves.size(), is(0));
                } else if (x > 0 && x < Board.COLUMNS) {
                    assertThat(moves.size(), is(3));
                    assertThat(moves.contains(index(index, +0, -1)), is(true)); // at top of pawn
                    assertThat(moves.contains(index(index, +1, -1)), is(true)); // at top right of pawn
                    assertThat(moves.contains(index(index, -1, -1)), is(true)); // at top left of pawn
                } else {
                    assertThat(moves.size(), is(1));
                    assertThat(moves.contains(index(index, +0, -1)), is(true));
                }
            }
        }
    }

     @Test
    public void testBlackPawnMoves() {
        for (byte x = 0; x < Board.COLUMNS; x++) {
            for (byte y = Board.ROWS - 1; y >= 0; y--) {
                byte index = index(x,y);

                ChessPieceMoveSet moves = ChessPieceMoves.BLACK_PAWN_MOVES[index];

                if (y == 0 || y == Board.ROWS - 1) {
                    assertThat(moves.size(), is(0));
                } else if (x > 0 && x < Board.COLUMNS) {
                    assertThat(moves.size(), is(3));
                    assertThat(moves.contains(index(index, +0, +1)), is(true));
                    assertThat(moves.contains(index(index, +1, +1)), is(true));
                    assertThat(moves.contains(index(index, -1, +1)), is(true));
                } else {
                    assertThat(moves.size(), is(1));
                    assertThat(moves.contains(index(index, +0, +1)), is(true));
                }
            }
        }
    }

    @Test
    public void testKnightMoves() {
        for (byte x = 0; x < Board.COLUMNS; x++) {
            for (byte y = 0; y < Board.ROWS; y++) {
                byte index = index(x,y);
                ChessPieceMoveSet moves = ChessPieceMoves.KNIGHT_MOVES[index];
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
                    if (move > 0 && move < ChessPieceMoves.KNIGHT_MOVES.length) {
                        counter++;
                        assertThat(moves.contains(move), is(true));
                    }
                }

                assertThat(moves.size(), is(counter));
            }
        }
    }

    @Test
    public void testKingMoves() {
        for (byte x = 0; x < Board.COLUMNS; x++) {
            for (byte y = 0; y < Board.ROWS; y++) {
                byte index = index(x,y);

                ChessPieceMoveSet moves = ChessPieceMoves.KING_MOVES[index];

                ArrayList<Byte> possibleMoves = new ArrayList<Byte>();
                possibleMoves.add(index(index, +0, -1));
                possibleMoves.add(index(index, +1, -1));
                possibleMoves.add(index(index, +1, -0));
                possibleMoves.add(index(index, +1, +1));
                possibleMoves.add(index(index, +0, +1));
                possibleMoves.add(index(index, -1, +1));
                possibleMoves.add(index(index, -1, +0));
                possibleMoves.add(index(index, -1, -1));

                byte counter = 0;
                for (byte move : possibleMoves) {
                    if (move > 0 && move < ChessPieceMoves.KING_MOVES.length) {
                        counter++;
                        assertThat(moves.contains(move), is(true));
                    }
                }

                assertThat((byte)moves.size(), is(counter));
                assertThat(ChessPieceMoves.KING_TOTAL_MOVES[index], is(counter));
            }
        }
    }

    @Test
    public void testRookMoves() {
        byte index = index((byte)1, (byte)2);
        ChessPieceMoveSet moves = ChessPieceMoves.ROOK_MOVES[index];

        assertThat((byte)moves.size(), is((byte)10));
        assertThat(ChessPieceMoves.ROOK_TOTAL_MOVES[index], is((byte)10));

        // can move two up
        assertThat(moves.contains(index(index, +0, -1)), is(true));
        assertThat(moves.contains(index(index, +0, -2)), is(true));

        // can move four right
        assertThat(moves.contains(index(index, +1, +0)), is(true));
        assertThat(moves.contains(index(index, +2, +0)), is(true));
        assertThat(moves.contains(index(index, +3, +0)), is(true));
        assertThat(moves.contains(index(index, +4, +0)), is(true));

        // can move one left
        assertThat(moves.contains(index(index, -1, +0)), is(true));

        // can move three down
        assertThat(moves.contains(index(index, +0, +1)), is(true));
        assertThat(moves.contains(index(index, +0, +2)), is(true));
        assertThat(moves.contains(index(index, +0, +3)), is(true));
    }

    @Test
    public void testQueenMoves() {
        byte index = index((byte)1, (byte)2);
        ChessPieceMoveSet moves = ChessPieceMoves.QUEEN_MOVES[index];

        // can move two up
        assertThat(moves.contains(index(index, +0, -1)), is(true));
        assertThat(moves.contains(index(index, +0, -2)), is(true));

        // can move four right
        assertThat(moves.contains(index(index, +1, +0)), is(true));
        assertThat(moves.contains(index(index, +2, +0)), is(true));
        assertThat(moves.contains(index(index, +3, +0)), is(true));
        assertThat(moves.contains(index(index, +4, +0)), is(true));

        // can move one left
        assertThat(moves.contains(index(index, -1, +0)), is(true));

        // can move three down
        assertThat(moves.contains(index(index, +0, +1)), is(true));
        assertThat(moves.contains(index(index, +0, +2)), is(true));
        assertThat(moves.contains(index(index, +0, +3)), is(true));

        // can move towards top left
        assertThat(moves.contains(index(index, -1, -1)), is(true));

        // can move two toward top right
        assertThat(moves.contains(index(index, +1, -1)), is(true));
        assertThat(moves.contains(index(index, +2, -2)), is(true));

        // can move one toward bottom left
        assertThat(moves.contains(index(index, -1, +1)), is(true));

        // can move three toward bottom right
        assertThat(moves.contains(index(index, +1, +1)), is(true));
        assertThat(moves.contains(index(index, +2, +2)), is(true));
        assertThat(moves.contains(index(index, +3, +3)), is(true));
        
        assertThat((byte)moves.size(), is((byte)17));
        assertThat(ChessPieceMoves.QUEEN_TOTAL_MOVES[index], is((byte)17));

    }
}
