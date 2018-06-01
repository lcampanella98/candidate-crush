package com.gmail.enzocampanella98.candidatecrush.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.gamemode.CCGameMode;

import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_HEIGHT;
import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_WIDTH;


public class CandidateCrushPlayScreen implements Screen {

    private final CandidateCrush game;

    private CCGameMode gameMode;

    private Viewport gameViewport;
    private OrthographicCamera cam;

    public Stage playStage;

    public CandidateCrushPlayScreen(CandidateCrush game) {
        this.game = game;

        // init camera
        cam = new OrthographicCamera();

        // init viewport
        gameViewport = new FitViewport(V_WIDTH, V_HEIGHT, cam);
        gameViewport.apply(true);

        // init stage
        playStage = new Stage(gameViewport, game.batch);

    }

    public void setGameMode(CCGameMode gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update(); // update camera

        game.batch.setProjectionMatrix(cam.combined);

        // draw background
        game.batch.begin();
        game.batch.draw(gameMode.getBackgroundTexture(), 0, 0, V_WIDTH, V_HEIGHT);
        game.batch.end();

        gameMode.update(delta);

        playStage.act(delta);
        playStage.draw();


        gameMode.drawHUD(delta);
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
    }

}
