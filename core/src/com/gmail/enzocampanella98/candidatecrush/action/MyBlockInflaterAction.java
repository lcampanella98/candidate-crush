package com.gmail.enzocampanella98.candidatecrush.action;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Lorenzo Campanella on 12/19/2016.
 */
public class MyBlockInflaterAction extends Action {

    private float duration;
    private float time;
    private boolean started;
    private boolean finished;
    private float hFinal;
    private float h0;
    private float h;

    public MyBlockInflaterAction(float finalHeight, float duration) {
        super();
        started = finished = false;
        this.duration = duration;
        hFinal = finalHeight;
    }

    public MyBlockInflaterAction(float finalHeight) {
        this(finalHeight, 0f);
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public void setFinalHeight(float finalHeight) {
        hFinal = finalHeight;
    }

    @Override
    public boolean act(float delta) {
        if (finished) return true;
        Actor a = getActor();
        if (!started) {
            started = true;
            h0 = a.getHeight();
            h = h0;
        }
        if (time + delta >= duration) {
            delta = duration - time;
            finished = true;
        }
        time += delta;

        Vector2 prevOrigin = new Vector2(a.getOriginX(), a.getOriginY());
        a.setOrigin(0f, 0f);
        float delH = (hFinal - h0) * delta / duration;
        if (Float.isNaN(delH)) {
            delH = hFinal - h0;
        }
        h += delH;
        a.setHeight(h);

        a.setOrigin(prevOrigin.x, prevOrigin.y);
        return finished;
    }
}
