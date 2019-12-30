package com.gmail.enzocampanella98.candidatecrush.scoringsystem;


import com.gmail.enzocampanella98.candidatecrush.board.Crush;
import com.gmail.enzocampanella98.candidatecrush.board.SimpleBlockGroup;

import java.util.HashMap;
import java.util.Map;

public abstract class ScoringSystem {
    /*
    class to get the score for a given crush
    based on the number of blocks crushed and type of crush

    we want it to be able to take into account
        - type of crush (3, 4, 5 (>4), t-shape)
        - the behavior of non-user invoked crushes
            - scale points?
            - base off number of blocks alone?
        - the specific blockType that was crushed
            - scale points?
     */


    protected final CrushVals crushVals;

    public ScoringSystem(CrushVals crushVals) {
        this.crushVals = crushVals;
    }

    public abstract void updateScore(Crush crush);

    public abstract void reset();

    public abstract int getBlockGroupValue(SimpleBlockGroup bg, boolean userInvoked);

    public abstract int getPlayerScore();

}
