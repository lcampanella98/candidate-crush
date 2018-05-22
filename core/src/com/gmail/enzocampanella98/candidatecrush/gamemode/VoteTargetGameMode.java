package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Queue;
import com.gmail.enzocampanella98.candidatecrush.board.BlockGroup;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.Board;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.VoteTargetScoringSystem;

import static com.gmail.enzocampanella98.candidatecrush.tools.Methods.getCommaSeparatedNumber;

public class VoteTargetGameMode extends CCTimeBasedGameMode {
    private static int boardWidth = 8;

    private ObjectMap<BlockType, Texture> blockTextures;
    private HeadsUpDisplay hud;

    private VoteTargetScoringSystem scoringSystem;
    private int targetScore;


    public VoteTargetGameMode(Table table, double gameLength, int targetScore) {
        super(table, gameLength);

        this.targetScore = targetScore;
        this.blockTextures = BlockType.getBlockTextures();
        this.board = new Board(boardWidth, this.blockTextures);

        int score3 = 100, score4 = 1000, score5 = 3000, scoreT = 2000;
        double nonUserInvokedCrushScale = 1.0 / 5.0;
        this.scoringSystem = new VoteTargetScoringSystem(score3, score4, score5, scoreT, nonUserInvokedCrushScale);


        // create and add HUD
        this.hud = new HeadsUpDisplay(this);

        // add top of HUD
        this.table.add(hud.getTopHUDTable());
        this.table.row();

        // add board to main table
        Table boardTable = new Table();
        boardTable.add(board);
        this.table.add(boardTable);
        this.table.row();

        // add bottom part of HUD
        this.table.add(hud.getBottomHUDTable());

    }

    @Override
    public void onGameStart() {

    }

    @Override
    public void onGameEnd() {

    }

    @Override
    public void update(float dt) {
        // call this after all acting of screen stage has been completed
        super.advanceGameTime(dt);
        if (super.isGameTimeUp()) { // LOSE
            onGameEnd();
            return;
        }
        while (board.getCrushStack().size() > 0) {
            this.scoringSystem.updateScore(board.getCrushStack().pop(), board.userFlippedBlocks);
        }
        this.hud.updateLabels();
        if (this.scoringSystem.getUserScore() >= targetScore) { // WIN
            onGameEnd();
        }
    }

    @Override
    public void dispose() {
        for (ObjectMap.Entry<BlockType, Texture> e : blockTextures) {
            e.value.dispose(); // dispose of block textures
        }
        this.hud.dispose();
    }

    private class HeadsUpDisplay implements Disposable {
        private VoteTargetGameMode gameMode;
        private Table topHUDTable;
        private Table bottomHUDTable;

        private BitmapFont font;
        private Label.LabelStyle labelStyle;

        private Label labelScore;
        private Label labelTimeLeft;
        private Label labelTargetScore;

        public HeadsUpDisplay(VoteTargetGameMode gameMode) {
            this.gameMode = gameMode;

            // init font
            FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("data/fonts/ShareTechMono-Regular.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
            param.size = 70;
            param.color = Color.BLACK;
            font = gen.generateFont(param);

            // init table elements
            labelStyle = new Label.LabelStyle(font, Color.BLACK);

            labelScore = new Label(null, labelStyle);
            labelTimeLeft = new Label(null, labelStyle);
            labelTargetScore = new Label(null, labelStyle);

            updateLabels();

            topHUDTable = new Table();
            topHUDTable.add(labelTimeLeft);
            topHUDTable.row();
            topHUDTable.add(labelTargetScore);

            bottomHUDTable = new Table();
            bottomHUDTable.add(labelScore);
        }

        public Table getTopHUDTable() {
            return topHUDTable;
        }

        public Table getBottomHUDTable() {
            return bottomHUDTable;
        }

        private void updateLabels() {
            labelScore.setText("Votes: " + getCommaSeparatedNumber(gameMode.scoringSystem.getUserScore()));
            labelTargetScore.setText("Target Score: " + getCommaSeparatedNumber(gameMode.targetScore));
            labelTimeLeft.setText("Time Left: " + ((int) Math.ceil(gameMode.getTimeLeft())));
        }

        public void dispose() {
            font.dispose();
        }
    }
}
