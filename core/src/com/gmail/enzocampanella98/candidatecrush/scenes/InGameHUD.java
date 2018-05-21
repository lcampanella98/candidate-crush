package com.gmail.enzocampanella98.candidatecrush.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gmail.enzocampanella98.candidatecrush.board.BoardHandler;

/**
 * Created by lorenzo on 9/6/2016.
 */
public class InGameHUD {

    private String scorePrefix;
    public Table table;
    private Label labelScore;
    private Label labelScoreLabel;
    private Label labelLevel;
    private BitmapFont font;
    private Label.LabelStyle labelStyle;
    private BoardHandler boardHandler;

    public InGameHUD(BoardHandler boardHandler, Table table) {
        this.boardHandler = boardHandler;
        this.table = table;

        // init font
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("data/fonts/ShareTechMono-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 100;
        param.color = Color.BLACK;
        font = gen.generateFont(param);

        // init table elements
        scorePrefix = "Votes: ";
        labelStyle = new Label.LabelStyle(font, Color.BLACK);
        labelScoreLabel = new Label(scorePrefix, labelStyle);
        labelScore = new Label(Integer.toString(boardHandler.getScore()), labelStyle);

        table.add(labelScoreLabel);
        table.add(labelScore);
    }

    public void update(float dt) {
        labelScore.setText(Integer.toString(boardHandler.getScore()));
    }
}
