package ai;

import java.util.ArrayList;

public class BoardEvaluator  {

    private final static byte[] PAWN_TABLE = {
            127,127,127,127,127,127,
            80, 80, 80,80, 80, 80,
            0,  25,  27,27,25,  0,
            5,  20, 25,25, 20,  5,
            5, -25,-25,-25,-25, 5,
            0,  0,  0,  0,  0,  0
    };

    private static final byte[] KNIGHT_TABLE = {
            -50,-40,-30,-30,-40,-50,
            -40,-20,  0,  0,-20,-40,
            -30,  5, 20, 20,  5,-30,
            -30,  0, 20, 20,  0,-30,
            -40,-20,  5,  5,-20,-40,
            -50,-40,-30,-30,-40,-50
    };

    private static final byte[] KING_TABLE = {
            -40,-50,-50,-50,-50,-50,
            -30,-40,-40,-40,-40,-40,
            -20,-30,-30,-30,-30,-20,
            -10,-20,-20,-20,-20,-10,
             -5, 20,  0,  0, -5, 20,
             20, 30,  0,  0, 30, 20
    };

    private static final byte[] KING_TABLE_END_GAME = {
            -50,-40,-40,-40,-40,-50,
            -30, 20, 30, 30, 20,-30,
            -30, 30, 40, 40, 30,-30,
            -30, 30, 40, 40, 30,-30,
            -30,  0,  0,  0,  0,-30,
            -50,-30,-30,-30,-30,-50
    };
    
