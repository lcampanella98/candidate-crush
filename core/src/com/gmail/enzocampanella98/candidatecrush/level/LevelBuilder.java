package com.gmail.enzocampanella98.candidatecrush.level;

import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.gamemode.config.GameModeConfig;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.CrushVals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.gmail.enzocampanella98.candidatecrush.level.GameModeType.ELECTION;
import static com.gmail.enzocampanella98.candidatecrush.level.GameModeType.PRIMARY;
import static com.gmail.enzocampanella98.candidatecrush.screens.HUD.scoreText;
import static com.gmail.enzocampanella98.candidatecrush.tools.Methods.roundToNearest;

public class LevelBuilder {
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

    public static final String DEMOCRAT_LONG_NAME = "The Democrats";
    public static final String REPUBLICAN_LONG_NAME = "Donald Trump";

    static final float defaultSingleBlockDropTime = 0.3f;
    static final double defaultNonUserInvokedCrushScale = 0.8;
    static final CrushVals defaultCrushVals = new CrushVals(
            500, 1200, 2500, 5000
    );

    public static final int DEFAULT_BOARD_SIZE = 8;

    private int levelNum;
    private int soundTier;

    private GameModeType gameModeType;
    private int difficulty; // an integer between 1 and 10
    private Object initialGameParameter;
    private char playerParty;
    private BlockType primaryCandidate;
    private BlockType exceptCandidate;

    public LevelBuilder(int levelNum,
                        int soundTier) {
        this.levelNum = levelNum;
        this.soundTier = soundTier;
    }

    public LevelBuilder gameModeType(GameModeType gameModeType) {
        this.gameModeType = gameModeType;
        return this;
    }

    public LevelBuilder exceptCandidate(BlockType exceptCandidate) {
        this.exceptCandidate = exceptCandidate;
        return this;
    }

    public LevelBuilder difficulty(int difficulty) {
        if (difficulty < 0 || difficulty > 9) {
            throw new RuntimeException("Difficulty out of bounds");
        }
        this.difficulty = difficulty;
        return this;
    }

    public LevelBuilder playerParty(char playerParty) {
        this.playerParty = playerParty;
        return this;
    }

    public LevelBuilder primaryCandidate(BlockType primaryCandidate) {
        this.primaryCandidate = primaryCandidate;
        return this;
    }

    /*
    For soundByte gamemode, a time limit (double)
    For Primary/Election/move limit gamemmode, a move limit (int)
     */
    public LevelBuilder initialGameParameter(Object initialGameParameter) {
        this.initialGameParameter = initialGameParameter;
        return this;
    }

    public Level build() {
        GameModeConfig config = defaultConfig(levelNum, soundTier);
        config.gameModeType = gameModeType;
        boolean isElection = gameModeType == ELECTION;
        boolean isPrimary = gameModeType == PRIMARY;

        if (gameModeType == PRIMARY) {
            config.candidates = getCandidates2020();
        } else {
            config.candidates = getDemCandidates2020();
        }
        if (exceptCandidate != null) {
            config.candidates.remove(exceptCandidate);
        }

        config.playerParty = playerParty;
        config.primaryPlayer = primaryCandidate;

        setGameModeParams(config);
        config.instructionLines = getGameInstructions(config);

        return new Level(config, null, isElection, isPrimary, levelNum);
    }

    /*
    Set game parameters by Game-Mode, some initial parameter value, and game difficulty
     */
    private void setGameModeParams(GameModeConfig config) {
        switch (config.gameModeType) {
            case SOUND_BYTE:
                setSoundByteGameModeParams(config, (double) initialGameParameter, difficulty);
                break;
            case MOVE_LIMIT:
                setMoveLimitGameModeParams(config, (int) initialGameParameter, difficulty);
                break;
            case PRIMARY:
                setDemocratPrimaryGameModeParams(config, difficulty);
                break;
            case ELECTION:
                setElectionGameModeParams(config, difficulty);
                break;
        }
    }

    /*
    For sound-byte game-mode, we should expect 1 sound-byte every 4 seconds.
    Difficulty level alters the appearance frequency of sound-bytes
     */
    private static void setSoundByteGameModeParams(GameModeConfig config, double timeLimit, int difficulty) {
        config.gameLength = timeLimit;
        config.soundByteFrequency = (float) linearDiff(0.10, 0.05, difficulty);
        config.targetNumSoundBytes = roundToNearest(timeLimit / (linearDiff(4, 6, difficulty)), 5);
    }

    private static void setMoveLimitGameModeParams(GameModeConfig config, int numMoves, int difficulty) {
        config.numMoves = numMoves;
        double target = linearDiff(5000, 12000, difficulty)
                + numMoves * linearDiff(1000, 1600, difficulty);
        config.targetScore = roundToNearest(target, 5000);
    }

    private static void setDemocratPrimaryGameModeParams(GameModeConfig config, int difficulty) {
        double nMoves = linearDiff(10, 25, difficulty);
        config.numMoves = roundToNearest(nMoves, 5);
    }

    private static void setElectionGameModeParams(GameModeConfig config, int difficulty) {
        double nMoves = linearDiff(10, 35, difficulty);
        config.numMoves = roundToNearest(nMoves, 5);
    }

    private static double linearDiff(double f0, double f9, int d) {
        return f0 + d * (f9 - f0) / 10.0;
    }

    /*
    Other helpful configuration methods
     */
    public static GameModeConfig defaultConfig(int lvl, int soundTier) {
        return new GameModeConfig.Builder()
                .levelNum(lvl)
                .soundTier(soundTier)
                .boardSize(DEFAULT_BOARD_SIZE)
                .singleBlockDropTime(defaultSingleBlockDropTime)
                .nonUserInvokedCrushScale(defaultNonUserInvokedCrushScale)
                .crushVals(defaultCrushVals)
                .showCrushLabels(true)
                .build();
    }

    public static Collection<String> getGameInstructions(GameModeConfig config) {
        switch (config.gameModeType) {
            case SOUND_BYTE:
                return getTimedSoundByteInstructions(config);
            case MOVE_LIMIT:
                return getMoveLimitInstructions(config);
            case PRIMARY:
                return getDemocratPrimaryInstructions(config);
            case ELECTION:
                return getElectionInstructions(config);
        }
        return null;
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

    public static Collection<String> getDemocratPrimaryInstructions(GameModeConfig config) {
        return getRaceInstructions(config.primaryPlayer.getFriendlyName(), config.numMoves);
    }

    public static Collection<String> getElectionInstructions(GameModeConfig config) {
        return getRaceInstructions(config.playerParty == 'D' ? DEMOCRAT_LONG_NAME : REPUBLICAN_LONG_NAME, config.numMoves);
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

}
