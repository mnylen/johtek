
import java.util.ArrayList;

public class YourEvaluator extends Evaluator {
    public static final int VALUE_KING  = 100000;
    public static final int VALUE_QUEEN = 500;
    public static final int VALUE_KNIGHT = 300;
    public static final int VALUE_ROOK = 100;
    public static final int VALUE_PAWN = 50;

	public double eval(Position pos) {
        double ret = 0;
        int[][] board = pos.board;

        int kingX = 0;
        int kingY = 0;

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board.length; y++) {
                if (board[x][y] == Position.WKing) {
                    kingX = x;
                    kingY = y;
                }
            }
        }

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board.length; y++) {
                int piece = board[x][y];
                double dangerFactor = dangerFactor(x, y, kingX, kingY); 

                switch (piece) {
                    case Position.WKing:
                        ret += VALUE_KING;
                        break;

                    case Position.BKing:
                        ret -= VALUE_KING;
                        ret -= dangerFactor * 10; 
                        break;

                    case Position.WQueen:
                        ret += VALUE_QUEEN;
                        break;

                    case Position.BQueen:
                        ret -= VALUE_QUEEN;
                        ret -= dangerFactor * 500; 
                        break;

                    case Position.WKnight:
                        ret += VALUE_KNIGHT;
                        break;

                    case Position.BKnight:
                        ret -= VALUE_KNIGHT;
                        ret -= dangerFactor * 100;
                        break;

                    case Position.WRook:
                        ret += VALUE_ROOK;
                        break;

                    case Position.BRook:
                        ret -= VALUE_ROOK;
                        ret -= dangerFactor * 50;
                        break;

                    case Position.WPawn:
                        ret += VALUE_PAWN;
                        ret -= dangerFactor * 10;
                        break;

                    case Position.BPawn:
                        ret -= VALUE_PAWN;
                        break;
                }

                if (Position.isBlackPiece(piece)) {
                    ret -= valueOfEatablePieces(piece, x, y, pos);
                } else {
                    ret += valueOfEatablePieces(piece, x, y, pos);
                }
            }
        }

        return ret;
	}

    private double dangerFactor(int x1, int y1, int x2, int y2) {
        int distance = Math.abs(x2 - x1) + Math.abs(y2 - y1);

        if (distance == 0) {
            return 1;
        } else {
            return 1.0/distance;
        }
    }

    private double valueOfEatablePieces(int piece, int x, int y, Position pos) {
        int[][] eatMoves = new int[0][];

        switch (piece) {
            case Position.BPawn:
            case Position.WPawn:
                eatMoves = this.pawnEatMoves(piece, x, y);
                break;

            case Position.BRook:
            case Position.WRook:
                eatMoves = this.rookEatMoves(piece, x, y, pos.board);
                break;

            case Position.BKnight:
            case Position.WKnight:
                eatMoves = this.knightEatMoves(piece, x, y);
                break;

            case Position.BKing:
            case Position.WKing:
                eatMoves = this.kingEatMoves(piece, x, y);
                break;

            case Position.BQueen:
            case Position.WQueen:
                eatMoves = this.queenEatMoves(piece, x, y, pos.board);
                break;
        }

        double ret = 0;

        for (int[] move : eatMoves) {
            int eatX = move[0];
            int eatY = move[1];

            if (eatX < 0 || eatX >= Position.bCols || eatY < 0 || eatY >= Position.bRows) {
                continue;
            }

            int eatPiece = pos.board[eatX][eatY];

            boolean skip = eatPiece == Position.Empty;
            skip = skip || Position.isBlackPiece(eatPiece) && Position.isBlackPiece(piece);
            skip = skip || (Position.isWhitePiece(eatPiece) && Position.isWhitePiece(piece));

            if (!(skip)) {
                switch (eatPiece) {
                    case Position.WKing:
                    case Position.BKing:
                        ret += VALUE_KING;
                        break;

                    case Position.WQueen:
                    case Position.BQueen:
                        ret += VALUE_QUEEN;
                        break;

                    case Position.WRook:
                    case Position.BRook:
                        ret += VALUE_ROOK;
                        break;

                    case Position.WPawn:
                    case Position.BPawn:
                        ret += VALUE_PAWN;
                        break;

                    case Position.WKnight:
                    case Position.BKnight:
                        ret += VALUE_KNIGHT;
                }
            }
        }

        return ret;
    }

    private int[][] pawnEatMoves(int piece, int x, int y) {
        if (Position.isBlackPiece(piece)) {
            return new int[][] {
                { x - 1, y - 1 },
                { x + 1, y - 1 }
            };
        } else {
            return new int[][] {
                { x - 1, y + 1 },
                { x + 1, y + 1 }
            };
        }
    }

    private int[][] kingEatMoves(int piece, int x, int y) {
        return new int[][] {
            { x + 1, y },
            { x - 1, y },
            { x, y + 1 },
            { x, y - 1 },
            { x + 1, y + 1 },
            { x + 1, y - 1 },
            { x - 1, y + 1 },
            { x - 1, y - 1 }
        };
    }

    private int[][] knightEatMoves(int piece, int x, int y) {
        return new int[][] {
            { x + 1, y + 2 },
            { x + 2, y + 1 },
            { x + 2, y - 1 },
            { x + 1, y - 2 },
            { x - 1, y - 2 },
            { x - 2, y - 1 },
            { x - 2, y + 1 },
            { x - 1, y + 2 }
        };
    }

    private int[][] rookEatMoves(int piece, int pieceX, int pieceY, int[][] board) {
        ArrayList<int[]> moves = new ArrayList<int[]>();

        for (int x = pieceX - 1; x >= 0; x--) {
            if (board[x][pieceY] == Position.Empty) {
                moves.add(new int[] { x, pieceY });
            } else {
                break;
            }
        }

        for (int x = pieceX + 1; x < Position.bCols; x++) {
            if (board[x][pieceY] == Position.Empty) {
                moves.add(new int[] { x, pieceY });
            }
        }

        for (int y = pieceY - 1; y >= 0; y--) {
            if (board[pieceX][y] == Position.Empty) {
                moves.add(new int[] { pieceX, y });
            } else {
                break;
            }
        }

        for (int y = pieceY + 1; y < Position.bRows; y++) {
            if (board[pieceX][y] == Position.Empty) {
                moves.add(new int[] { pieceX, y });
            } else {
                break;
            }
        }

        return moves.toArray(new int[moves.size()][]);
    }

    private int[][] queenEatMoves(int piece, int pieceX, int pieceY, int[][] board) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        for (int[] move : rookEatMoves(piece, pieceX, pieceY, board)) {
            moves.add(move);
        }

        for (int x = pieceX+1; x < Position.bCols; x++) {
            for (int y = pieceY + 1; y < Position.bRows; y++) {
                if (board[x][y] == Position.Empty) {
                    moves.add(new int[] { x, y });
                } else {
                    break;
                }
            }

            for (int y = pieceY - 1; y >= 0; y --) {
                if (board[x][y] == Position.Empty) {
                    moves.add(new int[] { x, y });
                } else {
                    break;
                }
            }
        }

        for (int x = pieceX - 1; x >= 0; x--) {
            for (int y = pieceY + 1; y < Position.bRows; y++) {
                if (board[x][y] == Position.Empty) {
                    moves.add(new int[] { x, y });
                } else {
                    break;
                }
            }

            for (int y = pieceY - 1; y >= 0; y --) {
                if (board[x][y] == Position.Empty) {
                    moves.add(new int[] { x, y });
                } else {
                    break;
                }
            }
        }

        return moves.toArray(new int[moves.size()][]);
    }
}