    public double eval(int[][] boardValues) {
        double score = 0.0;
        Board board = new Board(boardValues);
        boolean endGame = board.getPiecesOnBoard() <= 6;

        byte[] whitePawnCount = new byte[6];
        byte[] blackPawnCount = new byte[6];

        byte whiteKingIndex = -1;
        byte blackKingIndex = -1;
        
        for (byte index = 0; index < board.size(); index++) {
            Square sq = board.get(index);

            if (!(sq.isEmpty())) {
                ChessPiece pc = sq.getPiece();

                ArrayList<Byte> validMoves = board.validMoves(index);

                if (pc.getType() == ChessPieceType.KING) {
                    if (pc.getColor() == ChessPieceColor.WHITE) {
                        whiteKingIndex = index;
                    } else {
                        blackKingIndex = index;
                    }
                }

                double pcScore = pc.getType().getValue();
                pcScore += validMoves.size();
                pcScore += pc.getDefendedValue();
                pcScore -= pc.getAttackedValue();

                if (pc.getDefendedValue() < pc.getAttackedValue()) {
                    pcScore -= (pc.getAttackedValue() - pc.getDefendedValue()) * 10;
                }

                byte pcTablePos = (pc.getColor() == ChessPieceColor.WHITE) ? index : ((byte)(board.size() - index - 1));
                if (pc.getType() == ChessPieceType.PAWN) {
                    if (index % Board.ROWS == 0 || index % Board.ROWS == 7) {
                        pcScore -= 15;
                    }

                    pcScore += PAWN_TABLE[pcTablePos];

                    if (pc.getColor() == ChessPieceColor.WHITE) {
                        if (whitePawnCount[index % Board.ROWS] > 0) {
                            // doubled pawn
                            score -= 16;
                        }

                        if (index > 6 && index <= 11) {
                            if (pc.getAttackedValue() == 0) {
                                whitePawnCount[index % Board.ROWS] += 200;

                                if (pc.getDefendedValue() != 0) {
                                    whitePawnCount[index % Board.ROWS] += 50;
                                }
                            }
                        } else if (index >= 12 && index <= 17) {
                            if (pc.getAttackedValue() == 0) {
                                whitePawnCount[index % Board.ROWS] += 100;

                                if (pc.getDefendedValue() != 0) {
                                    whitePawnCount[index % Board.ROWS] += 25;
                                }
                            }
                        }

                        whitePawnCount[index % Board.ROWS] += 10;
                    } else {
                        if (blackPawnCount[index % Board.ROWS] > 0) {
                            // doubled pawn
                            score -= 16;
                        }

                        if (index >= 30 && index <= 36) {
                            if (pc.getAttackedValue() == 0) {
                                blackPawnCount[index % Board.ROWS] += 200;

                                if (pc.getDefendedValue() != 0) {
                                    blackPawnCount[index % Board.ROWS] += 50;
                                }
                            }
                        } else if (index >= 24 && index <= 29) {
                            if (pc.getAttackedValue() == 0) {
                                blackPawnCount[index % Board.ROWS] += 100;

                                if (pc.getDefendedValue() != 0) {
                                    blackPawnCount[index % Board.ROWS] += 25;
                                }
                            }
                        }

                        blackPawnCount[index % Board.ROWS] += 10;
                    }
                } else if (pc.getType() == ChessPieceType.KNIGHT) {
                    pcScore += KNIGHT_TABLE[pcTablePos];

                    if (endGame) {
                        pcScore -= 10;
                    }
                } else if (pc.getType() == ChessPieceType.KING) {
                    if (pc.getAttackedValue() > 0) {
                        pcScore -= 75;
                    }
                    if (endGame) {
                        pcScore += KING_TABLE_END_GAME[pcTablePos];
                    } else {
                        pcScore += KING_TABLE[pcTablePos];
                    }
                } else if (pc.getType() == ChessPieceType.QUEEN) {
                    boolean moved = (pc.getColor() == ChessPieceColor.BLACK && index != Helpers.index(2, 0))
                            || (pc.getColor() == ChessPieceColor.WHITE && index != Helpers.index(2, 5));

                    if (moved && !(endGame)) {
                        pcScore -= 10;
                    }
                }

                if (pc.getColor() == ChessPieceColor.BLACK) {
                    score -= pcScore;
                } else {
                    score += pcScore;
                }
            }
        }

        if (whiteKingIndex == -1) {
            score -= ChessPieceType.KING.getValue();
        }

        if (blackKingIndex == -1) {
            score += ChessPieceType.KING.getValue();
        }

        if (blackPawnCount[0] >= 1 && blackPawnCount[1] == 0) {
            score += 12;
        }
        
        if (blackPawnCount[1] >= 1 && blackPawnCount[0] == 0 && blackPawnCount[2] == 0) {
            score += 14;
        }
        if (blackPawnCount[2] >= 1 && blackPawnCount[1] == 0 && blackPawnCount[3] == 0) {
            score += 16;
        }
        if (blackPawnCount[3] >= 1 && blackPawnCount[2] == 0 &&
                blackPawnCount[4] == 0)
        {
            score += 20;
        }
        if (blackPawnCount[4] >= 1 && blackPawnCount[3] == 0 &&
                blackPawnCount[5] == 0)
        {
            score += 20;
        }
        
//White Isolated Pawns
        if (whitePawnCount[0] >= 1 && whitePawnCount[1] == 0)
        {
            score -= 12;
        }
        if (whitePawnCount[1] >= 1 && whitePawnCount[0] == 0 &&
                whitePawnCount[2] == 0)
        {
            score -= 14;
        }
        if (whitePawnCount[2] >= 1 && whitePawnCount[1] == 0 &&
                whitePawnCount[3] == 0)
        {
            score -= 16;
        }
        if (whitePawnCount[3] >= 1 && whitePawnCount[2] == 0 &&
                whitePawnCount[4] == 0)
        {
            score -= 20;
        }
        if (whitePawnCount[4] >= 1 && whitePawnCount[3] == 0 &&
                whitePawnCount[5] == 0)
        {
            score -= 20;
        }


//Black Passed Pawns
        if (blackPawnCount[0] >= 1 && whitePawnCount[0] == 0)
        {
            score -= blackPawnCount[0];
        }
        if (blackPawnCount[1] >= 1 && whitePawnCount[1] == 0)
        {
            score -= blackPawnCount[1];
        }
        if (blackPawnCount[2] >= 1 && whitePawnCount[2] == 0)
        {
            score -= blackPawnCount[2];
        }
        if (blackPawnCount[3] >= 1 && whitePawnCount[3] == 0)
        {
            score -= blackPawnCount[3];
        }
        if (blackPawnCount[4] >= 1 && whitePawnCount[4] == 0)
        {
            score -= blackPawnCount[4];
        }
        if (blackPawnCount[5] >= 1 && whitePawnCount[5] == 0)
        {
            score -= blackPawnCount[5];
        }


//White Passed Pawns
        if (whitePawnCount[0] >= 1 && blackPawnCount[1] == 0)
        {
            score += whitePawnCount[0];
        }
        if (whitePawnCount[1] >= 1 && blackPawnCount[1] == 0)
        {
            score += whitePawnCount[1];
        }
        if (whitePawnCount[2] >= 1 && blackPawnCount[2] == 0)
        {
            score += whitePawnCount[2];
        }
        if (whitePawnCount[3] >= 1 && blackPawnCount[3] == 0)
        {
            score += whitePawnCount[3];
        }
        if (whitePawnCount[4] >= 1 && blackPawnCount[4] == 0)
        {
            score += whitePawnCount[4];
        }
        if (whitePawnCount[5] >= 1 && blackPawnCount[5] == 0)
        {
            score += whitePawnCount[5];
        }

        return score;
    }
}
