package com.gmail.enzocampanella98.candidatecrush.fonts;

import com.badlogic.gdx.Gdx;

import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.scaled;

public class FontManager {

    public static final int MD = 50;
    public static final int SM = 30;
    public static final int LG = 70;
    public static final int XL = 100;

    public static final float iphone11Width = 1242.0f;

    public static int fontSize(int baseSize) {
        int b = baseSize;

        int w = Gdx.graphics.getWidth();
        // f(w, b) = the font size for a screen width and a base font size
        // f(i11_width, 50) = 50
        // f(i11_width, 30) = 30
        // f(i11_width, 70) = 70
        // f(w, b) = b + (some delta depending on the w - i11_width)
        // f(w, b) = b
        return Math.round(scaled(b));
    }

}
