package com.gmail.enzocampanella98.candidatecrush.scoringsystem;

import com.badlogic.gdx.utils.Array;
import com.gmail.enzocampanella98.candidatecrush.board.SimpleBlockGroup;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

import java.util.Collections;
import java.util.List;

/**
 * Created by enzoc on 5/22/2018.
 */

public class RaceScoringSystem extends ScoringSystem {

    private List<NamedCandidateGroup> groups;
    private NamedCandidateGroup playerGroup;

    public RaceScoringSystem(
            List<NamedCandidateGroup> groups,
            NamedCandidateGroup playerGroup,
            int crushVal3, int crushVal4, int crushVal5, int crushValTShape) {
        super(crushVal3, crushVal4, crushVal5, crushValTShape);
        this.groups = groups;
        this.playerGroup = playerGroup;
    }

    public void resetScores() {
        for (NamedCandidateGroup c : groups) {
            c.score = 0;
        }
    }

    public NamedCandidateGroup getPlayerCandidateGroup() {
        return playerGroup;
    }

    public List<NamedCandidateGroup> getGroups() {
        return groups;
    }

    @Override
    public void updateScore(Array<SimpleBlockGroup> crushedBlocks, boolean wasUserInvoked) {
        for (SimpleBlockGroup crushedGroup : crushedBlocks) {
            BlockType blockType = crushedGroup.getType();
            char crushType = getCrushType(crushedGroup);
            int val = getCrushValue(crushType);
            if (playerGroup.containsCandidate(blockType) && !wasUserInvoked) {
                val *= 0.8;
            }
            for (NamedCandidateGroup c : groups) {
                if (c.containsCandidate(blockType)) {
                    if (playerGroup.score == 0) {
                        c.score += val;
                    } else {
                        int diff = playerGroup.score - c.score;
                        double diffProportion = ((double)diff) / playerGroup.score;
                        double newVal = (1+diffProportion) * val;
                        newVal = (((int)newVal) / 10) * 10.0;
                        c.score += (int)newVal;
                    }
                }
            }
        }
        Collections.sort(groups);
        Collections.reverse(groups);
    }

    @Override
    public int getPlayerScore() {
        return playerGroup.score;
    }
}
