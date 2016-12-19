package com.gmail.enzocampanella98.candidatecrush.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.Board;
import com.gmail.enzocampanella98.candidatecrush.scenes.InGameHUD;

import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_WIDTH;
import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.V_HEIGHT;

/**
 * Created by Lorenzo Campanella on 8/7/2016.
 */
public class PlayScreen implements Screen {

    private InGameHUD hud;
    private CandidateCrush game;

    private Viewport gameViewport;
    private OrthographicCamera cam;

    private Texture worldTexture;
    private Pixmap bgMap;
    private Sprite s;

    private BitmapFont font;

    private Stage playStage;
    private Board gameBoard;
    private Image tImage;
    private Label tLabelHello;
    private Table table;

    private ObjectMap<BlockType, Texture> blockTextures;

    private static ObjectMap<BlockType, Texture> getBlockTextures() {
        ObjectMap<BlockType, Texture> blockTextures = new ObjectMap<BlockType, Texture>();
        for (BlockType b : BlockType.values()) {
            Texture t;
            if (b.equals(BlockType.BLANK)) continue;
            t = new Texture(b.getInternalPath());
            blockTextures.put(b, t);
        }
        return blockTextures;
    }

    public PlayScreen(CandidateCrush game) {
        this.game = game;

        blockTextures = getBlockTextures();

        // init camera
        cam = new OrthographicCamera();

        // init viewport
        gameViewport = new FitViewport(V_WIDTH, V_HEIGHT, cam);
        gameViewport.apply(true);

        // init textures and sprites
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
        font = fg.generateFont(p);

        // init stage
        playStage = new Stage(gameViewport, game.batch);

        // init gameBoard
        gameBoard = new Board(8, blockTextures);

        // init mainTable
        table = new Table();

        // init hud
        hud = new InGameHUD(gameBoard.getBoardHandler(), table);

        table.addActor(gameBoard);
        playStage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update(); // update camera

        game.batch.setProjectionMatrix(cam.combined);
        playStage.act(delta);
        playStage.draw();
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
        playStage.dispose();
        font.dispose();
        for(ObjectMap.Entry<BlockType, Texture> e : blockTextures) {
            e.value.dispose();
        }
        blockTextures.clear();
        gameBoard.dispose();
        s.getTexture().dispose();
        worldTexture.dispose();
    }
}
