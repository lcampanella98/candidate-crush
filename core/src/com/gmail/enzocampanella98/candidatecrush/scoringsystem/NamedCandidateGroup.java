package com.gmail.enzocampanella98.candidatecrush.scoringsystem;

import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class NamedCandidateGroup implements Comparable<NamedCandidateGroup> {
    private Set<BlockType> candidates;
    private String name;
    private String longName;
    public int score;

    public NamedCandidateGroup(Collection<BlockType> candidates, String name, String longName) {
        this.candidates = new HashSet<>(candidates);
        this.name = name;
        this.longName = longName;
        score = 0;
    }

    public String getLongName() {
        return longName;
    }

    public boolean containsCandidate(BlockType type) {
        return candidates.contains(type);
    }

    public Set<BlockType> getCandidates() {
        return candidates;
    }

    public void setCandidates(Collection<BlockType> candidates) {
        this.candidates = new HashSet<>(candidates);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(NamedCandidateGroup candidate) {
        if (score < candidate.score) return -1;
        if (score > candidate.score) return 1;
        return 0;
    }
}
