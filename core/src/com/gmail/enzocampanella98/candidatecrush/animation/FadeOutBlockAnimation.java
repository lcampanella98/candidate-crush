package com.gmail.enzocampanella98.candidatecrush.animation;

/**
 * Created by Lorenzo Campanella on 6/9/2016.
 */
public class FadeOutBlockAnimation extends ScaleBlockAnimation {

    public FadeOutBlockAnimation(float totalAnimationTime, float initialWidth, float initialHeight) {
        super(totalAnimationTime, initialWidth, initialHeight, 0f, 0f);
    }

    public FadeOutBlockAnimation(float totalAnimationTime, float waitTime, float initialWidth, float initialHeight) {
        super(totalAnimationTime, waitTime, initialWidth, initialHeight, 0f, 0f);
    }
}
