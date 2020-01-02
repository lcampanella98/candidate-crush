package com.gmail.enzocampanella98.candidatecrush.level;

import java.util.Arrays;
import java.util.List;

import static com.gmail.enzocampanella98.candidatecrush.board.BlockType.BIDEN;
import static com.gmail.enzocampanella98.candidatecrush.board.BlockType.BUTTIGIEG;
import static com.gmail.enzocampanella98.candidatecrush.board.BlockType.SANDERS;
import static com.gmail.enzocampanella98.candidatecrush.board.BlockType.WARREN;
import static com.gmail.enzocampanella98.candidatecrush.level.GameModeType.ELECTION;
import static com.gmail.enzocampanella98.candidatecrush.level.GameModeType.MOVE_LIMIT;
import static com.gmail.enzocampanella98.candidatecrush.level.GameModeType.PRIMARY;
import static com.gmail.enzocampanella98.candidatecrush.level.GameModeType.SOUND_BYTE;

public class LevelFactory {
    public static final int NUM_LEVELS = 14;
    public static final int NUM_TIERS = 4;
    public static final List<Integer> increaseTierLevels = Arrays.asList(4, 8, 11);

    public Level getLevel(int levelNum) {
        int soundTier = getSoundTierOfLevel(levelNum);
        LevelBuilder builder = new LevelBuilder(levelNum, soundTier);
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

    public static int getSoundTierOfLevel(int level) {
        int tier = 1;
        int nextTierLvlIdx = 0;
        while (nextTierLvlIdx < increaseTierLevels.size()
                && level >= increaseTierLevels.get(nextTierLvlIdx)) {
            tier++;
            nextTierLvlIdx++;
        }
        return tier;
    }

    public static void printLevels() {
        LevelFactory lf = new LevelFactory();
        for (int i = 1; i <= NUM_LEVELS; i++) {
            System.out.println(lf.getLevel(i));
        }
    }

}
