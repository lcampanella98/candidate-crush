package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.BlockTextureProvider;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.Board;
import com.gmail.enzocampanella98.candidatecrush.board.GoodBoardAnalyzer;
import com.gmail.enzocampanella98.candidatecrush.board.BadBoardInitializer;
import com.gmail.enzocampanella98.candidatecrush.board.IBlockColorProvider;
import com.gmail.enzocampanella98.candidatecrush.board.IBoardAnalyzer;
import com.gmail.enzocampanella98.candidatecrush.board.IBlockTypeProvider;
import com.gmail.enzocampanella98.candidatecrush.board.IBoardInitializer;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.ScoringSystem;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;
import com.gmail.enzocampanella98.candidatecrush.sound.MusicHandler;

import java.util.Collection;


public abstract class CCGameMode implements Disposable {
    static final String BG_PATH = "data/img/general/screen_bg_votetarget.png";

    protected Stage stage;
    protected Board board;
    protected HUD hud;
    protected CandidateCrush game;
    protected InputMultiplexer inputMultiplexer;

    protected boolean isGameOver;

    protected Texture backgroundTexture;
    protected MusicHandler musicHandler;
    protected IBlockTypeProvider newBlockTypeProvider;
    protected BlockTextureProvider blockTextureProvider;
    protected IBlockColorProvider blockColorProvider;
    protected ScoringSystem scoringSystem;
    protected IBoardAnalyzer boardAnalyzer;
    protected IBoardInitializer boardInitializer;

    protected CCGameMode(CandidateCrush game, Stage stage, IBlockColorProvider blockColorProvider, Collection<BlockType> blockTypes) {
        this.stage = stage;
        this.game = game;
        this.boardInitializer = new BadBoardInitializer();
        this.blockColorProvider = blockColorProvider;
        this.boardAnalyzer = new GoodBoardAnalyzer();
        blockTextureProvider = new BlockTextureProvider(blockTypes, blockColorProvider);
        isGameOver = false;
        this.backgroundTexture = new Texture(getBackgroundTexturePath());
    }

    // override to set custom background texture
    protected String getBackgroundTexturePath() {
        return BG_PATH;
    }

    public abstract void onGameStart();

    public abstract void onGameEnd();

    public abstract void update(float dt);

    protected void setupInputMultiplexer() {
        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
        inputMultiplexer.addProcessor(hud.hudStage);
        inputMultiplexer.addProcessor(stage);
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
        if (musicHandler != null) musicHandler.dispose();
        if (blockTextureProvider != null) blockTextureProvider.dispose();
    }
}
