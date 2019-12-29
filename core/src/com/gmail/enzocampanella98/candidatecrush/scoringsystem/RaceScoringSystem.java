package com.gmail.enzocampanella98.candidatecrush.scoringsystem;

import com.gmail.enzocampanella98.candidatecrush.board.Crush;
import com.gmail.enzocampanella98.candidatecrush.board.SimpleBlockGroup;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

import java.util.Collections;
import java.util.List;

import static com.gmail.enzocampanella98.candidatecrush.scoringsystem.NamedCandidateGroup.firstGroupWithType;

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

    public NamedCandidateGroup getPlayerCandidateGroup() {
        return playerGroup;
    }

    public List<NamedCandidateGroup> getGroups() {
        return groups;
    }

    @Override
    public void updateScore(Crush crush) {
        assert crush != null && crush.crushedBlocks != null;
        for (SimpleBlockGroup bg : crush.crushedBlocks) {
            BlockType blockType = bg.getType();
            NamedCandidateGroup to = firstGroupWithType(blockType, groups);
            assert to != null;
            to.score += getBlockGroupValue(bg, crush.wasUserInvoked);
        }

        Collections.sort(groups);
        Collections.reverse(groups);
    }

    @Override
    public void reset() {
        for (NamedCandidateGroup c : groups) {
            c.score = 0;
        }
    }

    @Override
    public int getBlockGroupValue(SimpleBlockGroup bg, boolean userInvoked) {
        BlockType blockType = bg.getType();
        char crushType = getCrushType(bg);
        NamedCandidateGroup toGroup = firstGroupWithType(blockType, groups);
        assert toGroup != null;

        int score = crushValues.get(crushType);
        if (playerGroup.containsCandidate(blockType) && !userInvoked) {
            score *= 0.8;
        }

        if (playerGroup.score == 0) {
            return score;
        }
        int diff = Math.abs(playerGroup.score - toGroup.score);
        double diffProportion = ((double)diff) / playerGroup.score;
        double newScore = (1 + diffProportion) * score;
        newScore = 10.0 * Math.floor(newScore / 10.0);
        return (int) newScore;
    }

    @Override
    public int getPlayerScore() {
        return playerGroup.score;
    }
}
