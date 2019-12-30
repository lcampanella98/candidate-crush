package com.gmail.enzocampanella98.candidatecrush.scoringsystem;

import com.gmail.enzocampanella98.candidatecrush.board.Crush;
import com.gmail.enzocampanella98.candidatecrush.board.SimpleBlockGroup;


public class VoteTargetScoringSystem extends ScoringSystem {

    private int userScore;
    private final double nonUserInvokedCrushScale;

    public VoteTargetScoringSystem(CrushVals crushVals, double nonUserInvokedCrushScale) {
        super(crushVals);
        this.nonUserInvokedCrushScale = nonUserInvokedCrushScale;
        this.userScore = 0;
    }

    public int getBlockGroupValue(SimpleBlockGroup bg, boolean userInvoked) {
        return (int)Math.round(crushVals.get(bg.getCrushType()) * (userInvoked ? 1.0 : nonUserInvokedCrushScale));
    }

    public void setPlayerScore(int score) {
        this.userScore = score;
    }

    @Override
    public void updateScore(Crush crush) {
        assert crush != null && crush.crushedBlocks != null;
        for (SimpleBlockGroup bg : crush.crushedBlocks) {
            userScore += getBlockGroupValue(bg, crush.wasUserInvoked);
        }
    }

    @Override
    public void reset() {
        setPlayerScore(0);
    }

    @Override
    public int getPlayerScore() {
        return userScore;
    }
}
