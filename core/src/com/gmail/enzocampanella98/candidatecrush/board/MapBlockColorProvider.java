package com.gmail.enzocampanella98.candidatecrush.board;

import com.badlogic.gdx.graphics.Color;

import java.util.Map;

public class MapBlockColorProvider implements IBlockColorProvider {
    private Map<BlockType, Color> colorMap;

    public MapBlockColorProvider(Map<BlockType, Color> colorMap) {
        this.colorMap = colorMap;
    }

    @Override
    public Color getBlockColor(BlockType blockType) {
        return colorMap.get(blockType);
    }
}
