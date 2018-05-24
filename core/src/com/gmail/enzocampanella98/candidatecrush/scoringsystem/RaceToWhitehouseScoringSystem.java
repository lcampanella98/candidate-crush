package com.gmail.enzocampanella98.candidatecrush.scoringsystem;

import com.badlogic.gdx.utils.Array;
import com.gmail.enzocampanella98.candidatecrush.board.BlockGroup;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by enzoc on 5/22/2018.
 */

public class RaceToWhitehouseScoringSystem extends ScoringSystem {

    private List<Candidate> scores;
    private BlockType userBlockType;

    public RaceToWhitehouseScoringSystem(BlockType userBlockType, List<BlockType> blockTypes, int crushVal3, int crushVal4, int crushVal5, int crushValTShape) {
        super(crushVal3, crushVal4, crushVal5, crushValTShape);
        this.userBlockType = userBlockType;

        scores = new ArrayList<Candidate>();
        for (BlockType b : blockTypes) {
            scores.add(new Candidate(b));
        }
    }

    public void resetScores() {
        for (Candidate c : scores) {
            c.score = 0;
        }
    }

    public List<Candidate> getScores() {
        return this.scores;
    }

    @Override
    public void updateScore(Array<BlockGroup> crushedBlocks, boolean wasUserInvoked) {
        for (BlockGroup crushedGroup : crushedBlocks) {
            BlockType blockType = crushedGroup.getGroupBlockType();
            char crushType = getCrushType(crushedGroup);
            int val = getCrushValue(crushType);
            if (blockType == userBlockType && !wasUserInvoked) {
                val /= 2;
            }
            for (Candidate c : scores) {
                if (c.type == blockType)
                    c.score += val;
            }
        }
        Collections.sort(scores);
    }
}
