package com.gmail.enzocampanella98.candidatecrush.board;

import com.badlogic.gdx.utils.Array;

import java.util.HashSet;
import java.util.Set;

public class GoodBoardAnalyzer implements IBoardAnalyzer {
    @Override
    public Array<SimpleBlockGroup> analyzeBoard(Board board) {
        return analyzeBoard(board.getBlocks());
    }

    @Override
    public Array<SimpleBlockGroup> analyzeBoard(Block[][] blocks) {
        int minCrushLen = 3;
        Array<SimpleBlockGroup> groups = new Array<>();

        for (Block[] row : blocks) {
            for (Block b : row) { // clear
                b.visited = false;
                b.blockGroup = null;
            }
        }

        Block b;
        SimpleBlockGroup curGroup;
        for (int r = 0; r < blocks.length; ++r) {
            curGroup = new SimpleBlockGroup();
            for (int c = 0; c < blocks[r].length; ++c) {
                b = blocks[r][c];
                boolean typeMatch = curGroup.typeMatch(b);
                if (typeMatch) curGroup.addBlock(b);

                if (!typeMatch || c == blocks[r].length - 1) {
                    // terminate block group

                    // 1. determine if it is a crush
                    if (curGroup.size() >= minCrushLen) {
                        // 2. determine whether to look for an l-shape
                        if (curGroup.size() < 5) { // we don't look for l-shape for horizontal sizes >= 5
                            // 3. look for l-shape
                            Block b1, b2;
                            for (int curCol = curGroup.minCol; curCol <= curGroup.maxCol; ++curCol) {
                                if ((r > 1
                                        && curGroup.typeMatch((b1 = blocks[r - 1][curCol]))
                                        && curGroup.typeMatch((b2 = blocks[r - 2][curCol])))
                                        ||
                                        (r < blocks.length - 2
                                                && curGroup.typeMatch((b1 = blocks[r + 1][curCol]))
                                                && curGroup.typeMatch((b2 = blocks[r + 2][curCol])))
                                        ||
                                        (r > 0 && r < blocks.length - 1
                                                && curGroup.typeMatch((b1 = blocks[r + 1][curCol]))
                                                && curGroup.typeMatch((b2 = blocks[r - 1][curCol])))) {
                                    if (b1.blockGroup != null || b2.blockGroup != null) continue;
                                    if (curGroup.size() == 4) {
                                        if (curCol == curGroup.minCol || curCol == curGroup.minCol + 1) {
                                            curGroup.remove(blocks[r][curGroup.maxCol]);
                                        } else {
                                            curGroup.remove(blocks[r][curGroup.minCol]);
                                        }
                                    }
                                    curGroup.addBlock(b1);
                                    curGroup.addBlock(b2);
                                    break;
                                }
                            }
                        }
                        for (Block block : curGroup) block.blockGroup = curGroup;
                        groups.add(curGroup);
                    }
                    curGroup = new SimpleBlockGroup();
                    if (!typeMatch) curGroup.addBlock(b);
                }
            }
        }
        for (int c = 0; c < blocks[0].length; ++c) {
            curGroup = new SimpleBlockGroup();
            for (int r = 0; r < blocks.length; ++r) {
                b = blocks[r][c];
                boolean typeMatch = curGroup.typeMatch(b);
                if (typeMatch) {
                    curGroup.addBlock(b);
                }

                if (!typeMatch || r == blocks.length - 1) {
                    // terminate current group

                    // 1. determine if it is a crush
                    if (curGroup.size() >= minCrushLen) {
                        // 2. determine if there are any horizontal groups attached
                        Set<SimpleBlockGroup> attachedGroups = new HashSet<SimpleBlockGroup>();
                        for (Block b1 : curGroup) {
                            if (b1.blockGroup != null) {
                                attachedGroups.add(b1.blockGroup);
                            }
                        }
                        boolean shouldAddVerticalGroup = true;
                        if (attachedGroups.size() > 0) {
                            int fiveHorizontalCount = 0, lShapeCount = 0;
                            for (SimpleBlockGroup g : attachedGroups) {
                                if (g.isLShape()) ++lShapeCount;
                                else ++fiveHorizontalCount;
                            }
                            if (fiveHorizontalCount > 0) {
                                // keep all horizontal fives and remove all l-shapes
                                for (SimpleBlockGroup g : attachedGroups) {
                                    if (g.isLShape()) {
                                        groups.removeValue(g, true);
                                        for (Block block : g) block.blockGroup = null;
                                    }
                                }
                                shouldAddVerticalGroup = false;
                            } else if (curGroup.size() < 5) {
                                // keep first l-shape we see and remove everything else
                                SimpleBlockGroup toKeep = null;
                                for (SimpleBlockGroup g : attachedGroups) {
                                    if (toKeep == null) toKeep = g;
                                    else {
                                        groups.removeValue(g, true);
                                        for (Block block : g) block.blockGroup = null;
                                    }
                                }
                                shouldAddVerticalGroup = false;
                            } else {
                                shouldAddVerticalGroup = true;
                                // remove all attached groups
                                for (SimpleBlockGroup g : attachedGroups) {
                                    groups.removeValue(g, true);
                                    for (Block block : g) block.blockGroup = null;
                                }
                            }
                        }
                        if (shouldAddVerticalGroup) {
                            groups.add(curGroup);
                            for (Block block : curGroup) block.blockGroup = curGroup;
                        }
                    }
                    curGroup = new SimpleBlockGroup();
                    if (!typeMatch) curGroup.addBlock(b);
                }
            }
        }
        return groups;
    }
}
