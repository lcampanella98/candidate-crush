package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.Board;
import com.gmail.enzocampanella98.candidatecrush.board.FrequencyRandomBlockTypeProvider;
import com.gmail.enzocampanella98.candidatecrush.board.IBlockColorProvider;
import com.gmail.enzocampanella98.candidatecrush.board.SimpleBlockGroup;
import com.gmail.enzocampanella98.candidatecrush.customui.GameInfoBox;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.NamedCandidateGroup;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.RaceScoringSystem;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;
import com.gmail.enzocampanella98.candidatecrush.sound.NoLevelMusicHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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


    public RaceGameMode(CandidateCrush game,
                        Stage stage,
                        final List<BlockType> blockTypes,
                        List<NamedCandidateGroup> groups,
                        NamedCandidateGroup playerGroup,
                        Map<BlockType, Double> blockFrequencies,
                        IBlockColorProvider blockColorProvider) {
        this(game, stage, blockTypes, groups, playerGroup, blockColorProvider, blockFrequencies, defaultNumMoves);
    }

    public RaceGameMode(CandidateCrush game,
                        Stage stage,
                        List<BlockType> blockTypes,
                        List<NamedCandidateGroup> groups,
                        NamedCandidateGroup playerGroup,
                        IBlockColorProvider blockColorProvider,
                        Map<BlockType, Double> blockFrequencies,
                        int numMoves) {
        super(game, stage, blockColorProvider, blockTypes);

        this.numMoves = numMoves;
        this.blockTypes = blockTypes;
        this.groups = groups;
        this.playerGroup = playerGroup;
        this.blockFrequencies = blockFrequencies;

        musicHandler = new NoLevelMusicHandler();
        musicHandler.start();

        newBlockTypeProvider = new FrequencyRandomBlockTypeProvider(this.blockFrequencies);

        this.board = new Board(boardWidth, musicHandler, newBlockTypeProvider, blockTextureProvider, boardAnalyzer, boardInitializer);

        int score3 = 500, score4 = 1200, score5 = 3000, scoreT = 2000;
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
        hud.initStage();
    }

    public int getNumMovesLeft() {
        return this.numMovesLeft;
    }

    @Override
    protected boolean wonGame() {
        return ((RaceScoringSystem) scoringSystem).getGroups().get(0) == playerGroup;
    }

    @Override
    protected boolean isGameOver() {
        return numMovesLeft <= 0 && board.isWaitingForInput();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        numMovesLeft = numMoves - board.getNumTotalUserCrushes();
    }

    private static class HeadsUpDisplay extends HUD {
        private static final int FONT_LG = 100, FONT_MD = 60, FONT_SM = 40;

        private Label labelNumMovesLeft;

        private ScoreTable topScoreTable;
        private List<ScoreTable> otherScoreTables;
        private RaceGameMode gameMode;
        private Table playerViewTable;

        HeadsUpDisplay(CCGameMode gameMode) {
            super(gameMode);
            this.gameMode = (RaceGameMode) gameMode;
        }

        @Override
        public Vector2 getScoreInfoBoxPosition(SimpleBlockGroup group) {
            if (gameMode.playerGroup.getCandidates().contains(group.getType())) {
                return playerViewTable.localToStageCoordinates(new Vector2(playerViewTable.getWidth() / 2, playerViewTable.getHeight() / 2));
            } else {
                ScoreTable scoreTable = firstCompetingScoreTableWithBlockType(group.getType());
                return scoreTable.localToStageCoordinates(new Vector2(scoreTable.getWidth() / 2, scoreTable.getHeight() / 2));
            }
        }

        @Override
        protected void addActorsToTable() {
            // init table elements
            Label.LabelStyle topScoreLabelStyle = new Label.LabelStyle(fontCache.get(FONT_MD), Color.BLACK);
            Label.LabelStyle numMovesLeftLabelStyle = new Label.LabelStyle(fontCache.get(FONT_LG), Color.BLACK);
            Label.LabelStyle otherScoreLabelStyle = new Label.LabelStyle(fontCache.get(FONT_SM), Color.BLACK);

            labelNumMovesLeft = new Label(null, numMovesLeftLabelStyle);

            topScoreTable = new ScoreTable(topScoreLabelStyle);
            otherScoreTables = new ArrayList<>();
            for (int i = 0; i < gameMode.groups.size() - 1; i++) {
                otherScoreTables.add(new ScoreTable(otherScoreLabelStyle));
            }

            GameInfoBox infoBox;

            infoBox = new GameInfoBox();
            infoBox.add(labelNumMovesLeft).pad(10f).center();
            infoBox.pack();

            mainTable.add(infoBox).center();
            mainTable.row();

            mainTable.add(new Label("You play", otherScoreLabelStyle)).padTop(10f).center();
            mainTable.row();

            playerViewTable = new Table();
            for (BlockType bt : gameMode.playerGroup.getCandidates()) {
                Texture userTexture = gameMode.blockTextureProvider.provideBlockTexture(bt);
                Image userImg = new Image(userTexture);
                userImg.scaleBy(1.4f - (0.2f * gameMode.playerGroup.getCandidates().size()));
                userImg.setOrigin(Align.center);
                playerViewTable.add(userImg).padRight(10f).padLeft(10f);
            }
            mainTable.add(playerViewTable).padTop(40f).center();

            mainTable.row();

            infoBox = new GameInfoBox();
            infoBox.add(topScoreTable).pad(20f);
            infoBox.pack();

            mainTable.add(infoBox).padTop(100 + gameMode.board.getHeight()).center().expandX();
            mainTable.row();

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

            mainTable.add(infoBox).center().padTop(30f);
        }

        @Override
        public void updateLabels(float dt) {
            // update num moves label
            labelNumMovesLeft.setText(String.valueOf(gameMode.getNumMovesLeft()));

            // update scores labels
            List<NamedCandidateGroup> scores = ((RaceScoringSystem) gameMode.scoringSystem).getGroups();
            NamedCandidateGroup topGroup = scores.get(0);
            topScoreTable.getNameLabel().setText(topGroup.getName());
            topScoreTable.getScoreLabel().setText(String.valueOf(topGroup.score));

            for (int i = 0; i < scores.size() - 1 && i < otherScoreTables.size(); ++i) {
                otherScoreTables.get(i).getNameLabel().setText(scores.get(i + 1).getName());
                otherScoreTables.get(i).getScoreLabel().setText(String.valueOf(scores.get(i + 1).score));
            }
        }

        private ScoreTable firstCompetingScoreTableWithBlockType(BlockType type) {
            List<NamedCandidateGroup> groups = ((RaceScoringSystem) gameMode.scoringSystem).getGroups();
            NamedCandidateGroup grp = null;
            for (NamedCandidateGroup cur : groups) {
                if (cur.getCandidates().contains(type)) {
                    grp = cur;
                    break;
                }
            }
            if (grp == null) return null;
            for (ScoreTable st : otherScoreTables) {
                if (st.getNameLabel().getText().toString().equals(grp.getName())) {
                    return st;
                }
            }
            return null;
        }

        @Override
        public Collection<String> getGameInfoDialogTextLines() {
            return Arrays.asList(
                    "You play " + gameMode.playerGroup.getLongName() + ". ",
                    "Be on top after " + gameMode.numMoves + " moves!"
            );
        }

        private static class ScoreTable extends Table {
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
