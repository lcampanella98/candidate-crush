package com.gmail.enzocampanella98.candidatecrush.level;

import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.gamemode.CCGameMode;
import com.gmail.enzocampanella98.candidatecrush.gamemode.GameModeFactory;
import com.gmail.enzocampanella98.candidatecrush.gamemode.config.GameModeConfig;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.CrushVals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.gmail.enzocampanella98.candidatecrush.screens.HUD.scoreText;

public class LevelFactory {
    public static final int NUM_LEVELS = 14;
    public static final int NUM_TIERS = 4;
    public static final List<Integer> increaseTierLevels = Arrays.asList(4, 8, 11);
    public static final List<BlockType> CANDIDATES_2020 = new ArrayList<>(Arrays.asList(
            BlockType.TRUMP,
            BlockType.WARREN,
            BlockType.SANDERS,
            BlockType.BUTTIGIEG,
            BlockType.BIDEN
    ));
    public static final List<BlockType> DEM_CANDIDATES_2020 = new ArrayList<>(Arrays.asList(
            BlockType.WARREN,
            BlockType.SANDERS,
            BlockType.BUTTIGIEG,
            BlockType.BIDEN
    ));

    static final double defaultNonUserInvokedCrushScale = 0.8;
    static final CrushVals defaultCrushVals = new CrushVals(
            500, 1200, 2500, 5000
    );

    private final GameModeFactory gmf;

    public LevelFactory(GameModeFactory gmf) {
        this.gmf = gmf;
    }

