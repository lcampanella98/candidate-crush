package com.gmail.enzocampanella98.candidatecrush.board.blockConfig;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.gmail.enzocampanella98.candidatecrush.board.Block;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

public interface IBlockProvider {
    Block provide(int row, int col, Vector2 pos, float width, float height);
    Texture getBlockTexture(BlockType type);
    Block provideFromConfig(int row, int col, Vector2 pos, float width, float height, BlockConfig config);
}
