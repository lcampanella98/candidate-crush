package com.gmail.enzocampanella98.candidatecrush.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ObjectMap;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.Board;

/**
 * Created by Lorenzo Campanella on 6/1/2016.
 */
public class PlayState extends State {

    static final String SCORE_NAME = "Votes";

    private String fontFileInternal;

    private Board board;
    private ObjectMap<BlockType, Texture> blockTextures;
    private BitmapFont infoFont;

    private Stage stage;

    protected PlayState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setBlockTextures();
        board = new Board(6, blockTextures);
        board.setBoardHandler(300, 800, 3700, 2000);
        fontFileInternal = "fonts/ShareTechMono-Regular.ttf";
        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal(fontFileInternal));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.BLACK;
        parameter.size = 30;
        infoFont = generator.generateFont(parameter);
        generator.dispose();
        stage = new Stage();
    }

    private void setBlockTextures() {
        blockTextures = new ObjectMap<BlockType, Texture>();
        for (BlockType b : BlockType.values()) {
            Texture t;
            if (b.equals(BlockType.BLANK)) continue;
            t = new Texture(b.getInternalPath());
            blockTextures.put(b, t);
        }
    }

    @Override
    protected void handleInput() {
        mouse.set(Gdx.input.getX(), Gdx.input.getY());
        board.handleInput(mouse);
    }

    @Override
    public void update(float dt) {
        board.update(dt);
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        sb.begin();
        board.render(sb);
        drawInfo(sb);
        sb.end();
    }

    private void drawInfo(SpriteBatch sb) {
        GlyphLayout glScore = new GlyphLayout(infoFont, SCORE_NAME + ":\t" + board.getScore());
        Rectangle boardBounds = board.getBoardBounds();
        infoFont.draw(sb, glScore,
                boardBounds.x + boardBounds.width / 2 - glScore.width / 2,
                boardBounds.y / 2 + glScore.height / 2);
    }

    private void drawBackground(SpriteBatch sb) {

    }

    @Override
    public void dispose() {
        board.dispose();
        infoFont.dispose();
    }
}
