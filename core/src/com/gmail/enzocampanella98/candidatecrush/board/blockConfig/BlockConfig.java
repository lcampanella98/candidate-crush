package com.gmail.enzocampanella98.candidatecrush.board.blockConfig;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

public class BlockConfig {
    private BlockType type;
    private Color backgroundColor;
    private boolean isSoundByteBlock;
    private Texture candidateTexture;
    private Texture bgTexture;

    public BlockType getType() {
        return type;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isSoundByteBlock() {
        return isSoundByteBlock;
    }

    public void setSoundByteBlock(boolean soundByteBlock) {
        isSoundByteBlock = soundByteBlock;
    }

    public Texture getCandidateTexture() {
        return candidateTexture;
    }

    public void setCandidateTexture(Texture candidateTexture) {
        this.candidateTexture = candidateTexture;
    }

    public Texture getBgTexture() {
        return bgTexture;
    }

    public void setBgTexture(Texture bgTexture) {
        this.bgTexture = bgTexture;
    }
}
