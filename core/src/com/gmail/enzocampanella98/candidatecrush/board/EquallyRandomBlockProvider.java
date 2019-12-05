package com.gmail.enzocampanella98.candidatecrush.board;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EquallyRandomBlockProvider extends FrequencyRandomBlockProvider {
    public EquallyRandomBlockProvider(Collection<BlockType> blockTypes, IBlockColorProvider blockColorProvider) {
        super(equalFreqs(blockTypes), blockColorProvider);
    }

    private static Map<BlockType, Double> equalFreqs(Collection<BlockType> blockTypes) {
        Map<BlockType, Double> freqs = new HashMap<>();
        for (BlockType bt : blockTypes) {
            freqs.put(bt, 1.0 / blockTypes.size());
        }
        return freqs;
    }
}
