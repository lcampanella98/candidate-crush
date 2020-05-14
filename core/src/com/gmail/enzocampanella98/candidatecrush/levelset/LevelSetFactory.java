package com.gmail.enzocampanella98.candidatecrush.levelset;

import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.gamemode.GameModeFactory;
import com.gmail.enzocampanella98.candidatecrush.level.ILevelSet;

public abstract class LevelSetFactory {

    protected CandidateCrush game;

    public LevelSetFactory(CandidateCrush game) {
        this.game = game;
    }

    public abstract ILevelSet getLevelSet();
    public abstract GameModeFactory getGameModeFactory();
}
