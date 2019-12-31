package com.gmail.enzocampanella98.candidatecrush.board.blockConfig;

import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EquallyRandomBlockTypeProvider extends FrequencyRandomBlockTypeProvider {
    public EquallyRandomBlockTypeProvider(Collection<BlockType> blockTypes) {
        super(equalFreqs(blockTypes));
    }

    private static Map<BlockType, Double> equalFreqs(Collection<BlockType> blockTypes) {
        Map<BlockType, Double> freqs = new HashMap<>();
        for (BlockType bt : blockTypes) {
            freqs.put(bt, 1.0 / blockTypes.size());
        }
        return freqs;
    }
}
