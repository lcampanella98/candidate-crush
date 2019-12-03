package com.gmail.enzocampanella98.candidatecrush.board;

import com.badlogic.gdx.math.Vector2;

public interface IBlockProvider {
    Block provideBlock(
            Vector2 initialPosition,
            float width, float height,
            int row, int col
    );
}
