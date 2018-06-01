package com.gmail.enzocampanella98.candidatecrush.board;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class SimpleBlockGroup implements Iterable<Block>{

    private Set<Block> blocks;
    private BlockType type;

    int minRow, minCol, maxRow, maxCol;

    public SimpleBlockGroup() {
        blocks = new HashSet<Block>();
        initRanges();
    }

    public boolean addBlock(Block b)  { // returns whether add was successful. fails for type mismatch
        if (type == null) type = b.getBlockType();
        else if (type != b.getBlockType()) return false;
        blocks.add(b);
        setRanges(b);
        return true;
    }

    public boolean typeMatch(Block b) {
        return type == null || type == b.getBlockType();
    }

    private void initRanges() {
        minCol = minRow = Integer.MAX_VALUE;
        maxCol = maxRow = Integer.MIN_VALUE;
    }

    private void setRanges(Block newBlock) {
        int tmp;
        if ((tmp = newBlock.getRow()) < minRow) {
            minRow = tmp;
        }
        if (tmp > maxRow) {
            maxRow = tmp;
        }
        if ((tmp = newBlock.getCol()) < minCol) {
            minCol = tmp;
        }
        if (tmp > maxCol) {
            maxCol = tmp;
        }
    }

    private void setRanges() {
        initRanges();
        int tmp;
        for (Block b : blocks) {
            if ((tmp = b.getRow()) < minRow) {
                minRow = tmp;
            }
            if (tmp > maxRow) {
                maxRow = tmp;
            }
            if ((tmp = b.getCol()) < minCol) {
                minCol = tmp;
            }
            if (tmp > maxCol) {
                maxCol = tmp;
            }
        }
    }

    public BlockType getType() {
        return type;
    }

    public boolean isLShape() {
        return maxRow - minRow > 1 && maxCol - minCol > 1;
    }

    public boolean containsBlock(Block b) {
        return blocks.contains(b);
    }

    public void addAllFrom(SimpleBlockGroup group) {
        blocks.addAll(group.blocks);
        setRanges();
    }

    public void remove(Block b) {
        if (blocks.remove(b)) {
            setRanges();
        }
    }

    public int size() {
        return blocks.size();
    }

    @Override
    public Iterator<Block> iterator() {
        return blocks.iterator();
    }
}
