package ai;

public enum ChessPieceType {
    PAWN(100, 6),
    KNIGHT(320, 3),
    ROOK(500, 2),
    QUEEN(1200, 2),
    KING(32767, 1);

    private final int value;
    private final int actionValue;
    ChessPieceType(int value, int actionValue) {
        this.value = value;
        this.actionValue = actionValue;
    }

    public int getValue() {
        return value;
    }

    public int getActionValue() {
        return actionValue;
    }
}
