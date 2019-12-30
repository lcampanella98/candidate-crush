package com.gmail.enzocampanella98.candidatecrush.level;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gmail.enzocampanella98.candidatecrush.gamemode.CCGameMode;
import com.gmail.enzocampanella98.candidatecrush.gamemode.config.GameModeConfig;

public abstract class Level {

    public GameModeConfig config;
    public Stage stage;

    private boolean isElection;
    private boolean isPrimary;
    private int levelNumber;

    Level(GameModeConfig config, Stage stage,
                 boolean isElection, boolean isPrimary, int levelNumber) {
        this.config = config;
        this.stage = stage;
        this.isElection = isElection;
        this.isPrimary = isPrimary;
        this.levelNumber = levelNumber;
    }

    public abstract CCGameMode getGameMode();

    public boolean isElection() {
        return isElection;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public int getLevelNumber() {
        return levelNumber;
    }
}
