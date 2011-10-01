package ai;

import ai.ChessPiece;
import ai.Constants;

public class Square {
    private final ChessPiece piece;

    public Square(int squareValue) {
        if (squareValue == Constants.Empty) {
            this.piece = null;
        } else {
            this.piece = new ChessPiece(squareValue);
        }
    }

    public boolean isEmpty() {
        return this.piece == null;
    }

    public ChessPiece getPiece() {
        return piece;
    }
}
