package com.gmail.enzocampanella98.candidatecrush.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.scenes.HUD;

import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_WIDTH;
import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_HEIGHT;

/**
 * Created by Lorenzo Campanella on 8/7/2016.
 */
public class PlayScreen implements Screen {


    private HUD hud;
    private CandidateCrush game;
    private Viewport gameViewport;
    private Texture texture;
    private Pixmap bgMap;
    private Stage myStage;
    Texture worldTexture;
    Image tImage;
    Label tLabelHello;
    Sprite s;

    private OrthographicCamera cam;

    ShapeRenderer r;

    public PlayScreen(CandidateCrush game) {
        this.game = game;

        // init hud
        hud = new HUD(game.batch);

        // init camera
        cam = new OrthographicCamera();

        // init viewport
        gameViewport = new FitViewport(V_WIDTH, CandidateCrush.V_HEIGHT, cam);
        gameViewport.apply(true);

        // init textures and sprites
        texture = new Texture(Gdx.files.internal("img/block_sprites/trump_sprite.png"));
        // r = new ShapeRenderer();
        // texture = new Texture("badlogic.jpg");
        Pixmap myWorldPixmap = new Pixmap(V_WIDTH, CandidateCrush.V_HEIGHT, Pixmap.Format.RGBA8888);
        myWorldPixmap.setColor(Color.BLUE);
        myWorldPixmap.fillRectangle(0, 0, myWorldPixmap.getWidth(), myWorldPixmap.getHeight());
        worldTexture = new Texture(myWorldPixmap);
        s = new Sprite(worldTexture);

        // init font
        FreeTypeFontGenerator fg = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ShareTechMono-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size = 50;
        p.color = Color.WHITE;
        BitmapFont font = fg.generateFont(p);

        // init stage
        myStage = new Stage(gameViewport, game.batch);
        // init table
        Table t = new Table();
        t.setFillParent(true);
        t.setDebug(true);
        tImage = new Image(texture);
        tImage.scaleBy(5f);
        tLabelHello = new Label("Hello World!", new Label.LabelStyle(font, Color.WHITE));

        t.bottom();
        t.add(tImage).center().padBottom(10);
        t.row();
        t.add(tLabelHello);
        for (Cell c : t.getCells()) {
            System.out.println(String.format("C: %s, x = %f, y = %f", c.getActor().toString(), c.getActorX(), c.getActorY()));
        }
        // add actors to table
        myStage.addActor(new Image(s));
        myStage.addActor(t);
    }


    public void handleInput(float delta) {
        if (Gdx.input.justTouched()) {
            int mult = 10;
            tImage.moveBy(mult, (int) (mult * ((double)V_HEIGHT / V_WIDTH)));
            System.out.println(String.format("Texture: x=%f, y=%f", tImage.getX(), tImage.getY()));
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        handleInput(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        cam.update(); // update camera

        game.batch.setProjectionMatrix(cam.combined);
        hud.stage.draw();
        // myStage.draw(); // draws my stage (background, table (labels))
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        s.getTexture().dispose();
        texture.dispose();
        worldTexture.dispose();
    }
}
