package com.gmail.enzocampanella98.candidatecrush.level;

import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.gmail.enzocampanella98.candidatecrush.board.BlockType.BIDEN;
import static com.gmail.enzocampanella98.candidatecrush.board.BlockType.BUTTIGIEG;
import static com.gmail.enzocampanella98.candidatecrush.board.BlockType.SANDERS;
import static com.gmail.enzocampanella98.candidatecrush.board.BlockType.TRUMP;
import static com.gmail.enzocampanella98.candidatecrush.board.BlockType.WARREN;
import static com.gmail.enzocampanella98.candidatecrush.level.GameModeType.ELECTION;
import static com.gmail.enzocampanella98.candidatecrush.level.GameModeType.MOVE_LIMIT;
import static com.gmail.enzocampanella98.candidatecrush.level.GameModeType.PRIMARY;
import static com.gmail.enzocampanella98.candidatecrush.level.GameModeType.SOUND_BYTE;

public class NormalLevelSet implements ILevelSet {

    private static final int NUM_LEVELS = 14;
    private static final int NUM_TIERS = 4;
    private static final List<Integer> increaseTierLevels = Arrays.asList(4, 8, 11);

    @Override
    public List<Integer> getTierIncreaseLevels() {
        return increaseTierLevels;
    }

    @Override
    public Level getLevel(int levelNum) {
        int soundTier = getSoundTierOfLevel(levelNum);
        LevelBuilder builder = new LevelBuilder(LS_NORMAL, levelNum, soundTier, TRUMP);
        switch (levelNum) {
            case 1:
                builder.gameModeType(SOUND_BYTE)
                        .difficulty(0)
                        .exceptCandidate(BUTTIGIEG)
                        .initialGameParameter(60.0);
                break;
            case 2:
                builder.gameModeType(MOVE_LIMIT)
                        .difficulty(0)
                        .exceptCandidate(BUTTIGIEG)
                        .initialGameParameter(20);
                break;
            case 3:
                builder.gameModeType(PRIMARY)
                        .difficulty(0)
                        .primaryCandidate(BUTTIGIEG);
                break;
            case 4:
                builder.gameModeType(SOUND_BYTE)
                        .difficulty(2)
                        .exceptCandidate(WARREN)
                        .initialGameParameter(60.0);
                break;
            case 5:
                builder.gameModeType(MOVE_LIMIT)
                        .difficulty(2)
                        .exceptCandidate(WARREN)
                        .initialGameParameter(25);
                break;
            case 6:
                builder.gameModeType(PRIMARY)
                        .difficulty(2)
                        .primaryCandidate(WARREN);
                break;
            case 7:
                builder.gameModeType(ELECTION)
                        .difficulty(2)
                        .playerParty('D');
                break;
            case 8:
                builder.gameModeType(SOUND_BYTE)
                        .difficulty(6)
                        .exceptCandidate(BIDEN)
                        .initialGameParameter(75.0);
                break;
            case 9:
                builder.gameModeType(MOVE_LIMIT)
                        .difficulty(6)
                        .exceptCandidate(BIDEN)
                        .initialGameParameter(25);
                break;
            case 10:
                builder.gameModeType(PRIMARY)
                        .difficulty(6)
                        .primaryCandidate(BIDEN);
                break;
            case 11:
                builder.gameModeType(SOUND_BYTE)
                        .difficulty(9)
                        .exceptCandidate(SANDERS)
                        .initialGameParameter(75.0);
                break;
            case 12:
                builder.gameModeType(MOVE_LIMIT)
                        .difficulty(9)
                        .exceptCandidate(SANDERS)
                        .initialGameParameter(20);
                break;
            case 13:
                builder.gameModeType(PRIMARY)
                        .difficulty(9)
                        .primaryCandidate(SANDERS);
                break;
            case 14:
                builder.gameModeType(ELECTION)
                        .difficulty(9)
                        .playerParty('R');
                break;
        }
        return builder.build();
    }

    @Override
    public int getNumLevels() {
        return NUM_LEVELS;
    }

    @Override
    public Collection<String> getUnlockMessages(int levelBeaten, boolean inHardMode) {
        List<String> lines = new ArrayList<>();
        if (levelBeaten == NUM_LEVELS) {
            if (inHardMode) {
                lines.addAll(Arrays.asList(
                        "Congratulations!",
                        "You've beaten hard mode.",
                        "You're a career politician!"
                ));
            } else {
                lines.addAll(Arrays.asList(
                        "Congratulations!",
                        "You've beaten the game!",
                        "Hard mode unlocked"
                ));
            }
        } else {
            lines.add("Level " + (levelBeaten+1) + " unlocked!");
            if (getTierIncreaseLevels().contains(levelBeaten+1)) {
                lines.add("New sound-bytes unlocked!");
            }
        }
        return lines;
    }

    @Override
    public Collection<BlockType> getUnlockedCandidates(int levelNum) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return LS_NORMAL;
    }

    @Override
    public int getNumSoundTiers() {
        return NUM_TIERS;
    }

    private static int getSoundTierOfLevel(int level) {
        int tier = 1;
        int nextTierLvlIdx = 0;
        while (nextTierLvlIdx < increaseTierLevels.size()
                && level >= increaseTierLevels.get(nextTierLvlIdx)) {
            tier++;
            nextTierLvlIdx++;
        }
        return tier;
    }
}
