package ai;

public class ChessPiece {
    private final ChessPieceColor color;
    private final ChessPieceType type;
    private int attackedValue = 0;
    private int defendedValue = 0;
    private int actionValue = 0;
    
    public ChessPiece(int squareValue) {
        if (squareValue > 0 && squareValue < 7) {
            this.color = ChessPieceColor.WHITE;
        } else {
            this.color = ChessPieceColor.BLACK;
        }
        
        switch (squareValue) {
            case Constants.BKing:
            case Constants.WKing:
                this.type = ChessPieceType.KING;
                break;
            
            case Constants.BKnight:
            case Constants.WKnight:
                this.type = ChessPieceType.KNIGHT;
                break;

            case Constants.BQueen:
            case Constants.WQueen:
                this.type = ChessPieceType.QUEEN;
                break;

            case Constants.BRook:
            case Constants.WRook:
                this.type = ChessPieceType.ROOK;
                break;

            case Constants.BPawn:
            case Constants.WPawn:
                this.type = ChessPieceType.PAWN;
                break;

            default:
                throw new RuntimeException("Invalid squareValue " + squareValue + " for a piece");
        }

        this.actionValue = this.type.getActionValue();
    }

    public int getAttackedValue() {
        return attackedValue;
    }

    public void setAttackedValue(int attackedValue) {
        this.attackedValue = attackedValue;
    }

    public int getDefendedValue() {
        return defendedValue;
    }

    public void setDefendedValue(int defendedValue) {
        this.defendedValue = defendedValue;
    }

    public int getActionValue() {
        return actionValue;
    }

    public void setActionValue(int actionValue) {
        this.actionValue = actionValue;
    }

    public ChessPieceColor getColor() {
        return color;
    }

    public ChessPieceType getType() {
        return type;
    }
}
