package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.BlockColorProviderFactory;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.IBlockColorProvider;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.NamedCandidateGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gmail.enzocampanella98.candidatecrush.screens.MenuScreen.CANDIDATES_2020;
import static com.gmail.enzocampanella98.candidatecrush.screens.MenuScreen.DEM_CANDIDATES_2020;
import static com.gmail.enzocampanella98.candidatecrush.tools.Methods.colorFromRGB;
import static com.gmail.enzocampanella98.candidatecrush.tools.Methods.firstToUpper;

public class GameModeFactory {
    public static List<Color> blockBgColors = new ArrayList<>(Arrays.asList(
            colorFromRGB(245, 66, 66),  // red
            colorFromRGB(188, 66, 245), // purple
            colorFromRGB(66, 245, 239), // cyan
            colorFromRGB(66, 245, 102), // green
            colorFromRGB(224, 245, 66), // yellow
            colorFromRGB(245, 179, 66) // orange
    ));

    private final CandidateCrush game;
    private Stage stage;
    private BlockColorProviderFactory blockColorProviderFactory;
    private boolean isHardMode;

    public GameModeFactory(CandidateCrush game) {
        this.game = game;
        blockColorProviderFactory = new BlockColorProviderFactory();
    }

    public RaceGameMode getDemocratPrimary2020GameMode(BlockType player) {
        assert stage != null;
        double playerBlockFreqAdvantange = 0.1;
        List<NamedCandidateGroup> groups = new ArrayList<>();
        NamedCandidateGroup playerGroup = null;

        Map<BlockType, Double> freqs = new HashMap<>();
        double playerBlockFrequency = 1.0 / DEM_CANDIDATES_2020.size() + playerBlockFreqAdvantange;
        double otherBlockFrequency = (1.0 - playerBlockFrequency) / (DEM_CANDIDATES_2020.size() - 1);

        for (BlockType dem : DEM_CANDIDATES_2020) {
            NamedCandidateGroup grp = new NamedCandidateGroup(
                    Collections.singletonList(dem), firstToUpper(dem.getLname())
            );
            groups.add(grp);
            freqs.put(dem, dem == player ? playerBlockFrequency : otherBlockFrequency);
            if (dem == player) {
                playerGroup = grp;
            }
        }
        assert playerGroup != null;
        return new RaceGameMode(game, stage, DEM_CANDIDATES_2020, groups, playerGroup,
                freqs, getBlockColorProvider(DEM_CANDIDATES_2020));
    }

    public TimedVoteTargetGameMode getTimedVoteTargetGameMode() {
        assert stage != null;
        return new TimedVoteTargetGameMode(game, stage,
                getBlockColorProvider(CANDIDATES_2020), CANDIDATES_2020, 60, 20000);
    }

    public MoveLimitVoteTargetGameMode getMoveLimitVoteTargetGameMode() {
        assert stage != null;
        return new MoveLimitVoteTargetGameMode(game, stage,
                getBlockColorProvider(CANDIDATES_2020), CANDIDATES_2020, 30, 50000);
    }

    public RaceGameMode getElection2020GameMode(Character playerParty) {
        assert stage != null;
        double trumpBlockFreq = 0.35;
        List<NamedCandidateGroup> groups = new ArrayList<>();
        NamedCandidateGroup playerGroup = null;
        List<BlockType> repCands = new ArrayList<>();
        List<BlockType> demCands = new ArrayList<>();

        Map<BlockType, Double> freqs = new HashMap<>();
        for (BlockType cand : CANDIDATES_2020) {
            double f;
            if (cand == BlockType.TRUMP) {
                f = trumpBlockFreq;
                repCands.add(cand);
            } else {
                f = (1.0 - trumpBlockFreq) / (CANDIDATES_2020.size() - 1);
                demCands.add(cand);
            }
            freqs.put(cand, f);
        }
        NamedCandidateGroup demGroup = new NamedCandidateGroup(demCands, "Democrats");
        NamedCandidateGroup repGroup = new NamedCandidateGroup(repCands, "Trump");
        groups.add(demGroup);
        groups.add(repGroup);
        playerGroup = playerParty == 'D' ? demGroup : repGroup;
        return new RaceGameMode(game, stage, CANDIDATES_2020, groups, playerGroup, freqs, getBlockColorProvider(CANDIDATES_2020));
    }

    private IBlockColorProvider getBlockColorProvider(List<BlockType> blockTypes) {
        return isHardMode
                ? blockColorProviderFactory.getEmptyBlockColorProvider()
                : blockColorProviderFactory.getRandomBlockColorProvider(blockTypes, blockBgColors);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean isHardMode() {
        return isHardMode;
    }

    public void setHardMode(boolean hardMode) {
        isHardMode = hardMode;
    }

}
