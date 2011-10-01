import java.util.HashMap;

public class MyEvaluator extends Evaluator {
    private HashMap<int[][], Double> previousEvaluationResults = new HashMap<int[][], Double>();
    private static final int ROWS = 6;
    private static final int COLUMNS = 6;
    private static final int MAX_INDEX = ROWS * COLUMNS - 1;
    private static final int MORE_MATERIAL_BONUS = 200;
    private static final int KING_VALUE = 32767;
    private static final int QUEEN_VALUE = 3000;
    private static final int ROOK_VALUE = 500;
    private static final int KNIGHT_VALUE = 700;
    private static final int PAWN_VALUE = 200;
    private static final int GUARD_VALUE = 200;
    private static final int KING_MOVE_COST = 600;
    private static final int MOVABILITY_BONUS = 20;
    
    private int[] xs;
    private int[] ys;
    private int[] boardValues;
    private int[] allMoves = new int[40];
    private int whitePieces;
    private int blackPieces;

    public MyEvaluator() {
        this.boardValues = new int[ROWS*COLUMNS];
        this.xs = new int[ROWS*COLUMNS];
        this.ys = new int[ROWS*COLUMNS];
    }

    @Override
    public double eval(Position position) {
        long score = 0;
        
        Double value = this.previousEvaluationResults.get(position.board);
        if (value != null) {
            // This position has already been
            // evaluated
            return value;
        }

        whitePieces = 0;
        blackPieces = 0;
        int kingIndex = -1;

        /*
            Initialize boardValues with values from position.board
            and count the number of white and black pieces in
            the board. Also, assign points based on each piece's
            default and positional values.
         */
        
        for (int x = 0; x < COLUMNS; x++) {
            for (int y = 0; y < ROWS; y++) {
                int index = index(x, y);
                int piece = position.board[x][y];
                this.xs[index] = x;
                this.ys[index] = y;
                score += value(piece, index);

                this.boardValues[index] = piece;
                if (piece == Position.WKing) {
                    kingIndex = index;
                    whitePieces++;
                } else if (isBlack(piece)) {
                    blackPieces++;
                } else if (isWhite(piece)) {
                    whitePieces++;
                }
            }
        }

        /*
            If our king was not found, then this is a mate
            and we certainly should not take this position
            if we can choose better.
         */

        if (kingIndex == -1) {
            this.previousEvaluationResults.put(position.board, Double.MIN_VALUE);
            return Double.MIN_VALUE;
        }

        /*
            Discourage moving king before end game.
         */

        if (!(isEndGame()) && ys[kingIndex] > 0) {
            score -= KING_MOVE_COST;
        }

        /*
            Assign some points if we have more material
            on board.
         */

        if (whitePieces > blackPieces) {
            score += MORE_MATERIAL_BONUS;
        } else {
            score -= MORE_MATERIAL_BONUS;
        }

        /*
            Assign some points for king safety. Only pawns that are
            on top of king count.
         */

        int topLeft = indexAdd(kingIndex, -1, -1);
        if (topLeft >= 0 && topLeft < this.boardValues.length && isWhite(this.boardValues[topLeft])) {
            score += GUARD_VALUE;
        }

        int top = indexAdd(kingIndex, 0, -1);
        if (top >= 0 && top < this.boardValues.length && isWhite(this.boardValues[top])) {
            score += GUARD_VALUE;
        }

        int topRight = indexAdd(kingIndex, 1, -1);
        if (topRight >= 0 && topRight < this.boardValues.length && isWhite(this.boardValues[topRight])) {
            score += GUARD_VALUE;
        }

        /*
            Assign some points for each piece for movability. Also
            assign points for threatening black's pieces.
         */

        for (int index = 0; index < this.boardValues.length; index++) {
            if (!(isEmptyOrOutside(index))) {
                score += movabilityAndThreateningScore(boardValues[index], index);
            }
        }

        /*
            Finally return the score.
         */

        if (this.previousEvaluationResults.size() > 1000) {
            this.previousEvaluationResults.clear();
        }

        this.previousEvaluationResults.put(position.board, (double)score);
        return score;
    }

