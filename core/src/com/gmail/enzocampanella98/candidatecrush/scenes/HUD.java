package com.gmail.enzocampanella98.candidatecrush.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;

import java.util.Locale;

/**
 * Created by Lorenzo Campanella on 8/8/2016.
 */
public class HUD {

    public Stage stage;
    private Viewport viewport;
    private int worldTimer;
    private float timeCount;
    private int score;
    private BitmapFont font;

    private Label countdownLabel, scoreLabel,
            timeLabel, levelLabel,
            worldLabel, marioLabel;

    public HUD(SpriteBatch sb) {
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(CandidateCrush.V_WIDTH, CandidateCrush.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        // table.setDebug(true);
        table.setFillParent(true);
        table.top();
        font = new BitmapFont();
        font.getData().setScale(4f);
        Label.LabelStyle lStyle = new Label.LabelStyle(font, Color.WHITE);
        countdownLabel = new Label(String.format(Locale.ENGLISH, "%03d", worldTimer), lStyle);
        scoreLabel = new Label(String.format(Locale.ENGLISH, "%06d", score), lStyle);
        timeLabel = new Label("TIME", lStyle);
        levelLabel = new Label("1-1", lStyle);
        worldLabel = new Label("WORLD", lStyle);
        marioLabel = new Label("MARIO", lStyle);

        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        stage.addActor(table);
    }
}
