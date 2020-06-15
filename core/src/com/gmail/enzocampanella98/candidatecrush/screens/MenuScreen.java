package com.gmail.enzocampanella98.candidatecrush.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
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
import com.gmail.enzocampanella98.candidatecrush.gamemode.CCGameMode;
import com.gmail.enzocampanella98.candidatecrush.gamemode.GameModeFactory;
import com.gmail.enzocampanella98.candidatecrush.level.ILevelSet;
import com.gmail.enzocampanella98.candidatecrush.level.Level;
import com.gmail.enzocampanella98.candidatecrush.level.NormalLevelSet;
import com.gmail.enzocampanella98.candidatecrush.level.OakBaesLevelSet;
import com.gmail.enzocampanella98.candidatecrush.levelset.LevelSetFactory;
import com.gmail.enzocampanella98.candidatecrush.levelset.Normal2020LevelSetFactory;
import com.gmail.enzocampanella98.candidatecrush.levelset.OakBaesLevelSetFactory;
import com.gmail.enzocampanella98.candidatecrush.sound.CCSoundBank;

import java.util.ArrayList;
import java.util.List;

import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_WIDTH;
import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.LG;
import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.MD;
import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.SM;
import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.fontSize;
import static com.gmail.enzocampanella98.candidatecrush.level.ILevelSet.LS_NORMAL;
import static com.gmail.enzocampanella98.candidatecrush.level.ILevelSet.LS_OAK;

/**
 * Created by lorenzo on 9/5/2016.
 */
public class MenuScreen implements Screen {

    private static final String BG_IMAGE_PATH = "data/img/general/cc_bg.png";

    private final CandidateCrush game;
    private final Stage menuStage;
    private final CCButtonFactory buttonFactory;
    private final FontCache fontCache;

    private Viewport viewport;
    private OrthographicCamera cam;
    private Texture textureBg;
    private Sprite bgSprite;

    private Table levelTable;
    private ScrollPane levelScrollPane;
    private Table table;
    private CCButton btnHardMode;
    private CCButton btnOakBaes;
    private ImageTextButton btnPlay;

    private Label.LabelStyle smallLabelStyle;
    private Label.LabelStyle titleLabelStyle;
    private Label.LabelStyle normalLabelStyle;

    private List<LevelButton> levelButtons;

    private LevelSetFactory lsFactory;
    private GameModeFactory gameModeFactory;
    private ILevelSet levelSet;

    public MenuScreen(final CandidateCrush game) {
        this(game, null);
    }

