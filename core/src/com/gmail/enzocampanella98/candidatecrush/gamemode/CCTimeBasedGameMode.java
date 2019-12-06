package com.gmail.enzocampanella98.candidatecrush.gamemode;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.IBlockColorProvider;

import java.util.Collection;

public abstract class CCTimeBasedGameMode extends CCGameMode {

    protected double t;
    protected double gameLength;

    protected CCTimeBasedGameMode(CandidateCrush game, Stage stage, IBlockColorProvider blockColorProvider, Collection<BlockType> blockTypes, double gameLength) {
        super(game, stage, blockColorProvider, blockTypes);
        this.gameLength = gameLength;
        this.t = 0.0;
    }

    public void resetTime() {
        this.t = 0.0;
    }

    public double getGameTimeElapsed() {
        return t;
    }

    public double getTimeLeft() {
        return gameLength - t;
    }

    public boolean isGameTimeUp() {
        return t >= gameLength;
    }

    public void advanceGameTime(float dt) {
        t += dt;
    }

}
