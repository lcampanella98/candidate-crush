package com.gmail.enzocampanella98.candidatecrush.board.blockConfig;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gmail.enzocampanella98.candidatecrush.board.Block;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BlockProvider implements IBlockProvider, Disposable {

    private final IBlockTypeProvider blockTypeProvider;
    private final IIsSoundbyteBlockProvider isSoundByteBlockProvider;
    private final Collection<BlockType> blockTypes;

    private ObjectMap<BlockType, Texture> candidateTextures;
    private ObjectMap<BlockType, Texture> candidateTexturesWithSound;
    private Map<BlockType, Color> colorMap;
    private ObjectMap<Color, Texture> backgroundTextures;

    public BlockProvider(
            Collection<BlockType> blockTypes,
            Map<BlockType, Color> colorMap,
            IBlockTypeProvider blockTypeProvider,
            IIsSoundbyteBlockProvider isSoundByteBlockProvider
    ) {
        this.blockTypes = blockTypes;
        this.blockTypeProvider = blockTypeProvider;
        this.isSoundByteBlockProvider = isSoundByteBlockProvider;
        this.colorMap = colorMap;

        initColorMap();

        candidateTextures = getCandidateTextures(blockTypes, false);
        candidateTexturesWithSound = getCandidateTextures(blockTypes, true);

        int size = candidateTextures.values().next().getWidth(); // size of bg is size of candidate
        backgroundTextures = getBgColorTextures(colorMap.values(), size);
    }

    private void initColorMap() {
        if (colorMap == null) colorMap = new HashMap<>();
        for (BlockType blockType : blockTypes) {
            if (!colorMap.containsKey(blockType)) {
                colorMap.put(blockType, null);
            }
        }
    }

    @Override
    public Block provide(int row, int col, Vector2 pos, float width, float height) {
        BlockConfig config = new BlockConfig();

        config.setType(blockTypeProvider.provide());
        config.setBackgroundColor(colorMap.get(config.getType()));
        config.setSoundByteBlock(isSoundByteBlockProvider.provide(config));

        Texture candidateTexture;
        if (config.isSoundByteBlock()) {
            candidateTexture = candidateTexturesWithSound.get(config.getType());
        } else {
            candidateTexture = candidateTextures.get(config.getType());
        }
        config.setCandidateTexture(candidateTexture);

        if (config.getBackgroundColor() != null) {
            config.setBgTexture(backgroundTextures.get(config.getBackgroundColor()));
        }

        return new Block(row, col, pos, width, height, config);
    }

    @Override
    public Texture getBlockTexture(BlockType type) {
        return candidateTextures.get(type);
    }

    @Override
    public void dispose() {
        for (ObjectMap.Entry<BlockType, Texture> kv : candidateTextures) {
            kv.value.dispose();
        }
        for (ObjectMap.Entry<BlockType, Texture> kv : candidateTexturesWithSound) {
            kv.value.dispose();
        }
        for (ObjectMap.Entry<Color, Texture> kv : backgroundTextures) {
            kv.value.dispose();
        }
    }

    private static ObjectMap<BlockType, Texture> getCandidateTextures(Collection<BlockType> blockTypes, boolean withSound) {
        ObjectMap<BlockType, Texture> blockTextures = new ObjectMap<>();
        for (BlockType b : blockTypes) {
            blockTextures.put(b, new Texture(
                    withSound
                            ? b.getInternalPathWithSound()
                            : b.getInternalPath()));
        }
        return blockTextures;
    }

    private static ObjectMap<Color, Texture> getBgColorTextures(Collection<Color> colors, int size) {
        ObjectMap<Color, Texture> bgTextures = new ObjectMap<>();
        for (Color color : colors) {
            if (color == null) continue;

            Pixmap bgPixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);

            // draw the background
            bgPixmap.setColor(color);
            bgPixmap.fillCircle(bgPixmap.getWidth() / 2, bgPixmap.getHeight() / 2, bgPixmap.getWidth() / 2);

            // put the new texture
            bgTextures.put(color, new Texture(bgPixmap));

            // no longer need pixmap
            bgPixmap.dispose();
        }
        return bgTextures;
    }

}
