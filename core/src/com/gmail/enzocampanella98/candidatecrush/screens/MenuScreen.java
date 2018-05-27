package com.gmail.enzocampanella98.candidatecrush.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import com.gmail.enzocampanella98.candidatecrush.gamemode.CCGameMode;
import com.gmail.enzocampanella98.candidatecrush.gamemode.GameModeButton;
import com.gmail.enzocampanella98.candidatecrush.gamemode.RaceToWhitehouseGameMode;
import com.gmail.enzocampanella98.candidatecrush.gamemode.VoteTargetGameMode;

/**
 * Created by lorenzo on 9/5/2016.
 */
public class MenuScreen implements Screen {

    private CandidateCrush game;
    private Stage menuStage;
    private Table table;

    private Viewport viewport;
    private OrthographicCamera cam;

    private BitmapFont font;
    private Texture texturebg;
    private Sprite bgSprite;

    private ImageTextButton btnPlay;
    private Label titleLabel;

    private Array<GameModeButton> gameModeButtons;

    public MenuScreen(final CandidateCrush game) {
        this.game = game;

        // init cam
        cam = new OrthographicCamera(CandidateCrush.V_WIDTH, CandidateCrush.V_HEIGHT);

        // init viewport
        viewport = new FitViewport(CandidateCrush.V_WIDTH, CandidateCrush.V_HEIGHT, cam);

        // init font
        FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("data/fonts/ShareTechMono-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 70;
        param.borderWidth = 2;
        param.color = Color.WHITE;
        font = fontGen.generateFont(param);
        fontGen.dispose();

        // init stage
        menuStage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(menuStage);

        // init table
        table = new Table();
        table.setFillParent(true);

        // init textures
        texturebg = new Texture(Gdx.files.internal("data/img/general/menu_image_boxing.jpg"));

        // init game mode selection buttons
        gameModeButtons = new Array<GameModeButton>();

        GameModeButton btnGameModeVoteTarget = new GameModeButton("Vote Target", CCGameMode.GameModeType.VOTE_TARGET);
        gameModeButtons.add(btnGameModeVoteTarget);

        GameModeButton btnGameModeRaceToWhitehouse = new GameModeButton("Race", CCGameMode.GameModeType.RACE_TO_WHITEHOUSE);
        gameModeButtons.add(btnGameModeRaceToWhitehouse);

        for (GameModeButton b : gameModeButtons) {
            b.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GameModeButton btn = (GameModeButton) actor;
                    if (btn.isChecked()) {
                        for (GameModeButton cur : gameModeButtons) {
                            if (cur != btn) cur.setChecked(false);
                        }
                    }
                }
            });
        }

        // init play button
        TextureAtlas btnAtlas = new TextureAtlas("data/playbutton.pack");
        Skin skinPlay = new Skin(btnAtlas);
        ImageTextButton.ImageTextButtonStyle btnPlayStyle = new ImageTextButton.ImageTextButtonStyle();
        btnPlayStyle.up = skinPlay.getDrawable("skin-up");
        btnPlayStyle.down = skinPlay.getDrawable("skin-down");
        btnPlayStyle.font = font;
        btnPlayStyle.fontColor = com.badlogic.gdx.graphics.Color.BLACK;

        btnPlay = new ImageTextButton("Start the Crush", btnPlayStyle);
        btnPlay.pad(50, 80, 50, 80);

        btnPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                GameModeButton checkedButton = null;
                for (GameModeButton b : gameModeButtons) {
                    if (b.isChecked()) {
                        checkedButton = b;
                        break;
                    }
                }

                if (checkedButton != null) {
                    dispose();
                    CandidateCrushPlayScreen playScreen = new CandidateCrushPlayScreen(game);
                    CCGameMode gameMode = null;
                    switch (checkedButton.getGameModeType()) {
                        case RACE_TO_WHITEHOUSE:
                            gameMode = new RaceToWhitehouseGameMode(game, playScreen.playStage);
                            break;
                        case VOTE_TARGET:
                        default:
                            gameMode = new VoteTargetGameMode(game, playScreen.playStage);
                            break;
                    }
                    assert gameMode != null;
                    playScreen.setGameMode(gameMode);
                    game.setScreen(playScreen);
                }

            }
        });


        // init title label
        Label.LabelStyle titleLabelStyle = new Label.LabelStyle(font, Color.WHITE);
        titleLabel = new Label("Welcome To " + CandidateCrush.TITLE, titleLabelStyle);

        // add items to table
        table.center();
        table.add(titleLabel).padBottom(CandidateCrush.V_HEIGHT / 4);
        table.row();

        // add game mode selection buttons
        Table gameModeTable = new Table();
        int buttonsPerRow = 2;
        int numButtonsInCurRow = 0;
        for (GameModeButton b : gameModeButtons) {
            if (numButtonsInCurRow >= buttonsPerRow) {
                gameModeTable.row();
                numButtonsInCurRow = 0;
            }
            gameModeTable.add(b).width(500).padLeft(10).padRight(10);
            numButtonsInCurRow++;
        }
        table.add(gameModeTable);
        table.row();

        // add play button
        table.add(btnPlay).padTop(200);


        bgSprite = new Sprite(texturebg);
        Image bgImage = new Image(new SpriteDrawable(bgSprite));
        bgImage.setWidth(CandidateCrush.V_WIDTH);
        bgImage.setHeight(CandidateCrush.V_HEIGHT);
        menuStage.addActor(bgImage);
        // add table to stage
        menuStage.addActor(table);
    }


    @Override
    public void show() {

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
        font.dispose();
        texturebg.dispose();
        menuStage.dispose();
    }
}
