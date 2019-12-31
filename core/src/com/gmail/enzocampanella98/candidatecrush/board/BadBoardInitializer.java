package com.gmail.enzocampanella98.candidatecrush.board;

import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.IBoardAnalyzer;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.IBoardInitializer;

public class BadBoardInitializer implements IBoardInitializer {
    @Override
    public Block[][] getInitializedBlocks(Board board) {
        int n = board.getNumBlocksAcross();
        Block[][] blocks = new Block[n][n];
        IBoardAnalyzer boardAnalyzer = board.getBoardAnalyzer();
        do {
            populateBoardWithProvider(blocks, board);
        } while (boardAnalyzer.analyzeBoard(blocks).size > 0);

        return blocks;
    }

    private static void populateBoardWithProvider(Block[][] b, Board board) {
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                b[i][j] = board.getNewBlock(i, j);
            }
        }
    }
}
