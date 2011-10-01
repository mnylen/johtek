package ai;

import ai.Board;
import ai.ChessPieceMoveSet;
import ai.ChessPieceMoves;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static ai.Helpers.*;

public class TestChessPieceMoves {

    @Test
    public void testWhitePawnMoves() {
        // From topmost row can't move anywhere
        byte index = index(3, 0);
        ChessPieceMoveSet moves = ChessPieceMoves.WHITE_PAWN_MOVES[index];
        assertThat(moves.size(), is(0));

        // From downmost row can't move anywhere
        index = index(3, 5);
        moves = ChessPieceMoves.WHITE_PAWN_MOVES[index];
        assertThat(moves.size(), is(0));

        // From left corner can move to only two directions
        index = index(0, 4);
        moves = ChessPieceMoves.WHITE_PAWN_MOVES[index];
        assertThat(moves.size(), is(2));
        assertThat(moves.contains(index(index, +1, -1)), is(true));
        assertThat(moves.contains(index(index, +0, -1)), is(true));

        // From center can move to three direction
        index = index(3, 4);
        moves = ChessPieceMoves.WHITE_PAWN_MOVES[index];
        assertThat(moves.size(), is(3));
        assertThat(moves.contains(index(index, +1, -1)), is(true));
        assertThat(moves.contains(index(index, +0, -1)), is(true));
        assertThat(moves.contains(index(index, -1, -1)), is(true));
        
        // From the right corner can move to only two directions
        index = index(5, 4);
        moves = ChessPieceMoves.WHITE_PAWN_MOVES[index];
        assertThat(moves.size(), is(2));
        assertThat(moves.contains(index(index, -1, -1)), is(true));
        assertThat(moves.contains(index(index, +0, -1)), is(true));
    }

     @Test
    public void testBlackPawnMoves() {
        // From topmost row can't move anywhere
        byte index = index(3, 0);
        ChessPieceMoveSet moves = ChessPieceMoves.BLACK_PAWN_MOVES[index];
        assertThat(moves.size(), is(0));

        // From downmost row can't move anywhere
        index = index(3, 5);
        moves = ChessPieceMoves.BLACK_PAWN_MOVES[index];
        assertThat(moves.size(), is(0));

        // From left corner can move to only two directions
        index = index(0, 4);
        moves = ChessPieceMoves.BLACK_PAWN_MOVES[index];
        assertThat(moves.size(), is(2));
        assertThat(moves.contains(index(index, +1, +1)), is(true));
        assertThat(moves.contains(index(index, +0, +1)), is(true));

        // From center can move to three direction
        index = index(3, 4);
        moves = ChessPieceMoves.BLACK_PAWN_MOVES[index];
        assertThat(moves.size(), is(3));
        assertThat(moves.contains(index(index, +1, +1)), is(true));
        assertThat(moves.contains(index(index, +0, +1)), is(true));
        assertThat(moves.contains(index(index, -1, +1)), is(true));

        // From the right corner can move to only two directions
        index = index(5, 4);
        moves = ChessPieceMoves.BLACK_PAWN_MOVES[index];
        assertThat(moves.size(), is(2));
        assertThat(moves.contains(index(index, -1, +1)), is(true));
        assertThat(moves.contains(index(index, +0, +1)), is(true));
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
            }
        }
    }

    @Test
    public void testRookMoves() {
        byte index = index((byte)1, (byte)2);

        // can move two up
        ChessPieceMoveSet moves = ChessPieceMoves.ROOK_MOVES_UP[index];

        assertThat((byte)moves.size(), is((byte)2));
        assertThat(moves.contains(index(index, +0, -1)), is(true));
        assertThat(moves.contains(index(index, +0, -2)), is(true));

        // can move four right
        moves = ChessPieceMoves.ROOK_MOVES_RIGHT[index];
        assertThat((byte)moves.size(), is((byte)4));
        assertThat(moves.contains(index(index, +1, +0)), is(true));
        assertThat(moves.contains(index(index, +2, +0)), is(true));
        assertThat(moves.contains(index(index, +3, +0)), is(true));
        assertThat(moves.contains(index(index, +4, +0)), is(true));

        // can move one left
        moves = ChessPieceMoves.ROOK_MOVES_LEFT[index];
        assertThat((byte)moves.size(), is((byte)1));
        assertThat(moves.contains(index(index, -1, +0)), is(true));

        // can move three down
        moves = ChessPieceMoves.ROOK_MOVES_DOWN[index];
        assertThat((byte)moves.size(), is((byte)3));
        assertThat(moves.contains(index(index, +0, +1)), is(true));
        assertThat(moves.contains(index(index, +0, +2)), is(true));
        assertThat(moves.contains(index(index, +0, +3)), is(true));
    }

    @Test
    public void testQueenMoves() {
        byte index = index((byte)1, (byte)2);

        // can move two up
        ChessPieceMoveSet moves = ChessPieceMoves.QUEEN_MOVES_UP[index];
        assertThat((byte)moves.size(), is((byte)2));
        assertThat(moves.contains(index(index, +0, -1)), is(true));
        assertThat(moves.contains(index(index, +0, -2)), is(true));

        // can move four right
        moves = ChessPieceMoves.QUEEN_MOVES_RIGHT[index];
        assertThat((byte)moves.size(), is((byte)4));
        assertThat(moves.contains(index(index, +1, +0)), is(true));
        assertThat(moves.contains(index(index, +2, +0)), is(true));
        assertThat(moves.contains(index(index, +3, +0)), is(true));
        assertThat(moves.contains(index(index, +4, +0)), is(true));

        // can move one left
        moves = ChessPieceMoves.QUEEN_MOVES_LEFT[index];
        assertThat((byte)moves.size(), is((byte)1));
        assertThat(moves.contains(index(index, -1, +0)), is(true));

        // can move three down
        moves = ChessPieceMoves.QUEEN_MOVES_DOWN[index];
        assertThat((byte)moves.size(), is((byte)3));
        assertThat(moves.contains(index(index, +0, +1)), is(true));
        assertThat(moves.contains(index(index, +0, +2)), is(true));
        assertThat(moves.contains(index(index, +0, +3)), is(true));
        
        // can move one up left
        moves = ChessPieceMoves.QUEEN_MOVES_UP_LEFT[index];
        assertThat((byte)moves.size(), is((byte)1));
        assertThat(moves.contains(index(index, -1, -1)), is(true));

        // can move two up right
        moves = ChessPieceMoves.QUEEN_MOVES_UP_RIGHT[index];
        assertThat((byte)moves.size(), is((byte)2));
        assertThat(moves.contains(index(index, +1, -1)), is(true));
        assertThat(moves.contains(index(index, +2, -2)), is(true));

        // can move one down left
        moves = ChessPieceMoves.QUEEN_MOVES_DOWN_LEFT[index];
        assertThat((byte)moves.size(), is((byte)1));
        assertThat(moves.contains(index(index, -1, +1)), is(true));

        // can move three down right
        moves = ChessPieceMoves.QUEEN_MOVES_DOWN_RIGHT[index];
        assertThat((byte)moves.size(), is((byte)3));
        assertThat(moves.contains(index(index, +1, +1)), is(true));
        assertThat(moves.contains(index(index, +2, +2)), is(true));
        assertThat(moves.contains(index(index, +3, +3)), is(true));
    }
}
