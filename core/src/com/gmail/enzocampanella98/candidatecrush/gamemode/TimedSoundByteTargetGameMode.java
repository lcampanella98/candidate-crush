package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.Crush;
import com.gmail.enzocampanella98.candidatecrush.board.SimpleBlockGroup;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.BlockProvider;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.EquallyRandomBlockTypeProvider;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.FrequencyIsSoundByteBlockProvider;
import com.gmail.enzocampanella98.candidatecrush.customui.GameInfoBox;
import com.gmail.enzocampanella98.candidatecrush.gamemode.config.GameModeConfig;
import com.gmail.enzocampanella98.candidatecrush.level.ILevelSet;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.SoundByteTargetScoringSystem;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;
import com.gmail.enzocampanella98.candidatecrush.sound.PersistentTierMusicHandler;

import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.LG;
import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.XL;
import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.fontSize;

import java.util.ArrayList;
import java.util.List;

public class TimedSoundByteTargetGameMode extends CCTimeBasedGameMode {

    private PersistentTierMusicHandler tierMusicHandler;
    private BlockProvider myBlockProvider;

    public TimedSoundByteTargetGameMode(CandidateCrush game,
                                           Stage stage,
                                           GameModeConfig config,
                                           ILevelSet levelSet) {
        super(game, stage, config, levelSet);
    }

    @Override
    protected void setHUD() {
        hud = new HeadsUpDisplay(this);
    }

    @Override
    protected void setScoringSystem() {
        scoringSystem = new SoundByteTargetScoringSystem(config.crushVals, config.targetNumSoundBytes);
    }

    @Override
    protected void setBlockProvider() {
        blockProvider = myBlockProvider = new BlockProvider(
                config.candidates,
                CCGameMode.getBlockColorMap(config.isHardMode, config.candidates),
                new EquallyRandomBlockTypeProvider(config.candidates),
                new FrequencyIsSoundByteBlockProvider(config.soundByteFrequency)
        );
    }

    @Override
    protected void setMusicHandler() {
        musicHandler = tierMusicHandler = new PersistentTierMusicHandler(levelSet.getNumSoundTiers(), config.soundTier);
    }

    @Override
    public void playSoundsForCrush(Crush crush) {
        musicHandler.playSound(soundBank.popSound);
        List<SimpleBlockGroup> blockGroupsWithSoundByte = new ArrayList<>();
        if (crush.getLargestGroup().numSoundByteBlocks() > 0) {
            blockGroupsWithSoundByte.add(crush.getLargestGroup());
        }
        for (SimpleBlockGroup bg : crush.getCrushedBlocks()) {
            if (bg.numSoundByteBlocks() > 0 && bg != crush.getLargestGroup()) {
                blockGroupsWithSoundByte.add(bg);
            }
        }
        if (blockGroupsWithSoundByte.size() > 0) {
            SimpleBlockGroup toPlay = blockGroupsWithSoundByte.get(0);
            musicHandler.queueSoundByte(toPlay.getType(), toPlay.getCrushType());
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (myBlockProvider != null) myBlockProvider.dispose();
        if (tierMusicHandler != null) tierMusicHandler.dispose();
    }

    @Override
    protected boolean wonGame() {
        return !isGameTimeUp() && scoringSystem.getPlayerScore() >= config.targetNumSoundBytes;
    }

    @Override
    protected boolean lostGame() {
        return isGameTimeUp();
    }

    private static class HeadsUpDisplay extends HUD {

        private Label labelSoundBytesLeft;
        private Label labelTimeLeft;

        private TimedSoundByteTargetGameMode gameMode;


        protected HeadsUpDisplay(TimedSoundByteTargetGameMode gameMode) {
            super(gameMode);
            this.gameMode = gameMode;
        }

        @Override
        public Vector2 getScoreInfoBoxPosition(SimpleBlockGroup group) {
            return null;
        }

        @Override
        protected void addActorsToTable() {
            // init table elements
            Label.LabelStyle timeLabelStyle = new Label.LabelStyle(defaultFontCache.get(fontSize(XL)), Color.BLACK);
            Label.LabelStyle targetLabelStyle = new Label.LabelStyle(defaultFontCache.get(fontSize(LG)), Color.BLACK);

            labelTimeLeft = new Label(null, timeLabelStyle);
            labelSoundBytesLeft = new Label(null, targetLabelStyle);

            GameInfoBox infoBox;
            infoBox = new GameInfoBox();
            infoBox.add(labelTimeLeft).pad(20f);
            infoBox.pack();

            mainTable.add(infoBox).padBottom(35f);
            mainTable.row();

            infoBox = new GameInfoBox();
            infoBox.add(labelSoundBytesLeft).pad(20f);
            infoBox.pack();

            setFirstCellBelowBoard(mainTable.add(infoBox).expandX());

        }


        @Override
        public void updateLabels(float dt) {
            int remainingSoundBytes = gameMode.config.targetNumSoundBytes - gameMode.scoringSystem.getPlayerScore();
            labelSoundBytesLeft.setText("Remaining: " + remainingSoundBytes);
            labelTimeLeft.setText("" + ((int) Math.ceil(gameMode.getTimeLeft())));
        }
    }
}
