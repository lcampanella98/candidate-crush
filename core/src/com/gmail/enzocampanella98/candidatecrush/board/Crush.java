package com.gmail.enzocampanella98.candidatecrush.board;


import com.badlogic.gdx.utils.Array;

public class Crush {
    private Array<SimpleBlockGroup> crushedBlocks;
    private SimpleBlockGroup largestGroup;
    private boolean wasUserInvoked;

    public Crush(Array<SimpleBlockGroup> crushedBlocks, SimpleBlockGroup largestGroup, boolean wasUserInvoked) {
        this.crushedBlocks = crushedBlocks;
        this.largestGroup = largestGroup;
        this.wasUserInvoked = wasUserInvoked;
    }

    public Array<SimpleBlockGroup> getCrushedBlocks() {
        return crushedBlocks;
    }

    public SimpleBlockGroup getLargestGroup() {
        return largestGroup;
    }

    public boolean isWasUserInvoked() {
        return wasUserInvoked;
    }
}
