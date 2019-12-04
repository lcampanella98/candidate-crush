package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.gmail.enzocampanella98.candidatecrush.fonts.FontGenerator;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.NamedCandidateGroup;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.RaceScoringSystem;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;
import com.gmail.enzocampanella98.candidatecrush.screens.MenuScreen;
import com.gmail.enzocampanella98.candidatecrush.sound.NoLevelMusicHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RaceGameMode extends CCGameMode {
    private static int boardWidth = 8;
    private static int defaultNumMoves = 20;
    private final List<NamedCandidateGroup> groups;
    private final NamedCandidateGroup playerGroup;
    private final Map<BlockType, Double> blockFrequencies;

    private int numMoves;
    private List<BlockType> blockTypes;

    private Table mainTable;

    private int numMovesLeft;
    private float messageTimer;


    public RaceGameMode(CandidateCrush game,
                        Stage stage,
                        final List<BlockType> blockTypes,
                        List<NamedCandidateGroup> groups,
                        NamedCandidateGroup playerGroup,
                        Map<BlockType, Double> blockFrequencies) {
        this(game, stage, blockTypes, groups, playerGroup, blockFrequencies, defaultNumMoves);
    }

    public RaceGameMode(CandidateCrush game,
                        Stage stage,
                        List<BlockType> blockTypes,
                        List<NamedCandidateGroup> groups,
                        NamedCandidateGroup playerGroup,
                        Map<BlockType, Double> blockFrequencies,
                        int numMoves) {
        super(game, stage);

        this.numMoves = numMoves;
        this.blockTypes = blockTypes;
        this.groups = groups;
        this.playerGroup = playerGroup;
        this.blockFrequencies = blockFrequencies;

        Set<String> blockTypeSet = new HashSet<>();
        for (BlockType bt : blockTypes) blockTypeSet.add(bt.getLname());
        musicHandler = new NoLevelMusicHandler(blockTypeSet);
        musicHandler.start();

        blockProvider = new FrequencyRandomBlockProvider(this.blockFrequencies);

        this.board = new Board(boardWidth, this.blockTypes, musicHandler, blockProvider);

        int score3 = 100, score4 = 1000, score5 = 3000, scoreT = 2000;
        this.scoringSystem = new RaceScoringSystem(
                groups, playerGroup, score3, score4, score5, scoreT);

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

    @Override
    public void onGameEnd() {
        isGameOver = true;
        this.board.pauseInput();

        ((HeadsUpDisplay) hud).showEndGameMessage(win());

        messageTimer = 5;
    }

    protected boolean win() {
        return isGameOver && ((RaceScoringSystem)scoringSystem).getGroups().get(0) == playerGroup;
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

        private static final int FONT_LG = 100, FONT_MD = 60, FONT_SM = 40;

        private Label labelNumMovesLeft;

        private ScoreTable topScoreTable;
        private List<ScoreTable> otherScoreTables;

        private BitmapFont endFont;

        public HeadsUpDisplay(RaceGameMode gameMode) {
            super(gameMode);

            // init table elements
            Label.LabelStyle topScoreLabelStyle = new Label.LabelStyle(fontCache.get(FONT_MD), Color.BLACK);
            Label.LabelStyle numMovesLeftLabelStyle = new Label.LabelStyle(fontCache.get(FONT_LG), Color.BLACK);
            Label.LabelStyle otherScoreLabelStyle = new Label.LabelStyle(fontCache.get(FONT_SM), Color.BLACK);

            labelNumMovesLeft = new Label(null, numMovesLeftLabelStyle);

            topScoreTable = new ScoreTable(topScoreLabelStyle);
            otherScoreTables = new ArrayList<ScoreTable>();
            for (int i = 0; i < groups.size()-1; i++) {
                otherScoreTables.add(new ScoreTable(otherScoreLabelStyle));
            }

            table = new Table();

//            table.setDebug(true);
            table.setFillParent(true);
            table.top();

            addExitButton();

            GameInfoBox infoBox;

            infoBox = new GameInfoBox();
            infoBox.add(labelNumMovesLeft).pad(10f).center();
            infoBox.pack();

            table.add(infoBox).center();
            table.row();

            table.add(new Label("You play", otherScoreLabelStyle)).padTop(10f).center();
            table.row();
            Table playerViewTable = new Table();
            for (BlockType bt : gameMode.playerGroup.getCandidates()) {
                Texture userTexture = blockProvider.getBlockTexture(bt);
                Image userImg = new Image(userTexture);
                userImg.scaleBy(1.4f - (0.2f * gameMode.playerGroup.getCandidates().size()));
                userImg.setOrigin(Align.center);
                playerViewTable.add(userImg).padRight(10f).padLeft(10f);
            }
            table.add(playerViewTable).padTop(40f).center();

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
                //curScoresInRow++;
            }

            infoBox = new GameInfoBox();
            infoBox.add(otherScoresTable).center().pad(20f);
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

            endFont = new FontGenerator(win ? Color.GREEN : Color.RED).generateFont(FONT_LG);
            addMessage(msg, endFont);
        }

        private void updateLabels() {
            // update num moves label
            labelNumMovesLeft.setText(String.valueOf(((RaceGameMode) gameMode).getNumMovesLeft()));

            // update scores labels
            List<NamedCandidateGroup> scores = ((RaceScoringSystem)((RaceGameMode) gameMode).scoringSystem).getGroups();
            NamedCandidateGroup topGroup = scores.get(0);
            topScoreTable.getNameLabel().setText(topGroup.getName());
            topScoreTable.getScoreLabel().setText(String.valueOf(topGroup.score));

            for (int i = 0; i < scores.size() - 1 && i < otherScoreTables.size(); ++i) {
                otherScoreTables.get(i).getNameLabel().setText(scores.get(i + 1).getName());
                otherScoreTables.get(i).getScoreLabel().setText(String.valueOf(scores.get(i + 1).score));
            }

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

        @Override
        public void dispose() {
            super.dispose();
            if (endFont != null) endFont.dispose();
        }

        private class ScoreTable extends Table {
            private Label nameLabel;
            private Label scoreLabel;

            public ScoreTable(Label.LabelStyle labelStyle) {
                nameLabel = new Label(null, labelStyle);
                scoreLabel = new Label(null, labelStyle);

                setFillParent(false);

                add(nameLabel).center();
                row();
                add(scoreLabel).center();
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
