package com.gmail.enzocampanella98.candidatecrush.board;

/**
 * Created by Lorenzo Campanella on 7/29/2016.
 */
abstract class BoardTask implements Runnable {
    private boolean isDone;
    private boolean wasStarted = false;
    boolean wasStarted() { return wasStarted; }
    boolean isDone() { return isDone; };
    void done() { isDone = true; }
    void start() { wasStarted = true; }

    abstract void update(float dt);
}
