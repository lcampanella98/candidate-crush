package com.gmail.enzocampanella98.candidatecrush.board;

import java.util.Map;
import java.util.Random;

public class FrequencyRandomBlockTypeProvider implements IBlockTypeProvider {

    private Random rand = new Random();
    private Map<BlockType, Double> blockTypeFrequencies;

    public FrequencyRandomBlockTypeProvider(
            Map<BlockType, Double> blockTypeFrequencies) {
        this.blockTypeFrequencies = blockTypeFrequencies;
    }

    @Override
    public BlockType provideBlockType() {
        BlockType blockType = null;

        double num = rand.nextDouble();
        for (Map.Entry<BlockType, Double> freq : blockTypeFrequencies.entrySet()) {
            if (num <= freq.getValue()) {
                blockType = freq.getKey();
                break;
            }
            num -= freq.getValue();
        }
        assert blockType != null;
        return blockType;
    }
}
