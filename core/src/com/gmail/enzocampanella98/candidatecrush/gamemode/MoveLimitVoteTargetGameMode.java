package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.SimpleBlockGroup;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.AlwaysFalseSoundByteBlockProvider;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.BlockProvider;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.EquallyRandomBlockTypeProvider;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.FrequencyIsSoundByteBlockProvider;
import com.gmail.enzocampanella98.candidatecrush.customui.GameInfoBox;
import com.gmail.enzocampanella98.candidatecrush.gamemode.config.GameModeConfig;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.VoteTargetScoringSystem;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;
import com.gmail.enzocampanella98.candidatecrush.sound.PersistentTierMusicHandler;

import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.LG;
import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.MD;
import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.SM;
import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.XL;

import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_WIDTH;
import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.fontSize;

public class MoveLimitVoteTargetGameMode extends CCGameMode {

    private PersistentTierMusicHandler tierMusicHandler;
    private BlockProvider myBlockProvider;
    private int numMovesLeft;

    public MoveLimitVoteTargetGameMode(CandidateCrush game,
                                       Stage stage,
                                       GameModeConfig config) {
        super(game, stage, config);

        this.numMovesLeft = config.numMoves;
    }

    @Override
    protected void setHUD() {
        hud = new HeadsUpDisplay(this);
    }

    @Override
    protected void setScoringSystem() {
        scoringSystem = new VoteTargetScoringSystem(config.crushVals, config.nonUserInvokedCrushScale);
    }

    @Override
    protected void setBlockProvider() {
        blockProvider = myBlockProvider = new BlockProvider(
                config.candidates,
                GameModeFactory.getBlockColorMap(config.isHardMode, config.candidates),
                new EquallyRandomBlockTypeProvider(config.candidates),
                new AlwaysFalseSoundByteBlockProvider()
        );
    }

    @Override
    protected void setMusicHandler() {
        musicHandler = tierMusicHandler = new PersistentTierMusicHandler(config.soundTier);
    }

    @Override
    protected boolean wonGame() {
        return scoringSystem.getPlayerScore() >= config.targetScore;
    }

    @Override
    protected boolean lostGame() {
        return numMovesLeft <= 0
                && board.isWaitingForInput()
                && scoringSystem.getPlayerScore() < config.targetScore;
    }

    @Override
    public void restartGame() {
        super.restartGame();
        numMovesLeft = config.numMoves;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        numMovesLeft = config.numMoves - board.getNumTotalUserCrushes();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (tierMusicHandler != null) tierMusicHandler.dispose();
        if (myBlockProvider != null) myBlockProvider.dispose();
    }

    private static class HeadsUpDisplay extends HUD {

        private Label labelScore;
        private Label labelMovesLeft;
        private Label labelTargetScore;
        private MoveLimitVoteTargetGameMode gameMode;

        HeadsUpDisplay(MoveLimitVoteTargetGameMode gameMode) {
            super(gameMode);
            this.gameMode = gameMode;
        }

        @Override
        public void updateLabels(float dt) {
            labelScore.setText("Votes: " + scoreText(gameMode.scoringSystem.getPlayerScore()));
            labelTargetScore.setText("Target: " + scoreText(gameMode.config.targetScore));
            labelMovesLeft.setText("" + ((int) Math.ceil(gameMode.numMovesLeft)));
        }

        @Override
        public Vector2 getScoreInfoBoxPosition(SimpleBlockGroup group) {
            return labelScore.localToStageCoordinates(new Vector2(labelScore.getWidth() / 2, labelScore.getHeight() / 2));
        }

        @Override
        protected void addActorsToTable() {
            // init table elements
            Label.LabelStyle scoreLabelStyle = new Label.LabelStyle(defaultFontCache.get(fontSize(LG)), Color.BLACK);
            Label.LabelStyle timeLabelStyle = new Label.LabelStyle(defaultFontCache.get(fontSize(LG)), Color.BLACK);
            Label.LabelStyle targetLabelStyle = new Label.LabelStyle(defaultFontCache.get(fontSize(LG)), Color.BLACK);

            labelScore = new Label(null, scoreLabelStyle);
            labelMovesLeft = new Label(null, timeLabelStyle);
            labelTargetScore = new Label(null, targetLabelStyle);

            GameInfoBox infoBox;

            infoBox = new GameInfoBox();
            infoBox.add(labelMovesLeft).pad(20f);
            infoBox.pack();

            mainTable.add(infoBox).padBottom(35);
            mainTable.row();

            infoBox = new GameInfoBox();
            infoBox.add(labelScore).pad(20f);
            infoBox.pack();

            mainTable.add(infoBox);
            mainTable.row();

            infoBox = new GameInfoBox();
            infoBox.add(labelTargetScore).pad(20f);
            infoBox.pack();
            setFirstCellBelowBoard(mainTable.add(infoBox).expandX());
        }
    }
}
