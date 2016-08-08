package com.gmail.enzocampanella98.candidatecrush.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Lorenzo Campanella on 6/6/2016.
 */
public abstract class BlockAnimation {

    protected boolean wasStarted, justEnded;
    protected float currentTime, totalAnimationTime, dt, waitTime, currentWaitTime;
    private boolean isStarted;

    public BlockAnimation(float totalAnimationTime) {
        this.totalAnimationTime = totalAnimationTime;
        waitTime = 0f;

        wasStarted = false;
        isStarted = false;

    }

    public BlockAnimation(float totalAnimationTime, float waitTime) {
        this.totalAnimationTime = totalAnimationTime;
        this.waitTime = waitTime;

        wasStarted = false;
        isStarted = false;
    }

    public void update(float dt) {
        this.dt = dt;
        if (isStarted) {
            if (isWaiting()) {
                currentWaitTime += this.dt;
                if (!isWaiting()) {
                    this.dt = currentWaitTime - waitTime;
                    currentTime = this.dt;
                }
            }
            else if (isRunning()) {
                currentTime += this.dt;
                if (currentTime >= totalAnimationTime) {
                    if (justEnded) {
                        isStarted = false;
                        justEnded = false;
                    } else {
                        justEnded = true;
                        this.dt = totalAnimationTime - (currentTime - dt);
                    }
                }
            }
        }

    }

    public boolean wasStarted() {
        return wasStarted;
    }

    public boolean isRunning() {
        return isStarted && ! isWaiting();
    }

    public boolean isWaiting() {return currentWaitTime < waitTime;}

    public boolean isStarted() { return isStarted; }

    public void animate() {
        isStarted = true;
        wasStarted = true;
        currentTime = 0f;
        currentWaitTime = 0f;
    }

    public float timeLeft() {
        return totalAnimationTime - currentTime - currentWaitTime;
    }
}
