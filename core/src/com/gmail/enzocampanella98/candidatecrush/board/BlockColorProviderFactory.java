package com.gmail.enzocampanella98.candidatecrush.board;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockColorProviderFactory {

    private Random rand = new Random();


    public IBlockColorProvider getRandomBlockColorProvider(List<BlockType> blockTypes, List<Color> colors) {
        assert colors.size() >= blockTypes.size();

        List<Color> colorsLeft = new ArrayList<>(colors);
        Map<BlockType, Color> map = new HashMap<>();
        for (BlockType bt : blockTypes) {
            int i = rand.nextInt(colorsLeft.size());
            map.put(bt, colorsLeft.remove(i));
        }
        return new MapBlockColorProvider(map);
    }

    public IBlockColorProvider getEmptyBlockColorProvider() {
        return new NoBlockColorProvider();
    }

    public IBlockColorProvider getMapBlockColorProvider(Map<BlockType, Color> map) {
        return new MapBlockColorProvider(map);
    }

}
