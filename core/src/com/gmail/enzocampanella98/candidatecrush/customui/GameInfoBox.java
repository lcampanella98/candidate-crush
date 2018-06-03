package com.gmail.enzocampanella98.candidatecrush.customui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by enzoc on 6/2/2018.
 */

public class GameInfoBox extends Table implements Disposable {
    private static String bgPath = "data/img/general/info_bg.png";
    private static Texture bgTexture;

    private Drawable bgDrawable;

    public GameInfoBox() {
        if (bgTexture == null) bgTexture = new Texture(bgPath);
        TextureRegion region = new TextureRegion(bgTexture, bgTexture.getWidth(), bgTexture.getHeight());
        bgDrawable = new TextureRegionDrawable(region);
        bgDrawable.setMinHeight(0);
        bgDrawable.setMinWidth(0);
        setBackground(bgDrawable);
    }

    @Override
    public void dispose() {
        if (bgTexture != null) bgTexture.dispose();
    }
}
