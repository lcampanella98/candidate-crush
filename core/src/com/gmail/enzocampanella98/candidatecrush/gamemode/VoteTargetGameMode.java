package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.Board;
import com.gmail.enzocampanella98.candidatecrush.customui.GameInfoBox;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.VoteTargetScoringSystem;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;
import com.gmail.enzocampanella98.candidatecrush.screens.MenuScreen;
import com.gmail.enzocampanella98.candidatecrush.sound.EqualMusicHandler;
import com.gmail.enzocampanella98.candidatecrush.sound.MusicHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.gmail.enzocampanella98.candidatecrush.tools.Methods.getCommaSeparatedNumber;

public class VoteTargetGameMode extends CCTimeBasedGameMode {
    private static int boardWidth = 8;
    private static int defaultGameLength = 60; // 60 seconds
    private static int defaultTargetScore = 20000;

    private VoteTargetScoringSystem scoringSystem;
    private int targetScore;

    private List<BlockType> blockTypes;

    private Table mainTable;
    private MusicHandler musicHandler;

    public VoteTargetGameMode(CandidateCrush game, Stage stage, List<BlockType> blockTypes) {
        this(game, stage, blockTypes, defaultGameLength, defaultTargetScore);
    }

    public VoteTargetGameMode(CandidateCrush game, Stage stage, List<BlockType> blockTypes, double gameLength, int targetScore) {
        super(game, stage, gameLength);

        this.targetScore = targetScore;

        this.blockTypes = blockTypes;
        this.blockTypes.remove(BlockType.BLANK);

        this.blockTextures = BlockType.getBlockTextures(this.blockTypes);

        Set<String> blockTypeSet = new HashSet<>();
        for (BlockType bt : blockTypes) blockTypeSet.add(bt.getLname());
        musicHandler = new EqualMusicHandler(blockTypeSet);
        musicHandler.start();

        this.board = new Board(boardWidth, this.blockTypes, this.blockTextures, musicHandler);

        int score3 = 100, score4 = 1000, score5 = 3000, scoreT = 2000;
        double nonUserInvokedCrushScale = 1.0 / 5.0;
        this.scoringSystem = new VoteTargetScoringSystem(score3, score4, score5, scoreT, nonUserInvokedCrushScale);

        // set background texture
        String bg1 = "data/img/general/screen_bg_votetarget.png";
        this.backgroundTexture = new Texture(bg1);

        this.mainTable = new Table();
        this.mainTable.setFillParent(true);

        // instantiate hud
        this.hud = new HeadsUpDisplay(this);
        setupInputMultiplexer();


        // add board to main table
        Table boardTable = new Table();
        boardTable.add(board);
        this.mainTable.add(boardTable);

        this.stage.addActor(mainTable);
    }

    @Override
    public void onGameStart() {

    }

    private float messageTimer;

    @Override
    public void onGameEnd() {
        this.isGameOver = true;
        this.board.pauseInput();


        ((HeadsUpDisplay) hud).showEndGameMessage(win());
        messageTimer = 5;
    }

    public boolean win() {
        return isGameOver && !super.isGameTimeUp();
    }

    @Override
    public void dispose() {
        super.dispose();
        musicHandler.dispose();
    }

    @Override
    public void update(float dt) {
        if (isGameOver) {
            messageTimer -= dt;
            if (messageTimer <= 0) {
                game.getScreen().dispose();
                game.setScreen(new MenuScreen(game));
            }
            return;
        }

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

    private class HeadsUpDisplay extends HUD {

        private int largeFontSize = 100, medFontSize = 70, smallFontSize = 50;

        private Label labelScore;
        private Label labelTimeLeft;
        private Label labelTargetScore;

        public HeadsUpDisplay(VoteTargetGameMode gameMode) {
            super(gameMode);

            // init font
            addFontSizes(new int[]{smallFontSize, medFontSize, largeFontSize});

            // init table elements
            Label.LabelStyle scoreLabelStyle = new Label.LabelStyle(getFont(medFontSize), Color.BLACK);
            Label.LabelStyle timeLabelStyle = new Label.LabelStyle(getFont(largeFontSize), Color.BLACK);
            Label.LabelStyle targetLabelStyle = new Label.LabelStyle(getFont(medFontSize), Color.BLACK);

            labelScore = new Label(null, scoreLabelStyle);
            labelTimeLeft = new Label(null, timeLabelStyle);
            labelTargetScore = new Label(null, targetLabelStyle);

            updateLabels();

            table = new Table();
//            table.setDebug(true);
            table.setFillParent(true);
            table.top();

            addExitButton();

            GameInfoBox infoBox;

            infoBox = new GameInfoBox();
            infoBox.add(labelTimeLeft).pad(20f);
            infoBox.pack();

            table.add(infoBox).padBottom(35);
            table.row();

            infoBox = new GameInfoBox();
            infoBox.add(labelTargetScore).pad(20f);
            infoBox.pack();

            table.add(infoBox);
            table.row();

            infoBox = new GameInfoBox();
            infoBox.add(labelScore).pad(20f);
            infoBox.pack();

            table.add(infoBox).padTop(45 + board.getHeight()).expandX();

            hudStage.addActor(table);
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
            VoteTargetGameMode gameMode = (VoteTargetGameMode) this.gameMode;
            labelScore.setText("Votes: " + getCommaSeparatedNumber(gameMode.scoringSystem.getUserScore()));
            labelTargetScore.setText("Target: " + getCommaSeparatedNumber(gameMode.targetScore));
            labelTimeLeft.setText("" + ((int) Math.ceil(gameMode.getTimeLeft())));
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
    }
}
