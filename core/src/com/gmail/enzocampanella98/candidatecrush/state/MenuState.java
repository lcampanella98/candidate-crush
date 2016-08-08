package com.gmail.enzocampanella98.candidatecrush.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Lorenzo Campanella on 6/1/2016.
 */
public class MenuState extends State {

    private Texture bgTexture;
    private Image bgImage;
    private TextButton btnPlay;
    private TextButton.TextButtonStyle btnPlayStyle;
    private Skin skinPlay;
    private Stage stage;
    private Table tablePlay;
    private TextureAtlas btnAtlas;
    private BitmapFont font;
    private GameStateManager gsm;

    public MenuState(final GameStateManager gsm) {
        super(gsm);

        this.gsm = gsm;

        // init textures
        bgTexture = new Texture("img/general/menu_image_boxing.jpg");
        bgImage = new Image(bgTexture);

        // init stage
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // init bg
        bgImage.setBounds(0f, 0f, stage.getWidth(), stage.getHeight());
        stage.addActor(bgImage);

        // init font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/TitilliumWeb-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
        generator.dispose();

        // init buttons
        btnAtlas = new TextureAtlas("playbutton.pack");
        skinPlay = new Skin(btnAtlas);

        btnPlayStyle = new TextButton.TextButtonStyle();
        btnPlayStyle.up = skinPlay.getDrawable("skin-up");
        btnPlayStyle.down = skinPlay.getDrawable("skin-down");
        btnPlayStyle.font = font;
        btnPlayStyle.fontColor = com.badlogic.gdx.graphics.Color.BLACK;
        btnPlay = new TextButton("Play", btnPlayStyle);

        btnPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gsm.set(new PlayState(gsm));
            }
        });

        tablePlay = new Table(skinPlay);
        tablePlay.setPosition(Gdx.graphics.getWidth() / 2 - btnPlay.getWidth() / 2, Gdx.graphics.getHeight() / 4 - btnPlay.getHeight() / 2);
        tablePlay.addActor(btnPlay);
        stage.addActor(tablePlay);
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        mouse.set(Gdx.input.getX(), Gdx.input.getY());
    }

    @Override
    public void render(SpriteBatch sb) {

        stage.act();
        stage.draw();
    }

    public void drawBackground(SpriteBatch sb) {
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    @Override
    public void dispose() {
        skinPlay.dispose();
        font.dispose();
        btnAtlas.dispose();
        stage.dispose();
    }
}
