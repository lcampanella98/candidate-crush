package com.gmail.enzocampanella98.candidatecrush.board;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Lorenzo Campanella on 6/7/2016.
 */
public class Block extends Image {

    private int row;
    private int col;
    private BlockType blockType;

    private Texture texture;


    public Block(BlockType blockType, Texture texture, Vector2 initialPosition,
                 float width, float height,
                 int row, int col) {
        super();
        this.row = row;
        this.col = col;

        setBlockType(blockType);
        setPosition(initialPosition.x, initialPosition.y);

        this.texture = texture;
        TextureRegion myTextureRegion = new TextureRegion(texture);
        super.setDrawable(new TextureRegionDrawable(myTextureRegion));

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
        this.blockType = blockType;
    }

    public BlockType getBlockType() {
        return blockType;
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

    public int getTextureWidth() {
        return texture.getWidth();
    }

    public int getTextureHeight() {
        return texture.getHeight();
    }

    public Texture getTexture() {
        return texture;
    }

    public static void flipRowAndCol(Block b1, Block b2) {
        int row1 = b1.getRow(), col1 = b1.getCol();
        b1.setRow(b2.getRow());
        b1.setCol(b2.getCol());
        b2.setRow(row1);
        b2.setCol(col1);
    }

    public boolean isPerformingAction() {
        return super.getActions().size > 0;
    }

    @Override
    public String toString() {
        String[] spl = blockType.getFriendlyName().split(" ");
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

    @Override
    public int hashCode() {
        return 100 * row + 10 * col + blockType.ordinal();
    }
}