    public Level getLevel(int levelNum) {
        int soundTier = getSoundTierOfLevel(levelNum);
        GameModeConfig config;
        switch (levelNum) {
            case 1:
                config = new GameModeConfig.Builder()
                        .levelNum(levelNum)
                        .boardSize(8)
                        .soundTier(soundTier)
                        .candidates(DEM_CANDIDATES_2020)
                        .crushVals(defaultCrushVals)
                        .nonUserInvokedCrushScale(defaultNonUserInvokedCrushScale)
                        .gameLength(60)
                        .targetScore(1000)
                        .build();
                config.instructionLines = getTimedVoteInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getTimedVoteTargetGameMode(stage, config);
                    }
                };
            case 2:
                config = new GameModeConfig.Builder()
                        .levelNum(levelNum)
                        .boardSize(8)
                        .soundTier(soundTier)
                        .candidates(DEM_CANDIDATES_2020)
                        .crushVals(defaultCrushVals)
                        .nonUserInvokedCrushScale(defaultNonUserInvokedCrushScale)
                        .gameLength(60)
                        .targetScore(1000)
                        .build();
                config.instructionLines = getTimedVoteInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getTimedVoteTargetGameMode(stage, config);
                    }
                };
            case 3:
                config = new GameModeConfig.Builder()
                        .levelNum(levelNum)
                        .boardSize(8)
                        .soundTier(soundTier)
                        .candidates(DEM_CANDIDATES_2020)
                        .crushVals(defaultCrushVals)
                        .nonUserInvokedCrushScale(defaultNonUserInvokedCrushScale)
                        .gameLength(60)
                        .targetScore(1000)
                        .build();
                config.instructionLines = getTimedVoteInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getTimedVoteTargetGameMode(stage, config);
                    }
                };
            case 4:
                config = new GameModeConfig.Builder()
                        .levelNum(levelNum)
                        .boardSize(8)
                        .soundTier(soundTier)
                        .candidates(DEM_CANDIDATES_2020)
                        .crushVals(defaultCrushVals)
                        .nonUserInvokedCrushScale(defaultNonUserInvokedCrushScale)
                        .gameLength(60)
                        .targetScore(1000)
                        .build();
                config.instructionLines = getTimedVoteInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getTimedVoteTargetGameMode(stage, config);
                    }
                };
            case 5:
                config = new GameModeConfig.Builder()
                        .levelNum(levelNum)
                        .boardSize(8)
                        .soundTier(soundTier)
                        .candidates(DEM_CANDIDATES_2020)
                        .crushVals(defaultCrushVals)
                        .nonUserInvokedCrushScale(defaultNonUserInvokedCrushScale)
                        .gameLength(60)
                        .targetScore(1000)
                        .build();
                config.instructionLines = getTimedVoteInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getTimedVoteTargetGameMode(stage, config);
                    }
                };
            case 6:
                config = new GameModeConfig.Builder()
                        .levelNum(levelNum)
                        .boardSize(8)
                        .soundTier(soundTier)
                        .candidates(DEM_CANDIDATES_2020)
                        .crushVals(defaultCrushVals)
                        .nonUserInvokedCrushScale(defaultNonUserInvokedCrushScale)
                        .gameLength(60)
                        .targetScore(1000)
                        .build();
                config.instructionLines = getTimedVoteInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getTimedVoteTargetGameMode(stage, config);
                    }
                };
            case 7:
                config = new GameModeConfig.Builder()
                        .levelNum(levelNum)
                        .boardSize(8)
                        .soundTier(soundTier)
                        .candidates(DEM_CANDIDATES_2020)
                        .crushVals(defaultCrushVals)
                        .nonUserInvokedCrushScale(defaultNonUserInvokedCrushScale)
                        .gameLength(60)
                        .targetScore(1000)
                        .build();
                config.instructionLines = getTimedVoteInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getTimedVoteTargetGameMode(stage, config);
                    }
                };
            case 8:
                config = new GameModeConfig.Builder()
                        .levelNum(levelNum)
                        .boardSize(8)
                        .soundTier(soundTier)
                        .candidates(DEM_CANDIDATES_2020)
                        .crushVals(defaultCrushVals)
                        .nonUserInvokedCrushScale(defaultNonUserInvokedCrushScale)
                        .gameLength(60)
                        .targetScore(1000)
                        .build();
                config.instructionLines = getTimedVoteInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getTimedVoteTargetGameMode(stage, config);
                    }
                };
            case 9:
                config = new GameModeConfig.Builder()
                        .levelNum(levelNum)
                        .boardSize(8)
                        .soundTier(soundTier)
                        .candidates(DEM_CANDIDATES_2020)
                        .crushVals(defaultCrushVals)
                        .nonUserInvokedCrushScale(defaultNonUserInvokedCrushScale)
                        .gameLength(60)
                        .targetScore(1000)
                        .build();
                config.instructionLines = getTimedVoteInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getTimedVoteTargetGameMode(stage, config);
                    }
                };
            case 10:
                config = new GameModeConfig.Builder()
                        .levelNum(levelNum)
                        .boardSize(8)
                        .soundTier(soundTier)
                        .candidates(DEM_CANDIDATES_2020)
                        .crushVals(defaultCrushVals)
                        .nonUserInvokedCrushScale(defaultNonUserInvokedCrushScale)
                        .gameLength(60)
                        .targetScore(1000)
                        .build();
                config.instructionLines = getTimedVoteInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getTimedVoteTargetGameMode(stage, config);
                    }
                };
            case 11:
                config = new GameModeConfig.Builder()
                        .levelNum(levelNum)
                        .boardSize(8)
                        .soundTier(soundTier)
                        .candidates(DEM_CANDIDATES_2020)
                        .crushVals(defaultCrushVals)
                        .nonUserInvokedCrushScale(defaultNonUserInvokedCrushScale)
                        .gameLength(60)
                        .targetScore(1000)
                        .build();
                config.instructionLines = getTimedVoteInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getTimedVoteTargetGameMode(stage, config);
                    }
                };
            case 12:
                config = new GameModeConfig.Builder()
                        .levelNum(levelNum)
                        .boardSize(8)
                        .soundTier(soundTier)
                        .candidates(DEM_CANDIDATES_2020)
                        .crushVals(defaultCrushVals)
                        .nonUserInvokedCrushScale(defaultNonUserInvokedCrushScale)
                        .gameLength(60)
                        .targetScore(1000)
                        .build();
                config.instructionLines = getTimedVoteInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getTimedVoteTargetGameMode(stage, config);
                    }
                };
            case 13:
                config = new GameModeConfig.Builder()
                        .levelNum(levelNum)
                        .boardSize(8)
                        .soundTier(soundTier)
                        .candidates(DEM_CANDIDATES_2020)
                        .crushVals(defaultCrushVals)
                        .nonUserInvokedCrushScale(defaultNonUserInvokedCrushScale)
                        .gameLength(60)
                        .targetScore(1000)
                        .build();
                config.instructionLines = getTimedVoteInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getTimedVoteTargetGameMode(stage, config);
                    }
                };
            case 14:
                config = new GameModeConfig.Builder()
                        .levelNum(levelNum)
                        .boardSize(8)
                        .soundTier(soundTier)
                        .candidates(DEM_CANDIDATES_2020)
                        .crushVals(defaultCrushVals)
                        .nonUserInvokedCrushScale(defaultNonUserInvokedCrushScale)
                        .gameLength(60)
                        .targetScore(1000)
                        .build();
                config.instructionLines = getTimedVoteInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getTimedVoteTargetGameMode(stage, config);
                    }
                };

        }
        return null;
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

    public static Collection<String> getTimedVoteInstructions(GameModeConfig config) {
        return getTimedVoteInstructions(config.targetScore);
    }

    public static Collection<String> getTimedVoteInstructions(int targetScore) {
        return Arrays.asList(
                "Reach " + scoreText(targetScore) + " votes",
                "before time runs out!"
        );
    }

    public static Collection<String> getMoveLimitInstructions(GameModeConfig config) {
        return getMoveLimitInstructions(config.numMoves, config.targetScore);
    }

    public static Collection<String> getMoveLimitInstructions(int numMoves, int targetScore) {
        return Arrays.asList(
                "You have " + numMoves + " moves",
                "to reach " + scoreText(targetScore) + " votes!"
        );
    }

    public static Collection<String> getRaceInstructions(String playerName, int numMoves) {
        return Arrays.asList(
                "You play " + playerName + ". ",
                "Be on top after " + numMoves + " moves!"
        );
    }

}
