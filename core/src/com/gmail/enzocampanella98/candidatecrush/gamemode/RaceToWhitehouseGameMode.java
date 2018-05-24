package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.Board;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.Candidate;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.RaceToWhitehouseScoringSystem;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RaceToWhitehouseGameMode extends CCGameMode {
    private static int boardWidth = 8;
    private static int defaultNumMoves = 20;

    private int numMoves;
    private BlockType userBlockType;
    private List<BlockType> blockTypes;
    private ObjectMap<BlockType, Texture> blockTextures;

    private RaceToWhitehouseScoringSystem scoringSystem;

    private Table mainTable;
    private int numMovesLeft;

    public RaceToWhitehouseGameMode(Stage stage) {
        this(stage, defaultNumMoves);
    }

    public RaceToWhitehouseGameMode(Stage stage, int numMoves) {
        super(stage);

        this.numMoves = numMoves;

        this.blockTypes = new ArrayList<BlockType>(Arrays.asList(BlockType.values()));
        this.blockTypes.remove(BlockType.BLANK);

        this.userBlockType = this.blockTypes.get(new Random().nextInt(blockTypes.size()));

        this.blockTextures = BlockType.getBlockTextures(this.blockTypes);

        this.board = new Board(boardWidth, this.blockTypes, this.blockTextures);

        int score3 = 100, score4 = 1000, score5 = 3000, scoreT = 2000;
        this.scoringSystem = new RaceToWhitehouseScoringSystem(
                userBlockType, this.blockTypes,
                score3, score4, score5, scoreT);

        this.mainTable = new Table();
        this.mainTable.setFillParent(true);

        this.mainTable.add(board).center();

        this.stage.addActor(this.mainTable);

        this.hud = new HeadsUpDisplay(this);
    }

    public int getNumMovesLeft() {
        return this.numMovesLeft;
    }

    @Override
    public void onGameStart() {

    }

    @Override
    public void onGameEnd() {

    }

    @Override
    public void update(float dt) {
        numMovesLeft = numMoves - board.getNumTotalUserCrushes();

        if (numMovesLeft >= 0 && board.isWaitingForInput()) {
            onGameEnd();
            return;
        }
        while (board.getCrushStack().size() > 0) {
            this.scoringSystem.updateScore(board.getCrushStack().pop(), board.userFlippedBlocks);
        }
    }

    @Override
    public void dispose() {
        this.board.dispose();
        this.hud.dispose();
        for (ObjectMap.Entry<BlockType, Texture> e : blockTextures) {
            e.value.dispose(); // dispose of block textures
        }
        blockTextures.clear();
    }

    private class HeadsUpDisplay extends HUD implements Disposable {
        private RaceToWhitehouseGameMode gameMode;
        private Table table;

        private BitmapFont largeFont, medFont, smallFont;
        private int largeFontSize = 120, medFontSize = 60, smallFontSize = 40;

        private Label labelNumMovesLeft;

        private ScoreTable topScoreTable;
        private ScoreTable[] otherScoreTables;


        public HeadsUpDisplay(RaceToWhitehouseGameMode gameMode) {
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
            Label.LabelStyle topScoreLabelStyle = new Label.LabelStyle(medFont, Color.BLACK);
            Label.LabelStyle numMovesLeftLabelStyle = new Label.LabelStyle(largeFont, Color.BLACK);
            Label.LabelStyle otherScoreLabelStyle = new Label.LabelStyle(smallFont, Color.BLACK);

            labelNumMovesLeft = new Label(null, numMovesLeftLabelStyle);

            topScoreTable = new ScoreTable(topScoreLabelStyle);
            otherScoreTables = new ScoreTable[blockTypes.size() - 1];
            for (int i = 0; i < otherScoreTables.length; ++i) {
                otherScoreTables[i] = new ScoreTable(otherScoreLabelStyle);
            }


            table = new Table();
//            table.setDebug(true);
            table.setFillParent(true);
            table.top();

            table.add(labelNumMovesLeft).padTop(20).padBottom(20).center();
            table.row();

            Texture userTexture = this.gameMode.blockTextures.get(this.gameMode.userBlockType);
            Image userImg = new Image(userTexture);
            userImg.scaleBy(2.5f);
            userImg.setOrigin(Align.center);

            table.add(userImg).padTop(70).center();
            table.row();

            table.add(topScoreTable).padTop(120+board.getHeight()).center();
            table.row();

            int numScoresPerRow = 3;
            int curScoresInRow = 0;
            Table otherScoresTable = new Table();
            for (ScoreTable scoreTable : otherScoreTables) {
                if (curScoresInRow >= numScoresPerRow) {
                    otherScoresTable.row();
                    curScoresInRow = 0;
                }
                otherScoresTable.add(scoreTable).expandX().padLeft(30).padRight(30);
            }
            table.add(otherScoresTable).center();

            hudStage.addActor(table);

            updateLabels();

        }

        private void updateLabels() {
            // update num moves label
            labelNumMovesLeft.setText(String.valueOf(gameMode.getNumMovesLeft()));

            // update scores labels
            List<Candidate> scores = gameMode.scoringSystem.getScores();
            Candidate topCandidate = scores.get(0);
            topScoreTable.getNameLabel().setText(topCandidate.type.getFriendlyName());
            topScoreTable.getScoreLabel().setText(String.valueOf(topCandidate.score));

            for (int i = 0; i < scores.size() - 1 && i < otherScoreTables.length; ++i) {
                otherScoreTables[i].getNameLabel().setText(scores.get(i + 1).type.getFriendlyName());
                otherScoreTables[i].getScoreLabel().setText(String.valueOf(scores.get(i + 1).score));
            }

        }

        @Override
        public void render(float dt) {
            hudCam.update();

            updateLabels();

            hudStage.act(dt);
            hudStage.draw();
        }

        @Override
        public void dispose() {
            largeFont.dispose();
            medFont.dispose();
            smallFont.dispose();
        }

        private class ScoreTable extends Table {
            private Label nameLabel;
            private Label scoreLabel;

            public ScoreTable(Label.LabelStyle labelStyle) {
                nameLabel = new Label(null, labelStyle);
                scoreLabel = new Label(null, labelStyle);

                add(nameLabel);
                row();
                add(scoreLabel);
            }

            public Label getNameLabel() {
                return nameLabel;
            }

            public Label getScoreLabel() {
                return scoreLabel;
            }
        }
    }


}
