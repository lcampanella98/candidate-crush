package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.BlockColorProviderFactory;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.IBlockColorProvider;
import com.gmail.enzocampanella98.candidatecrush.gamemode.config.GameModeConfig;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.NamedCandidateGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gmail.enzocampanella98.candidatecrush.tools.Methods.colorFromRGB;
import static com.gmail.enzocampanella98.candidatecrush.tools.Methods.firstToUpper;
import static com.gmail.enzocampanella98.candidatecrush.tools.Methods.getGameVal;

public class GameModeFactory {
    public static List<Color> blockBgColors = new ArrayList<>(Arrays.asList(
            colorFromRGB(245, 66, 66),  // red
            colorFromRGB(188, 66, 245), // purple
            colorFromRGB(66, 245, 239), // cyan
            colorFromRGB(66, 245, 102), // green
//            colorFromRGB(224, 245, 66), // yellow
            colorFromRGB(245, 179, 66) // orange
    ));

    private final CandidateCrush game;
    private final BlockColorProviderFactory blockColorProviderFactory;

    public GameModeFactory(CandidateCrush game) {
        this.game = game;
        blockColorProviderFactory = new BlockColorProviderFactory();
    }

    public RaceGameMode getDemocratPrimary2020GameMode(Stage stage, GameModeConfig config) {
        double playerBlockFreqAdvantange = 0.1;
        List<NamedCandidateGroup> groups = new ArrayList<>();
        NamedCandidateGroup playerGroup = null;

        Map<BlockType, Double> freqs = new HashMap<>();
        double playerBlockFrequency = 1.0 / config.candidates.size() + playerBlockFreqAdvantange;
        double otherBlockFrequency = (1.0 - playerBlockFrequency) / (config.candidates.size() - 1);

        for (BlockType dem : config.candidates) {
            NamedCandidateGroup grp = new NamedCandidateGroup(
                    Collections.singletonList(dem),
                    firstToUpper(dem.getLname()),
                    dem.getFriendlyName()
            );
            groups.add(grp);
            freqs.put(dem, dem == config.primaryPlayer ? playerBlockFrequency : otherBlockFrequency);
            if (dem == config.primaryPlayer) {
                playerGroup = grp;
            }
        }
        assert playerGroup != null;

        config.numMoves = getGameVal(config.numMoves, 5);
        return new RaceGameMode(game, stage, groups, playerGroup,
                getBlockColorProvider(config.isHardMode, config.candidates), freqs, config);
    }

    public TimedVoteTargetGameMode getTimedVoteTargetGameMode(Stage stage, GameModeConfig config) {
        config.gameLength = getGameVal(config.gameLength, 30);
        config.targetScore = getGameVal(config.targetScore, 2000);
        return new TimedVoteTargetGameMode(game, stage,
                getBlockColorProvider(config.isHardMode, config.candidates), config);
    }

    public MoveLimitVoteTargetGameMode getMoveLimitVoteTargetGameMode(Stage stage, GameModeConfig config) {
        config.numMoves = getGameVal(config.numMoves, 1);
        config.targetScore = getGameVal(config.targetScore, 100);
        return new MoveLimitVoteTargetGameMode(game, stage,
                getBlockColorProvider(config.isHardMode, config.candidates), config);
    }

    public RaceGameMode getElection2020GameMode(Stage stage,
                                                GameModeConfig config,
                                                String demGroupLongName,
                                                String repGroupLongName) {
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
        NamedCandidateGroup demGroup = new NamedCandidateGroup(demCands, "Democrats", demGroupLongName);
        NamedCandidateGroup repGroup = new NamedCandidateGroup(repCands, "Trump", repGroupLongName);
        groups.add(demGroup);
        groups.add(repGroup);
        playerGroup = config.playerParty == 'D' ? demGroup : repGroup;

        config.numMoves = getGameVal(config.numMoves, 3);
        return new RaceGameMode(
                game, stage, groups, playerGroup,
                getBlockColorProvider(config.isHardMode, config.candidates), freqs, config);
    }

    private IBlockColorProvider getBlockColorProvider(boolean isHardMode, List<BlockType> blockTypes) {
        return isHardMode
                ? blockColorProviderFactory.getEmptyBlockColorProvider()
                : blockColorProviderFactory.getRandomBlockColorProvider(blockTypes, blockBgColors);
    }
}
