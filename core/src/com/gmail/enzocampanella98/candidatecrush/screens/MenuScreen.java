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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.customui.CCButton;
import com.gmail.enzocampanella98.candidatecrush.customui.CCButtonFactory;
import com.gmail.enzocampanella98.candidatecrush.customui.LevelButton;
import com.gmail.enzocampanella98.candidatecrush.fonts.FontCache;
import com.gmail.enzocampanella98.candidatecrush.fonts.FontGenerator;
import com.gmail.enzocampanella98.candidatecrush.gamemode.GameModeFactory;
import com.gmail.enzocampanella98.candidatecrush.level.Level;
import com.gmail.enzocampanella98.candidatecrush.level.LevelFactory;
import com.gmail.enzocampanella98.candidatecrush.sound.CCSoundBank;

import java.util.ArrayList;
import java.util.List;

import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_WIDTH;

/**
 * Created by lorenzo on 9/5/2016.
 */
public class MenuScreen implements Screen {

    private static final String BG_IMAGE_PATH = "data/img/general/cc_bg.png";

    private static final int FONT_LG = 70;
    private static final int FONT_MD = 50;
    private static final int FONT_SM = 30;

    private final CandidateCrush game;
    private final Stage menuStage;
    private final CCButtonFactory buttonFactory;
    private final FontCache fontCache;
    private final GameModeFactory gameModeFactory;
    private final LevelFactory levelFactory;

    private Viewport viewport;
    private OrthographicCamera cam;
    private Texture textureBg;
    private Sprite bgSprite;

    private Table levelTable;
    private ScrollPane levelScrollPane;
    private Table table;
    private CCButton btnHardMode;
    private ImageTextButton btnPlay;

    private Label.LabelStyle smallLabelStyle;
    private Label.LabelStyle titleLabelStyle;
    private Label.LabelStyle normalLabelStyle;

    private List<LevelButton> levelButtons;

    public MenuScreen(final CandidateCrush game) {
        this.game = game;
        fontCache = new FontCache(new FontGenerator(2, Color.WHITE));
        buttonFactory = new CCButtonFactory(fontCache);
        gameModeFactory = new GameModeFactory(game);
        levelFactory = new LevelFactory(gameModeFactory);

        // init cam
        cam = new OrthographicCamera(V_WIDTH, CandidateCrush.V_HEIGHT);

        // init viewport
        viewport = new FitViewport(V_WIDTH, CandidateCrush.V_HEIGHT, cam);

        // init stage
        menuStage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(menuStage);

        // init table
        table = new Table();
        table.setFillParent(true);
//        table.setDebug(true);

        // init textures
        textureBg = new Texture(Gdx.files.internal(BG_IMAGE_PATH));

        // init label style
        smallLabelStyle = new Label.LabelStyle(fontCache.get(FONT_SM), Color.WHITE);
        normalLabelStyle = new Label.LabelStyle(fontCache.get(FONT_MD), Color.WHITE);
        titleLabelStyle = new Label.LabelStyle(fontCache.get(FONT_LG), Color.WHITE);

        // ACTORS
        levelTable = new Table();
        initLevelScrollPane();

        // init play button
        initPlayButton();

        // add items to table
        table.padTop(700f).top();

        if (game.isHardModeUnlocked()) {
            initHardModeButton();
            table.add(btnHardMode).right().width(400f).height(btnHardMode.scaledHeight(400f)).pad(50f);
            table.row();
        }

        // init level buttons
        initLevelButtons();

        // add levels
        table.add(levelScrollPane);
        table.row();

        // add play button
        table.add(btnPlay).padTop(100f);

        bgSprite = new Sprite(textureBg);
        Image bgImage = new Image(new SpriteDrawable(bgSprite));
        bgImage.setWidth(V_WIDTH);
        bgImage.setHeight(CandidateCrush.V_HEIGHT);
        menuStage.addActor(bgImage);
        // add table to stage

        menuStage.addActor(table);

        table.pack();
        scrollToNextLevel();
    }

    private void initLevelScrollPane() {
        levelScrollPane = new ScrollPane(levelTable);
        levelScrollPane.setScrollbarsVisible(false);
        levelScrollPane.setFlickScroll(true);
    }

    private void scrollToNextLevel() {
        int levelToScroll = isHardModeSelected()
                ? Math.min(game.gameData.getMaxBeatenLevelHardMode() + 1, LevelFactory.NUM_LEVELS)
                : Math.min(game.gameData.getMaxBeatenLevel() + 1, LevelFactory.NUM_LEVELS);
        System.out.println("Scrolling to level " + levelToScroll);
        levelScrollPane.setScrollX(getButtonXInScrollPane(levelToScroll));
    }

