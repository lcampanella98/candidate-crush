package com.gmail.enzocampanella98.candidatecrush.gamemode;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.IBlockColorProvider;
import com.gmail.enzocampanella98.candidatecrush.gamemode.config.GameModeConfig;

public abstract class CCTimeBasedGameMode extends CCGameMode {

    protected double t;

    protected CCTimeBasedGameMode(
            CandidateCrush game,
            Stage stage,
            IBlockColorProvider blockColorProvider,
            GameModeConfig config) {
        super(game, stage, blockColorProvider, config);
        this.t = 0.0;
    }

    @Override
    public void restartGame() {
        super.restartGame();
        resetTime();
    }

    public void resetTime() {
        this.t = 0.0;
    }

    public double getGameTimeElapsed() {
        return t;
    }

    public double getTimeLeft() {
        return config.gameLength - t;
    }

    public boolean isGameTimeUp() {
        return t >= config.gameLength;
    }

    public void advanceGameTime(float dt) {
        t += dt;
    }

}
