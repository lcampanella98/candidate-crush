package com.gmail.enzocampanella98.candidatecrush.board;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Collection;

public abstract class BlockProvider implements IBlockProvider, Disposable {

    private ObjectMap<BlockType, Texture> blockTextures;
    protected Collection<BlockType> blockTypes;

    public BlockProvider(Collection<BlockType> blockTypes) {
        this.blockTextures = BlockType.getBlockTextures(blockTypes);
        this.blockTypes = blockTypes;
    }

    public Texture getBlockTexture(BlockType blockType) {
        return blockTextures.get(blockType);
    }

    @Override
    public void dispose() {
        for (ObjectMap.Entry<BlockType, Texture> ent : blockTextures) {
            ent.value.dispose();
        }
    }

}
