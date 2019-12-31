package com.gmail.enzocampanella98.candidatecrush.board.blockConfig;

import com.gmail.enzocampanella98.candidatecrush.board.Block;
import com.gmail.enzocampanella98.candidatecrush.board.Board;

public interface IBoardInitializer {
    Block[][] getInitializedBlocks(Board board);
}
