package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.BadBoardInitializer;
import com.gmail.enzocampanella98.candidatecrush.board.BlockTextureProvider;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.Board;
import com.gmail.enzocampanella98.candidatecrush.board.Crush;
import com.gmail.enzocampanella98.candidatecrush.board.GoodBoardAnalyzer;
import com.gmail.enzocampanella98.candidatecrush.board.IBlockColorProvider;
import com.gmail.enzocampanella98.candidatecrush.board.IBlockTypeProvider;
import com.gmail.enzocampanella98.candidatecrush.board.IBoardAnalyzer;
import com.gmail.enzocampanella98.candidatecrush.board.IBoardInitializer;
import com.gmail.enzocampanella98.candidatecrush.board.SimpleBlockGroup;
import com.gmail.enzocampanella98.candidatecrush.fonts.FontCache;
import com.gmail.enzocampanella98.candidatecrush.fonts.FontGenerator;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.ScoringSystem;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;
import com.gmail.enzocampanella98.candidatecrush.screens.MenuScreen;
import com.gmail.enzocampanella98.candidatecrush.sound.MusicHandler;
import com.gmail.enzocampanella98.candidatecrush.tools.Methods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_HEIGHT;
import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_WIDTH;


public abstract class CCGameMode implements Disposable {
    static final String BG_PATH = "data/img/general/screen_bg_votetarget.png";
    static final float DISPLAY_GAME_INFO_SEC = 4f;
    static final float DISPLAY_GAME_END_SEC = 4f;


    protected Stage stage;
    protected Board board;
    protected HUD hud;
    protected FontCache fontCache;
    protected CandidateCrush game;
    protected InputMultiplexer inputMultiplexer;
    protected Queue<Crush> latestCrushes;

    private boolean isGameStarted;
    private boolean isInitialized;
    private boolean displayedGameEndMessage;
    private boolean showedGameInstructions;

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
        this.blockColorProvider = blockColorProvider;
        this.boardInitializer = new BadBoardInitializer();
        this.boardAnalyzer = new GoodBoardAnalyzer();
        this.blockTextureProvider = new BlockTextureProvider(blockTypes, blockColorProvider);
        this.backgroundTexture = new Texture(getBackgroundTexturePath());
        this.latestCrushes = new LinkedList<>();
        this.fontCache = new FontCache(new FontGenerator(Color.WHITE));
    }

    // override to set custom background texture
    protected String getBackgroundTexturePath() {
        return BG_PATH;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    private void init() {
        if (isInitialized) return;
        setupInputMultiplexer();
        isInitialized = true;
    }

    public void returnToMenu() {
        game.disposeCurrentScreen();
        game.setScreen(new MenuScreen(game));
    }

    protected abstract boolean wonGame();

    public void update(float dt) {
        init();
        stage.act(dt);

        if (!isGameStarted) {
            board.pauseInput();
            if (!showedGameInstructions) {
                hud.showGameInfoDialog(DISPLAY_GAME_INFO_SEC);
                showedGameInstructions = true;
            } else if (!hud.isGameInstructionsShowing()) {
                isGameStarted = true;
            }
        } else {
            board.resumeInput();
        }
        if (isGameOver()) {
            board.pauseInput();
            if (!displayedGameEndMessage) {
                hud.showGameEndMessage(wonGame(), DISPLAY_GAME_END_SEC);
                displayedGameEndMessage = true;
            } else if (!hud.isGameOverMessageShowing()) {
                returnToMenu(); // done!
            }
        }

        List<Crush> latestCrushes = new ArrayList<>();
        while (board.latestCrushes().size() > 0 && !isGameOver()) {
            Crush crush = board.latestCrushes().poll();
            latestCrushes.add(crush);
            scoringSystem.updateScore(crush);
        }
        hud.update(dt);

        for (Crush c : latestCrushes) {
            addCrushVotesAndAnimations(Objects.requireNonNull(c));
        }
    }

    private void addCrushVotesAndAnimations(Crush crush) {
        for (SimpleBlockGroup bg : crush.crushedBlocks) {
            stage.addActor(getCrushVoteLabelWithAnimation(bg, crush.wasUserInvoked));
        }
    }

    protected abstract boolean isGameOver();

    private void setupInputMultiplexer() {
        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
        inputMultiplexer.addProcessor(hud.hudStage);
        inputMultiplexer.addProcessor(stage);
    }

    public CandidateCrush getGame() {
        return game;
    }

    private Label getCrushVoteLabelWithAnimation(SimpleBlockGroup group, boolean wasUserInvoked) {
        Vector2 start = Methods.avg(Arrays.asList(
                board.getPositionOfRowAndCol(group.getMinRow(), group.getMinCol()),
                board.getPositionOfRowAndCol(group.getMaxRow(), group.getMaxCol())
        ));
        Vector2 end = hud.getScoreInfoBoxPosition(group);

        Label.LabelStyle style = new Label.LabelStyle(fontCache.get(50), Color.WHITE);

        String labText = scoringSystem.getBlockGroupValue(group, wasUserInvoked) + "";
        final Label l = new Label(labText, style);
        l.setPosition(start.x, start.y);

        l.addAction(Actions.sequence(
                Actions.moveTo(end.x, end.y, 2.0f),
                Actions.removeActor()
        ));
        return l;
    }

    public void draw() {
        Batch sb = stage.getBatch();

        sb.begin();
        // draw background
        sb.draw(getBackgroundTexture(), 0, 0, V_WIDTH, V_HEIGHT);

        sb.end();
        stage.draw();

        if (hud != null) {
            hud.draw();
        }
    }

    public Texture getBackgroundTexture() {
        return backgroundTexture;
    }

    public void dispose() {
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (board != null) board.dispose();
        if (hud != null) hud.dispose();
        if (musicHandler != null) musicHandler.dispose();
        if (blockTextureProvider != null) blockTextureProvider.dispose();
    }
}
