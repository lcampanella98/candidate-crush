package com.gmail.enzocampanella98.candidatecrush.screens;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.SimpleBlockGroup;
import com.gmail.enzocampanella98.candidatecrush.customui.CCButton;
import com.gmail.enzocampanella98.candidatecrush.customui.CCButtonFactory;
import com.gmail.enzocampanella98.candidatecrush.customui.GameInfoBox;
import com.gmail.enzocampanella98.candidatecrush.customui.GameInstructionRow;
import com.gmail.enzocampanella98.candidatecrush.fonts.FontCache;
import com.gmail.enzocampanella98.candidatecrush.fonts.FontGenerator;
import com.gmail.enzocampanella98.candidatecrush.gamemode.CCGameMode;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.Locale;

import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_HEIGHT;
import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.MD;
import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.SM;
import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.XL;
import static com.gmail.enzocampanella98.candidatecrush.fonts.FontManager.fontSize;

public abstract class HUD implements Disposable {

    public static final String FONT_FILE = "data/fonts/LibreFranklin-Bold.ttf";
    protected final FontCache defaultFontCache;
    protected final FontCache whiteFontCache;

    protected Camera hudCam;
    protected Viewport hudViewport;
    protected SpriteBatch batch;
    protected Table mainTable;
    protected Stack stack;
    protected CCGameMode gameMode;

    public Stage hudStage;

    protected TextureAtlas btnExitAtlas;
    protected ImageButton btnExit;

    private float gameInfoMessageTimeLeft = 0f;

    private BitmapFont endFont;
    private GameInfoBox gameInstructionsBox;
    private GameInfoBox gameOverBox;

    private Cell firstCellBelowBoard;

    public static String scoreText(int votes) {
        return NumberFormat.getNumberInstance(Locale.US).format(votes);
    }

    protected HUD(CCGameMode gameMode) {
        this.gameMode = gameMode;

        hudCam = new OrthographicCamera();
        hudViewport = new FitViewport(CandidateCrush.V_WIDTH, CandidateCrush.V_HEIGHT, hudCam);
        hudViewport.apply(false);
        batch = new SpriteBatch();
        hudStage = new Stage(hudViewport, batch);

        defaultFontCache = new FontCache(new FontGenerator(Color.BLACK));
        whiteFontCache = new FontCache(new FontGenerator(Color.WHITE));
    }

    public void initStage() {
        stack = new Stack();
        hudStage.addActor(stack);
        stack.setFillParent(true);
        initTable();
        initInfoOverlay();
        initGameoverOverlay();
    }

    public abstract Vector2 getScoreInfoBoxPosition(SimpleBlockGroup group);

    private void initTable() {
        mainTable = new Table();
        mainTable.top();
        stack.add(mainTable);

        addExitButton();

        // overridden by subclass
        addActorsToTable();
    }

