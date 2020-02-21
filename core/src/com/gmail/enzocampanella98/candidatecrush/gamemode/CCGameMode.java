package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.BadBoardInitializer;
import com.gmail.enzocampanella98.candidatecrush.board.Board;
import com.gmail.enzocampanella98.candidatecrush.board.Crush;
import com.gmail.enzocampanella98.candidatecrush.board.GoodBoardAnalyzer;
import com.gmail.enzocampanella98.candidatecrush.board.IOnCrushListener;
import com.gmail.enzocampanella98.candidatecrush.board.SimpleBlockGroup;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.IBlockProvider;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.IBoardAnalyzer;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.IBoardInitializer;
import com.gmail.enzocampanella98.candidatecrush.fonts.FontCache;
import com.gmail.enzocampanella98.candidatecrush.fonts.FontGenerator;
import com.gmail.enzocampanella98.candidatecrush.gamemode.config.GameModeConfig;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.ScoringSystem;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;
import com.gmail.enzocampanella98.candidatecrush.screens.MenuScreen;
import com.gmail.enzocampanella98.candidatecrush.sound.CCSoundBank;
import com.gmail.enzocampanella98.candidatecrush.sound.IMusicHandler;
import com.gmail.enzocampanella98.candidatecrush.tools.Methods;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_HEIGHT;
import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_WIDTH;
import static com.gmail.enzocampanella98.candidatecrush.tools.Methods.getGameVal;


public abstract class CCGameMode implements Disposable, IOnCrushListener {
    private static final String BG_PATH = "data/img/general/screen_bg_votetarget.png";
    private static final float DISPLAY_GAME_INFO_SEC = getGameVal(4f, 1f);

    protected final CCSoundBank soundBank;

    protected Stage stage;
    public Board board;
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
    protected IMusicHandler musicHandler;
    public IBlockProvider blockProvider;
    protected ScoringSystem scoringSystem;
    protected IBoardAnalyzer boardAnalyzer;
    protected IBoardInitializer boardInitializer;
    protected GameModeConfig config;

    protected CCGameMode(CandidateCrush game,
                         Stage stage,
                         GameModeConfig config) {
        this.game = game;
        this.stage = stage;
        this.boardInitializer = new BadBoardInitializer();
        this.boardAnalyzer = new GoodBoardAnalyzer();
        this.backgroundTexture = new Texture(getBackgroundTexturePath());
        this.latestCrushes = new LinkedList<>();
        this.fontCache = new FontCache(new FontGenerator(Color.WHITE));
        this.config = config;

        this.soundBank = CCSoundBank.getInstance();
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
        // overridden by subclass
        setMusicHandler();
        setBlockProvider();
        setScoringSystem();

        board = instantiateBoard();
        initStage();
        setupInputMultiplexer();
        startBackgroundMusic();
        isInitialized = true;
    }

    protected Board instantiateBoard() {
        return new Board(
                config.boardSize,
                config.singleBlockDropTime,
                blockProvider,
                this,
                boardAnalyzer,
                boardInitializer
        );
    }

    private void initStage() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);

        // add board to main table
        Table boardTable = new Table();
        boardTable.add(board);
        mainTable.add(boardTable);

        stage.addActor(mainTable);
        mainTable.validate();

        // instantiate hud
        setHUD();
        hud.initStage();
    }

    private void startBackgroundMusic() {
        soundBank.bgMusic1.setLooping(true);
        soundBank.bgMusic1.setVolume(0.2f);
        musicHandler.setBackgroundMusic(soundBank.bgMusic1);
        musicHandler.playBackgroundMusic();
    }

    public void returnToMenu() {
        game.disposeCurrentScreen();
        game.setScreen(new MenuScreen(game));
    }

    public Music getGameEndedMusic() {
        Music music;
        if (wonGame()) {
            music = soundBank.winMusic;
        } else {
            music = soundBank.loseMusic;
        }
        music.setLooping(true);
        return music;
    }

    protected abstract void setHUD();

    protected abstract void setScoringSystem();

    protected abstract void setBlockProvider();

    protected abstract void setMusicHandler();

    protected abstract boolean wonGame();

    protected abstract boolean lostGame();

    protected final boolean isGameOver() {
        return wonGame() || lostGame();
    }

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
                boolean hasBeatenLevel = game.hasBeatenLevel(config.levelNum, config.isHardMode);
                hud.showGameEndMessage(wonGame(), hasBeatenLevel);
                displayedGameEndMessage = true;

                if (wonGame()) {
                    game.beatLevel(config.levelNum, config.isHardMode);
                }

                // play win/loss music
                musicHandler.stopAll();
                musicHandler.playMusic(getGameEndedMusic());
            }
        }

        hud.update(dt);
    }

    @Override
    public void onCrush(Crush crush) {
        if (!isGameOver()) {
            playSoundsForCrush(crush);
            scoringSystem.updateScore(crush);
            if (config.showCrushLabels) {
                addCrushVotesAndAnimations(crush);
            }
        }
    }

    public void playSoundsForCrush(Crush crush) {
        musicHandler.playSound(soundBank.popSound);
        if (crush.isWasUserInvoked()) {
            musicHandler.queueSoundByte(
                    crush.getLargestGroup().getType(),
                    crush.getLargestGroup().getCrushType());
        }
    }

    private void addCrushVotesAndAnimations(Crush crush) {
        for (SimpleBlockGroup bg : crush.getCrushedBlocks()) {
            stage.addActor(getCrushVoteLabelWithAnimation(bg, crush.isWasUserInvoked()));
        }
    }

    public void restartGame() {
        isGameStarted
                = showedGameInstructions
                = displayedGameEndMessage
                = false;

        board.initBoard();
        scoringSystem.reset();
        hud.reset();
        musicHandler.stopAll();
        musicHandler.playBackgroundMusic();
    }

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

    public GameModeConfig getConfig() {
        return config;
    }

    @Override
    public void dispose() {
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (board != null) board.dispose();
        if (hud != null) hud.dispose();
    }

}
