package com.gmail.enzocampanella98.candidatecrush.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.customui.CCButton;
import com.gmail.enzocampanella98.candidatecrush.customui.CCButtonFactory;
import com.gmail.enzocampanella98.candidatecrush.customui.CandidateButton;
import com.gmail.enzocampanella98.candidatecrush.customui.GameModeButton;
import com.gmail.enzocampanella98.candidatecrush.fonts.FontCache;
import com.gmail.enzocampanella98.candidatecrush.fonts.FontGenerator;
import com.gmail.enzocampanella98.candidatecrush.gamemode.CCGameMode;
import com.gmail.enzocampanella98.candidatecrush.gamemode.GameModeFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lorenzo on 9/5/2016.
 */
public class MenuScreen implements Screen {

    private static final String BG_IMAGE_PATH = "data/img/general/cc_bg.png";

    public enum GameMode {
        MOVE_LIMIT_VOTE_TARGET, TIMED_VOTE_TARGET, PRIMARIES, ELECTION
    }

    public static final List<BlockType> CANDIDATES_2020 = new ArrayList<>(Arrays.asList(
            BlockType.TRUMP,
            BlockType.WARREN,
            BlockType.SANDERS,
            BlockType.BUTTIGIEG,
            BlockType.BIDEN
    ));
    public static final List<BlockType> DEM_CANDIDATES_2020 = new ArrayList<>(Arrays.asList(
            BlockType.WARREN,
            BlockType.SANDERS,
            BlockType.BUTTIGIEG,
            BlockType.BIDEN
    ));
    private static final int FONT_LG = 70;
    private static final int FONT_MD = 50;
    private static final int FONT_SM = 30;

    private Label.LabelStyle smallLabelStyle;
    private Label.LabelStyle titleLabelStyle;
    private Label.LabelStyle normalLabelStyle;

    private Table gameModeTable;
    private Table partyBtnTable;
    private Table candidateSelectBtnTable;
    private CCButton btnHardMode;

    private CandidateCrush game;
    private Stage menuStage;
    private Table table;

    private Viewport viewport;
    private OrthographicCamera cam;

    private Texture texturebg;
    private Sprite bgSprite;

    private ImageTextButton btnPlay;
    private Label titleLabel;
    private Container<Table> optionTableContainer;

    private List<GameModeButton> gameModeButtons;
    private List<CCButton> partyButtons;
    private List<CandidateButton> candidateButtons;

    private CCButtonFactory buttonFactory;
    private FontCache fontCache;
    private GameModeFactory gameModeFactory;

    public MenuScreen(final CandidateCrush game) {
        this.game = game;
        fontCache = new FontCache(new FontGenerator(2, Color.WHITE));
        buttonFactory = new CCButtonFactory(fontCache);
        gameModeFactory = new GameModeFactory(game);

        // init cam
        cam = new OrthographicCamera(CandidateCrush.V_WIDTH, CandidateCrush.V_HEIGHT);

        // init viewport
        viewport = new FitViewport(CandidateCrush.V_WIDTH, CandidateCrush.V_HEIGHT, cam);

        // init stage
        menuStage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(menuStage);

        // init table
        table = new Table();
        table.setFillParent(true);
//        table.setDebug(true);

        // init textures
        texturebg = new Texture(Gdx.files.internal(BG_IMAGE_PATH));

        // init label style
        smallLabelStyle = new Label.LabelStyle(fontCache.get(FONT_SM), Color.WHITE);
        normalLabelStyle = new Label.LabelStyle(fontCache.get(FONT_MD), Color.WHITE);
        titleLabelStyle = new Label.LabelStyle(fontCache.get(FONT_LG), Color.WHITE);

        // ACTORS

        // init title label
        titleLabel = new Label(CandidateCrush.TITLE + " 2020", titleLabelStyle);


        // init game mode selection buttons
        initGameModeButtons();

        // init play button
        initPlayButton();

        // init other buttons
        initCandidateSelection();
        initPartySelection();
        initHardModeButton();

        // add items to table
        table.padTop(560f).top();
        table.add(btnHardMode).right().width(400f).height(btnHardMode.scaledHeight(400f)).padBottom(50f);
        table.row();

//        table.add(titleLabel).padBottom(CandidateCrush.V_HEIGHT / 4.0f);
//        table.row();

        // add game mode selection buttons
        table.add(gameModeTable);
        table.row();

        // add select party/candidate container
        optionTableContainer = new Container<>();
        table.add(optionTableContainer).padTop(50f);
        //table.add(candidateSelectBtnTable);
        table.row();

        // add play button
        table.add(btnPlay).padTop(100f);

        bgSprite = new Sprite(texturebg);
        Image bgImage = new Image(new SpriteDrawable(bgSprite));
        bgImage.setWidth(CandidateCrush.V_WIDTH);
        bgImage.setHeight(CandidateCrush.V_HEIGHT);
        menuStage.addActor(bgImage);
        // add table to stage
        menuStage.addActor(table);

    }

