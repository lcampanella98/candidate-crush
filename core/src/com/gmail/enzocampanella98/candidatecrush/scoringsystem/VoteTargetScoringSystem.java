package com.gmail.enzocampanella98.candidatecrush.scoringsystem;

import com.badlogic.gdx.utils.Array;
import com.gmail.enzocampanella98.candidatecrush.board.SimpleBlockGroup;


public class VoteTargetScoringSystem extends ScoringSystem {

    private int userScore;
    private double nonUserInvokedScale;

    public VoteTargetScoringSystem(int crushVal3, int crushVal4, int crushVal5, int crushValTShape, double nonUserInvokedScale) {
        super(crushVal3, crushVal4, crushVal5, crushValTShape);

        this.userScore = 0;
        this.nonUserInvokedScale = nonUserInvokedScale;
    }

    public VoteTargetScoringSystem(int crushVal3, int crushVal4, int crushVal5, int crushValTShape) {
        this(crushVal3, crushVal4, crushVal5, crushValTShape, 1.0);
    }

    @Override
    public void updateScore(Array<SimpleBlockGroup> crushedBlocks, boolean wasUserInvoked) {
        if (crushedBlocks == null) return;
        int curScore = 0;
        for (SimpleBlockGroup bg : crushedBlocks) {
            int numBlocks = bg.size();

            if (bg.isLShape()) curScore += getCrushValue(CRUSHTYPE_T_SHAPE);
            else if (numBlocks >= 5) curScore += getCrushValue(CRUSHTYPE_FIVE);
            else if (numBlocks == 4) curScore += getCrushValue(CRUSHTYPE_FOUR);
            else if (numBlocks == 3) curScore += getCrushValue(CRUSHTYPE_THREE);
        }
        if (!wasUserInvoked) {
            curScore = (int) Math.round(curScore * nonUserInvokedScale);
        }

        this.userScore += curScore;
    }

    public void setUserScore(int score) {
        this.userScore = score;
    }

    public int getUserScore() {
        return userScore;
    }
}
