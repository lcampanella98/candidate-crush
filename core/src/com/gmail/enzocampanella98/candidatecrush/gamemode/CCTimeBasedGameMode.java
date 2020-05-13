package com.gmail.enzocampanella98.candidatecrush.gamemode;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.gamemode.config.GameModeConfig;
import com.gmail.enzocampanella98.candidatecrush.level.ILevelSet;

public abstract class CCTimeBasedGameMode extends CCGameMode {

    protected double t;

    protected CCTimeBasedGameMode(
            CandidateCrush game,
            Stage stage,
            GameModeConfig config, ILevelSet levelSet) {
        super(game, stage, config, levelSet);
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

    @Override
    public void update(float dt) {
        super.update(dt);
        if (isGameStarted() && !isGameOver()) {
            t += dt; // advance game time
        }
    }
}
