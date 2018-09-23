package com.gmail.enzocampanella98.candidatecrush.board;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Lorenzo Campanella on 6/14/2016.
 */
public class BlockGroup {

    private Array<Block> group;
    private int[] rowRange, colRange;
    private int numCols;

    public BlockGroup(Array<Block> group, int numCols) {
        setGroup(group, numCols);
    }

    public BlockGroup(int numCols) {
        this.numCols = numCols;
        this.group = new Array<Block>();
        rowRange = new int[]{~0, 0};
        colRange = new int[]{~0, 0};
    }

    public BlockType getGroupBlockType() {
        if (group.size > 0) {
            return group.first().getBlockType();
        } else return null;
    }

    private void setRanges() {
        for (Block b : group) {
            if (b.getRow() < rowRange[0]) {
                rowRange[0] = b.getRow();
            } else if (b.getRow() > rowRange[1]) {
                rowRange[1] = b.getRow();
            }
            if (b.getCol() < colRange[0]) {
                colRange[0] = b.getCol();
            } else if (b.getCol() > colRange[1]) {
                colRange[1] = b.getCol();
            }
        }
    }

    public int getNumBlocks() {
        return group.size;
    }

    public boolean isJoinedGroup() {
        return rowRange[1] - rowRange[0] > 1 && colRange[1] - colRange[0] > 1;
    }

    public void setGroup(Array<Block> group, int numCols) {
        this.numCols = numCols;
        this.group = new Array<Block>(group);
        rowRange = new int[]{Integer.MAX_VALUE, 0};
        colRange = new int[]{Integer.MAX_VALUE, 0};
        setRanges();
    }

    public Array<Block> getGroup() {
        return group;
    }

    private static boolean areRangesOverlapping(int[] r1, int[] r2) {
        return (r1[0] <= r2[0] && r2[0] <= r1[1])
                || (r1[0] <= r2[1] && r2[1] <= r1[1])
                || (r1[0] >= r2[0] && r1[1] <= r2[1]);
    }

    @Override
    public String toString() {
        return group.toString();
    }

    public static BlockGroup getMergedGroup(BlockGroup group1, BlockGroup group2) {

        ObjectSet<Block> merged = new ObjectSet<Block>();
        merged.addAll(group1.group);
        merged.addAll(group2.group);

        if (merged.size < group1.getNumBlocks() + group2.getNumBlocks())
            return new BlockGroup(merged.iterator().toArray(),
                    Math.max(group1.colRange[1], group2.colRange[1])
                            - Math.min(group1.colRange[0], group2.colRange[0]));
        return null;
    }
}
