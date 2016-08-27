package com.gmail.enzocampanella98.candidatecrush;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gmail.enzocampanella98.candidatecrush.screens.PlayScreen;
import com.gmail.enzocampanella98.candidatecrush.state.GameStateManager;
import com.gmail.enzocampanella98.candidatecrush.state.MenuState;

public class CandidateCrush extends Game {

    public static final int V_WIDTH = 1080, V_HEIGHT = 1920;
    public static final float ASPECT_RATIO = (float)V_WIDTH/(float)V_HEIGHT;
    public static final String TITLE = "Candidate Crush";

    public SpriteBatch batch;
    GameStateManager gsm;


    @Override
    public void create() {
        batch = new SpriteBatch();
        //gsm = new GameStateManager();
        //gsm.set(new MenuState(gsm));
        setScreen(new PlayScreen(this));
        System.out.println("We're on " + Gdx.app.getType().name());

    }

    @Override
    public void render() {
        /*Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);*/
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        //gsm.dispose();
    }
}
