package com.gmail.enzocampanella98.candidatecrush.customui;

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

import static com.gmail.enzocampanella98.candidatecrush.tools.ImgTools.texRegionToPixmap;
import static com.gmail.enzocampanella98.candidatecrush.tools.ImgTools.texToPixmap;

public class CCButtonFactory implements Disposable {
    private static final String VOTE_BTN_PACK_PATH = "data/img/button_skin/vote_btn.pack";
    private static final String VOTE_BTN_UP = "btn_skin_up";
    private static final String VOTE_BTN_DOWN = "btn_skin_down";
    private static final String VOTE_BTN_CHECKED = "btn_skin_checked";

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
        return new Skin(new TextureAtlas(VOTE_BTN_PACK_PATH));
    }

    private ImageTextButton.ImageTextButtonStyle getVoteButtonStyle(BitmapFont font, Skin skin) {
        ImageTextButton.ImageTextButtonStyle voteButtonStyle = new ImageTextButton.ImageTextButtonStyle();
        voteButtonStyle.checked = skin.getDrawable(VOTE_BTN_CHECKED);
        voteButtonStyle.down = skin.getDrawable(VOTE_BTN_DOWN);
        voteButtonStyle.up = skin.getDrawable(VOTE_BTN_UP);
        voteButtonStyle.font = font;
        return voteButtonStyle;
    }

    private ImageTextButton.ImageTextButtonStyle getCandidateButtonStyle(
            BitmapFont font, Skin skin, Texture candidateTexture) {
        ImageTextButton.ImageTextButtonStyle voteButtonStyle = new ImageTextButton.ImageTextButtonStyle();
        Pixmap pixmapCand = texToPixmap(candidateTexture);

        TextureRegion regChecked = skin.getRegion(VOTE_BTN_CHECKED);
        TextureRegion regDown = skin.getRegion(VOTE_BTN_DOWN);
        TextureRegion regUp = skin.getRegion(VOTE_BTN_UP);

        Pixmap skinPixmap = texToPixmap(regChecked.getTexture());

        Pixmap pixmapChecked = texRegionToPixmap(skinPixmap, regChecked);
        Pixmap pixmapDown    = texRegionToPixmap(skinPixmap, regDown);
        Pixmap pixmapUp      = texRegionToPixmap(skinPixmap, regUp);

        int xf = 4, yf = 0;
        pixmapChecked.drawPixmap(pixmapCand, 0, 0,
                pixmapCand.getWidth(), pixmapCand.getHeight(), xf, yf, pixmapChecked.getHeight(), pixmapChecked.getHeight());
        pixmapDown.drawPixmap(pixmapCand, 0, 0,
                pixmapCand.getWidth(), pixmapCand.getHeight(), xf, yf, pixmapDown.getHeight(), pixmapDown.getHeight());
        pixmapUp.drawPixmap(pixmapCand, 0, 0,
                pixmapCand.getWidth(), pixmapCand.getHeight(), xf, yf, pixmapUp.getHeight(), pixmapUp.getHeight());

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
