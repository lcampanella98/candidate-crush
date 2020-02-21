package com.gmail.enzocampanella98.candidatecrush.customui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.IBlockProvider;

import java.util.Collection;

/**
 * Created by enzoc on 6/2/2018.
 */

public class GameInfoBox extends Table implements Disposable {
    private static String bgPath = "data/img/general/info_bg.png";
    private static Texture bgTexture;

    private Drawable bgDrawable;
    private final IBlockProvider blockProvider;
    private final Float blockWidth;

    public GameInfoBox() {
        this(null, null);
    }

    public GameInfoBox(IBlockProvider blockProvider, Float blockWidth) {
        this.blockProvider = blockProvider;
        this.blockWidth = blockWidth;
        getTexture();
        TextureRegion region = new TextureRegion(bgTexture, bgTexture.getWidth(), bgTexture.getHeight());
        bgDrawable = new TextureRegionDrawable(region);
        bgDrawable.setMinHeight(0);
        bgDrawable.setMinWidth(0);
        setBackground(bgDrawable);
    }

    public void addRows(Label.LabelStyle style, Collection<GameInstructionRow> rows) {
        for (GameInstructionRow row : rows) {
            Actor a;
            if (row.isTextLine()) {
                a = new Label(row.line, style);
            } else if (row.isBlock()) {
                a = blockProvider.provideFromConfig(0, 0, Vector2.Zero,
                        blockWidth, blockWidth, row.blockConfig);
            }
            else {
                a = row.actor; // we are sure the game instruction row has a actor
            }
            add(a).pad(10f).row();
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
