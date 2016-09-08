package com.gmail.enzocampanella98.candidatecrush.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.Board;

/**
 * Created by lorenzo on 9/6/2016.
 */
public class InGameHUD {

    private String scorePrefix;
    public Stage stage;
    private Label labelScore;
    private Label labelLevel;
    private SpriteBatch sb;
    private Viewport viewport;
    private OrthographicCamera cam;
    private Table table;
    private BitmapFont font;
    private Label.LabelStyle labelStyle;
    private Board board;

    public InGameHUD(SpriteBatch sb, Board board) {
        this.sb = sb;
        this.board = board;
        cam = new OrthographicCamera(CandidateCrush.V_WIDTH, CandidateCrush.V_HEIGHT);
        viewport = new FitViewport(CandidateCrush.V_WIDTH, CandidateCrush.V_HEIGHT, cam);
        stage = new Stage(viewport, this.sb);

        table = new Table();

        // init font
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ShareTechMono-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 30;
        param.color = Color.BLACK;
        font = gen.generateFont(param);

        // init table elements
        scorePrefix = "Votes: ";
        labelStyle = new Label.LabelStyle(font, Color.BLACK);
        labelScore = new Label(scorePrefix + Integer.toString(0), labelStyle);
        
    }
}
