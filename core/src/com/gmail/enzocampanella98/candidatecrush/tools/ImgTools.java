package com.gmail.enzocampanella98.candidatecrush.tools;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class ImgTools {

    public static Pixmap texRegionToPixmap(Pixmap texPixmap, TextureRegion region) {
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

    public static Pixmap texToPixmap(Texture texture) {
        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        return texture.getTextureData().consumePixmap();
    }

}
