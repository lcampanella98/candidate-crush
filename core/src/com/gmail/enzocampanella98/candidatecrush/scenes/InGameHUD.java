package com.gmail.enzocampanella98.candidatecrush.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.Board;
import com.gmail.enzocampanella98.candidatecrush.board.BoardHandler;

/**
 * Created by lorenzo on 9/6/2016.
 */
public class InGameHUD {

    private String scorePrefix;
    public Table mainTable;
    private Label labelScore;
    private Label labelScoreLabel;
    private Label labelLevel;
    private BitmapFont font;
    private Label.LabelStyle labelStyle;
    private BoardHandler boardHandler;

    public InGameHUD(BoardHandler boardHandler, Table mainTable) {
        this.boardHandler = boardHandler;
        this.mainTable = mainTable;

        // init font
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ShareTechMono-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 30;
        param.color = Color.BLACK;
        font = gen.generateFont(param);

        // init table elements
        scorePrefix = "Votes:";
        labelStyle = new Label.LabelStyle(font, Color.BLACK);
        labelScoreLabel = new Label(scorePrefix, labelStyle);
        labelScore = new Label(Integer.toString(boardHandler.getScore()), labelStyle);

        float normalPad = 10f, padScoreLabel, padScore;
        if (labelScoreLabel.getWidth() > labelScore.getWidth()) {
            padScoreLabel = normalPad;
            padScore = (labelScoreLabel.getWidth() - labelScore.getWidth()) / 2 + padScoreLabel;
        } else if (labelScore.getWidth() > labelScoreLabel.getWidth()) {
            padScore = normalPad;
            padScoreLabel = (labelScore.getWidth() - labelScoreLabel.getWidth()) / 2 + padScore;
        } else {
            padScoreLabel = normalPad;
            padScore = normalPad;
        }
        mainTable.top();
        mainTable.add(labelScoreLabel).right().padRight(padScoreLabel);
        mainTable.row();
        mainTable.add(labelScore).right().padRight(padScore);
    }
}
