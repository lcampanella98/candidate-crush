package com.gmail.enzocampanella98.candidatecrush.board;

import com.badlogic.gdx.graphics.Texture;

public interface IBlockTextureProvider {
    Texture provideBlockTexture(BlockType blockType);
}
