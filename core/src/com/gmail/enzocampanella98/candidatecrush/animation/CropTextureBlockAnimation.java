package com.gmail.enzocampanella98.candidatecrush.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lorenzo Campanella on 6/9/2016.
 */
public class CropTextureBlockAnimation extends BlockAnimation {

    private Texture texture;
    private int x, y, startWidth, endWidth, startHeight, endHeight;
    private Rectangle cropRectangle;

    public CropTextureBlockAnimation(float totalAnimationTime,
                                     Texture texture,
                                     int x, int y,
                                     int startWidth, int endWidth,
                                     int startHeight, int endHeight) {
        super(totalAnimationTime);
        this.x = x;
        this.texture = texture;
        this.y = y;
        this.startHeight = startHeight;
        this.startWidth = startWidth;
        this.endHeight = endHeight;
        this.endWidth = endWidth;
        cropRectangle = new Rectangle();
    }

    public CropTextureBlockAnimation(float totalAnimationTime, float waitTime,
                                     Texture texture,
                                     int x, int y,
                                     int startWidth, int endWidth,
                                     int startHeight, int endHeight) {
        super(totalAnimationTime, waitTime);
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.startHeight = startHeight;
        this.startWidth = startWidth;
        this.endHeight = endHeight;
        this.endWidth = endWidth;
        cropRectangle = new Rectangle();
    }

    public Rectangle getCropRectangle() {
        return cropRectangle;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setCropRectangle();
    }

    private void setCropRectangle() {
        float rx, ry, rw, rh;
        rh = isRunning()
                ? (endHeight - startHeight)
                * (currentTime / totalAnimationTime) + startHeight
                : texture.getHeight();
        rw = isRunning()
                ? (endWidth - startWidth)
                * (currentTime / totalAnimationTime) + startWidth
                : texture.getWidth();
        rx = x;
        ry = texture.getHeight() - rh;
        cropRectangle.set(rx, ry, rw, rh);
    }

    public int getAnimatedHeight() {
        return Math.round(cropRectangle.getHeight());
    }

    public int getAnimatedWidth() {
        return Math.round(cropRectangle.getWidth());
    }

    public int getX() {
        return Math.round(cropRectangle.x);
    }

    public int getY() {
        return Math.round(cropRectangle.y);
    }
}
