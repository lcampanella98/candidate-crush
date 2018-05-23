package com.gmail.enzocampanella98.candidatecrush.scoringsystem;


import com.badlogic.gdx.utils.Array;
import com.gmail.enzocampanella98.candidatecrush.board.BlockGroup;

import java.util.HashMap;

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

    private HashMap<Character, Integer> crushValues;

    public ScoringSystem(int crushVal3, int crushVal4, int crushVal5, int crushValTShape) {
        crushValues = new HashMap<Character, Integer>();
        crushValues.put(CRUSHTYPE_THREE, crushVal3);
        crushValues.put(CRUSHTYPE_FOUR, crushVal4);
        crushValues.put(CRUSHTYPE_FIVE, crushVal5);
        crushValues.put(CRUSHTYPE_T_SHAPE, crushValTShape);
    }

    public abstract void updateScore(Array<BlockGroup> crushedBlocks, boolean wasUserInvoked);

    public int getCrushValue(char crushType) {
        return crushValues.get(crushType);
    }

    public void setCrushValue(char crushType, int value) {
        crushValues.put(crushType, value);
    }

    public static char getCrushType(BlockGroup blockGroup) {
        int nBlocks = blockGroup.getNumBlocks();
        if (nBlocks >= 5) return CRUSHTYPE_FIVE;
        if (blockGroup.isJoinedGroup()) return CRUSHTYPE_T_SHAPE;
        if (nBlocks == 4) return CRUSHTYPE_FOUR;
        return CRUSHTYPE_THREE;
    }

}
