package com.gmail.enzocampanella98.candidatecrush;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gmail.enzocampanella98.candidatecrush.screens.MenuScreen;

public class CandidateCrush extends Game {

    public static final int V_WIDTH = 1080, V_HEIGHT = 1920;
    public static final String TITLE = "Candidate Crush";
    public static final boolean IS_TESTING_LEVELS = false;

    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new MenuScreen(this));
    }

    public void disposeCurrentScreen() {
        getScreen().dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
