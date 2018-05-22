package com.gmail.enzocampanella98.candidatecrush.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.gamemode.CCGameMode;

import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_HEIGHT;
import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_WIDTH;

/**
 * Created by Lorenzo Campanella on 8/7/2016.
 */

// TODO move most of this to Gamemode class
public class CandidateCrushPlayScreen implements Screen {

    private final CandidateCrush game;

    private CCGameMode gameMode;

    private Viewport gameViewport;
    private OrthographicCamera cam;

    private Texture worldTexture;
    private Pixmap bgMap;
    private Sprite s;


    private Stage playStage;
    public Table mainTable;

    private Image tImage;
    private Label tLabelHello;


    public CandidateCrushPlayScreen(CandidateCrush game) {
        this.game = game;

        // init camera
        cam = new OrthographicCamera();

        // init viewport
        gameViewport = new FitViewport(V_WIDTH, V_HEIGHT, cam);
        gameViewport.apply(true);

        // init textures and sprites
        Pixmap myWorldPixmap = new Pixmap(V_WIDTH, CandidateCrush.V_HEIGHT, Pixmap.Format.RGBA8888);
        myWorldPixmap.setColor(Color.BLUE);
        myWorldPixmap.fillRectangle(0, 0, myWorldPixmap.getWidth(), myWorldPixmap.getHeight());
        worldTexture = new Texture(myWorldPixmap);
        s = new Sprite(worldTexture);

        // init stage
        playStage = new Stage(gameViewport, game.batch);

        // init mainTable
        mainTable = new Table();
        mainTable.setFillParent(true);

        // add main table to stage
        playStage.addActor(mainTable);

    }

    public void setGameMode(CCGameMode gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update(); // update camera

        game.batch.setProjectionMatrix(cam.combined);

        gameMode.update(delta);

        playStage.act(delta);
        playStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
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
        gameMode.dispose();
        playStage.dispose();
        worldTexture.dispose();
    }

}
