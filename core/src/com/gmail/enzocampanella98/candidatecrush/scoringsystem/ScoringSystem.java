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
    public static final Character CRUSHTYPE_THREE = '3';
    public static final Character CRUSHTYPE_FOUR = '4';
    public static final Character CRUSHTYPE_FIVE = '5';
    public static final Character CRUSHTYPE_T_SHAPE = 't';

    protected Map<Character, Integer> crushValues;
    protected int userScore;

    public ScoringSystem(int crushVal3, int crushVal4, int crushVal5, int crushValTShape) {
        crushValues = new HashMap<>();
        crushValues.put(CRUSHTYPE_THREE, crushVal3);
        crushValues.put(CRUSHTYPE_FOUR, crushVal4);
        crushValues.put(CRUSHTYPE_FIVE, crushVal5);
        crushValues.put(CRUSHTYPE_T_SHAPE, crushValTShape);
        userScore = 0;
    }

    public abstract void updateScore(Crush crush);

    public void setCrushValue(char crushType, int value) {
        crushValues.put(crushType, value);
    }

    public abstract int getBlockGroupValue(SimpleBlockGroup bg, boolean userInvoked);

    public static char getCrushType(SimpleBlockGroup blockGroup) {
        int nBlocks = blockGroup.size();
        if (blockGroup.isLShape()) return CRUSHTYPE_T_SHAPE;
        if (nBlocks >= 5) return CRUSHTYPE_FIVE;
        if (nBlocks == 4) return CRUSHTYPE_FOUR;
        return CRUSHTYPE_THREE;
    }

    public abstract int getPlayerScore();

}
