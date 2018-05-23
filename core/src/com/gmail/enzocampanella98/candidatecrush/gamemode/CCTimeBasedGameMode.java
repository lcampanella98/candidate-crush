package com.gmail.enzocampanella98.candidatecrush.gamemode;


import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class CCTimeBasedGameMode extends CCGameMode {

    protected double t;
    protected double gameLength;

    protected CCTimeBasedGameMode(Stage stage, double gameLength) {
        super(stage);
        this.gameLength = gameLength;
        this.t = 0.0;
    }

    public void resetTime() {
        this.t = 0.0;
    }

    public double getGameTimeElapsed() {
        return t;
    }

    public double getTimeLeft() {
        return gameLength - t;
    }

    public boolean isGameTimeUp() {
        return t >= gameLength;
    }

    public void advanceGameTime(float dt) {
        t += dt;
    }

}
