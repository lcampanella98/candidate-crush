package com.gmail.enzocampanella98.candidatecrush.board.blockConfig;

import com.gmail.enzocampanella98.candidatecrush.board.Block;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;


/*
Should be able to handle "nulls" in the board.
A null indicates there is currently no block there or it has yet
to be chosen
 */
public interface IBlockTypeProvider {
    BlockType provide(Block[][] board, int row, int col);
}
