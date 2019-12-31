package com.gmail.enzocampanella98.candidatecrush.board.blockConfig;

import java.util.Random;

public class FrequencyIsSoundByteBlockProvider implements IIsSoundbyteBlockProvider {

    private static Random rand = new Random();
    private double isSoundByteFreq;

    public FrequencyIsSoundByteBlockProvider(double isSoundByteFreq) {
        this.isSoundByteFreq = isSoundByteFreq;
    }

    @Override
    public boolean provide(BlockConfig config) {
        return rand.nextDouble() < isSoundByteFreq;
    }
}
