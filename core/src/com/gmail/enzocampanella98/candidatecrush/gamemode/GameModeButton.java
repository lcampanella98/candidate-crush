package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;


public class GameModeButton extends ImageTextButton implements Disposable{
    private static ImageTextButtonStyle style = getButtonStyle();

    private static Skin voteButtonSkin;
    private static BitmapFont font;

    private static ImageTextButtonStyle getButtonStyle() {
        if (voteButtonSkin == null) {
            voteButtonSkin = new Skin(new TextureAtlas("data/img/button_skin/vote_button.atlas"));
        }
        if (font == null) {
            // init font
            FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal(HUD.FONT_FILE));
            FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
            param.size = 50;
            param.borderWidth = 2;
            param.color = Color.WHITE;
            font = fontGen.generateFont(param);
            fontGen.dispose();
        }

        ImageTextButton.ImageTextButtonStyle voteButtonStyle = new ImageTextButton.ImageTextButtonStyle();
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

    public float getHeightToWidthRatio() {
        return voteButtonSkin.getDrawable("btn-checked").getMinHeight() / voteButtonSkin.getDrawable("btn-checked").getMinWidth();
    }

    @Override
    public void dispose() {
        if (voteButtonSkin != null) {
            voteButtonSkin.dispose();
            voteButtonSkin = null;
        }
        if (font != null) {
            font.dispose();
            font = null;
        }
    }
}