    private long movabilityAndThreateningScore(int piece, int index) {
        int moveIndex = 0;
        
        if (isPawn(piece)) {
            if (isBlack(piece)) {
                moveIndex += addIfCanMoveTo(piece, indexAdd(index, 0, -1), moveIndex, allMoves);
                moveIndex += addIfCanMoveTo(piece, indexAdd(index, -1, -1), moveIndex, allMoves);
                moveIndex += addIfCanMoveTo(piece, indexAdd(index, 1, -1), moveIndex, allMoves);
            } else {
                moveIndex += addIfCanMoveTo(piece, indexAdd(index, 0, 1), moveIndex, allMoves);
                moveIndex += addIfCanMoveTo(piece, indexAdd(index, -1, 1), moveIndex, allMoves);
                moveIndex += addIfCanMoveTo(piece, indexAdd(index, 1, 1), moveIndex, allMoves);
            }
        } else if (isKnight(piece)) {
            moveIndex += addIfCanMoveTo(piece, indexAdd(index, 1, -2), moveIndex, allMoves);
            moveIndex += addIfCanMoveTo(piece, indexAdd(index, 2, -1), moveIndex, allMoves);
            moveIndex += addIfCanMoveTo(piece, indexAdd(index, 2, 1), moveIndex, allMoves);
            moveIndex += addIfCanMoveTo(piece, indexAdd(index, 1, 2), moveIndex, allMoves);
            moveIndex += addIfCanMoveTo(piece, indexAdd(index, -1, 2), moveIndex, allMoves);
            moveIndex += addIfCanMoveTo(piece, indexAdd(index, -2, 1), moveIndex, allMoves);
            moveIndex += addIfCanMoveTo(piece, indexAdd(index, -2, -1), moveIndex, allMoves);
            moveIndex += addIfCanMoveTo(piece, indexAdd(index, -1, -2), moveIndex, allMoves);
        } else if (isRook(piece) || isQueen(piece)) {
            int x = xs[index];

            for (int y = ys[index] + 1; y < ROWS; y++) {
                int moveTo = index(x,y);
                if (addIfCanMoveTo(piece, moveTo, moveIndex, allMoves) == 0) {
                    break;
                } else {
                    moveIndex++;
                }
            }

            for (int y = ys[index] - 1; y >= ROWS; y--) {
                int moveTo = index(x,y);
                if (addIfCanMoveTo(piece, moveTo, moveIndex, allMoves) == 0) {
                    break;
                } else {
                    moveIndex++;
                }
            }
            
            int y = ys[index];
            for (x = xs[index] + 1; x < COLUMNS; x++) {
                int moveTo = index(x,y);
                if (addIfCanMoveTo(piece, moveTo, moveIndex, allMoves) == 0) {
                    break;
                } else {
                    moveIndex++;
                }
            }
            
            for (x = xs[index] - 1; x >= 0; x--) {
                int moveTo = index(x,y);
                if (addIfCanMoveTo(piece, moveTo, moveIndex, allMoves) == 0) {
                    break;
                } else {
                    moveIndex++;
                }
            }
        }

        if (isQueen(piece)) {
            for (int x = xs[index] + 1; x < COLUMNS; x++) {
                for (int y = ys[index] + 1; y < ROWS; y++) {
                    int moveTo = index(x,y);
                    if (addIfCanMoveTo(piece, moveTo, moveIndex, allMoves) == 0) {
                        break;
                    } else {
                        moveIndex++;
                    }
                }

                for (int y = ys[index] - 1; y >= 0; y--) {
                    int moveTo = index(x,y);
                    if (addIfCanMoveTo(piece, moveTo, moveIndex, allMoves) == 0) {
                        break;
                    } else {
                        moveIndex++;
                    }
                }
            }

            for (int x = xs[index] - 1; x >= 0; x--) {
                for (int y = ys[index] + 1; y < ROWS; y++) {
                    int moveTo = index(x,y);
                    if (addIfCanMoveTo(piece, moveTo, moveIndex, allMoves) == 0) {
                        break;
                    } else {
                        moveIndex++;
                    }
                }

                for (int y = ys[index] - 1; y >= 0; y--) {
                    int moveTo = index(x,y);
                    if (addIfCanMoveTo(piece, moveTo, moveIndex, allMoves) == 0) {
                        break;
                    } else {
                        moveIndex++;
                    }
                }
            }
        }

        return (isBlack(piece) ? - moveIndex : moveIndex ) * MOVABILITY_BONUS ;
    }

