package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.Board;
import com.gmail.enzocampanella98.candidatecrush.board.FrequencyRandomBlockProvider;
import com.gmail.enzocampanella98.candidatecrush.customui.GameInfoBox;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.Candidate;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.RaceToWhitehouseScoringSystem;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;
import com.gmail.enzocampanella98.candidatecrush.screens.MenuScreen;
import com.gmail.enzocampanella98.candidatecrush.sound.NoLevelMusicHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class RaceToWhitehouseGameMode extends CCGameMode {
    private static int boardWidth = 8;
    private static int defaultNumMoves = 20;
    private static double defaultUserBlockFrequencyAdvantage = 0.1;

    private int numMoves;
    private BlockType userBlockType;
    private List<BlockType> blockTypes;

    private RaceToWhitehouseScoringSystem scoringSystem;

    private Table mainTable;
    private int numMovesLeft;

    public RaceToWhitehouseGameMode(CandidateCrush game, Stage stage, List<BlockType> blockTypes) {
        this(game, stage, blockTypes, defaultNumMoves);
    }

    public RaceToWhitehouseGameMode(CandidateCrush game, Stage stage, List<BlockType> blockTypes, int numMoves) {
        super(game, stage);

        this.numMoves = numMoves;

        this.blockTypes = blockTypes;

        this.userBlockType = this.blockTypes.get(new Random().nextInt(blockTypes.size()));

        Set<String> blockTypeSet = new HashSet<>();
        for (BlockType bt : blockTypes) blockTypeSet.add(bt.getLname());
        musicHandler = new NoLevelMusicHandler(blockTypeSet);
        musicHandler.start();

        Map<BlockType, Double> blockFrequencies = new HashMap<BlockType, Double>();
        double userBlockFrequency = 1.0 / this.blockTypes.size() + defaultUserBlockFrequencyAdvantage;
        double otherBlockFrequency = (1.0 - userBlockFrequency) / (this.blockTypes.size() - 1);
        for (BlockType type : this.blockTypes) {
            blockFrequencies.put(type, type == userBlockType ? userBlockFrequency : otherBlockFrequency);
        }
        blockProvider = new FrequencyRandomBlockProvider(blockFrequencies);

        this.board = new Board(boardWidth, this.blockTypes, musicHandler, blockProvider);

        int score3 = 100, score4 = 1000, score5 = 3000, scoreT = 2000;
        this.scoringSystem = new RaceToWhitehouseScoringSystem(
                userBlockType, this.blockTypes,
                score3, score4, score5, scoreT);

        // set background texture
        String bg1 = "data/img/general/screen_bg_race.png";
        this.backgroundTexture = new Texture(bg1);

        this.mainTable = new Table();
        this.mainTable.setFillParent(true);

        this.mainTable.add(board).center();

        this.stage.addActor(this.mainTable);

        this.hud = new HeadsUpDisplay(this);
        setupInputMultiplexer();
    }

    public int getNumMovesLeft() {
        return this.numMovesLeft;
    }

    @Override
    public void onGameStart() {

    }

    private float messageTimer;

    @Override
    public void onGameEnd() {
        isGameOver = true;
        this.board.pauseInput();

        ((HeadsUpDisplay) hud).showEndGameMessage(win());

        messageTimer = 5;
    }

    protected boolean win() {
        return isGameOver && scoringSystem.getScores().get(0).type == userBlockType;
    }

    @Override
    public void update(float dt) {
        if (isGameOver) {
            messageTimer -= dt;
            if (messageTimer <= 0) {
                game.disposeCurrentScreen();
                game.setScreen(new MenuScreen(game));
            }
            return;
        }
        numMovesLeft = numMoves - board.getNumTotalUserCrushes();

        if (numMovesLeft <= 0 && board.isWaitingForInput()) {
            onGameEnd();
            return;
        }
        while (board.getCrushStack().size() > 0) {
            this.scoringSystem.updateScore(board.getCrushStack().pop(), board.userFlippedBlocks);
        }
    }

    protected class HeadsUpDisplay extends HUD implements Disposable {

        private int largeFontSize = 100, medFontSize = 60, smallFontSize = 40;

        private Label labelNumMovesLeft;

        private ScoreTable topScoreTable;
        private ScoreTable[] otherScoreTables;


        public HeadsUpDisplay(RaceToWhitehouseGameMode gameMode) {
            super(gameMode);

            // init font
            addFontSizes(new int[]{largeFontSize, medFontSize, smallFontSize});

            // init table elements
            Label.LabelStyle topScoreLabelStyle = new Label.LabelStyle(getFont(medFontSize), Color.BLACK);
            Label.LabelStyle numMovesLeftLabelStyle = new Label.LabelStyle(getFont(largeFontSize), Color.BLACK);
            Label.LabelStyle otherScoreLabelStyle = new Label.LabelStyle(getFont(smallFontSize), Color.BLACK);

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

            addExitButton();

            GameInfoBox infoBox;

            infoBox = new GameInfoBox();
            infoBox.add(labelNumMovesLeft).pad(10f);
            infoBox.pack();

            table.add(infoBox).padBottom(20).center();
            table.row();

            Texture userTexture = blockProvider.getBlockTexture(((RaceToWhitehouseGameMode) this.gameMode).userBlockType);
            Image userImg = new Image(userTexture);
            userImg.scaleBy(2.1f);
            userImg.setOrigin(Align.center);

            table.add(userImg).padTop(50).center();
            table.row();

            infoBox = new GameInfoBox();
            infoBox.add(topScoreTable).pad(20f);
            infoBox.pack();

            table.add(infoBox).padTop(100 + board.getHeight()).center().expandX();
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

            infoBox = new GameInfoBox();
            infoBox.add(otherScoresTable).pad(20f);
            infoBox.pack();

            table.add(infoBox).center().padTop(30f);

            hudStage.addActor(table);

            updateLabels();

        }

        public void hideEndGameMessage() {
            clearMessage();
        }

        public void showEndGameMessage(boolean win) {
            String msg;
            if (win) msg = "You win!";
            else msg = "You lose!";

            String endFont = "end-font";
            if (!hasNamedFont(endFont))
                addNewFont(largeFontSize, win ? Color.GREEN : Color.RED, endFont);

            addMessage(msg, getFont(endFont));
        }

        private void updateLabels() {
            // update num moves label
            labelNumMovesLeft.setText(String.valueOf(((RaceToWhitehouseGameMode) gameMode).getNumMovesLeft()));

            // update scores labels
            List<Candidate> scores = ((RaceToWhitehouseGameMode) gameMode).scoringSystem.getScores();
            Candidate topCandidate = scores.get(0);
            topScoreTable.getNameLabel().setText(firstToUpper(topCandidate.type.getLname()));
            topScoreTable.getScoreLabel().setText(String.valueOf(topCandidate.score));

            for (int i = 0; i < scores.size() - 1 && i < otherScoreTables.length; ++i) {
                otherScoreTables[i].getNameLabel().setText(firstToUpper(scores.get(i + 1).type.getLname()));
                otherScoreTables[i].getScoreLabel().setText(String.valueOf(scores.get(i + 1).score));
            }

        }

        private String firstToUpper(String s) {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        }

        @Override
        public void render(float dt) {
            hudCam.update();

            updateLabels();

            if (hasMessage()) {
                batch.begin();
                drawCenteredMessage();
                batch.end();
            }

            hudStage.act(dt);
            hudStage.draw();
        }

        private class ScoreTable extends Table {
            private Label nameLabel;
            private Label scoreLabel;

            public ScoreTable(Label.LabelStyle labelStyle) {
                nameLabel = new Label(null, labelStyle);
                scoreLabel = new Label(null, labelStyle);

                setFillParent(false);

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
