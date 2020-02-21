package com.gmail.enzocampanella98.candidatecrush.customui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.BlockConfig;

public class GameInstructionRow {

    public String line;
    public Actor actor;
    public BlockConfig blockConfig;

    public GameInstructionRow(String line) {
        this.line = line;
    }

    public GameInstructionRow(Actor actor) {
        this.actor = actor;
    }

    public GameInstructionRow(BlockConfig blockConfig) {
        this.blockConfig = blockConfig;
    }

    public boolean isTextLine() {
        return line != null;
    }

    public boolean isBlock() {
        return blockConfig != null;
    }

    public boolean isActor() {
        return actor != null;
    }
}
