package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.gamemode.config.GameModeConfig;
import com.gmail.enzocampanella98.candidatecrush.level.ILevelSet;
import com.gmail.enzocampanella98.candidatecrush.level.LevelBuilder;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.NamedCandidateGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gmail.enzocampanella98.candidatecrush.tools.Methods.firstToUpper;
import static com.gmail.enzocampanella98.candidatecrush.tools.Methods.getGameVal;

public class GameModeFactory {

    private final CandidateCrush game;

    public GameModeFactory(CandidateCrush game) {
        this.game = game;
    }

    public CCGameMode getGameMode(Stage stage, GameModeConfig config, ILevelSet levelSet) {
        switch (config.gameModeType) {
            case SOUND_BYTE:
                return getTimedSoundByteTargetGameMode(stage, config, levelSet);
            case MOVE_LIMIT:
                return getMoveLimitVoteTargetGameMode(stage, config, levelSet);
            case PRIMARY:
                return getDemocratPrimary2020GameMode(stage, config, levelSet);
            case ELECTION:
                return getElection2020GameMode(stage, config, levelSet);
        }
        return null;
    }

    public RaceGameMode getDemocratPrimary2020GameMode(Stage stage, GameModeConfig config, ILevelSet levelSet) {
        double playerBlockFreqAdvantage = 0.1;
        List<NamedCandidateGroup> groups = new ArrayList<>();
        NamedCandidateGroup playerGroup = null;

        Map<BlockType, Double> freqMap = new HashMap<>();
        double playerBlockFrequency = 1.0 / config.candidates.size() + playerBlockFreqAdvantage;
        double otherBlockFrequency = (1.0 - playerBlockFrequency) / (config.candidates.size() - 1);

        for (BlockType dem : config.candidates) {
            NamedCandidateGroup grp = new NamedCandidateGroup(
                    Collections.singletonList(dem),
                    firstToUpper(dem.getLname()),
                    dem.getFriendlyName()
            );
            groups.add(grp);
            freqMap.put(dem, dem == config.primaryPlayer ? playerBlockFrequency : otherBlockFrequency);
            if (dem == config.primaryPlayer) {
                playerGroup = grp;
            }
        }
        assert playerGroup != null;

        config.numMoves = getGameVal(config.numMoves, 5);

        return new RaceGameMode(game, stage, groups, playerGroup, freqMap, config, levelSet);
    }

    public TimedVoteTargetGameMode getTimedVoteTargetGameMode(Stage stage, GameModeConfig config, ILevelSet levelSet) {
        config.gameLength = getGameVal(config.gameLength, 30);
        config.targetScore = getGameVal(config.targetScore, 2000);
        return new TimedVoteTargetGameMode(game, stage, config, levelSet);
    }

    public TimedSoundByteTargetGameMode getTimedSoundByteTargetGameMode(Stage stage, GameModeConfig config, ILevelSet levelSet) {
        config.showCrushLabels = false;
        config.singleBlockDropTime = 0.15f;
        config.gameLength = getGameVal(config.gameLength, 30);
        config.targetNumSoundBytes = getGameVal(config.targetNumSoundBytes, 2);
        return new TimedSoundByteTargetGameMode(game, stage, config, levelSet);
    }

    public MoveLimitVoteTargetGameMode getMoveLimitVoteTargetGameMode(Stage stage, GameModeConfig config, ILevelSet levelSet) {
        config.numMoves = getGameVal(config.numMoves, 1);
        config.targetScore = getGameVal(config.targetScore, 100);
        return new MoveLimitVoteTargetGameMode(game, stage, config, levelSet);
    }

    public RaceGameMode getElection2020GameMode(Stage stage,
                                                GameModeConfig config,
                                                ILevelSet levelSet) {
        double trumpBlockFreq = 0.4;
        List<NamedCandidateGroup> groups = new ArrayList<>();
        NamedCandidateGroup playerGroup;
        List<BlockType> repCands = new ArrayList<>();
        List<BlockType> demCands = new ArrayList<>();

        Map<BlockType, Double> freqs = new HashMap<>();
        for (BlockType cand : config.candidates) {
            double f;
            if (cand == BlockType.TRUMP) {
                f = trumpBlockFreq;
                repCands.add(cand);
            } else {
                f = (1.0 - trumpBlockFreq) / (config.candidates.size() - 1);
                demCands.add(cand);
            }
            freqs.put(cand, f);
        }
        NamedCandidateGroup demGroup = new NamedCandidateGroup(demCands, "Democrats", LevelBuilder.DEMOCRAT_LONG_NAME);
        NamedCandidateGroup repGroup = new NamedCandidateGroup(repCands, "Trump", LevelBuilder.REPUBLICAN_LONG_NAME);
        groups.add(demGroup);
        groups.add(repGroup);
        playerGroup = config.playerParty == 'D' ? demGroup : repGroup;

        config.numMoves = getGameVal(config.numMoves, 3);
        return new RaceGameMode(game, stage, groups, playerGroup, freqs, config, levelSet);
    }

}