    private void initPlayButton() {
        TextureAtlas btnAtlas = new TextureAtlas("data/playbutton.pack");
        Skin skinPlay = new Skin(btnAtlas);
        ImageTextButton.ImageTextButtonStyle btnPlayStyle = new ImageTextButton.ImageTextButtonStyle();
        btnPlayStyle.up = skinPlay.getDrawable("skin-up");
        btnPlayStyle.down = skinPlay.getDrawable("skin-down");
        btnPlayStyle.font = fontCache.get(FONT_LG);
        btnPlayStyle.fontColor = com.badlogic.gdx.graphics.Color.BLACK;

        btnPlay = new ImageTextButton("To the Campaign Trail", btnPlayStyle);
        btnPlay.pad(50, 80, 50, 80);

        btnPlay.setVisible(false);
        btnPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playStampIfChecked(true);

                LevelButton levelButton = getSelectedLevelButton();

                if (levelButton != null) {
                    Level level = levelButton.getLevel();
                    CandidateCrushPlayScreen playScreen = new CandidateCrushPlayScreen(game);

                    level.stage = playScreen.playStage;
                    level.config.isHardMode = isHardModeSelected();

                    dispose();
                    playScreen.setGameMode(level.getGameMode());
                    game.setScreen(playScreen);
                }
            }
        });
    }

    private LevelButton getSelectedLevelButton() {
        for (LevelButton btn :
                levelButtons) {
            if (btn.isChecked()) {
                return btn;
            }
        }
        return null;
    }

    private void updatePlayButtonVisibility() {
        btnPlay.setVisible(shouldPlayButtonBeVisible());
    }

    private boolean shouldPlayButtonBeVisible() {
        return getSelectedLevelButton() != null;
    }

    @Override
    public void show() {

    }

    private boolean isHardModeSelected() {
        return btnHardMode != null && btnHardMode.isChecked();
    }

    private boolean isLevelUnlockedInCurrentMode(int lvl) {
        return isHardModeSelected()
                ? lvl <= game.gameData.getMaxBeatenLevelHardMode() + 1
                : lvl <= game.gameData.getMaxBeatenLevel() + 1;
    }

    private void initLevelButtons() {
        levelButtons = new ArrayList<>();
        levelTable.clearChildren();

        for (int lvlNum = 1; lvlNum <= LevelFactory.NUM_LEVELS; ++lvlNum) {
            Level level = levelFactory.getLevel(lvlNum);
            LevelButton btn = buttonFactory.getLevelButton(
                    level, FONT_MD,
                    isLevelUnlockedInCurrentMode(lvlNum)
            );
            btn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    LevelButton btn = (LevelButton) actor;
                    if (btn.isChecked()) {
                        for (LevelButton cur : levelButtons) {
                            if (cur != btn && cur.isChecked()) {
                                cur.setChecked(false);
                            }
                        }
                    }

                    playStampIfChecked(btn.isChecked());
                    updatePlayButtonVisibility();
                }
            });
            levelButtons.add(btn);
        }
        float w = 400f;
        for (LevelButton btn : levelButtons) {
            float padLeft = btn.getLevel().getLevelNumber() == 1 ? getInitialLevelButtonPad() : 0f;
            float padRight = btn.getLevel().getLevelNumber() == LevelFactory.NUM_LEVELS ? getInitialLevelButtonPad() : levelButtonPadRight;
            levelTable.add(btn).width(w).height(btn.scaledHeight(w)).padLeft(padLeft).padRight(padRight);
        }
        levelTable.invalidate();
    }

    private float levelButtonWidth = 400f;
    private float levelButtonPadRight = 20f;

    private float getInitialLevelButtonPad() {
        return (V_WIDTH - levelButtonWidth) / 2f;
    }

    private float getButtonXInScrollPane(int level) {
        return //getInitialLevelButtonPadLeft() +
                (level-1) * (400f + 20f);
    }

    private void initHardModeButton() {
        btnHardMode = buttonFactory.getVoteButton("Hard Mode", FONT_MD);
        btnHardMode.setChecked(true);
        btnHardMode.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Button btn = (Button)actor;
                playStampIfChecked(btn.isChecked());
                initLevelButtons();
                scrollToNextLevel();
                updatePlayButtonVisibility();
            }
        });
    }

    private void playStampIfChecked(boolean checked) {
        if (checked) {
            CCSoundBank.getInstance().stampSound.play();
        }
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
        textureBg.dispose();
        menuStage.dispose();
    }
}
