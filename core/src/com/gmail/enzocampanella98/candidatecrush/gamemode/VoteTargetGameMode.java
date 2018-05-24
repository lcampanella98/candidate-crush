package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.Board;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.VoteTargetScoringSystem;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.gmail.enzocampanella98.candidatecrush.tools.Methods.getCommaSeparatedNumber;

public class VoteTargetGameMode extends CCTimeBasedGameMode {
    private static int boardWidth = 8;
    private static int defaultGameLength = 60; // 60 seconds
    private static int defaultTargetScore = 20000;

    private VoteTargetScoringSystem scoringSystem;
    private int targetScore;

    private List<BlockType> blockTypes;
    private ObjectMap<BlockType, Texture> blockTextures;

    private Table mainTable;

    public VoteTargetGameMode(Stage stage) {
        this(stage, defaultGameLength, defaultTargetScore);
    }

    public VoteTargetGameMode(Stage stage, double gameLength, int targetScore) {
        super(stage, gameLength);

        this.targetScore = targetScore;

        this.blockTypes = new ArrayList<BlockType>(Arrays.asList(BlockType.values()));
        this.blockTypes.remove(BlockType.BLANK);

        this.blockTextures = BlockType.getBlockTextures(this.blockTypes);

        this.board = new Board(boardWidth, this.blockTypes, this.blockTextures);

        int score3 = 100, score4 = 1000, score5 = 3000, scoreT = 2000;
        double nonUserInvokedCrushScale = 1.0 / 5.0;
        this.scoringSystem = new VoteTargetScoringSystem(score3, score4, score5, scoreT, nonUserInvokedCrushScale);

        this.mainTable = new Table();
        this.mainTable.setFillParent(true);

        // instantiate hud
        this.hud = new HeadsUpDisplay(this);


        // add board to main table
        Table boardTable = new Table();
        boardTable.add(board);
        this.mainTable.add(boardTable);

        this.stage.addActor(mainTable);

    }

    @Override
    public void onGameStart() {

    }

    @Override
    public void onGameEnd() {
        if (super.isGameTimeUp()) { // LOSE

        } else { // WIN

        }
    }

    @Override
    public void update(float dt) {
        // call this after all acting of screen stage has been completed
        if (super.isGameTimeUp()) { // LOSE
            onGameEnd();
            return;
        }

        super.advanceGameTime(dt);

        while (board.getCrushStack().size() > 0) {
            this.scoringSystem.updateScore(board.getCrushStack().pop(), board.userFlippedBlocks);
        }
        if (this.scoringSystem.getUserScore() >= targetScore) { // WIN
            onGameEnd();
        }
    }

    @Override
    public void dispose() {
        this.hud.dispose();
        this.board.dispose();
        for (ObjectMap.Entry<BlockType, Texture> e : blockTextures) {
            e.value.dispose(); // dispose of block textures
        }
        blockTextures.clear();
    }

    private class HeadsUpDisplay extends HUD {
        private VoteTargetGameMode gameMode;
        private Table table;

        private BitmapFont largeFont, medFont, smallFont;
        private int largeFontSize = 120, medFontSize = 90, smallFontSize = 70;

        private Label labelScore;
        private Label labelTimeLeft;
        private Label labelTargetScore;

        public HeadsUpDisplay(VoteTargetGameMode gameMode) {
            this.gameMode = gameMode;

            // init font
            FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("data/fonts/ShareTechMono-Regular.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
            param.color = Color.BLACK;

            param.size = largeFontSize;
            largeFont = gen.generateFont(param);

            param.size = medFontSize;
            medFont = gen.generateFont(param);

            param.size = smallFontSize;
            smallFont = gen.generateFont(param);

            // init table elements
            Label.LabelStyle scoreLabelStyle = new Label.LabelStyle(medFont, Color.BLACK);
            Label.LabelStyle timeLabelStyle = new Label.LabelStyle(largeFont, Color.BLACK);
            Label.LabelStyle targetLabelStyle = new Label.LabelStyle(medFont, Color.BLACK);

            labelScore = new Label(null, scoreLabelStyle);
            labelTimeLeft = new Label(null, timeLabelStyle);
            labelTargetScore = new Label(null, targetLabelStyle);

            updateLabels();

            table = new Table();
//            table.setDebug(true);
            table.setFillParent(true);
            table.top();

            table.add(labelTimeLeft).padTop(50).padBottom(100);
            table.row();
            table.add(labelTargetScore);

            table.row();
            table.add(labelScore).padTop(80 + board.getHeight());

            hudStage.addActor(table);
        }

        private void updateLabels() {
            labelScore.setText("Votes: " + getCommaSeparatedNumber(gameMode.scoringSystem.getUserScore()));
            labelTargetScore.setText("Target: " + getCommaSeparatedNumber(gameMode.targetScore));
            labelTimeLeft.setText("" + ((int) Math.ceil(gameMode.getTimeLeft())));
        }

        public void dispose() {
            largeFont.dispose();
            medFont.dispose();
            smallFont.dispose();
        }

        @Override
        public void render(float dt) {
            hudCam.update();

            updateLabels();

            hudStage.act(dt);
            hudStage.draw();
        }
    }
}
