package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.Board;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;


public abstract class CCGameMode implements Disposable {

    public enum GameModeType {
        VOTE_TARGET, RACE_TO_WHITEHOUSE
    }

    protected Stage stage;
    protected Board board;
    protected HUD hud;
    protected CandidateCrush game;
    protected InputMultiplexer inputMultiplexer;

    protected boolean isGameOver;

    protected ObjectMap<BlockType, Texture> blockTextures;
    protected Texture backgroundTexture;


    protected CCGameMode(CandidateCrush game, Stage stage) {
        this.stage = stage;
        this.game = game;
        isGameOver = false;
    }

    public abstract void onGameStart();

    public abstract void onGameEnd();

    public abstract void update(float dt);

    protected void setupInputMultiplexer() {
        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
        inputMultiplexer.addProcessor(this.hud.hudStage);
        inputMultiplexer.addProcessor(this.stage);
    }

    public CandidateCrush getGame() {
        return game;
    }

    public void drawHUD(float delta) {
        if (this.hud != null) {
            this.hud.render(delta);
        }
    }

    public Texture getBackgroundTexture() {
        return this.backgroundTexture;
    }

    public void dispose() {
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (board != null) board.dispose();
        if (hud != null) hud.dispose();
        if (blockTextures != null) {
            for (ObjectMap.Entry<BlockType, Texture> e : blockTextures) {
                e.value.dispose(); // dispose of block textures
            }
            blockTextures.clear();
        }
    }


}