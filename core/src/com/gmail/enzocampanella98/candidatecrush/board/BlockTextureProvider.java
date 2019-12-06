package com.gmail.enzocampanella98.candidatecrush.board;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gmail.enzocampanella98.candidatecrush.tools.ImgTools;

import java.util.Collection;

public class BlockTextureProvider implements Disposable, IBlockTextureProvider {

    private ObjectMap<BlockType, Texture> blockTextures;
    private ObjectMap<BlockType, Texture> oldBlockTextures;
    protected Collection<BlockType> blockTypes;
    private IBlockColorProvider blockColorProvider;

    public BlockTextureProvider(Collection<BlockType> blockTypes, IBlockColorProvider blockColorProvider) {
        this.blockTextures = BlockType.getBlockTextures(blockTypes);
        this.blockTypes = blockTypes;
        this.blockColorProvider = blockColorProvider;

        modifyTexturesWithColors();
    }

    private void modifyTexturesWithColors() {
        oldBlockTextures = blockTextures;
        blockTextures = new ObjectMap<>(oldBlockTextures);

        for (ObjectMap.Entry<BlockType, Texture> e : oldBlockTextures) {
            Color bgColor = blockColorProvider.getBlockColor(e.key);
            if (bgColor == null) continue;

            Texture tex = e.value;
            // create new pixmap to store bg circle and candidate sprite
            Pixmap newPixmap = new Pixmap(tex.getWidth(), tex.getHeight(), Pixmap.Format.RGBA8888);

            // draw the background
            newPixmap.setColor(bgColor);
            newPixmap.fillCircle(newPixmap.getWidth()/2,newPixmap.getHeight()/2,newPixmap.getWidth()/2);

            // draw the original image over it
            Pixmap origPixmap = ImgTools.texToPixmap(tex);
            newPixmap.drawPixmap(origPixmap, 0, 0);

            Texture newTexture = new Texture(newPixmap);
            blockTextures.put(e.key, newTexture);

            newPixmap.dispose();
            origPixmap.dispose();
        }

    }

    @Override
    public Texture provideBlockTexture(BlockType blockType) {
        return blockTextures.get(blockType);
    }

    @Override
    public void dispose() {
        for (ObjectMap.Entry<BlockType, Texture> ent : blockTextures) {
            if (ent.value != null) ent.value.dispose();
        }
        for (ObjectMap.Entry<BlockType, Texture> ent : oldBlockTextures) {
            if (ent.value != null) ent.value.dispose();
        }
    }


}
