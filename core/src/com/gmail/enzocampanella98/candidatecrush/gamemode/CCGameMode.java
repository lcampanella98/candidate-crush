package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.gmail.enzocampanella98.candidatecrush.board.Board;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;


public abstract class CCGameMode implements Disposable {

    public enum GameModeType {
        VOTE_TARGET, RACE_TO_WHITEHOUSE
    }

    protected Stage stage;
    protected Board board;
    protected HUD hud;


    protected CCGameMode(Stage stage) {
        this.stage = stage;
    }

    public abstract void onGameStart();

    public abstract void onGameEnd();

    public abstract void update(float dt);


    public void drawHUD(float delta) {
        if (this.hud != null) {
            this.hud.render(delta);
        }
    }

}