    private long threatensFor(int piece, int index) {
        if (isBlack(piece) && isWhite(this.boardValues[index]) || isWhite(piece) && isBlack(this.boardValues[index])) {
            return value(this.boardValues[index], index);
        } else {
            return 0;
        }
    }

    private int addIfCanMoveTo(int piece, int moveTo, int moveIndex, int[] allMoves) {
        if (canMoveTo(moveTo, piece)) {
            allMoves[moveIndex] = moveTo;
            return 1;
        }
        return 0;
    }

    private long value(int piece, int index) {
        switch (piece) {
            case Position.BKing:
                return -(PiecePositionalValuesTables.KING_TABLE_BLACK[index] + KING_VALUE);

            case Position.WKing:
                return PiecePositionalValuesTables.KING_TABLE[index] + KING_VALUE;

            case Position.BKnight:
                return -(PiecePositionalValuesTables.KNIGHT_TABLE_BLACK[index] + KNIGHT_VALUE);

            case Position.WKnight:
                return PiecePositionalValuesTables.KNIGHT_TABLE[index] + KNIGHT_VALUE;

            case Position.BQueen:
                return -QUEEN_VALUE;

            case Position.WQueen:
                return QUEEN_VALUE;

            case Position.BRook:
                return -ROOK_VALUE;

            case Position.WRook:
                return ROOK_VALUE;

            case Position.BPawn:
                if (isEndGame()) {
                    return -(PiecePositionalValuesTables.PAWN_TABLE_ENDGAME_BLACK[index] + PAWN_VALUE);
                } else {
                    return -(PiecePositionalValuesTables.PAWN_TABLE_BLACK[index] + PAWN_VALUE);
                }

            case Position.WPawn:
                if (isEndGame()) {
                    return PiecePositionalValuesTables.PAWN_TABLE_ENDGAME[index] + PAWN_VALUE;
                } else {
                    return PiecePositionalValuesTables.PAWN_TABLE[index] + PAWN_VALUE;
                }

            default:
                return 0;
        }
    }

    private boolean isEndGame() {
        return whitePieces + blackPieces <= 8;
    }

    private boolean isQueen(int piece) {
        return piece == Position.BQueen || piece == Position.WQueen;
    }
    
    private boolean isRook(int piece) {
        return piece == Position.BRook || piece == Position.WRook;
    }
    
    private boolean isKing(int piece) {
        return piece == Position.BKing || piece == Position.WKing;
    }
    
    private boolean isPawn(int piece) {
        return piece == Position.BPawn || piece == Position.WPawn;
    }

    private boolean isKnight(int piece) {
        return piece == Position.BKing || piece == Position.WKnight;
    }
    
    private int indexAdd(int index, int xDelta, int yDelta) {
        return index + xDelta + (yDelta * ROWS);
    }

    private int index(int x, int y) {
        return indexAdd(0, x, y);
    }

    private boolean isEmptyOrOutside(int index) {
        return index < 0 || index > MAX_INDEX || boardValues[index] == Position.Empty;
    }

    private boolean canMoveTo(int index, int piece) {
        return  (index >= 0 && index < this.boardValues.length)
                && (this.boardValues[index] == Position.Empty
                || isBlack(piece) && isWhite(this.boardValues[index])
                || isWhite(piece) && isBlack(this.boardValues[index]));
    }
    
    private boolean isBlack(int piece) {
        return Position.isBlackPiece(piece);
    }

    private boolean isWhite(int piece) {
        return Position.isWhitePiece(piece);
    }
}
