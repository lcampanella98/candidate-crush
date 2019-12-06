package com.gmail.enzocampanella98.candidatecrush.board;

import com.badlogic.gdx.utils.Array;

public interface IBoardAnalyzer {
    Array<SimpleBlockGroup> analyzeBoard(Board board);
    Array<SimpleBlockGroup> analyzeBoard(Block[][] blocks);
}
