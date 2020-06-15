package com.gmail.enzocampanella98.candidatecrush.level;

import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.NamedCandidateGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.gmail.enzocampanella98.candidatecrush.board.BlockType.*;
import static com.gmail.enzocampanella98.candidatecrush.level.GameModeType.*;

public class OakBaesLevelSet implements ILevelSet {

    private static final int NUM_LEVELS = 22;

    @Override
    public List<Integer> getTierIncreaseLevels() {
        return new ArrayList<>();
    }

    @Override
    public Level getLevel(int levelNum) {
        int soundTier = 1;
        LevelBuilder builder = new LevelBuilder(LS_OAK, levelNum, soundTier, SPIKEBALL);

        switch (levelNum) {
            case 1:
                builder.gameModeType(MOVE_LIMIT)
                        .difficulty(0)
                        .withCandidates(Arrays.asList(SPIKEBALL, QUESADILLA, TURKEY, PUMPKIN))
                        .initialGameParameter(20)
                        .withCustomInstructions(getLevel1ExtraInstructions());
                break;
            case 2:
                builder.gameModeType(SOUND_BYTE)
                        .difficulty(2)
                        .withCandidates(Arrays.asList(SPIKEBALL, QUESADILLA, TURKEY, PUMPKIN))
                        .initialGameParameter(60.0);
                break;
            case 3:
                builder.gameModeType(PRIMARY)
                        .difficulty(0)
                        .withCandidates(Arrays.asList(YOUNG, QUESADILLA, TURKEY, PUMPKIN))
                        .primaryCandidate(YOUNG);
                break;
            case 4:
                builder.gameModeType(MOVE_LIMIT)
                        .difficulty(2)
                        .withCandidates(Arrays.asList(YOUNG, QUESADILLA, TURKEY, PUMPKIN))
                        .initialGameParameter(20);
                break;
            case 5:
                builder.gameModeType(PRIMARY)
                        .difficulty(0)
                        .withCandidates(Arrays.asList(YOUNG, KOZAN, TURKEY, PUMPKIN))
                        .primaryCandidate(KOZAN);
                break;
            case 6:
                builder.gameModeType(SOUND_BYTE)
                        .difficulty(0)
                        .withCandidates(Arrays.asList(YOUNG, KOZAN, TURKEY, PUMPKIN))
                        .initialGameParameter(60.0);
                break;
            case 7:
                builder.gameModeType(PRIMARY)
                        .difficulty(0)
                        .withCandidates(Arrays.asList(YOUNG, KOZAN, DAUDELIN, PUMPKIN))
                        .primaryCandidate(DAUDELIN);
                break;
            case 8:
                builder.gameModeType(MOVE_LIMIT)
                        .difficulty(2)
                        .withCandidates(Arrays.asList(YOUNG, KOZAN, DAUDELIN, PUMPKIN))
                        .initialGameParameter(20);
                break;
            case 9:
                builder.gameModeType(PRIMARY)
                        .difficulty(0)
                        .withCandidates(Arrays.asList(YOUNG, KOZAN, DAUDELIN, GHATTAS))
                        .primaryCandidate(GHATTAS);
                break;
            case 10:
                builder.gameModeType(SOUND_BYTE)
                        .difficulty(0)
                        .withCandidates(Arrays.asList(YOUNG, KOZAN, DAUDELIN, GHATTAS))
                        .initialGameParameter(60.0);
                break;
            case 11:
                builder.gameModeType(PRIMARY)
                        .difficulty(0)
                        .withCandidates(Arrays.asList(CAMPANELLA, KOZAN, DAUDELIN, GHATTAS))
                        .primaryCandidate(CAMPANELLA);
                break;
            case 12:
                builder.gameModeType(MOVE_LIMIT)
                        .difficulty(2)
                        .withCandidates(Arrays.asList(CAMPANELLA, KOZAN, DAUDELIN, GHATTAS))
                        .initialGameParameter(20);
                break;
            case 13:
                builder.gameModeType(PRIMARY)
                        .difficulty(0)
                        .withCandidates(Arrays.asList(CAMPANELLA, LOMUSCIO, DAUDELIN, GHATTAS))
                        .primaryCandidate(LOMUSCIO);
                break;
            case 14:
                builder.gameModeType(SOUND_BYTE)
                        .difficulty(0)
                        .withCandidates(Arrays.asList(CAMPANELLA, LOMUSCIO, DAUDELIN, GHATTAS))
                        .initialGameParameter(60.0);
                break;
            case 15:
                builder.gameModeType(PRIMARY)
                        .difficulty(0)
                        .withCandidates(Arrays.asList(CAMPANELLA, LOMUSCIO, MORGAN, GHATTAS))
                        .primaryCandidate(MORGAN);
                break;
            case 16:
                builder.gameModeType(MOVE_LIMIT)
                        .difficulty(2)
                        .withCandidates(Arrays.asList(CAMPANELLA, LOMUSCIO, MORGAN, GHATTAS))
                        .initialGameParameter(20);
                break;
            case 17:
                builder.gameModeType(PRIMARY)
                        .difficulty(0)
                        .withCandidates(Arrays.asList(CAMPANELLA, LOMUSCIO, MORGAN, MEZA))
                        .primaryCandidate(MEZA);
                break;
            case 18:
                builder.gameModeType(SOUND_BYTE)
                        .difficulty(0)
                        .withCandidates(Arrays.asList(CAMPANELLA, LOMUSCIO, MORGAN, MEZA))
                        .initialGameParameter(60.0);
                break;
            case 19:
                builder.gameModeType(SOUND_BYTE)
                        .difficulty(0)
                        .withCandidates(Arrays.asList(CAMPANELLA, YOUNG, DAUDELIN, MEZA))
                        .initialGameParameter(60.0);
                break;
            case 20:
                builder.gameModeType(SOUND_BYTE)
                        .difficulty(0)
                        .withCandidates(Arrays.asList(KOZAN, LOMUSCIO, MORGAN, GHATTAS))
                        .initialGameParameter(60.0);
                break;
            case 21:
                builder.gameModeType(ELECTION)
                        .difficulty(0)
                        .withElectionGroups(
                                new NamedCandidateGroup(Arrays.asList(KOZAN, LOMUSCIO, MORGAN, GHATTAS), "Girls", "Girls"),
                                new NamedCandidateGroup(Arrays.asList(CAMPANELLA, YOUNG, DAUDELIN, MEZA), "Boys", "Boys"),
                                "Girls"
                        );
                break;
            case 22:
                builder.gameModeType(SOUND_BYTE)
                        .difficulty(0)
                        .withCandidates(Arrays.asList(KOZAN, LOMUSCIO, MORGAN, GHATTAS, CAMPANELLA, YOUNG, DAUDELIN, MEZA))
                        .initialGameParameter(60.0);
                break;
        }

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
        int unlockIdx = levelNum - 1;
        List<BlockType> unlocksByLevel = Arrays.asList(
                null, YOUNG, null, KOZAN, null, DAUDELIN, null, GHATTAS,
                null, CAMPANELLA, null, LOMUSCIO, null, MORGAN, null, MEZA
        );
        List<BlockType> unlocks = new ArrayList<>();
        if (unlockIdx < unlocksByLevel.size()) {
            BlockType unlock = unlocksByLevel.get(unlockIdx);
            if (unlock != null) {
                unlocks.add(unlock);
            }
        }
        return unlocks;
    }

    private List<String> getLevel1ExtraInstructions() {
        return Arrays.asList(
                String.format("You have %s,", SPIKEBALL.getFriendlyName()),
                String.format("%s,", QUESADILLA.getFriendlyName()),
                String.format("%s,", TURKEY),
                String.format("and %s", PUMPKIN.getFriendlyName()),
                ""
        );
    }
}
