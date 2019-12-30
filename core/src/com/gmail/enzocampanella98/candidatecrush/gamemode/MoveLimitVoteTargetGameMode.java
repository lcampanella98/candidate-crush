package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.EquallyRandomBlockTypeProvider;
import com.gmail.enzocampanella98.candidatecrush.board.IBlockColorProvider;
import com.gmail.enzocampanella98.candidatecrush.board.SimpleBlockGroup;
import com.gmail.enzocampanella98.candidatecrush.customui.GameInfoBox;
import com.gmail.enzocampanella98.candidatecrush.gamemode.config.GameModeConfig;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.VoteTargetScoringSystem;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;
import com.gmail.enzocampanella98.candidatecrush.sound.NoLevelMusicHandler;

public class MoveLimitVoteTargetGameMode extends CCGameMode {

    private NoLevelMusicHandler noLevelMusicHandler;
    private int numMovesLeft;

    public MoveLimitVoteTargetGameMode(CandidateCrush game,
                                       Stage stage,
                                       IBlockColorProvider blockColorProvider,
                                       GameModeConfig config) {
        super(game, stage, blockColorProvider, config);

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
    protected void setBlockTypeProvider() {
        newBlockTypeProvider = new EquallyRandomBlockTypeProvider(config.candidates);
    }

    @Override
    protected void setMusicHandler() {
        musicHandler = noLevelMusicHandler = new NoLevelMusicHandler();
    }

    @Override
    protected boolean wonGame() {
        return scoringSystem.getPlayerScore() >= config.targetScore;
    }

    @Override
    protected boolean isGameOver() {
        // game is over if the player surpassed the score or has no more moves (or both in which case the player WINS)
        return scoringSystem.getPlayerScore() >= config.targetScore || (numMovesLeft == 0 && board.isWaitingForInput());
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
        if (noLevelMusicHandler != null) noLevelMusicHandler.dispose();
    }

    private static class HeadsUpDisplay extends HUD {
        private static int FONT_LG = 100, FONT_MD = 70, FONT_SM = 50;

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
            Label.LabelStyle scoreLabelStyle = new Label.LabelStyle(defaultFontCache.get(FONT_MD), Color.BLACK);
            Label.LabelStyle timeLabelStyle = new Label.LabelStyle(defaultFontCache.get(FONT_LG), Color.BLACK);
            Label.LabelStyle targetLabelStyle = new Label.LabelStyle(defaultFontCache.get(FONT_MD), Color.BLACK);

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

            mainTable.add(infoBox).padTop(45 + gameMode.board.getHeight()).expandX();
        }
    }
}
