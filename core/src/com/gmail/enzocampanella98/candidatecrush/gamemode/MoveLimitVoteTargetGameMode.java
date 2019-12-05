package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.BlockProvider;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.Board;
import com.gmail.enzocampanella98.candidatecrush.board.EquallyRandomBlockProvider;
import com.gmail.enzocampanella98.candidatecrush.board.IBlockColorProvider;
import com.gmail.enzocampanella98.candidatecrush.customui.GameInfoBox;
import com.gmail.enzocampanella98.candidatecrush.fonts.FontGenerator;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.VoteTargetScoringSystem;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;
import com.gmail.enzocampanella98.candidatecrush.screens.MenuScreen;
import com.gmail.enzocampanella98.candidatecrush.sound.MusicHandler;
import com.gmail.enzocampanella98.candidatecrush.sound.NoLevelMusicHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.gmail.enzocampanella98.candidatecrush.tools.Methods.getCommaSeparatedNumber;

public class MoveLimitVoteTargetGameMode extends CCGameMode {
    private static double nonUserInvokedCrushScale = 2.0 / 5.0;
    private static int boardWidth = 8;
    private static int defaultNumMoves = 30;
    private static int defaultTargetScore = 20000;

    private int targetScore;
    private int numMovesLeft;

    private List<BlockType> blockTypes;

    private Table mainTable;
    private MusicHandler musicHandler;
    private BlockProvider blockProvider;

    public MoveLimitVoteTargetGameMode(CandidateCrush game, Stage stage, List<BlockType> blockTypes, IBlockColorProvider blockColorProvider) {
        this(game, stage, blockColorProvider, blockTypes, defaultNumMoves, defaultTargetScore);
    }

    public MoveLimitVoteTargetGameMode(CandidateCrush game, Stage stage, IBlockColorProvider blockColorProvider, List<BlockType> blockTypes, int numMoves, int targetScore) {
        super(game, stage, blockColorProvider);

        this.targetScore = targetScore;
        this.numMovesLeft = numMoves;

        this.blockTypes = blockTypes;
        this.blockTypes.remove(BlockType.BLANK);

        Set<String> blockTypeSet = new HashSet<>();
        for (BlockType bt : blockTypes) blockTypeSet.add(bt.getLname());
        musicHandler = new NoLevelMusicHandler(blockTypeSet);
        musicHandler.start();

        blockProvider = new EquallyRandomBlockProvider(blockTypes, this.blockColorProvider);

        this.board = new Board(boardWidth, this.blockTypes, musicHandler, blockProvider);

        int score3 = 800, score4 = 1000, score5 = 3000, scoreT = 2000;
        this.scoringSystem = new VoteTargetScoringSystem(score3, score4, score5, scoreT, nonUserInvokedCrushScale);

        this.mainTable = new Table();
        this.mainTable.setFillParent(true);

        // instantiate hud
        this.hud = new MoveLimitVoteTargetGameMode.HeadsUpDisplay(this);
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

        ((MoveLimitVoteTargetGameMode.HeadsUpDisplay) hud).showEndGameMessage(win());
        messageTimer = 5;
    }

    public boolean win() {
        return isGameOver && scoringSystem.getPlayerScore() >= targetScore;
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

        while (board.getCrushStack().size() > 0) {
            scoringSystem.updateScore(board.getCrushStack().pop(), board.userFlippedBlocks);
            if (board.userFlippedBlocks && --numMovesLeft == 0) break;
        }
        // game is over if the player surpassed the score or has no more moves (or both in which case the player WINS)
        if (scoringSystem.getPlayerScore() >= targetScore || numMovesLeft == 0) {
            onGameEnd();
        }
    }

    private class HeadsUpDisplay extends HUD {

        private int FONT_LG = 100, FONT_MD = 70, FONT_SM = 50;

        private Label labelScore;
        private Label labelMovesLeft;
        private Label labelTargetScore;
        private BitmapFont endFont;
        private MoveLimitVoteTargetGameMode gameMode;

        public HeadsUpDisplay(MoveLimitVoteTargetGameMode gameMode) {
            super(gameMode);
            this.gameMode = gameMode;

            // init table elements
            Label.LabelStyle scoreLabelStyle = new Label.LabelStyle(fontCache.get(FONT_MD), Color.BLACK);
            Label.LabelStyle timeLabelStyle = new Label.LabelStyle(fontCache.get(FONT_LG), Color.BLACK);
            Label.LabelStyle targetLabelStyle = new Label.LabelStyle(fontCache.get(FONT_MD), Color.BLACK);

            labelScore = new Label(null, scoreLabelStyle);
            labelMovesLeft = new Label(null, timeLabelStyle);
            labelTargetScore = new Label(null, targetLabelStyle);

            updateLabels();

            table = new Table();
//            table.setDebug(true);
            table.setFillParent(true);
            table.top();

            addExitButton();

            GameInfoBox infoBox;

            infoBox = new GameInfoBox();
            infoBox.add(labelMovesLeft).pad(20f);
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

            endFont = new FontGenerator(win ? Color.GREEN : Color.RED).generateFont(FONT_LG);
            addMessage(msg, endFont);
        }

        private void updateLabels() {
            labelScore.setText("Votes: " + getCommaSeparatedNumber(gameMode.scoringSystem.getPlayerScore()));
            labelTargetScore.setText("Target: " + getCommaSeparatedNumber(gameMode.targetScore));
            labelMovesLeft.setText("" + ((int) Math.ceil(gameMode.numMovesLeft)));
        }

        @Override
        public void dispose() {
            super.dispose();
            if (endFont != null) endFont.dispose();
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
