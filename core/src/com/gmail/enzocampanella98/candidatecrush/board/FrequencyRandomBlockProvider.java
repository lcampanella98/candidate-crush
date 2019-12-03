package com.gmail.enzocampanella98.candidatecrush.board;

import com.badlogic.gdx.math.Vector2;

import java.util.Map;
import java.util.Random;

public class FrequencyRandomBlockProvider extends BlockProvider {

    private Random rand = new Random();
    private Map<BlockType, Double> blockTypeFrequencies;

    public FrequencyRandomBlockProvider(
            Map<BlockType, Double> blockTypeFrequencies) {
        super(blockTypeFrequencies.keySet());
        this.blockTypeFrequencies = blockTypeFrequencies;
    }

    @Override
    public Block provideBlock(Vector2 initialPosition, float width, float height, int row, int col) {
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
        return new Block(blockType, getBlockTexture(blockType), initialPosition,
                width, height, row, col);
    }
}
