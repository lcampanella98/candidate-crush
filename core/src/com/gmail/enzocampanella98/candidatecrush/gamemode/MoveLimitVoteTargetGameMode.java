package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.Board;
import com.gmail.enzocampanella98.candidatecrush.board.EquallyRandomBlockTypeProvider;
import com.gmail.enzocampanella98.candidatecrush.board.IBlockColorProvider;
import com.gmail.enzocampanella98.candidatecrush.customui.GameInfoBox;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.VoteTargetScoringSystem;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;
import com.gmail.enzocampanella98.candidatecrush.sound.MusicHandler;
import com.gmail.enzocampanella98.candidatecrush.sound.NoLevelMusicHandler;

import java.util.Collection;
import java.util.Collections;
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

    public MoveLimitVoteTargetGameMode(CandidateCrush game, Stage stage, List<BlockType> blockTypes, IBlockColorProvider blockColorProvider) {
        this(game, stage, blockColorProvider, blockTypes, defaultNumMoves, defaultTargetScore);
    }

    public MoveLimitVoteTargetGameMode(CandidateCrush game, Stage stage, IBlockColorProvider blockColorProvider, List<BlockType> blockTypes, int numMoves, int targetScore) {
        super(game, stage, blockColorProvider, blockTypes);

        this.targetScore = targetScore;
        this.numMovesLeft = numMoves;

        this.blockTypes = blockTypes;
        this.blockTypes.remove(BlockType.BLANK);

        musicHandler = new NoLevelMusicHandler();
        musicHandler.start();

        newBlockTypeProvider = new EquallyRandomBlockTypeProvider(blockTypes);

        this.board = new Board(boardWidth, musicHandler, newBlockTypeProvider, blockTextureProvider, boardAnalyzer, boardInitializer);

        int score3 = 500, score4 = 1000, score5 = 3000, scoreT = 2000;
        this.scoringSystem = new VoteTargetScoringSystem(score3, score4, score5, scoreT, nonUserInvokedCrushScale);

        this.mainTable = new Table();
        this.mainTable.setFillParent(true);

        // instantiate hud
        hud = new MoveLimitVoteTargetGameMode.HeadsUpDisplay(this);
        hud.initStage();

        // add board to main table
        Table boardTable = new Table();
        boardTable.add(board);
        this.mainTable.add(boardTable);

        this.stage.addActor(mainTable);
    }

    @Override
    protected boolean wonGame() {
        return scoringSystem.getPlayerScore() >= targetScore;
    }

    @Override
    protected boolean isGameOver() {
        // game is over if the player surpassed the score or has no more moves (or both in which case the player WINS)
        return scoringSystem.getPlayerScore() >= targetScore || numMovesLeft == 0;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        while (board.getCrushStack().size() > 0) {
            scoringSystem.updateScore(board.getCrushStack().pop(), board.userFlippedBlocks);
            if (board.userFlippedBlocks && --numMovesLeft == 0) break;
        }
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
            labelScore.setText("Votes: " + getCommaSeparatedNumber(gameMode.scoringSystem.getPlayerScore()));
            labelTargetScore.setText("Target: " + getCommaSeparatedNumber(gameMode.targetScore));
            labelMovesLeft.setText("" + ((int) Math.ceil(gameMode.numMovesLeft)));
        }

        @Override
        protected void addActorsToTable() {
            // init table elements
            Label.LabelStyle scoreLabelStyle = new Label.LabelStyle(fontCache.get(FONT_MD), Color.BLACK);
            Label.LabelStyle timeLabelStyle = new Label.LabelStyle(fontCache.get(FONT_LG), Color.BLACK);
            Label.LabelStyle targetLabelStyle = new Label.LabelStyle(fontCache.get(FONT_MD), Color.BLACK);

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
            infoBox.add(labelTargetScore).pad(20f);
            infoBox.pack();

            mainTable.add(infoBox);
            mainTable.row();

            infoBox = new GameInfoBox();
            infoBox.add(labelScore).pad(20f);
            infoBox.pack();

            mainTable.add(infoBox).padTop(45 + gameMode.board.getHeight()).expandX();
        }

        @Override
        public Collection<String> getGameInfoDialogTextLines() {
            return Collections.singletonList(
                    "You have " + gameMode.numMovesLeft + " moves to reach " + gameMode.targetScore + " votes!"
            );
        }
    }
}
