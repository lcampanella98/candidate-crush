package com.gmail.enzocampanella98.candidatecrush.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.customui.GameInfoBox;
import com.gmail.enzocampanella98.candidatecrush.gamemode.CCGameMode;

import java.util.HashMap;

import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_HEIGHT;
import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_WIDTH;

public abstract class HUD implements Disposable {

    public static final String FONT_FILE = "data/fonts/LibreFranklin-Bold.ttf";

    protected Camera hudCam;
    protected Viewport hudViewport;
    protected SpriteBatch batch;
    protected Table table;
    protected CCGameMode gameMode;

    private HashMap<Integer, BitmapFont> fonts;
    private HashMap<String, BitmapFont> namedFonts;

    public Stage hudStage;

    protected TextureAtlas btnExitAtlas;
    protected ImageButton btnExit;

    private String messageText;
    private BitmapFont messageFont;

    protected HUD(CCGameMode gameMode) {
        this.gameMode = gameMode;

        hudCam = new OrthographicCamera();
        hudViewport = new FitViewport(CandidateCrush.V_WIDTH, CandidateCrush.V_HEIGHT, hudCam);
        hudViewport.apply(false);
        batch = new SpriteBatch();
        hudStage = new Stage(hudViewport, batch);

        fonts = new HashMap<Integer, BitmapFont>();
        namedFonts = new HashMap<String, BitmapFont>();
    }

    protected void addFontSize(int fontSize) {
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal(FONT_FILE));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.color = Color.BLACK;
        param.size = fontSize;
        fonts.put(fontSize, gen.generateFont(param));
        gen.dispose();
    }

    protected void addFontSizes(int[] sizes) {
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal(FONT_FILE));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.color = Color.BLACK;
        for (int fontSize : sizes) {
            param.size = fontSize;
            fonts.put(fontSize, gen.generateFont(param));
        }
        gen.dispose();
    }

    protected void addNewFont(int fontSize, Color color, String name) {
        if (namedFonts.containsKey(name)) return;
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal(FONT_FILE));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.color = color;
        param.size = fontSize;
        namedFonts.put(name, gen.generateFont(param));
        gen.dispose();
    }

    public boolean hasNamedFont(String name) {return namedFonts.containsKey(name);}

    public BitmapFont getFont(String name) {return namedFonts.get(name);}

    public BitmapFont getFont(int fontSize) {
        return fonts.get(fontSize);
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
                gameMode.dispose();
                gameMode.getGame().setScreen(new MenuScreen(gameMode.getGame()));
                return true;
            }
        });
        if (table != null) {
            table.add(btnExit).left();
            table.row();
        }
    }

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
        float pad=60, w = layout.width + pad, h = layout.height + pad;
        batch.draw(tex, V_WIDTH / 2 - w / 2, V_HEIGHT / 2 - h/2, w, h);
        messageFont.draw(batch, messageText, V_WIDTH / 2 - layout.width / 2, V_HEIGHT / 2 +layout.height/2);

    }

    public abstract void render(float dt);

    public void dispose() {
        if (btnExitAtlas != null) btnExitAtlas.dispose();
        for (BitmapFont font : fonts.values()) font.dispose();
        for (BitmapFont font : namedFonts.values()) font.dispose();
    }

}
