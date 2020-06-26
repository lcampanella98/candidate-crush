package com.gmail.enzocampanella98.candidatecrush.board.blockConfig;

import com.gmail.enzocampanella98.candidatecrush.board.Block;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MinimalChainReactionBlockTypeProvider implements IBlockTypeProvider {

    protected Map<BlockType, Double> blockTypeFrequencies;
    protected Random rand = new Random();
    
    public MinimalChainReactionBlockTypeProvider(Map<BlockType, Double> blockTypeFrequencies) {
        this.blockTypeFrequencies = blockTypeFrequencies;
    }
    
    @Override
    public BlockType provide(Block[][] board, int row, int col) {
        Set<BlockType> available = new HashSet<>(blockTypeFrequencies.keySet());
        List<BlockType> blockTypePriorityList = new ArrayList<>();
        while (available.size() > 0) {
            double num = rand.nextDouble();
            for (BlockType avail : available) {
                double freq = blockTypeFrequencies.get(avail);
                if (num <= freq) {
                    blockTypePriorityList.add(avail);
                    available.remove(avail);
                    break;
                }
                num -= freq;
            }
        }
        // initialize the list of blockTypes

        // set minX, minY, minT = inf, inf, null
        // for each possible block type T to choose from:
        // 1. look north/south and count how many consecutive blocks of type T there would be
        //      if we chose T. Let that be X
        // 2. look east/west and count number of consecutive blocks of type T there would be
        //      if we chose T. Let that be Y
        // 3. if X < minX and Y < minY set mins to be current values
        //    if X >= minX and Y >= minY continue
        //    numCrushesCur = (X < 3 ? 0 : 1) + (Y < 3 ? 0 : 1)
        //    numCrushesMin = (minX < 3 ? 0 : 1) + (minY < 3 ? 0 : 1)
        //    if numCrushesCur < numCrushesMin set mins to be current values
        //    if numCrushesCur == numCrushesMin && X + Y < minX + minY set mins to be current values

        // return minT

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        BlockType minT = null;

        for (BlockType T : blockTypePriorityList) {
            int X = 1, Y = 1;
            int i, j;
            Block C;

            // look up
            i = row + 1;
            while (true) {
                if (i < board.length && ((C = board[i][col]) != null && C.getBlockType() == T)) {
                    ++Y;
                    ++i;
                } else break;
            }
            // look down
            i = row - 1;
            while (true) {
                if (i >= 0 && ((C = board[i][col]) != null && C.getBlockType() == T)) {
                    ++Y;
                    --i;
                } else break;
            }
            // look right
            j = col + 1;
            while (true) {
                if (j < board[0].length && ((C = board[row][j]) != null && C.getBlockType() == T)) {
                    ++X;
                    ++j;
                } else break;
            }
            // look left
            j = col - 1;
            while (true) {
                if (j >= 0 && ((C = board[row][j]) != null && C.getBlockType() == T)) {
                    ++X;
                    --j;
                } else break;
            }
            // 3.
            //    if X >= minX and Y >= minY continue
            //    numCrushesCur = (X < 3 ? 0 : 1) + (Y < 3 ? 0 : 1)
            //    numCrushesMin = (minX < 3 ? 0 : 1) + (minY < 3 ? 0 : 1)
            //    if X < minX and Y < minY set mins to be current values
            //    if numCrushesCur < numCrushesMin set mins to be current values
            //    if numCrushesCur == numCrushesMin && X + Y < minX + minY set mins to be current values

            if (X >= minX && Y >= minY) continue; // no use continuing

            int numCrushesCur = (X < 3 ? 0 : 1) + (Y < 3 ? 0 : 1);
            int numCrushesMin = (minX < 3 ? 0 : 1) + (minY < 3 ? 0 : 1);

            if ((X < minX && Y < minY)
                    || (numCrushesCur < numCrushesMin)
                    || (numCrushesCur == numCrushesMin && X + Y < minX + minY)) {
                minX = X;
                minY = Y;
                minT = T;
            }
        }

        return minT;
    }
}
