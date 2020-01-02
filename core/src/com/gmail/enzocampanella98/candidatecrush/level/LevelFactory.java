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
    static final double defaultSoundByteFrequency = 0.10;
    static final CrushVals defaultCrushVals = new CrushVals(
            500, 1200, 2500, 5000
    );

    private final GameModeFactory gmf;

    public LevelFactory(GameModeFactory gmf) {
        this.gmf = gmf;
    }

    public Level getLevel(int levelNum) {
        int soundTier = getSoundTierOfLevel(levelNum);
        GameModeConfig config = defConfig(levelNum, soundTier);
        switch (levelNum) {
            case 1:
                config.candidates = getCandidates2020();
                    config.candidates.remove(BlockType.BUTTIGIEG); // save for primary round
                config.gameLength = 60;
                config.targetNumSoundBytes = 10;
                config.instructionLines = getTimedSoundByteInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getTimedSoundByteTargetGameMode(stage, config);
                    }
                };
            case 2:
                config.candidates = getCandidates2020();
                    config.candidates.remove(BlockType.BUTTIGIEG); // save for primary round
                config.numMoves = 20;
                config.targetScore = 25000;
                config.instructionLines = getMoveLimitInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getMoveLimitVoteTargetGameMode(stage, config);
                    }
                };
            case 3:
                config.candidates = getDemCandidates2020();
                config.numMoves = 10;
                config.primaryPlayer = BlockType.BUTTIGIEG;
                config.instructionLines = getRaceInstructions(BlockType.BUTTIGIEG.getFriendlyName(), config.numMoves);
                return new Level(config, null, false, true, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getDemocratPrimary2020GameMode(stage, config);
                    }
                };
            case 4:
                config.candidates = getCandidates2020();
                    config.candidates.remove(BlockType.WARREN);
                config.gameLength = 60;
                config.targetNumSoundBytes = 15;
                config.instructionLines = getTimedSoundByteInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getTimedSoundByteTargetGameMode(stage, config);
                    }
                };
            case 5:
                config.candidates = getCandidates2020();
                    config.candidates.remove(BlockType.WARREN);
                config.numMoves = 25;
                config.targetScore = 30000;
                config.instructionLines = getMoveLimitInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getMoveLimitVoteTargetGameMode(stage, config);
                    }
                };
            case 6:
                config.candidates = getDemCandidates2020();
                config.numMoves = 15;
                config.primaryPlayer = BlockType.WARREN;
                config.instructionLines = getRaceInstructions(BlockType.WARREN.getFriendlyName(), config.numMoves);
                return new Level(config, null, false, true, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getDemocratPrimary2020GameMode(stage, config);
                    }
                };
            case 7:
                config.candidates = getCandidates2020();
                config.numMoves = 15;
                config.playerParty = 'D';
                config.instructionLines = getRaceInstructions("The Democrats", config.numMoves);
                return new Level(config, null, true, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getElection2020GameMode(stage, config, "The Democrats", "Donald Trump");
                    }
                };
            case 8:
                config.candidates = getCandidates2020();
                config.candidates.remove(BlockType.BIDEN);
                config.gameLength = 75;
                config.targetNumSoundBytes = 25;
                config.instructionLines = getTimedSoundByteInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getTimedSoundByteTargetGameMode(stage, config);
                    }
                };
            case 9:
                config.candidates = getCandidates2020();
                config.candidates.remove(BlockType.BIDEN);
                config.numMoves = 25;
                config.targetScore = 35000;
                config.instructionLines = getMoveLimitInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getMoveLimitVoteTargetGameMode(stage, config);
                    }
                };
            case 10:
                config.candidates = getDemCandidates2020();
                config.numMoves = 20;
                config.primaryPlayer = BlockType.BIDEN;
                config.instructionLines = getRaceInstructions(BlockType.BIDEN.getFriendlyName(), config.numMoves);
                return new Level(config, null, false, true, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getDemocratPrimary2020GameMode(stage, config);
                    }
                };
            case 11:
                config.candidates = getCandidates2020();
                config.candidates.remove(BlockType.SANDERS);
                config.gameLength = 75;
                config.targetNumSoundBytes = 30;
                config.instructionLines = getTimedSoundByteInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getTimedSoundByteTargetGameMode(stage, config);
                    }
                };
            case 12:
                config.candidates = getCandidates2020();
                config.candidates.remove(BlockType.SANDERS);
                config.numMoves = 20;
                config.targetScore = 35000;
                config.instructionLines = getMoveLimitInstructions(config);
                return new Level(config, null, false, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getMoveLimitVoteTargetGameMode(stage, config);
                    }
                };
            case 13:
                config.candidates = getDemCandidates2020();
                config.numMoves = 25;
                config.primaryPlayer = BlockType.SANDERS;
                config.instructionLines = getRaceInstructions(BlockType.BUTTIGIEG.getFriendlyName(), config.numMoves);
                return new Level(config, null, false, true, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getDemocratPrimary2020GameMode(stage, config);
                    }
                };
            case 14:
                config.candidates = getCandidates2020();
                config.numMoves = 30;
                config.playerParty = 'R';
                config.instructionLines = getRaceInstructions("Donald Trump", config.numMoves);
                return new Level(config, null, true, false, levelNum) {
                    @Override
                    public CCGameMode getGameMode() {
                        return gmf.getElection2020GameMode(stage, config, "The Democrats", "Donald Trump");
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

    public static Collection<String> getTimedSoundByteInstructions(GameModeConfig config) {
        return getTimedSoundByteInstructions(config.targetNumSoundBytes);
    }

    public static Collection<String> getTimedSoundByteInstructions(int targetNumSoundBytes) {
        return Arrays.asList(
                "Crush " + targetNumSoundBytes + " sound-bytes",
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

    public static List<BlockType> getCandidates2020() {
        return new ArrayList<>(CANDIDATES_2020);
    }

    public static List<BlockType> getDemCandidates2020() {
        return new ArrayList<>(DEM_CANDIDATES_2020);
    }

    public static GameModeConfig defConfig(int lvl, int soundTier) {
        return new GameModeConfig.Builder()
                .boardSize(8)
                .levelNum(lvl)
                .soundTier(soundTier)
                .nonUserInvokedCrushScale(defaultNonUserInvokedCrushScale)
                .crushVals(defaultCrushVals)
                .soundByteFrequency(defaultSoundByteFrequency)
                .showCrushLabels(true)
                .build();
    }

}
