package com.gmail.enzocampanella98.candidatecrush.level;

import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

import java.util.Collection;
import java.util.List;

public interface ILevelSet {
    String LS_NORMAL = "normal";
    String LS_OAK = "oakbaes";
    
    List<Integer> getTierIncreaseLevels();
    Level getLevel(int levelNum);
    int getNumLevels();
    Collection<String> getUnlockMessages(int levelBeaten, boolean inHardMode);
    Collection<BlockType> getUnlockedCandidates(int levelNum);
    String getName();
    int getNumSoundTiers();
}
