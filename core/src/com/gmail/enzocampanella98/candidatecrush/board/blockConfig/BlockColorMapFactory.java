package com.gmail.enzocampanella98.candidatecrush.board.blockConfig;

import com.badlogic.gdx.graphics.Color;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class BlockColorMapFactory {

    private static Random rand = new Random();

    private BlockColorMapFactory() {}

    public static Map<BlockType, Color> getRandomBlockColorProvider(List<BlockType> blockTypes, List<Color> colors) {
        assert colors.size() >= blockTypes.size();

        List<Color> colorsLeft = new ArrayList<>(colors);
        Map<BlockType, Color> map = new HashMap<>();
        for (BlockType bt : blockTypes) {
            int i = rand.nextInt(colorsLeft.size());
            map.put(bt, colorsLeft.remove(i));
        }
        return map;
    }
}
