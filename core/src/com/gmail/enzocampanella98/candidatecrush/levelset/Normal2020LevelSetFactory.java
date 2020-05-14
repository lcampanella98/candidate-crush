package com.gmail.enzocampanella98.candidatecrush.levelset;

import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.gamemode.GameModeFactory;
import com.gmail.enzocampanella98.candidatecrush.gamemode.gameModeFactory.Normal2020GameModeFactory;
import com.gmail.enzocampanella98.candidatecrush.level.ILevelSet;
import com.gmail.enzocampanella98.candidatecrush.level.NormalLevelSet;

public class Normal2020LevelSetFactory extends LevelSetFactory {

    public Normal2020LevelSetFactory(CandidateCrush game) {
        super(game);
    }

    @Override
    public ILevelSet getLevelSet() {
        return new NormalLevelSet();
    }

    @Override
    public GameModeFactory getGameModeFactory() {
        return new Normal2020GameModeFactory(game);
    }
}
