package com.gmail.enzocampanella98.candidatecrush.scoringsystem;

import com.gmail.enzocampanella98.candidatecrush.board.Crush;
import com.gmail.enzocampanella98.candidatecrush.board.SimpleBlockGroup;


public class VoteTargetScoringSystem extends ScoringSystem {

    private float nonUserInvokedScale;

    public VoteTargetScoringSystem(int crushVal3, int crushVal4, int crushVal5, int crushValTShape, float nonUserInvokedScale) {
        super(crushVal3, crushVal4, crushVal5, crushValTShape);
        this.nonUserInvokedScale = nonUserInvokedScale;
    }

    public VoteTargetScoringSystem(int crushVal3, int crushVal4, int crushVal5, int crushValTShape) {
        this(crushVal3, crushVal4, crushVal5, crushValTShape, 1.0f);
    }

    public int getBlockGroupValue(SimpleBlockGroup bg, boolean userInvoked) {
        return Math.round(crushValues.get(getCrushType(bg)) * (userInvoked ? 1f : nonUserInvokedScale));
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

    public void setPlayerScore(int score) {
        this.userScore = score;
    }

    @Override
    public int getPlayerScore() {
        return userScore;
    }
}
