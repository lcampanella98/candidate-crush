package com.gmail.enzocampanella98.candidatecrush.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;

/**
 * Created by lorenzo on 9/5/2016.
 */
public class MenuScreen implements Screen{

    private CandidateCrush game;
    private Stage menuStage;
    private Table table;
    private ImageTextButton btnPlay;
    private Label titleLabel;
    private BitmapFont font;
    private Viewport viewport;
    private OrthographicCamera cam;
    private Texture texturePlayBtn, texturebg;

    public MenuScreen(final CandidateCrush game) {
        this.game = game;

        // init cam
        cam = new OrthographicCamera(CandidateCrush.V_WIDTH, CandidateCrush.V_HEIGHT);

        // init viewport
        viewport = new FitViewport(CandidateCrush.V_WIDTH, CandidateCrush.V_HEIGHT, cam);

        // init font
        FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ShareTechMono-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 70;
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
        texturePlayBtn = new Texture(Gdx.files.internal("img/general/menu_image_boxing.jpg"));

        // init play button
        TextureAtlas btnAtlas = new TextureAtlas("playbutton.pack");
        Skin skinPlay = new Skin(btnAtlas);
        ImageTextButton.ImageTextButtonStyle btnPlayStyle = new ImageTextButton.ImageTextButtonStyle();
        btnPlayStyle.up = skinPlay.getDrawable("skin-up");
        btnPlayStyle.down = skinPlay.getDrawable("skin-down");
        btnPlayStyle.font = font;
        btnPlayStyle.fontColor = com.badlogic.gdx.graphics.Color.BLACK;
        btnPlay = new ImageTextButton("Play", btnPlayStyle);
        btnPlay.padLeft(20);
        btnPlay.padRight(20);
        btnPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Play button Pressed!");
                game.setScreen(new PlayScreen(game));
            }
        });

        // init title label
        Label.LabelStyle titleLabelStyle = new Label.LabelStyle(font, Color.WHITE);
        titleLabel = new Label("Welcome To " + CandidateCrush.TITLE, titleLabelStyle);

        // add items to table
        table.center();
        table.add(titleLabel).padBottom(CandidateCrush.V_HEIGHT / 4);
        table.row();
        table.add(btnPlay);

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
        texturePlayBtn.dispose();
        texturebg.dispose();
        menuStage.dispose();
    }
}
