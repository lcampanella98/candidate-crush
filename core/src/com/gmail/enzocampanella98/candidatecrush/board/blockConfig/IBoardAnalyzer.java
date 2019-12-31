package com.gmail.enzocampanella98.candidatecrush.board.blockConfig;

import com.badlogic.gdx.utils.Array;
import com.gmail.enzocampanella98.candidatecrush.board.Block;
import com.gmail.enzocampanella98.candidatecrush.board.Board;
import com.gmail.enzocampanella98.candidatecrush.board.SimpleBlockGroup;

public interface IBoardAnalyzer {
    Array<SimpleBlockGroup> analyzeBoard(Board board);
    Array<SimpleBlockGroup> analyzeBoard(Block[][] blocks);
}
