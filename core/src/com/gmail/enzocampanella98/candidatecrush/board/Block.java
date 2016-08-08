package com.gmail.enzocampanella98.candidatecrush.board;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.gmail.enzocampanella98.candidatecrush.animation.*;

/**
 * Created by Lorenzo Campanella on 6/7/2016.
 */
public class Block {

    private int row;
    private int col;
    private BlockType blockType;
    private Vector2 initialPosition;
    private Vector2 pos;

    private TextureRegion myTextureRegion;
    private Texture texture;

    private Queue<Array<BlockAnimation>> animationQueue;

    private Rectangle animatedBlock;
    private float initialWidth, initialHeight, widthScale, heightScale;

    public Block(BlockType blockType, Vector2 initialPosition,
                 float initialWidth, float initialHeight,
                 int row, int col) {
        this.row = row;
        this.col = col;

        animationQueue = new Queue<Array<BlockAnimation>>();

        setBlockType(blockType);
        setInitialPosition(new Vector2(initialPosition));

        texture = Board.blockTextures.get(blockType);

        this.pos = new Vector2(this.initialPosition);
        this.initialHeight = initialHeight;
        this.initialWidth = initialWidth;

        myTextureRegion = new TextureRegion(texture);

        widthScale = initialWidth / myTextureRegion.getRegionWidth();
        heightScale = initialHeight / myTextureRegion.getRegionHeight();

        animatedBlock = new Rectangle(
                initialPosition.x, initialPosition.y,
                initialWidth, initialHeight);
    }

    public void resetPosition() {
        pos = new Vector2(initialPosition);
    }

    public void lockPosition() {
        initialPosition = new Vector2(pos);
    }

    public void setInitialPosition(Vector2 initialPosition) {
        this.initialPosition = initialPosition;
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

    public void addAnimation(Array<BlockAnimation> animationList) {
        animationQueue.addLast(animationList);
    }

    public void addAnimation(BlockAnimation animation) {
        addAnimation(Array.with(animation));
    }

    public void render(SpriteBatch batch) {
        batch.draw(myTextureRegion,
                animatedBlock.x, animatedBlock.y,
                animatedBlock.getWidth(), animatedBlock.getHeight());

    }

    public void update(float dt) {
        setAnimatedBlock();
        if (animationQueue.size > 0) {
            Array<BlockAnimation> currentAnimations = animationQueue.first();
            if ((currentAnimations.size > 0) && (!currentAnimations.get(0).wasStarted())) {
                for (BlockAnimation animation : currentAnimations) {
                    animation.animate();
                }
            } else {
                boolean stillRunning = false;
                for (BlockAnimation animation : currentAnimations) {
                    if (animation.isStarted()) {
                        stillRunning = true;
                        animation.update(dt);
                    }
                }
                if (!stillRunning) {
                    animationQueue.removeFirst();
                }
            }
        }

    }

    public float getInitialWidth() {
        return initialWidth;
    }

    public float getInitialHeight() {
        return initialHeight;
    }

    public float getAnimatedWidth() {
        return animatedBlock.getWidth();
    }

    public float getAnimatedHeight() {
        return animatedBlock.getHeight();
    }

    public Vector2 getInitialPosition() {
        return initialPosition;
    }

    public Rectangle getAnimatedBlock() {
        return animatedBlock;
    }

    public CropTextureBlockAnimation getDropDownCropBlockAnimation(float totalAnimationTime) {
        return getDropDownCropBlockAnimation(totalAnimationTime, 0f);
    }

    public CropTextureBlockAnimation getDropDownCropBlockAnimation(float totalAnimationTime, float waitTime) {
        return new CropTextureBlockAnimation(totalAnimationTime, waitTime,
                getTexture(), 0, 0,
                getTextureWidth(), getTextureWidth(), 0, getTextureHeight());
    }


    private void setAnimatedBlock() {
        float addedWidth = 0f, addedHeight = 0f;
        if (animationQueue.size > 0) {
            Array<BlockAnimation> blockAnimations = animationQueue.first();
            for (BlockAnimation blockAnimation :
                    blockAnimations) {
                if (blockAnimation instanceof TranslationBlockAnimation) {
                    pos.add(((TranslationBlockAnimation) blockAnimation).getTranslation());
                } else if (blockAnimation instanceof ScaleBlockAnimation) {
                    addedWidth = ((ScaleBlockAnimation) blockAnimation).getWidth();
                    addedHeight = ((ScaleBlockAnimation) blockAnimation).getHeight();
                } else if (blockAnimation instanceof CropTextureBlockAnimation) {
                    CropTextureBlockAnimation cropTextureBlockAnimation = (CropTextureBlockAnimation) blockAnimation;
                    myTextureRegion.setRegion(
                            cropTextureBlockAnimation.getX(),
                            cropTextureBlockAnimation.getY(),
                            cropTextureBlockAnimation.getAnimatedWidth(),
                            cropTextureBlockAnimation.getAnimatedHeight());
                } else if (blockAnimation instanceof DisappearBlockAnimation) {
                    myTextureRegion.setRegion(0, 0, 0, 0);
                }
            }
        }
        float newWidth = widthScale * myTextureRegion.getRegionWidth() + addedWidth;
        float newHeight = heightScale * myTextureRegion.getRegionHeight() + addedHeight;

        animatedBlock.set(pos.x, pos.y, newWidth, newHeight);

        /*if (Board.testBlock == this) {
            System.out.println("animated block for " + toString() + ": " + animatedBlock.toString());
        }*/

    }

    public float getAnimationTimeLeft() {
        float maxTimeLeft = 0f;
        if (animationQueue.size == 0) return maxTimeLeft;
        for (BlockAnimation animation : animationQueue.first()) {
            float timeLeft = animation.timeLeft();
            if (timeLeft > maxTimeLeft) maxTimeLeft = timeLeft;
        }
        return maxTimeLeft;
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

    public boolean isAnimating() {
        return animationQueue.size > 0;
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
            return b.getRow() == getRow()
                    && b.getCol() == getCol()
                    && b.getBlockType() == getBlockType();
        } else {
            return false;
        }
    }

}
