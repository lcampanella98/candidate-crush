package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;


public class GameModeButton extends TextButton implements Disposable{
    private static TextButtonStyle style = getButtonStyle();

    private static TextureAtlas voteButtonAtlas;
    private static BitmapFont font;

    private static TextButtonStyle getButtonStyle() {
        if (voteButtonAtlas == null) {
            voteButtonAtlas = new TextureAtlas("data/img/button_skin/vote_button.atlas");
        }
        if (font == null) {
            // init font
            FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("data/fonts/ShareTechMono-Regular.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
            param.size = 50;
            param.borderWidth = 2;
            param.color = Color.WHITE;
            font = fontGen.generateFont(param);
            fontGen.dispose();
        }
        Skin voteButtonSkin = new Skin(voteButtonAtlas);

        TextButton.TextButtonStyle voteButtonStyle = new TextButton.TextButtonStyle();
        voteButtonStyle.checked = voteButtonSkin.getDrawable("btn-checked");
        voteButtonStyle.down = voteButtonSkin.getDrawable("btn-down");
        voteButtonStyle.up = voteButtonSkin.getDrawable("btn-up");
        voteButtonStyle.font = font;
        return voteButtonStyle;
    }

    private CCGameMode.GameModeType gameModeType;

    public GameModeButton(String text, CCGameMode.GameModeType gameModeType) {
        super(text, GameModeButton.style);
        this.gameModeType = gameModeType;
    }

    public CCGameMode.GameModeType getGameModeType() {
        return this.gameModeType;
    }

    @Override
    public void dispose() {
        if (voteButtonAtlas != null) {
            voteButtonAtlas.dispose();
            voteButtonAtlas = null;
        }
        if (font != null) {
            font.dispose();
            font = null;
        }
    }
}
