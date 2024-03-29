public abstract class PiecePositionalValuesTables {
    public static final int[] PAWN_TABLE = new int[] {
            0,   0,  0,  0,  0,  0,
            50, 50, 50, 50, 50, 50,
            10, 20, 30, 30, 20, 10,
             5, 10, 27, 27, 10,  5,
             5,-10, 25, 25,-10,  5,
             0,  0,  0,  0,  0,  0
    };

    public static final int[] PAWN_TABLE_ENDGAME = new int[] {
            400,   400,  400,  400,  400,  400,
            200, 200, 200, 200, 200, 200,
            75, 75, 75, 75, 75, 75,
            50, 50, 50, 50,  50,  50,
             0,  0,  0,  0,  0,  0,
             0,  0,  0,  0,  0,  0
    };

    public static final int[] PAWN_TABLE_BLACK = new int[] {
            0,  0,  0,  0,  0,  0,
            5,-10, 25, 25,-10,  5,
            5, 10, 27, 27, 10,  5,
            30, 30, 30, 30, 30, 30,
            50, 50, 50, 50, 50, 50,
            0,   0,  0,  0,  0,  0
    };

    public static final int[] PAWN_TABLE_ENDGAME_BLACK = new int[] {
             0,  0,  0,  0,  0,  0,
             0,  0,  0,  0,  0,  0,
             50, 50, 50, 50, 50, 50,
            75, 75, 75, 75, 75, 75,
            200, 200, 200, 200, 200, 200,
            400,   400,  400,  400,  400,  400
    };

    public static final int[] KNIGHT_TABLE = new int[] {
            -50, -40, -30, -30, -40, -50,
            -40, -20,   0,   0, -20, -40,
            -30,   15,  20,  20, 15, -30,
            -30,   5,  15,  15,   5, -30,
            -40, -20,   5,   5, -20, -40,
            -50, -40, -30, -30, -40, -50
    };

    public static final int[] KNIGHT_TABLE_BLACK = new int[] {
            -50, -40, -30, -30, -40, -50,
            -40, -20,   5,   5, -20, -40,
            -30,   5,  15,  15,   5, -30,
            -30,   15,  20,  20, 15, -30,
            -40, -20,   0,   0, -20, -40,
            -50, -40, -30, -30, -40, -50
    };

    public static final int[] KING_TABLE = new int[] {
            -30, -40, -50, -50, -40, -30,
            -30, -40, -50, -50, -40, -30,
            -30, -40, -50, -50, -40, -30,
            -20, -30, -40, -40, -30, -20,
             20,  20,   0,   0,  20,  20,
             20,  30,  10,   0,  30,  20
    };

    public static final int[] KING_TABLE_BLACK = new int[] {
            20,  30,  10,   0,  30,  20,
            20,  20,   0,   0,  20,  20,
            -20, -30, -40, -40, -30, -20,
            -30, -40, -50, -50, -40, -30,
            -30, -40, -50, -50, -40, -30,
            -30, -40, -50, -50, -40, -30
    };

    public static final int[] EMPTY_TABLE = new int[] {
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0
    };
}
