package com.gmail.enzocampanella98.candidatecrush.gamemode;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.gmail.enzocampanella98.candidatecrush.board.Board;


public abstract class CCGameMode implements Disposable {

    protected Table table;
    protected Board board;


    protected CCGameMode(Table table) {
        this.table = table;
    }

    public abstract void onGameStart();

    public abstract void onGameEnd();

    public abstract void update(float dt);

}
