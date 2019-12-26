package com.gmail.enzocampanella98.candidatecrush.customui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;

import java.util.Collection;

/**
 * Created by enzoc on 6/2/2018.
 */

public class GameInfoBox extends Table implements Disposable {
    private static String bgPath = "data/img/general/info_bg.png";
    private static Texture bgTexture;

    private Drawable bgDrawable;

    public GameInfoBox() {
        getTexture();
        TextureRegion region = new TextureRegion(bgTexture, bgTexture.getWidth(), bgTexture.getHeight());
        bgDrawable = new TextureRegionDrawable(region);
        bgDrawable.setMinHeight(0);
        bgDrawable.setMinWidth(0);
        setBackground(bgDrawable);
    }

    public void addLines(Label.LabelStyle style, Collection<String> lines) {
        for (String s : lines) {
            Label l = new Label(s, style);
            add(l).pad(10f).row();
        }
    }

    public static Texture getTexture() {
        if (bgTexture == null) bgTexture = new Texture(bgPath);
        return bgTexture;
    }

    @Override
    public void dispose() {
        if (bgTexture != null) bgTexture.dispose();
    }
}
