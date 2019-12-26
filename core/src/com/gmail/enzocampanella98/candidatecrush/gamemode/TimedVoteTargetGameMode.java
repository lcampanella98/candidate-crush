package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.Board;
import com.gmail.enzocampanella98.candidatecrush.board.EquallyRandomBlockTypeProvider;
import com.gmail.enzocampanella98.candidatecrush.board.IBlockColorProvider;
import com.gmail.enzocampanella98.candidatecrush.board.SimpleBlockGroup;
import com.gmail.enzocampanella98.candidatecrush.customui.GameInfoBox;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.VoteTargetScoringSystem;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;
import com.gmail.enzocampanella98.candidatecrush.sound.MusicHandler;
import com.gmail.enzocampanella98.candidatecrush.sound.NoLevelMusicHandler;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.gmail.enzocampanella98.candidatecrush.tools.Methods.getCommaSeparatedNumber;

public class TimedVoteTargetGameMode extends CCTimeBasedGameMode {
    private static double nonUserInvokedCrushScale = 2.0 / 5.0;
    private static int boardWidth = 8;
    private static int defaultGameLength = 60; // 60 seconds
    private static int defaultTargetScore = 20000;

    private int targetScore;

    private List<BlockType> blockTypes;

    private Table mainTable;
    private MusicHandler musicHandler;

    public TimedVoteTargetGameMode(CandidateCrush game, Stage stage, IBlockColorProvider blockColorProvider, List<BlockType> blockTypes) {
        this(game, stage, blockColorProvider, blockTypes, defaultGameLength, defaultTargetScore);
    }

    public TimedVoteTargetGameMode(CandidateCrush game, Stage stage, IBlockColorProvider blockColorProvider, List<BlockType> blockTypes, double gameLength, int targetScore) {
        super(game, stage, blockColorProvider, blockTypes, gameLength);

        this.targetScore = targetScore;

        this.blockTypes = blockTypes;
        this.blockTypes.remove(BlockType.BLANK);

        musicHandler = new NoLevelMusicHandler();
        musicHandler.start();

        newBlockTypeProvider = new EquallyRandomBlockTypeProvider(blockTypes);

        this.board = new Board(boardWidth, musicHandler, newBlockTypeProvider, blockTextureProvider, boardAnalyzer, boardInitializer);

        int score3 = 500, score4 = 1200, score5 = 3000, scoreT = 2000;
        this.scoringSystem = new VoteTargetScoringSystem(score3, score4, score5, scoreT, nonUserInvokedCrushScale);

        this.mainTable = new Table();
        this.mainTable.setFillParent(true);

        // instantiate hud
        this.hud = new HeadsUpDisplay(this);
        hud.initStage();

        // add board to main table
        Table boardTable = new Table();
        boardTable.add(board);
        this.mainTable.add(boardTable);

        this.stage.addActor(mainTable);
    }

    @Override
    protected boolean wonGame() {
        return !super.isGameTimeUp();
    }

    @Override
    protected boolean isGameOver() {
        return super.isGameTimeUp() || scoringSystem.getPlayerScore() >= targetScore;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (isGameStarted() && !isGameOver()) {
            super.advanceGameTime(dt);
        }
    }

    private static class HeadsUpDisplay extends HUD {

        private static int FONT_LG = 100, FONT_MD = 70, FONT_SM = 50;

        private Label labelScore;
        private Label labelTimeLeft;
        private Label labelTargetScore;

        private TimedVoteTargetGameMode gameMode;

        HeadsUpDisplay(TimedVoteTargetGameMode gameMode) {
            super(gameMode);
            this.gameMode = gameMode;
        }

        @Override
        public void updateLabels(float dt) {
            labelScore.setText("Votes: " + getCommaSeparatedNumber(gameMode.scoringSystem.getPlayerScore()));
            labelTargetScore.setText("Target: " + getCommaSeparatedNumber(gameMode.targetScore));
            labelTimeLeft.setText("" + ((int) Math.ceil(gameMode.getTimeLeft())));
        }

        @Override
        public Vector2 getScoreInfoBoxPosition(SimpleBlockGroup group) {
            return labelScore.localToStageCoordinates(new Vector2(labelScore.getWidth() / 2, labelScore.getHeight() / 2));
        }

        @Override
        protected void addActorsToTable() {
            // init table elements
            Label.LabelStyle scoreLabelStyle = new Label.LabelStyle(fontCache.get(FONT_MD), Color.BLACK);
            Label.LabelStyle timeLabelStyle = new Label.LabelStyle(fontCache.get(FONT_LG), Color.BLACK);
            Label.LabelStyle targetLabelStyle = new Label.LabelStyle(fontCache.get(FONT_MD), Color.BLACK);

            labelScore = new Label(null, scoreLabelStyle);
            labelTimeLeft = new Label(null, timeLabelStyle);
            labelTargetScore = new Label(null, targetLabelStyle);

            GameInfoBox infoBox;
            infoBox = new GameInfoBox();
            infoBox.add(labelTimeLeft).pad(20f);
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

        @Override
        public Collection<String> getGameInfoDialogTextLines() {
            return Arrays.asList(
                    "Reach " + gameMode.targetScore + " votes",
                    "before time runs out!"
            );
        }

    }
}