    private void addExitButton() {
        // init exit button
        btnExitAtlas = new TextureAtlas("data/img/button_skin/btn-exit.atlas");
        Skin exitBtnSkin = new Skin(btnExitAtlas);
        ImageButton.ImageButtonStyle btnExitStyle = new ImageButton.ImageButtonStyle();
        btnExitStyle.up = exitBtnSkin.getDrawable("btn");
        btnExitStyle.down = exitBtnSkin.getDrawable("btn");
        btnExitStyle.over = exitBtnSkin.getDrawable("btn");

        btnExit = new ImageButton(btnExitStyle);
        btnExit.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gameMode.returnToMenu();
                return true;
            }
        });
        mainTable.add(btnExit).pad(6f).left();
        mainTable.row();
    }

    private void initInfoOverlay() {
        // setup animations for info box
        gameInstructionsBox = new GameInfoBox(gameMode.blockProvider, gameMode.board.getBlockSpacing()); // we need to pass the parameters because we are adding GameInstructionRows

        Label.LabelStyle instructionsLabelStyle = new Label.LabelStyle(defaultFontCache.get(fontSize(MD)), Color.BLACK);
        gameInstructionsBox.addRows(instructionsLabelStyle, getGameInfoDialogTextLines());
        gameInstructionsBox.pad(20f);

        Table overlay = new Table();
        overlay.bottom();
        overlay.add(gameInstructionsBox);
        stack.add(overlay);
    }

    private void initGameoverOverlay() {
        Table overlay = new Table();
        gameOverBox = new GameInfoBox();
        gameOverBox.center();
        gameOverBox.setVisible(false);

        overlay.center().add(gameOverBox);
        stack.add(overlay);
    }

    public void showGameInfoDialog(float totalTimeSec) {
        // initialize wait time
        gameInfoMessageTimeLeft = totalTimeSec;

        float yFinal = -gameInstructionsBox.getPrefHeight() - 50;
        gameInstructionsBox.setY(0);

        DelayAction delay1 = new DelayAction(totalTimeSec / 6f);

        MoveByAction moveUp = new MoveByAction();
        moveUp.setAmountY(V_HEIGHT / 2f);
        moveUp.setDuration(totalTimeSec / 6f);

        DelayAction delay2 = new DelayAction(totalTimeSec * 3f / 6f);

        MoveByAction moveDown = new MoveByAction();
        moveDown.setAmountY(-V_HEIGHT / 2f + yFinal);
        moveDown.setDuration(totalTimeSec / 6f);
        SequenceAction seq = new SequenceAction(
                delay1, moveUp, delay2, moveDown
        );

        gameInstructionsBox.addAction(seq);
    }

    public void showGameEndMessage(boolean win, boolean hasBeatenLevel) {
        gameOverBox.clearChildren();

        String msg = win ? "You win!" : "You lose.";
        endFont = new FontGenerator(win ? Color.GREEN : Color.RED).generateFont(fontSize(XL));
        Label.LabelStyle lblStyle = new Label.LabelStyle(endFont, endFont.getColor());
        Label lblMessage = new Label(msg, lblStyle);
        int nCols = win ? 1 : 2;
        gameOverBox.center().pad(20f).add(lblMessage).colspan(nCols).padBottom(8f);
        gameOverBox.row();

        if (win && !hasBeatenLevel) {
            Label unlockLabelLine;
            Label.LabelStyle infoStyle = new Label.LabelStyle(defaultFontCache.get(fontSize(SM)), Color.BLACK);
            int lvlNum = gameMode.getConfig().levelNum;
            boolean isHardMode = gameMode.getConfig().isHardMode;

            Collection<BlockType> unlockedCandidates = gameMode.getLevelSet().getUnlockedCandidates(lvlNum);
            float candWidth = 200f;
            for (BlockType cand : unlockedCandidates) {
                Texture tex = gameMode.blockProvider.getBlockTexture(cand);
                gameOverBox.add(new Image(tex)).width(candWidth).height(candWidth).row();
                gameOverBox.add(new Label(String.format("%s unlocked!", cand.getFriendlyName()), infoStyle)).row();
            }

            Collection<String> lines = gameMode.getLevelSet().getUnlockMessages(lvlNum, isHardMode);
            for (String line : lines) {
                unlockLabelLine = new Label(line, infoStyle);
                gameOverBox.add(unlockLabelLine).center().colspan(nCols).row();
            }
        }

        CCButtonFactory fact = new CCButtonFactory(whiteFontCache);
        CCButton btnBack = fact.getVoteButton("Back", fontSize(MD));
        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMode.returnToMenu();
            }
        });
        float btnHt = 80f;
        gameOverBox.add(btnBack)
                .height(btnHt).width(btnBack.scaledWidth(btnHt)).center();
        if (!win) {
            CCButton btnPlayAgain = fact.getVoteButton("Play again", fontSize(MD));
            btnPlayAgain.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    gameMode.restartGame();
                }
            });
            gameOverBox.add(btnPlayAgain)
                    .height(btnHt).width(btnPlayAgain.scaledWidth(btnHt)).padLeft(10f).center();
        }
        gameOverBox.setVisible(true);
    }

    public void hideGameOver() {
        gameOverBox.setVisible(false);
    }

    public void update(float dt) {
        if (gameInfoMessageTimeLeft > 0) {
            gameInfoMessageTimeLeft -= dt;
        }
        updateLabels(dt);
        updateFirstCellBelowBoard();
        hudStage.act(dt);
    }

    private void updateFirstCellBelowBoard() {
        if (firstCellBelowBoard == null) return;

        float localY = firstCellBelowBoard.getActor().localToStageCoordinates(new Vector2(0, firstCellBelowBoard.getActorHeight())).y;
        float boardY = gameMode.board.getAbsY();
        float padTop = localY - boardY;
        if (padTop > 0) {
            // System.out.printf("%f, %f, %f\n", localY, boardY, padTop);
            firstCellBelowBoard.padTop(padTop + 10f); // add some extra space
            mainTable.invalidate();
        }
    }

    protected void setFirstCellBelowBoard(Cell firstCellBelowBoard) {
        this.firstCellBelowBoard = firstCellBelowBoard;
    }

    protected abstract void addActorsToTable();

    public boolean isGameInstructionsShowing() {
        return gameInfoMessageTimeLeft > 0;
    }

    public Collection<GameInstructionRow> getGameInfoDialogTextLines() {
        return gameMode.getConfig().instructionRows;
    }

    public abstract void updateLabels(float dt);

    public void draw() {
        hudCam.update();
        hudStage.draw();
    }

    public void reset() {
        hideGameOver();
    }

    @Override
    public void dispose() {
        if (btnExitAtlas != null) btnExitAtlas.dispose();
        defaultFontCache.dispose();
        if (endFont != null) endFont.dispose();
    }

}
