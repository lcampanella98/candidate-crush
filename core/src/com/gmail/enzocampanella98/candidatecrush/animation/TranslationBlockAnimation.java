package com.gmail.enzocampanella98.candidatecrush.animation;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Lorenzo Campanella on 6/7/2016.
 */
public class TranslationBlockAnimation extends BlockAnimation {

    private float distance;
    private Vector2 direction;

    public TranslationBlockAnimation(float totalAnimationTime, Vector2 direction, float distance) {
        super(totalAnimationTime);
        this.direction = new Vector2(direction).setLength(1f);
        this.distance = distance;
    }

    public TranslationBlockAnimation(float totalAnimationTime, float waitTime, Vector2 direction, float distance) {
        super(totalAnimationTime, waitTime);
        this.direction = new Vector2(direction).setLength(1f);
        this.distance = distance;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public Vector2 getTranslation() {
        if (isRunning()) {
            float scale = dt * distance / totalAnimationTime;
            return new Vector2(direction).scl(scale);
        } else {
            return new Vector2(0f, 0f);
        }

    }

}
