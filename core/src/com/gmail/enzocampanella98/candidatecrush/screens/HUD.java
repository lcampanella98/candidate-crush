package com.gmail.enzocampanella98.candidatecrush.screens;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.customui.GameInfoBox;
import com.gmail.enzocampanella98.candidatecrush.fonts.FontCache;
import com.gmail.enzocampanella98.candidatecrush.fonts.FontGenerator;
import com.gmail.enzocampanella98.candidatecrush.gamemode.CCGameMode;

import java.util.Collection;

import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_HEIGHT;
import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_WIDTH;

public abstract class HUD implements Disposable {

    public static final String FONT_FILE = "data/fonts/LibreFranklin-Bold.ttf";
    protected final FontCache fontCache;

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
    private float gameOverMessageTimeLeft = 0f;

    private String messageText;
    private BitmapFont messageFont;
    private BitmapFont endFont;
    private GameInfoBox gameInstructionsBox;

    protected HUD(CCGameMode gameMode) {
        this.gameMode = gameMode;

        hudCam = new OrthographicCamera();
        hudViewport = new FitViewport(CandidateCrush.V_WIDTH, CandidateCrush.V_HEIGHT, hudCam);
        hudViewport.apply(false);
        batch = new SpriteBatch();
        hudStage = new Stage(hudViewport, batch);

        fontCache = new FontCache(new FontGenerator(Color.BLACK));
    }

    public void initStage() {
        stack = new Stack();
        stack.setFillParent(true);
        initTable();
        initInfoOverlay();

        hudStage.addActor(stack);
    }


    private void initTable() {
        mainTable = new Table();
        mainTable.top();
        addExitButton();

        // overridden by subclass
        addActorsToTable();
        stack.add(mainTable);
    }

    protected void addExitButton() {
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
        mainTable.add(btnExit).left();
        mainTable.row();
    }

    private void initInfoOverlay() {
        // setup animations for info box
        gameInstructionsBox = new GameInfoBox();

        Label.LabelStyle instructionsLabelStyle = new Label.LabelStyle(fontCache.get(50), Color.BLACK);
        gameInstructionsBox.addLines(instructionsLabelStyle, getGameInfoDialogTextLines());

        Table overlay = new Table();
        overlay.bottom();
        overlay.add(gameInstructionsBox);
        stack.add(overlay);
    }

    public void showGameInfoDialog(float totalTimeSec) {
        // initialize wait time
        gameInfoMessageTimeLeft = totalTimeSec;

        DelayAction delay1 = new DelayAction(totalTimeSec / 6f);

        MoveByAction moveUp = new MoveByAction();
        moveUp.setAmountY(V_HEIGHT / 2f);
        moveUp.setDuration(totalTimeSec / 6f);

        DelayAction delay2 = new DelayAction(totalTimeSec * 3f / 6f);

        MoveByAction moveDown = new MoveByAction();
        moveDown.setAmountY(-V_HEIGHT / 2f - gameInstructionsBox.getPrefHeight() - 50);
        moveDown.setDuration(totalTimeSec / 6f);
        SequenceAction seq = new SequenceAction(
                delay1, moveUp, delay2, moveDown
        );

        gameInstructionsBox.addAction(seq);
    }

    public void showGameEndMessage(boolean win, float totalTime) {
        String msg;
        if (win) msg = "You win!";
        else msg = "You lose!";

        endFont = new FontGenerator(win ? Color.GREEN : Color.RED).generateFont(100);
        addMessage(msg, endFont);
        gameOverMessageTimeLeft = totalTime;
    }

    public void update(float dt) {
        if (gameInfoMessageTimeLeft > 0) {
            gameInfoMessageTimeLeft -= dt;
        }
        if (gameOverMessageTimeLeft > 0) {
            gameOverMessageTimeLeft -= dt;
        }
        updateLabels(dt);
        hudStage.act(dt);
    }

    protected abstract void addActorsToTable();

    public boolean isGameInstructionsShowing() {
        return gameInfoMessageTimeLeft > 0;
    }

    public boolean isGameOverMessageShowing() {
        return gameOverMessageTimeLeft > 0;
    }

    public abstract Collection<String> getGameInfoDialogTextLines();

    public abstract void updateLabels(float dt);

    public void addMessage(String msg, BitmapFont font) {
        this.messageText = msg;
        this.messageFont = font;
    }

    public void clearMessage() {
        this.messageText = null;
        this.messageFont = null;
    }

    public boolean hasMessage() {
        return messageFont != null && messageText != null;
    }

    public void drawCenteredMessage() {
        GlyphLayout layout = new GlyphLayout(messageFont, messageText);
        Texture tex = GameInfoBox.getTexture();
        float pad = 60, w = layout.width + pad, h = layout.height + pad;
        batch.draw(tex, V_WIDTH / 2 - w / 2, V_HEIGHT / 2 - h / 2, w, h);
        messageFont.draw(batch, messageText, V_WIDTH / 2 - layout.width / 2, V_HEIGHT / 2 + layout.height / 2);

    }

    public void draw() {
        hudCam.update();

        if (hasMessage()) {
            batch.begin();
            drawCenteredMessage();
            batch.end();
        }

        hudStage.draw();
    }

    @Override
    public void dispose() {
        if (btnExitAtlas != null) btnExitAtlas.dispose();
        fontCache.dispose();
        if (endFont != null) endFont.dispose();
    }

}
