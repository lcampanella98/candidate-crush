package com.gmail.enzocampanella98.candidatecrush.gamemode.config;

import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.customui.GameInstructionRow;
import com.gmail.enzocampanella98.candidatecrush.level.GameModeType;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.CrushVals;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.NamedCandidateGroup;

import java.util.Collection;
import java.util.List;

public class GameModeConfig {
    public GameModeType gameModeType;
    public int boardSize;
    public float singleBlockDropTime;
    public boolean isHardMode;
    public CrushVals crushVals;
    public BlockType primaryPlayer;
    public char playerParty;
    public int numMoves;
    public int targetScore;
    public double gameLength;
    public double nonUserInvokedCrushScale;
    public List<BlockType> candidates;

    // used for elections
    public NamedCandidateGroup electionGroup1;
    public NamedCandidateGroup electionGroup2;

    public int levelNum;
    public Collection<GameInstructionRow> instructionRows;
    public int soundTier;
    public String levelSet;
    public int targetNumSoundBytes;
    public double soundByteFrequency;
    public boolean showCrushLabels;

    public static class Builder {
        private final GameModeConfig config;

        public Builder() {
            config = new GameModeConfig();
        }

        public Builder gameModeType(GameModeType gameModeType) {
            config.gameModeType = gameModeType;
            return this;
        }

        public Builder boardSize(int boardSize) {
            config.boardSize = boardSize;
            return this;
        }

        public Builder singleBlockDropTime(float singleBlockDropTime) {
            config.singleBlockDropTime = singleBlockDropTime;
            return this;
        }

        public Builder isHardMode(boolean isHardMode) {
            config.isHardMode = isHardMode;
            return this;
        }

        public Builder crushVals(CrushVals crushVals) {
            config.crushVals = crushVals;
            return this;
        }

        public Builder primaryPlayer(BlockType primaryPlayer) {
            config.primaryPlayer = primaryPlayer;
            return this;
        }

        public Builder playerParty(char playerParty) {
            config.playerParty = playerParty;
            return this;
        }

        public Builder numMoves(int numMoves) {
            config.numMoves = numMoves;
            return this;
        }

        public Builder targetScore(int targetScore) {
            config.targetScore = targetScore;
            return this;
        }

        public Builder gameLength(double gameLength) {
            config.gameLength = gameLength;
            return this;
        }

        public Builder nonUserInvokedCrushScale(double nonUserInvokedCrushScale) {
            config.nonUserInvokedCrushScale = nonUserInvokedCrushScale;
            return this;
        }

        public Builder candidates(List<BlockType> candidates) {
            config.candidates = candidates;
            return this;
        }

        public Builder levelNum(int levelNum) {
            config.levelNum = levelNum;
            return this;
        }

        public Builder instructionRows(Collection<GameInstructionRow> instructionRows) {
            config.instructionRows = instructionRows;
            return this;
        }

        public Builder soundTier(int soundTier) {
            config.soundTier = soundTier;
            return this;
        }

        public Builder levelSet(String levelSet) {
            config.levelSet = levelSet;
            return this;
        }

        public Builder targetNumSoundBytes(int targetNumSoundBytes) {
            config.targetNumSoundBytes = targetNumSoundBytes;
            return this;
        }

        public Builder soundByteFrequency(double soundByteFrequency) {
            config.soundByteFrequency = soundByteFrequency;
            return this;
        }

        public Builder showCrushLabels(boolean showCrushLabels) {
            config.showCrushLabels = showCrushLabels;
            return this;
        }

        public GameModeConfig build() {
            return config;
        }
    }
}
