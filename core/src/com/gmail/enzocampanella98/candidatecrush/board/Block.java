package com.gmail.enzocampanella98.candidatecrush.board;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.gmail.enzocampanella98.candidatecrush.action.MyBlockInflaterAction;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.BlockConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lorenzo Campanella on 6/7/2016.
 */
public class Block extends Group {

    private int row;
    private int col;
    private BlockConfig config;

    boolean visited; // used for traversing the board
    SimpleBlockGroup blockGroup; // used for traversing the board

    public Block(int row, int col, Vector2 initialPosition, float width, float height, BlockConfig config) {
        super();
        this.row = row;
        this.col = col;
        this.config = config;

        if (config.getBgTexture() != null) {
            Image bgImg = new Image(config.getBgTexture());
            bgImg.setBounds(0, 0, width, height);
            addActor(bgImg);
        }
        Image candImg = new Image(config.getCandidateTexture());
        candImg.setBounds(0, 0, width, height);
        addActor(candImg);

        setWidth(width);
        setHeight(height);
        setOrigin(getWidth() / 2, getHeight() / 2);

        Rectangle myBounds = new Rectangle(
                initialPosition.x, initialPosition.y,
                width, height);
        setBounds(myBounds);
    }

    public void setBounds(Rectangle bounds) {
        super.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void setBlockType(BlockType blockType) {
        config.setType(blockType);
    }

    public BlockType getBlockType() {
        return config.getType();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRowAndCol(int row, int col) {
        setRow(row);
        setCol(col);
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public static void flipRowAndCol(Block b1, Block b2) {
        int row1 = b1.getRow(), col1 = b1.getCol();
        b1.setRow(b2.getRow());
        b1.setCol(b2.getCol());
        b2.setRow(row1);
        b2.setCol(col1);
    }

    public void animateDown(float initDelay,
                            float sizeUpDuration,
                            float deltaY,
                            float moveDuration) {
        List<Actor> actorList = new ArrayList<>();
        actorList.add(this);
        for (Actor child : getChildren()) {
            actorList.add(child);
        }

        // delay everything
        // size down everything
        // make everything visible
        // size up everything while only moving the parent group
        for (Actor actor : actorList) {

            Action action;
            if (actor == this) {
                action = Actions.sequence(
                        Actions.delay(initDelay),
                        new MyBlockInflaterAction(0f, 0f),
                        Actions.visible(true),
                        Actions.parallel(
                                new MyBlockInflaterAction(getHeight(), sizeUpDuration),
                                Actions.moveBy(0f, deltaY, moveDuration)
                        )
                );
            } else {
                action = Actions.sequence(
                        Actions.delay(initDelay),
                        new MyBlockInflaterAction(0f, 0f),
                        Actions.visible(true),
                        new MyBlockInflaterAction(getHeight(), sizeUpDuration)
                );
            }
            actor.addAction(action);
        }
    }

    public boolean isPerformingAction() {
        return super.getActions().size > 0;
    }

    @Override
    public String toString() {
        String[] spl = getBlockType().getFriendlyName().split(" ");
        return Character.toString(spl[0].charAt(0))
                + Character.toString(spl[1].charAt(0))
                + " (" + row + "," + col + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Block) {
            Block b = (Block) other;
            return hashCode() == b.hashCode();
        } else {
            return false;
        }
    }

    public boolean isSoundByteBlock() {
        return config.isSoundByteBlock();
    }

    @Override
    public int hashCode() {
        return 100 * row + 10 * col + getBlockType().ordinal();
    }

}
