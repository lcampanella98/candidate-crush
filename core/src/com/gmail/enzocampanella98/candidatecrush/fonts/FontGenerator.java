package com.gmail.enzocampanella98.candidatecrush.fonts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.gmail.enzocampanella98.candidatecrush.screens.HUD;

public class FontGenerator implements IFontGenerator {

    private int borderWidth;
    private Color color;

    public FontGenerator(Color color) {
        this(0, color);
    }

    public FontGenerator(int borderWidth, Color color) {
        this.borderWidth = borderWidth;
        this.color = color;
    }

    @Override
    public BitmapFont generateFont(int size) {
        FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal(HUD.FONT_FILE));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = size;
        param.borderWidth = borderWidth;
        param.color = color;
        BitmapFont font = fontGen.generateFont(param);
        fontGen.dispose();
        return font;
    }
}
