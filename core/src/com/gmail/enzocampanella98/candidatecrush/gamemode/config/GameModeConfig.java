package com.gmail.enzocampanella98.candidatecrush.gamemode.config;

import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.CrushVals;

import java.util.Collection;
import java.util.List;

public class GameModeConfig {
    public int boardSize;
    public boolean isHardMode;
    public CrushVals crushVals;
    public BlockType primaryPlayer;
    public char playerParty;
    public int numMoves;
    public int targetScore;
    public double gameLength;
    public double nonUserInvokedCrushScale;
    public List<BlockType> candidates;
    public int levelNum;
    public Collection<String> instructionLines;
    public int soundTier;
    public int targetNumSoundBytes;
    public double soundByteFrequency;
    public boolean showCrushLabels;

    public static class Builder {
        private final GameModeConfig config;

        public Builder() {
            config = new GameModeConfig();
        }

        public Builder boardSize(int boardSize) {
            config.boardSize = boardSize;
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

        public Builder instructionLines(Collection<String> instructionLines) {
            config.instructionLines = instructionLines;
            return this;
        }

        public Builder soundTier(int soundTier) {
            config.soundTier = soundTier;
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
