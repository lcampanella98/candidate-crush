package com.gmail.enzocampanella98.candidatecrush.gamemode.gameModeFactory;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.gamemode.GameModeFactory;
import com.gmail.enzocampanella98.candidatecrush.gamemode.MoveLimitVoteTargetGameMode;
import com.gmail.enzocampanella98.candidatecrush.gamemode.RaceGameMode;
import com.gmail.enzocampanella98.candidatecrush.gamemode.TimedSoundByteTargetGameMode;
import com.gmail.enzocampanella98.candidatecrush.gamemode.config.GameModeConfig;
import com.gmail.enzocampanella98.candidatecrush.level.ILevelSet;

public class OakBaesGameModeFactory extends GameModeFactory {

    public OakBaesGameModeFactory(CandidateCrush game) {
        super(game);
    }

    @Override
    public RaceGameMode getPrimaryGameMode(Stage stage, GameModeConfig config, ILevelSet levelSet) {
        return null;
    }

    @Override
    public TimedSoundByteTargetGameMode getTimedSoundByteTargetGameMode(Stage stage, GameModeConfig config, ILevelSet levelSet) {
        return null;
    }

    @Override
    public MoveLimitVoteTargetGameMode getMoveLimitVoteTargetGameMode(Stage stage, GameModeConfig config, ILevelSet levelSet) {
        return null;
    }

    @Override
    public RaceGameMode getElectionGameMode(Stage stage, GameModeConfig config, ILevelSet levelSet) {
        return null;
    }

}
