package com.gmail.enzocampanella98.candidatecrush.board;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Lorenzo Campanella on 6/21/2016.
 */
public class BoardHandler {

    private Board board;
    private int score3, score4, score5, scoreJoined, totalScore;
    private Array<BlockGroup> blockGroups;

    BoardHandler(Board board, int score3, int score4, int score5, int scoreJoined) {
        this.board = board;
        this.score3 = score3;
        this.score4 = score4;
        this.score5 = score5;
        this.scoreJoined = scoreJoined;
        blockGroups = board.getBlockGroups();
        totalScore = 0;
    }

    public void resetScore() {
        totalScore = 0;
    }

    public void setScore(int score) {
        totalScore = score;
    }

    public int getScore() {
        return totalScore;
    }

    private void setUpdatedScore() {
        if (board.getBlockGroups() != blockGroups) {
            blockGroups = board.getBlockGroups();
        } else {
            return;
        }
        if (blockGroups == null) return;
        for (BlockGroup group : blockGroups) {
            int curScore = 0;
            switch (group.getNumBlocks()) {
                case 3:
                    curScore = score3;
                    break;
                case 4:
                    curScore = score4;
                    break;
                default:
                    curScore = group.isJoinedGroup() ? scoreJoined : score5;
                    break;
            }
            totalScore += curScore;
        }
    }

    public void update(float dt) {
        setUpdatedScore();
    }

    public Board getBoard() {
        return board;
    }
}
