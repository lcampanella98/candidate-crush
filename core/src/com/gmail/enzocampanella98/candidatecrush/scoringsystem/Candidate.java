package com.gmail.enzocampanella98.candidatecrush.scoringsystem;

import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

public class Candidate implements Comparable<Candidate> {
    public BlockType type;
    public int score;

    public Candidate(BlockType type) {
        this.type = type;
        score = 0;
    }
    @Override
    public int compareTo(Candidate candidate) {
        if (score < candidate.score) return -1;
        if (score > candidate.score) return 1;
        return 0;
    }
}