package com.gmail.enzocampanella98.candidatecrush.animation;

/**
 * Created by Lorenzo Campanella on 6/8/2016.
 */
public class ScaleBlockAnimation extends BlockAnimation {

    protected float endWidthScale, endHeightScale, initialWidth, initialHeight, w, h, endWidth, endHeight;

    public ScaleBlockAnimation(float totalAnimationTime,
                               float initialWidth, float initialHeight,
                               float endWidthScale, float endHeightScale) {
        super(totalAnimationTime);
        this.initialWidth = initialWidth;
        this.initialHeight = initialHeight;
        endWidth = initialWidth * endWidthScale;
        endHeight = initialHeight * endHeightScale;
        w = 0;
        h = 0;
    }

    public ScaleBlockAnimation(float totalAnimationTime, float waitTime,
                               float initialWidth, float initialHeight,
                               float endWidthScale, float endHeightScale) {
        super(totalAnimationTime, waitTime);
        this.initialWidth = initialWidth;
        this.initialHeight = initialHeight;
        endWidth = initialWidth * endWidthScale;
        endHeight = initialHeight * endHeightScale;
        w = 0;
        h = 0;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (isRunning() && !isWaiting()) {
            w += this.dt * (endWidth - initialWidth) / totalAnimationTime;
            h += this.dt * (endHeight - initialHeight) / totalAnimationTime;
        }
    }

    public float getWidth() {
        return w;
    }

    public float getHeight() {
        return h;
    }

}
