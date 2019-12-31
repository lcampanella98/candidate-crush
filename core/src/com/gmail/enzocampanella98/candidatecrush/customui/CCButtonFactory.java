package com.gmail.enzocampanella98.candidatecrush.customui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.gmail.enzocampanella98.candidatecrush.fonts.FontCache;
import com.gmail.enzocampanella98.candidatecrush.level.Level;
import com.gmail.enzocampanella98.candidatecrush.tools.Methods;

public class CCButtonFactory implements Disposable {
    private static final String VOTE_BTN_PACK_PATH = "data/img/button_skin/vote_btn.pack";
    private static final String VOTE_BTN_UP = "btn_skin_up";
    private static final String VOTE_BTN_DOWN = "btn_skin_down";
    private static final String VOTE_BTN_CHECKED = "btn_skin_checked";
    private static final String VOTE_BTN_DISABLED = "btn_skin_disabled";

    private FontCache fontCache;
    private Skin voteSkin;

    public CCButtonFactory(FontCache fontCache) {
        this.fontCache = fontCache;
        voteSkin = getVoteSkin();
    }

    public LevelButton getLevelButton(Level level, int fontSize, boolean levelUnlocked) {
        return new LevelButton(
                getVoteButtonStyle(fontCache.get(fontSize), voteSkin),
                level, levelUnlocked);
    }

    public CCButton getVoteButton(String text, int fontSize) {
        return new CCButton(text, getVoteButtonStyle(fontCache.get(fontSize), voteSkin));
    }

    private Skin getVoteSkin() {
        return new Skin(new TextureAtlas(VOTE_BTN_PACK_PATH));
    }

    private ImageTextButton.ImageTextButtonStyle getVoteButtonStyle(BitmapFont font, Skin skin) {
        ImageTextButton.ImageTextButtonStyle voteButtonStyle = new ImageTextButton.ImageTextButtonStyle();
        voteButtonStyle.checked = skin.getDrawable(VOTE_BTN_CHECKED);
        voteButtonStyle.down = skin.getDrawable(VOTE_BTN_DOWN);
        voteButtonStyle.up = skin.getDrawable(VOTE_BTN_UP);
        voteButtonStyle.disabled = skin.getDrawable(VOTE_BTN_DISABLED);
        voteButtonStyle.disabledFontColor = Methods.colorFromRGB(219, 219, 219);
        voteButtonStyle.font = font;
        return voteButtonStyle;
    }

    @Override
    public void dispose() {
        voteSkin.dispose();
    }
}
