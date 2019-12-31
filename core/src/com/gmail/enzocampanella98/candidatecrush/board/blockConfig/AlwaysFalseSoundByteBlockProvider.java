package com.gmail.enzocampanella98.candidatecrush.board.blockConfig;

public class AlwaysFalseSoundByteBlockProvider implements IIsSoundbyteBlockProvider {
    @Override
    public boolean provide(BlockConfig config) {
        return false;
    }
}