    public MenuScreen(final CandidateCrush game, ILevelSet fromLevelSet) {
        this.game = game;
        fontCache = new FontCache(new FontGenerator(2, Color.WHITE));
        buttonFactory = new CCButtonFactory(fontCache);

        if (fromLevelSet == null || fromLevelSet instanceof NormalLevelSet) {
            setLevelSetType(LS_NORMAL); // set level set to the normal one
        } else if (fromLevelSet instanceof OakBaesLevelSet) {
            setLevelSetType(LS_OAK);
        }

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
        smallLabelStyle = new Label.LabelStyle(fontCache.get(fontSize(SM)), Color.WHITE);
        normalLabelStyle = new Label.LabelStyle(fontCache.get(fontSize(MD)), Color.WHITE);
        titleLabelStyle = new Label.LabelStyle(fontCache.get(fontSize(LG)), Color.WHITE);

        // ACTORS
        levelTable = new Table();
        initLevelScrollPane();

        // init play button
        initPlayButton();

        // add items to table
        table.padTop(700f).top();

        Table optionButtonTable = new Table();

        initOakBaesButton();
        optionButtonTable.add(btnOakBaes)
                .left()
                .width(400f)
                .height(btnOakBaes.scaledHeight(400f))
                .pad(0, 0, 50, 30);

        initHardModeButton();
        optionButtonTable
                .add(btnHardMode).right()
                .width(400f)
                .height(btnHardMode.scaledHeight(400f))
                .pad(0, 30, 50, 0);

        table.add(optionButtonTable).right().width(V_WIDTH);

        table.row();

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
        final int levelToScroll;
        if (levelSet instanceof OakBaesLevelSet) {
            levelToScroll = isHardModeSelected()
                    ? Math.min(game.gameData.getMaxBeatenOakBaesLevelHardMode() + 1, levelSet.getNumLevels())
                    : Math.min(game.gameData.getMaxBeatenOakBaesLevel() + 1, levelSet.getNumLevels());
        } else {
            levelToScroll = isHardModeSelected()
                    ? Math.min(game.gameData.getMaxBeatenLevelHardMode() + 1, levelSet.getNumLevels())
                    : Math.min(game.gameData.getMaxBeatenLevel() + 1, levelSet.getNumLevels());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                    levelScrollPane.setScrollX(getButtonXInScrollPane(levelToScroll));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initPlayButton() {
        TextureAtlas btnAtlas = new TextureAtlas("data/btn-play.pack");
        Skin skinPlay = new Skin(btnAtlas);
        ImageTextButton.ImageTextButtonStyle btnPlayStyle = new ImageTextButton.ImageTextButtonStyle();
        btnPlayStyle.up = skinPlay.getDrawable("up");
        btnPlayStyle.down = skinPlay.getDrawable("down");
        btnPlayStyle.disabled = skinPlay.getDrawable("disabled");
        btnPlayStyle.font = fontCache.get(fontSize(LG));
        btnPlayStyle.fontColor = com.badlogic.gdx.graphics.Color.BLACK;

        btnPlay = new ImageTextButton("To the Campaign Trail", btnPlayStyle);
        btnPlay.pad(50, 80, 50, 80);

        btnPlay.setDisabled(true);
        btnPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playStampIfChecked(true);

                LevelButton levelButton = getSelectedLevelButton();

                if (levelButton != null) {
                    Level level = levelButton.getLevel();
                    CandidateCrushPlayScreen playScreen = new CandidateCrushPlayScreen(game);

                    level.config.isHardMode = isHardModeSelected();

                    dispose();
                    CCGameMode gameMode = gameModeFactory.getGameMode(
                            playScreen.playStage,
                            level.config,
                            levelSet);
                    playScreen.setGameMode(gameMode);
                    game.setScreen(playScreen);
                }
            }
        });
    }

    private void setLevelSetType(String lsName) {
        if (lsName.equals(LS_NORMAL)) {
            lsFactory = new Normal2020LevelSetFactory(game);
            gameModeFactory = lsFactory.getGameModeFactory();
            levelSet = lsFactory.getLevelSet();
        } else {
            lsFactory = new OakBaesLevelSetFactory(game);
            gameModeFactory = lsFactory.getGameModeFactory();
            levelSet = lsFactory.getLevelSet();
        }
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

    private void updatePlayButtonEnabled() {
        btnPlay.setDisabled(!shouldPlayButtonBeEnabled());
    }

    private boolean shouldPlayButtonBeEnabled() {
        return getSelectedLevelButton() != null;
    }

    @Override
    public void show() {

    }

    private boolean isHardModeSelected() {
        return btnHardMode != null && btnHardMode.isChecked();
    }

    private boolean isLevelUnlockedInCurrentMode(int lvl) {
        if (levelSet instanceof OakBaesLevelSet) {
            return isHardModeSelected()
                    ? lvl <= game.gameData.getMaxBeatenOakBaesLevelHardMode() + 1
                    : lvl <= game.gameData.getMaxBeatenOakBaesLevel() + 1;
        } else {
            return isHardModeSelected()
                    ? lvl <= game.gameData.getMaxBeatenLevelHardMode() + 1
                    : lvl <= game.gameData.getMaxBeatenLevel() + 1;
        }
    }

    private void initLevelButtons() {
        levelButtons = new ArrayList<>();
        levelTable.clearChildren();

        for (int lvlNum = 1; lvlNum <= levelSet.getNumLevels(); ++lvlNum) {
            Level level = levelSet.getLevel(lvlNum);
            LevelButton btn = buttonFactory.getLevelButton(
                    level, fontSize(MD),
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
                    updatePlayButtonEnabled();
                }
            });
            levelButtons.add(btn);
        }
        float w = 400f;
        for (LevelButton btn : levelButtons) {
            float padLeft = btn.getLevel().getLevelNumber() == 1 ? getInitialLevelButtonPad() : 0f;
            float padRight = btn.getLevel().getLevelNumber() == levelSet.getNumLevels() ? getInitialLevelButtonPad() : levelButtonPadRight;
            Table subTable = new Table();
            String msg;
            if (btn.getLevel().isElection() || btn.getLevel().isPrimary()) {
                msg = btn.getLevel().isElection() ? "Election" : "Primary";
            } else {
                msg = "";
            }
            Label.LabelStyle style = new Label.LabelStyle(fontCache.get(fontSize(MD)), Color.BLACK);
            Label label = new Label(msg, style);
            subTable.add(btn).width(w).height(btn.scaledHeight(w));
            subTable.row();
            subTable.add(label);
            levelTable.add(subTable).padLeft(padLeft).padRight(padRight);
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
                (level - 1) * (400f + 20f);
    }

    private void initHardModeButton() {
        if (btnHardMode == null) {
            btnHardMode = buttonFactory.getVoteButton("Hard Mode", fontSize(MD));
            btnHardMode.setChecked(false);
            btnHardMode.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Button btn = (Button) actor;
                    playStampIfChecked(btn.isChecked());
                    initLevelButtons();
                    scrollToNextLevel();
                    updatePlayButtonEnabled();
                }
            });
        }
        btnHardMode.setVisible(game.isHardModeUnlocked(levelSet));
    }


    private void initOakBaesButton() {
        if (btnOakBaes == null) {
            btnOakBaes = buttonFactory.getVoteButton("Oak Baes!", fontSize(MD));
            btnOakBaes.setChecked(false);
            btnOakBaes.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Button btn = (Button) actor;
                    setLevelSetType(btn.isChecked() ? LS_OAK : LS_NORMAL);
                    initHardModeButton();
                    playStampIfChecked(btn.isChecked());
                    initLevelButtons();
                    scrollToNextLevel();
                    updatePlayButtonEnabled();
                }
            });
        }
        btnOakBaes.setVisible(game.isOakBaesUnlocked());
    }

    private boolean isOakBaesSelected() {
        return btnOakBaes != null && btnOakBaes.isChecked();
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

    private boolean isGettingTextInput = false;

    private void handleInput(float delta) {
        if (Gdx.input.isTouched()) {
            Vector2 p = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            if (!game.isOakBaesUnlocked()) {
                int w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
                int targetWidth = Math.min(w / 8, h / 8);
                if ((p.x >= w - targetWidth && p.x <= w)
                        && (p.y >= 0 && p.y <= targetWidth) && !isGettingTextInput) {
                    showOakBaesPrompt("Enter Secret");
                }
            }

        }
    }

    private void showOakBaesPrompt(String title) {
        isGettingTextInput = true;
        Gdx.input.getTextInput(new Input.TextInputListener() {
            @Override
            public void input(String text) {
                if (!game.isOakBaesUnlocked()) {
                    if (game.tryUnlockOakBaes(text)) {
                        initOakBaesButton();
                        // maybe play a fun sound?
                    } else {
                        showOakBaesPrompt("Incorrect Secret");
                    }
                }
                isGettingTextInput = false;
            }

            @Override
            public void canceled() {
                isGettingTextInput = false;
            }
        }, title, "", "");
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
