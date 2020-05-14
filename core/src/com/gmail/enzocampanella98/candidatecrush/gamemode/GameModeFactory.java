package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.gamemode.config.GameModeConfig;
import com.gmail.enzocampanella98.candidatecrush.level.ILevelSet;

public abstract class GameModeFactory {

    protected final CandidateCrush game;

    public GameModeFactory(CandidateCrush game) {
        this.game = game;
    }

    public CCGameMode getGameMode(Stage stage, GameModeConfig config, ILevelSet levelSet) {
        switch (config.gameModeType) {
            case SOUND_BYTE:
                return getTimedSoundByteTargetGameMode(stage, config, levelSet);
            case MOVE_LIMIT:
                return getMoveLimitVoteTargetGameMode(stage, config, levelSet);
            case PRIMARY:
                return getPrimaryGameMode(stage, config, levelSet);
            case ELECTION:
                return getElectionGameMode(stage, config, levelSet);
        }
        return null;
    }

    public abstract RaceGameMode getPrimaryGameMode(Stage stage,
                                                    GameModeConfig config,
                                                    ILevelSet levelSet);
    public abstract TimedSoundByteTargetGameMode getTimedSoundByteTargetGameMode(Stage stage,
                                                                                 GameModeConfig config,
                                                                                 ILevelSet levelSet);
    public abstract MoveLimitVoteTargetGameMode getMoveLimitVoteTargetGameMode(Stage stage,
                                                                      GameModeConfig config,
                                                                      ILevelSet levelSet);
    public abstract RaceGameMode getElectionGameMode(Stage stage,
                                                GameModeConfig config,
                                                ILevelSet levelSet);

}
