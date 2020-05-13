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
import com.gmail.enzocampanella98.candidatecrush.board.SimpleBlockGroup;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.AlwaysFalseSoundByteBlockProvider;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.BlockProvider;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.FrequencyRandomBlockTypeProvider;
import com.gmail.enzocampanella98.candidatecrush.customui.GameInfoBox;
import com.gmail.enzocampanella98.candidatecrush.gamemode.config.GameModeConfig;
import com.gmail.enzocampanella98.candidatecrush.level.ILevelSet;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.NamedCandidateGroup;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.RaceScoringSystem;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;
import com.gmail.enzocampanella98.candidatecrush.sound.PersistentTierMusicHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.scaled;
import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.LG;
import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.MD;
import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.XL;
import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.fontSize;

public class RaceGameMode extends CCGameMode {

    private final List<NamedCandidateGroup> groups;
    private final NamedCandidateGroup playerGroup;
    private final Map<BlockType, Double> blockFrequencies;

    private int numMovesLeft;
    private PersistentTierMusicHandler tierMusicHandler;
    private BlockProvider myBlockProvider;
    private RaceScoringSystem myScoringSystem;

    public RaceGameMode(CandidateCrush game,
                        Stage stage,
                        List<NamedCandidateGroup> groups,
                        NamedCandidateGroup playerGroup,
                        Map<BlockType, Double> blockFrequencies,
                        GameModeConfig config,
                        ILevelSet levelSet) {
        super(game, stage, config, levelSet);

        this.groups = groups;
        this.playerGroup = playerGroup;
        this.blockFrequencies = blockFrequencies;
    }

    @Override
    protected String getBackgroundTexturePath() {
        return "data/img/general/screen_bg_race.png";
    }

    @Override
    protected void setHUD() {
        hud = new HeadsUpDisplay(this);
    }

    @Override
    protected void setScoringSystem() {
        scoringSystem = myScoringSystem = new RaceScoringSystem(groups, playerGroup, config.crushVals);
    }

    @Override
    protected void setBlockProvider() {
        blockProvider = myBlockProvider = new BlockProvider(
                config.candidates,
                CCGameMode.getBlockColorMap(config.isHardMode, config.candidates),
                new FrequencyRandomBlockTypeProvider(this.blockFrequencies),
                new AlwaysFalseSoundByteBlockProvider()
        );
    }

    public int getNumMovesLeft() {
        return this.numMovesLeft;
    }

    @Override
    protected void setMusicHandler() {
        musicHandler = tierMusicHandler = new PersistentTierMusicHandler(levelSet.getNumSoundTiers(), config.soundTier);
    }

    @Override
    protected boolean wonGame() {
        return numMovesLeft <= 0
                && board.isWaitingForInput()
                && myScoringSystem.getGroups().get(0) == playerGroup;
    }

    @Override
    protected boolean lostGame() {
        return numMovesLeft <= 0
                && board.isWaitingForInput()
                && myScoringSystem.getGroups().get(0) != playerGroup;
    }

    @Override
    public void restartGame() {
        super.restartGame();
        numMovesLeft = config.numMoves;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        numMovesLeft = config.numMoves - board.getNumTotalUserCrushes();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (tierMusicHandler != null) tierMusicHandler.dispose();
        if (myBlockProvider != null) myBlockProvider.dispose();
    }

    private static class HeadsUpDisplay extends HUD {

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
            Label.LabelStyle topScoreLabelStyle = new Label.LabelStyle(defaultFontCache.get(fontSize(LG)), Color.BLACK);
            Label.LabelStyle numMovesLeftLabelStyle = new Label.LabelStyle(defaultFontCache.get(fontSize(XL)), Color.BLACK);
            Label.LabelStyle otherScoreLabelStyle = new Label.LabelStyle(defaultFontCache.get(fontSize(MD)), Color.BLACK);

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
                Texture userTexture = gameMode.blockProvider.getBlockTexture(bt);
                Image userImg = new Image(userTexture);
                userImg.setOrigin(Align.center);
                //float size = gameMode.board.getBlockSpacing();
                //float scale = scaled(.75f); //scaled(1.2f) - (scaled(0.2f) * (gameMode.playerGroup.getCandidates().size() - 1));
                playerViewTable.add(userImg).width(scaled(90)).height(scaled(90)).padRight(5f);
            }
            mainTable.add(playerViewTable).padTop(15f).center();

            mainTable.row();

            infoBox = new GameInfoBox();
            infoBox.add(topScoreTable).pad(20f);
            infoBox.pack();

            setFirstCellBelowBoard(mainTable.add(infoBox).center().expandX());
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

            mainTable.add(infoBox).center().padTop(15f);
        }

        @Override
        public void updateLabels(float dt) {
            // update num moves label
            labelNumMovesLeft.setText(String.valueOf(gameMode.getNumMovesLeft()));

            // update scores labels
            List<NamedCandidateGroup> scores = ((RaceScoringSystem) gameMode.scoringSystem).getGroups();
            NamedCandidateGroup topGroup = scores.get(0);
            topScoreTable.getNameLabel().setText(topGroup.getName());
            topScoreTable.getScoreLabel().setText(scoreText(topGroup.score));

            for (int i = 0; i < scores.size() - 1 && i < otherScoreTables.size(); ++i) {
                otherScoreTables.get(i).getNameLabel().setText(scores.get(i + 1).getName());
                otherScoreTables.get(i).getScoreLabel().setText(scoreText(scores.get(i + 1).score));
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
            assert grp != null;
            if (topScoreTable.getNameLabel().getText().toString().equals(grp.getName())) {
                return topScoreTable;
            }
            ScoreTable ret = null;
            for (ScoreTable st : otherScoreTables) {
                if (st.getNameLabel().getText().toString().equals(grp.getName())) {
                    ret = st;
                    break;
                }
            }
            assert ret != null;
            return ret;
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
