package com.gmail.enzocampanella98.candidatecrush.fonts;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

public class FontCache implements Disposable {
    private ObjectMap<Integer, BitmapFont> generatedFonts;
    private IFontGenerator generator;

    public FontCache(IFontGenerator generator) {
        generatedFonts = new ObjectMap<>();
        this.generator = generator;
    }

    public BitmapFont get(int size) {
        if (!generatedFonts.containsKey(size)) {
            generatedFonts.put(size, generator.generateFont(size));
        }
        return generatedFonts.get(size);
    }

    @Override
    public void dispose() {
        for (BitmapFont f : generatedFonts.values()) {
            f.dispose();
        }
    }
}
