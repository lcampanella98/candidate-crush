package com.gmail.enzocampanella98.candidatecrush.level;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gmail.enzocampanella98.candidatecrush.gamemode.CCGameMode;
import com.gmail.enzocampanella98.candidatecrush.gamemode.config.GameModeConfig;

public class Level {

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

    public boolean isElection() {
        return isElection;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("Level %d\n", levelNumber));
        s.append(String.format("\tGame Mode: %s\n", config.gameModeType));
        switch (config.gameModeType) {
            case SOUND_BYTE:
                s.append(String.format("\tTime limit: %.1f sec\n", config.gameLength));
                s.append(String.format("\tNum Sound Bytes: %d\n", config.targetNumSoundBytes));
                s.append(String.format("\tFreq: %f\n", config.soundByteFrequency));
                break;
            case MOVE_LIMIT:
                s.append(String.format("\tNum Moves: %d\n", config.numMoves));
                s.append(String.format("\tTarget Score: %d\n", config.targetScore));
                break;
            case PRIMARY:
                s.append(String.format("\tNum Moves: %d\n", config.numMoves));
                break;
            case ELECTION:
                s.append(String.format("\tNum Moves: %d\n", config.numMoves));
                break;
        }
        s.append("\n");
        return s.toString();
    }
}
