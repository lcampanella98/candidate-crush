package com.gmail.enzocampanella98.candidatecrush.level;

import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.gmail.enzocampanella98.candidatecrush.board.BlockType.CAMPANELLA;
import static com.gmail.enzocampanella98.candidatecrush.board.BlockType.GHATTAS;
import static com.gmail.enzocampanella98.candidatecrush.board.BlockType.KOZAN;
import static com.gmail.enzocampanella98.candidatecrush.board.BlockType.MEZA;
import static com.gmail.enzocampanella98.candidatecrush.level.GameModeType.MOVE_LIMIT;

public class OakBaesLevelSet implements ILevelSet {

    private static final int NUM_LEVELS = 14;

    @Override
    public List<Integer> getTierIncreaseLevels() {
        return new ArrayList<>();
    }

    @Override
    public Level getLevel(int levelNum) {
        int soundTier = 1;
        LevelBuilder builder = new LevelBuilder(LS_OAK, levelNum, soundTier);



        builder.gameModeType(MOVE_LIMIT)
                .difficulty(1)
                .withCandidates(Arrays.asList(CAMPANELLA, MEZA, GHATTAS, KOZAN))
                .initialGameParameter(20);

        return builder.build();
    }

    @Override
    public int getNumLevels() {
        return NUM_LEVELS;
    }

    @Override
    public String getName() {
        return LS_OAK;
    }

    @Override
    public int getNumSoundTiers() {
        return 1;
    }

    @Override
    public Collection<String> getUnlockMessages(int levelBeaten, boolean inHardMode) {
        List<String> lines = new ArrayList<>();
        if (levelBeaten == NUM_LEVELS) {
            if (inHardMode) {
                lines.addAll(Arrays.asList(
                        "Congratulations!",
                        "You've beaten the oak baes levels in hard mode",
                        "You're a real oak bae!"
                ));
            } else {
                lines.addAll(Arrays.asList(
                        "Congratulations!",
                        "You've beaten the oak baes levels!",
                        "Hard mode unlocked"
                ));
            }
        }
        else {
            lines.add("Level " + (levelBeaten+1) + " unlocked!");
        }
        return lines;
    }

    @Override
    public Collection<BlockType> getUnlockedCandidates(int levelNum) {
        return Arrays.asList(MEZA);
    }
}