    private void initPlayButton() {
        TextureAtlas btnAtlas = new TextureAtlas("data/playbutton.pack");
        Skin skinPlay = new Skin(btnAtlas);
        ImageTextButton.ImageTextButtonStyle btnPlayStyle = new ImageTextButton.ImageTextButtonStyle();
        btnPlayStyle.up = skinPlay.getDrawable("skin-up");
        btnPlayStyle.down = skinPlay.getDrawable("skin-down");
        btnPlayStyle.font = fontCache.get(FONT_LG);
        btnPlayStyle.fontColor = com.badlogic.gdx.graphics.Color.BLACK;

        btnPlay = new ImageTextButton("Start the Crush", btnPlayStyle);
        btnPlay.pad(50, 80, 50, 80);

        btnPlay.setVisible(false);
        btnPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                GameModeButton checkedButton = getCheckedGameModeButton();

                if (checkedButton != null) {
                    CandidateCrushPlayScreen playScreen = new CandidateCrushPlayScreen(game);
                    gameModeFactory.setStage(playScreen.playStage);
                    gameModeFactory.setHardMode(btnHardMode.isChecked());
                    CCGameMode gameMode = null;
                    switch (checkedButton.getGameModeType()) {
                        case PRIMARIES:
                            BlockType chosen = getSelectedCandidate();
                            if (chosen != null) {
                                gameMode = gameModeFactory.getDemocratPrimary2020GameMode(chosen);
                            }
                            break;
                        case ELECTION:
                            Character party = getSelectedParty();
                            if (party != null) {
                                gameMode = gameModeFactory.getElection2020GameMode(party);
                            }
                            break;
                        case TIMED_VOTE_TARGET:
                            gameMode = gameModeFactory.getTimedVoteTargetGameMode();
                            break;
                        case MOVE_LIMIT_VOTE_TARGET:
                            gameMode = gameModeFactory.getMoveLimitVoteTargetGameMode();
                            break;
                    }
                    if (gameMode != null) {
                        dispose();
                        playScreen.setGameMode(gameMode);
                        game.setScreen(playScreen);
                    } else {
                        playScreen.dispose();
                    }
                }
            }
        });
    }

    private GameModeButton getCheckedGameModeButton() {
        for (GameModeButton b : gameModeButtons) {
            if (b.isChecked()) {
                return b;
            }
        }
        return null;
    }

    private void updatePlayButtonVisibility() {
        btnPlay.setVisible(shouldPlayButtonBeVisible());
    }

    private boolean shouldPlayButtonBeVisible() {
        GameModeButton checkedButton = getCheckedGameModeButton();
        if (checkedButton == null) return false;
        MenuScreen.GameMode gm = checkedButton.getGameModeType();
        if ((gm == GameMode.PRIMARIES && getSelectedCandidate() == null)
            || (gm == GameMode.ELECTION && getSelectedParty() == null)) {
            return false;
        }
        return true;
    }

    @Override
    public void show() {

    }

    private void initGameModeButtons() {
        gameModeButtons = new ArrayList<>();
        gameModeTable = new Table();
        int buttonsPerRow = 2;
        int numButtonsInCurRow = 0;

        GameModeButton btn;

        btn = buttonFactory.getGameModeButton("Timed Vote", FONT_MD, GameMode.TIMED_VOTE_TARGET);
        gameModeButtons.add(btn);

        btn = buttonFactory.getGameModeButton("Move Limit Vote", FONT_MD, GameMode.MOVE_LIMIT_VOTE_TARGET);
        gameModeButtons.add(btn);

        btn = buttonFactory.getGameModeButton("Primaries", FONT_MD, GameMode.PRIMARIES);
        btn.setRequiresCandidate(true);
        gameModeButtons.add(btn);

        btn = buttonFactory.getGameModeButton("Election", FONT_MD, GameMode.ELECTION);
        btn.setRequiresParty(true);
        gameModeButtons.add(btn);

        for (GameModeButton b : gameModeButtons) {
            b.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GameModeButton btn = (GameModeButton) actor;
                    if (btn.isChecked()) {
                        // set others to false
                        for (GameModeButton cur : gameModeButtons) {
                            if (cur != btn) cur.setChecked(false);
                        }
                        if (btn.isRequiresParty()) {
                            // show party selection
                            showPartySelection();
                        } else if (btn.isRequiresCandidate()) {
                            // show candidate selection
                            showCandidateSelection();
                        } else {
                            optionTableContainer.setActor(null);
                        }
                    } else {
                        optionTableContainer.setActor(null);
                    }
                    updatePlayButtonVisibility();
                }
            });
        }

        for (GameModeButton b : gameModeButtons) {
            if (numButtonsInCurRow >= buttonsPerRow) {
                gameModeTable.row();
                numButtonsInCurRow = 0;
            }
            gameModeTable.add(b).width(500f).height(b.scaledHeight(500f)).padLeft(10f).padRight(10f).padTop(10f);
            numButtonsInCurRow++;
        }
    }

    private void initPartySelection() {
        partyButtons = new ArrayList<>();
        partyBtnTable = new Table();

        partyBtnTable.add(new Label("Choose your Party", normalLabelStyle));
        partyBtnTable.row();

        CCButton button;

        button = buttonFactory.getVoteButton("D", 100);
        partyButtons.add(button);

        button = buttonFactory.getVoteButton("R", 100);
        partyButtons.add(button);

        for (CCButton b : partyButtons) {
            b.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    CCButton btn = (CCButton) actor;
                    if (btn.isChecked()) {
                        for (CCButton cur : partyButtons) {
                            if (cur != btn) cur.setChecked(false);
                        }
                    }
                    updatePlayButtonVisibility();
                }
            });

            partyBtnTable.add(b).width(500f).height(b.scaledHeight(500f)).padTop(10f);
            partyBtnTable.row();
        }
    }

    private void initCandidateSelection() {
        candidateButtons = new ArrayList<>();
        candidateSelectBtnTable = new Table();

        candidateSelectBtnTable.add(new Label("Choose your candidate", normalLabelStyle));
        candidateSelectBtnTable.row();

        for (BlockType dem : DEM_CANDIDATES_2020) {
            CandidateButton button = buttonFactory.getCandidateButton(FONT_MD, dem);
            candidateButtons.add(button);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    CandidateButton btn = (CandidateButton) actor;
                    if (btn.isChecked()) {
                        for (CandidateButton cur : candidateButtons) {
                            if (cur != btn) cur.setChecked(false);
                        }
                    }
                    updatePlayButtonVisibility();
                }
            });

            candidateSelectBtnTable.add(button).width(400f).height(button.scaledHeight(400f)).padTop(10f);
            candidateSelectBtnTable.row();
        }
    }

    private void initHardModeButton() {
        btnHardMode = buttonFactory.getVoteButton("Hard Mode", FONT_MD);
    }

    private BlockType getSelectedCandidate() {
        for (CandidateButton cb : candidateButtons) {
            if (cb.isChecked()) {
                return cb.getBlockType();
            }
        }
        return null;
    }

    private Character getSelectedParty() {
        for (CCButton btn : partyButtons) {
            if (btn.isChecked()) return btn.getText().charAt(0);
        }
        return null;
    }

    private void showPartySelection() {
        optionTableContainer.setActor(partyBtnTable);
    }

    private void showCandidateSelection() {
        optionTableContainer.setActor(candidateSelectBtnTable);
    }

    @Override
    public void render(float delta) {
        handleInput(delta);
        cam.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(cam.combined);
        menuStage.act();
        menuStage.draw();
    }

    private void handleInput(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        fontCache.dispose();
        buttonFactory.dispose();
        texturebg.dispose();
        menuStage.dispose();
    }
}
