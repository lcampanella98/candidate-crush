package com.gmail.enzocampanella98.candidatecrush.board;


import com.badlogic.gdx.utils.Array;

public class Crush {
    public Array<SimpleBlockGroup> crushedBlocks;
    public boolean wasUserInvoked;

    public Crush(Array<SimpleBlockGroup> crushedBlocks, boolean wasUserInvoked) {
        this.crushedBlocks = crushedBlocks;
        this.wasUserInvoked = wasUserInvoked;
    }
}
