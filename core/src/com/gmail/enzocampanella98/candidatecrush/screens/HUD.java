package com.gmail.enzocampanella98.candidatecrush.screens;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;

public abstract class HUD implements Disposable{

    protected Camera hudCam;
    protected Viewport hudViewport;
    protected Stage hudStage;
    protected SpriteBatch batch;


    protected HUD() {
        hudCam = new OrthographicCamera();
        hudViewport = new StretchViewport(CandidateCrush.V_WIDTH,CandidateCrush.V_HEIGHT,hudCam);
        hudViewport.apply();
        batch = new SpriteBatch();
        hudStage = new Stage(hudViewport, batch);
    }

    public abstract void render(float dt);

}
