package com.gmail.enzocampanella98.candidatecrush.customui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.fonts.FontCache;
import com.gmail.enzocampanella98.candidatecrush.screens.MenuScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CCButtonFactory implements Disposable {
    private FontCache fontCache;
    private ObjectMap<BlockType, Texture> candTextures;
    private List<Texture> extraTextures;
    private Skin voteSkin;

    public CCButtonFactory(FontCache fontCache) {
        this.fontCache = fontCache;
        candTextures = BlockType.getAllBlockTextures();
        voteSkin = getVoteSkin();
        extraTextures = new ArrayList<>();
    }

    public CandidateButton getCandidateButton(int fontSize, BlockType candidate) {
        ImageTextButton.ImageTextButtonStyle style = getCandidateButtonStyle(
                fontCache.get(fontSize), voteSkin, candTextures.get(candidate));
        return new CandidateButton(style, candidate);
    }

    public CCButton getVoteButton(String text, int fontSize) {
        return new CCButton(text, getVoteButtonStyle(fontCache.get(fontSize), voteSkin));
    }

    public GameModeButton getGameModeButton(String text, int fontSize, MenuScreen.GameMode gameMode) {
        return new GameModeButton(text, getVoteButtonStyle(fontCache.get(fontSize), voteSkin), gameMode);
    }

    private Skin getVoteSkin() {
        return new Skin(new TextureAtlas("data/img/button_skin/vote_button.atlas"));
    }

    private ImageTextButton.ImageTextButtonStyle getVoteButtonStyle(BitmapFont font, Skin skin) {
        ImageTextButton.ImageTextButtonStyle voteButtonStyle = new ImageTextButton.ImageTextButtonStyle();
        voteButtonStyle.checked = skin.getDrawable("btn-checked");
        voteButtonStyle.down = skin.getDrawable("btn-down");
        voteButtonStyle.up = skin.getDrawable("btn-up");
        voteButtonStyle.font = font;
        return voteButtonStyle;
    }

    private ImageTextButton.ImageTextButtonStyle getCandidateButtonStyle(
            BitmapFont font, Skin skin, Texture candidateTexture) {
        ImageTextButton.ImageTextButtonStyle voteButtonStyle = new ImageTextButton.ImageTextButtonStyle();
        Pixmap pixmapCand = texToPixmap(candidateTexture);

        TextureRegion regChecked = skin.getRegion("btn-checked");
        TextureRegion regDown = skin.getRegion("btn-down");
        TextureRegion regUp = skin.getRegion("btn-up");

        Pixmap skinPixmap = texToPixmap(regChecked.getTexture());

        Pixmap pixmapChecked = texRegionToPixmap(skinPixmap, regChecked);
        Pixmap pixmapDown = texRegionToPixmap(skinPixmap, regDown);
        Pixmap pixmapUp = texRegionToPixmap(skinPixmap, regUp);

        pixmapChecked.drawPixmap(pixmapCand, 0, 0,
                pixmapCand.getWidth(), pixmapCand.getHeight(), 0, 0, pixmapChecked.getHeight(), pixmapChecked.getHeight());
        pixmapDown.drawPixmap(pixmapCand, 0, 0,
                pixmapCand.getWidth(), pixmapCand.getHeight(), 0, 0, pixmapChecked.getHeight(), pixmapChecked.getHeight());
        pixmapUp.drawPixmap(pixmapCand, 0, 0,
                pixmapCand.getWidth(), pixmapCand.getHeight(), 0, 0, pixmapChecked.getHeight(), pixmapChecked.getHeight());

        Texture tChecked = new Texture(pixmapChecked);
        Texture tDown = new Texture(pixmapDown);
        Texture tUp = new Texture(pixmapUp);
        extraTextures.addAll(Arrays.asList(tChecked, tDown, tUp));

        voteButtonStyle.checked = new TextureRegionDrawable(tChecked);
        voteButtonStyle.down = new TextureRegionDrawable(tDown);
        voteButtonStyle.up = new TextureRegionDrawable(tUp);

        // no longer need these pixmaps
        pixmapCand.dispose();
        skinPixmap.dispose();
        pixmapChecked.dispose();
        pixmapDown.dispose();
        pixmapUp.dispose();

        voteButtonStyle.font = font;
        return voteButtonStyle;
    }


    private static Pixmap texRegionToPixmap(Pixmap texPixmap, TextureRegion region) {
        Pixmap pixmap = new Pixmap(region.getRegionWidth(), region.getRegionHeight(), Pixmap.Format.RGBA8888);

        for (int x = 0; x < region.getRegionWidth(); x++) {
            for (int y = 0; y < region.getRegionHeight(); y++) {
                int colorInt = texPixmap.getPixel(region.getRegionX() + x, region.getRegionY() + y);
                // you could now draw that color at (x, y) of another pixmap of the size (regionWidth, regionHeight)
                pixmap.drawPixel(x, y, colorInt);
            }
        }
        return pixmap;
    }

    private static Pixmap texToPixmap(Texture texture) {
        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        return texture.getTextureData().consumePixmap();
    }

    @Override
    public void dispose() {
        for (Texture t : candTextures.values()) {
            if (t != null) t.dispose();
        }
        for (Texture t : extraTextures) {
            if (t != null) t.dispose();
        }
        voteSkin.dispose();
    }
}
