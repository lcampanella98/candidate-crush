package com.gmail.enzocampanella98.candidatecrush.levelset;

import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.gamemode.GameModeFactory;
import com.gmail.enzocampanella98.candidatecrush.gamemode.gameModeFactory.OakBaesGameModeFactory;
import com.gmail.enzocampanella98.candidatecrush.level.ILevelSet;
import com.gmail.enzocampanella98.candidatecrush.level.OakBaesLevelSet;

public class OakBaesLevelSetFactory extends LevelSetFactory {

    public OakBaesLevelSetFactory(CandidateCrush game) {
        super(game);
    }

    @Override
    public ILevelSet getLevelSet() {
        return new OakBaesLevelSet();
    }

    @Override
    public GameModeFactory getGameModeFactory() {
        return new OakBaesGameModeFactory(game);
    }
}
