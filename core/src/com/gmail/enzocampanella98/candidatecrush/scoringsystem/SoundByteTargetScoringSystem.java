package com.gmail.enzocampanella98.candidatecrush.scoringsystem;

import com.gmail.enzocampanella98.candidatecrush.board.Crush;
import com.gmail.enzocampanella98.candidatecrush.board.SimpleBlockGroup;

public class SoundByteTargetScoringSystem extends ScoringSystem {

    private int targetNumSoundBytes;
    private int curNumSoundBytes;

    public SoundByteTargetScoringSystem(CrushVals crushVals, int targetNumSoundBytes) {
        super(crushVals);
        this.targetNumSoundBytes = targetNumSoundBytes;
        curNumSoundBytes = 0;
    }

    @Override
    public void updateScore(Crush crush) {
        for (SimpleBlockGroup bg : crush.getCrushedBlocks()) {
            curNumSoundBytes += getBlockGroupValue(bg, crush.isWasUserInvoked());
        }
    }

    @Override
    public void reset() {
        curNumSoundBytes = 0;
    }

    @Override
    public int getBlockGroupValue(SimpleBlockGroup bg, boolean userInvoked) {
        return bg.numSoundByteBlocks();
    }

    @Override
    public int getPlayerScore() {
        return curNumSoundBytes;
    }
}
