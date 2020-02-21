package com.gmail.enzocampanella98.candidatecrush.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.IBlockProvider;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.IBoardAnalyzer;
import com.gmail.enzocampanella98.candidatecrush.board.blockConfig.IBoardInitializer;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;


public class Board extends Group implements Disposable {

    private static final float SINGLE_BLOCK_SWAP_TIME = .3f;

    private Block[][] blocks;

    private Texture boardTexture;
    private Rectangle boardBounds;

    private int numBlocksAcross;
    private float singleBlockDropTime;

    private final IBlockProvider blockProvider;
    private final IOnCrushListener onCrushListener;
    private final IBoardAnalyzer boardAnalyzer;
    private final IBoardInitializer boardInitializer;

    private float blockSpacing;

    private Array<SimpleBlockGroup> blockGroups; // not set to null because we need it to animate

    private int numTotalCrushes;
    private float boardPad;

    public Board(int numBlocksAcross,
                 float singleBlockDropTime,
                 IBlockProvider blockProvider,
                 IOnCrushListener onCrushListener,
                 IBoardAnalyzer boardAnalyzer,
                 IBoardInitializer boardInitializer) {
        this.numBlocksAcross = numBlocksAcross;
        this.singleBlockDropTime = singleBlockDropTime;
        this.blockProvider = blockProvider;
        this.onCrushListener = onCrushListener;
        this.boardAnalyzer = boardAnalyzer;
        this.boardInitializer = boardInitializer;

        initBoard();
    }

    public IBoardAnalyzer getBoardAnalyzer() {
        return boardAnalyzer;
    }

    public int getNumBlocksAcross() {
        return numBlocksAcross;
    }

    public float getBlockSpacing() {
        return blockSpacing;
    }

    public void initBoard() {
        clearChildren();

        this.numTotalCrushes = 0; // track number of user-invoked crushes


        int boardWidth = (int)((9.0/16.0)*CandidateCrush.V_HEIGHT);

        //noinspection SuspiciousNameCombination
        int boardHeight = boardWidth;

        //super.setPosition(boardX, boardY);

        super.setWidth(boardWidth);
        super.setHeight(boardHeight);
        super.setOrigin(0, 0);

        Pixmap boardPixmap = new Pixmap(boardWidth, boardHeight, RGBA8888);
        boardPixmap.setColor(Color.BLUE);
        boardPixmap.fillRectangle(0, 0, boardWidth, boardHeight);
        String boardTexturePath = "data/img/general/board_bg.png";
        boardTexture = new Texture(boardTexturePath);
        Image bgImage = new Image(boardTexture);
        bgImage.setPosition(0, 0);
        bgImage.setFillParent(true);
        addActor(bgImage);

        this.boardPad = 23.0f;
        blockSpacing = (boardWidth - 2 * boardPad) / numBlocksAcross;

        boardBounds = new Rectangle(0, 0,
                boardWidth, boardHeight);

        shouldAnalyze = true;
        isInputPaused = false;

        blocks = boardInitializer.getInitializedBlocks(this);

        for (Block[] row : blocks) {
            for (Block b : row) {
                addActor(b);
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Vector2 mouse = screenToLocalCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        if (initialWaitTime > 0) {
            initialWaitTime -= delta;
            return;
        }
        this.handleInput(mouse);
    }

    public Rectangle getBoardBounds() {
        return boardBounds;
    }

    private Vector2 getBlockPosition(int row, int col) {
        return new Vector2(boardPad + col * blockSpacing, boardPad + row * blockSpacing);
    }

    private Array<Block> getColArray(int col) {
        int numCols = blocks[0].length;
        Array<Block> colArray = new Array<Block>(numCols);
        for (int row = 0; row < numCols; row++) {
            colArray.add(blocks[row][col]);
        }
        return colArray;
    }

    private static <T> void flipElements(T[][] a, int i1, int j1, int i2, int j2) {
        T temp = a[i1][j1];
        a[i1][j1] = a[i2][j2];
        a[i2][j2] = temp;
    }

    private void flipBlocks(int i1, int j1, int i2, int j2) {
        flipBlocks(blocks[i1][j1], blocks[i2][j2]);
    }

    private void flipBlocks(Block b1, Block b2) {
        flipElements(blocks, b1.getRow(), b1.getCol(), b2.getRow(), b2.getCol());
        Block.flipRowAndCol(b1, b2);
    }

    private boolean overwriteBlock(int row, int col) {
        if (blocks[row][col] == null) {
            boolean breakpoint = true;
        }
        Block toOverwrite = blocks[row][col];
        toOverwrite.clear();
        blocks[row][col] = null;
        removeActor(toOverwrite);
        return true;
    }

    private void moveBlock(int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow != fromCol || toRow != toCol) {
            insertBlock(blocks[fromRow][fromCol], toRow, toCol);
            blocks[fromRow][fromCol] = null;
        }
    }

    private void insertBlock(Block b, int row, int col) {
        b.setRowAndCol(row, col);
        blocks[row][col] = b;
    }

    private void insertBlock(Block b) {
        blocks[b.getRow()][b.getCol()] = b;
        addActor(b);
    }

    private void animateFillDown(int col, int crushedBlocksInCol) {
        int numRows = blocks.length;
        Vector2 initialBlockPosition = getBlockPosition(numRows, col);
        for (int bottomRow = numRows - crushedBlocksInCol, i = 0; bottomRow + i < numRows; i++) {
            final Block b = blocks[bottomRow + i][col];
            b.setPosition(initialBlockPosition.x, initialBlockPosition.y);
            b.animateDown(
                    singleBlockDropTime * i,
                    singleBlockDropTime,
                    -(crushedBlocksInCol - i) * blockSpacing,
                    singleBlockDropTime * (crushedBlocksInCol - i)
            );
        }
    }

    private void shiftAndAnimateBlockDown(Block b, int spaces) {
        moveBlock(b.getRow(), b.getCol(), b.getRow() - spaces, b.getCol());
        b.addAction(Actions.moveBy(0, -blockSpacing * spaces, singleBlockDropTime * spaces));
    }

    private boolean analyzeAndAnimateBoard(final boolean userInvoked) {

        Array<SimpleBlockGroup> analysis = boardAnalyzer.analyzeBoard(this);
        if (analysis.size > 0) { // there was a match
            blockGroups = analysis;
            int largestGroupNumBlocks = 0;
            SimpleBlockGroup largestGroup = null;
            for (SimpleBlockGroup group : blockGroups) {
                for (Block b : group) {
                    fadeBlockOut(b);
                }
                if (group.size() >= largestGroupNumBlocks) {
                    if (group.size() > largestGroupNumBlocks) {
                        largestGroupNumBlocks = group.size();
                        largestGroup = group;
                    } else { // equal
                        if (userFlippedBlocks && group.getType() == firstSelectedBlock.getBlockType()) {
                            largestGroup = group;
                        }
                    }
                }
            }
            Crush crush = new Crush(blockGroups, largestGroup, userFlippedBlocks);
            onCrushListener.onCrush(crush);
            return true;
        } else { // there was no match
            blockGroups = null;
            return false;
        }
    }

    private void fadeBlockOut(Block b) {
        b.addAction(Actions.scaleTo(0f, 0f, SINGLE_BLOCK_SWAP_TIME));
    }

    private void shiftBlockOneLeft(Block b) {
        b.addAction(Actions.moveBy(-blockSpacing, 0, SINGLE_BLOCK_SWAP_TIME));
    }

    private void shiftBlockOneRight(Block b) {
        b.addAction(Actions.moveBy(blockSpacing, 0, SINGLE_BLOCK_SWAP_TIME));
    }

    private void shiftBlockOneUp(Block b) {
        b.addAction(Actions.moveBy(0, blockSpacing, SINGLE_BLOCK_SWAP_TIME));
    }

    private void shiftBlockOneDown(Block b) {
        b.addAction(Actions.moveBy(0, -blockSpacing, SINGLE_BLOCK_SWAP_TIME));
    }

    private void refillBoard(Array<SimpleBlockGroup> groups) {
        int numCols = blocks[0].length, numRows = blocks.length;
        int[] totalCrushedBlocksInCols = new int[numCols];

        for (SimpleBlockGroup group : groups) {
            for (Block b : group) {
                overwriteBlock(b.getRow(), b.getCol());
                totalCrushedBlocksInCols[b.getCol()]++;
            }
        }
        for (int col = 0; col < numCols; col++) {
            Array<Block> columnBlocks = getColArray(col);
            int curSpaces = 0;
            for (int row = 0; row < numRows; row++) {
                while (columnBlocks.get(row) == null) {
                    curSpaces++;
                    row++;
                    if (row >= numRows) break;
                }
                if (row >= numRows) break;
                if (curSpaces <= 0) continue;
                while (columnBlocks.get(row) != null) {
                    shiftAndAnimateBlockDown(columnBlocks.get(row), curSpaces);
                    row++;
                    if (row >= numRows) break;
                }
                row--;
            }

        }
        for (int col = 0; col < numCols; col++) {
            int crushedBlocksInCol = totalCrushedBlocksInCols[col];
            for (int row = numRows - crushedBlocksInCol; row < numRows; row++) {
                Block fillBlock = getNewBlock(row, col);
                fillBlock.setVisible(false);
                insertBlock(fillBlock);
            }
            animateFillDown(col, crushedBlocksInCol);
        }
    }

    public Block getNewBlock(int row, int col) {
        return blockProvider
                .provide(row, col, getBlockPosition(row, col), blockSpacing, blockSpacing);
    }

    private boolean doChildrenHaveActions() {
        for (Block[] row : blocks) {
            for (Block b : row) {
                if (b.isPerformingAction()) return true;
            }
        }
        return false;
    }

    public boolean isWaitingForInput() {
        return !shouldAnalyze && !userFlippedBlocks && !doChildrenHaveActions();
    }

    public int getNumTotalUserCrushes() {
        return numTotalCrushes;
    }


    private boolean shouldAnalyze;
    private boolean gotMatches;

    private float initialWaitTime = 0.5f;

    private void handleInput(Vector2 mouse) {
        if (doChildrenHaveActions()) {
            return;
        }
        if (shouldAnalyze) {
            gotMatches = analyzeAndAnimateBoard(userFlippedBlocks); // crush
            shouldAnalyze = false;
            if (gotMatches) { // user crushed blocks
                if (userFlippedBlocks)
                    numTotalCrushes++; // increment
            }
            return;
        } else {
            if (gotMatches) {
                refillBoard(blockGroups); // we need blockGroups to animate the board in subsequent updates after crush
                shouldAnalyze = true;
            } else {
                if (userFlippedBlocks) {
                    flipBlocksAndAnimate(secondSelectedBlock, firstSelectedBlock);
                }
                shouldAnalyze = false;
            }
            if (userFlippedBlocks) {
                firstSelectedBlock = null;
                secondSelectedBlock = null;
                userFlippedBlocks = false;
            }
        }

        boolean shouldProcessInput = !shouldAnalyze && !isInputPaused;
        if (shouldProcessInput) {
            if (Gdx.input.isTouched()) {

                if (firstSelectedBlock == null) {
                    firstSelectedBlock = getSelectedBlock(mouse);
                } else {
                    secondSelectedBlock = getSelectedBlock(mouse);
                }
                if (Gdx.input.isTouched(0) && secondSelectedBlock != null) {
                    if (firstSelectedBlock.getRow() != secondSelectedBlock.getRow()
                            || firstSelectedBlock.getCol() != secondSelectedBlock.getCol()) {
                        // player flipped two blocks

                        // infer secondSelectedBlock
                        Vector2 dir = new Vector2(
                                secondSelectedBlock.getCol() - firstSelectedBlock.getCol(),
                                secondSelectedBlock.getRow() - firstSelectedBlock.getRow()
                        );
                        if (Math.abs(dir.x) > Math.abs(dir.y)) {
                            dir.set(dir.x, 0);
                        } else {
                            dir.set(0, dir.y);
                        }
                        dir.setLength(1f);
                        secondSelectedBlock = blocks
                                [firstSelectedBlock.getRow() + Math.round(dir.y)]
                                [firstSelectedBlock.getCol() + Math.round(dir.x)];
                        // secondSelectedBlock now correct

                        flipBlocksAndAnimate(firstSelectedBlock, secondSelectedBlock);
                        userFlippedBlocks = true;
                        shouldAnalyze = true;
                    }
                }
            } else {
                firstSelectedBlock = null;
                secondSelectedBlock = null;
            }
        }

    }

    private Block firstSelectedBlock, secondSelectedBlock;

    public boolean userFlippedBlocks;

    private void flipBlocksAndAnimate(Block from, Block to) {
        if (to.getCol() == from.getCol()) {
            if (to.getRow() > from.getRow()) {
                shiftBlockOneUp(from);
                shiftBlockOneDown(to);
            } else {
                shiftBlockOneDown(from);
                shiftBlockOneUp(to);
            }
        } else {
            if (to.getCol() > from.getCol()) {
                shiftBlockOneRight(from);
                shiftBlockOneLeft(to);
            } else {
                shiftBlockOneLeft(from);
                shiftBlockOneRight(to);
            }
        }
        flipBlocks(from, to);
    }

    private int getSelectedCol(Vector2 mouse) {
        return (int) Math.floor(mouse.x / blockSpacing);
    }

    private int getSelectedRow(Vector2 mouse) {
        return (int) Math.floor(mouse.y / blockSpacing);
    }

    private Block getSelectedBlock(Vector2 mouse) {

        if (boardBounds.contains(mouse)) {
            int selectedRow = getSelectedRow(mouse);
            if (selectedRow >= blocks.length || selectedRow < 0) return null;
            int selectedCol = getSelectedCol(mouse);
            if (selectedCol >= blocks[selectedRow].length || selectedCol < 0) return null;
            return blocks[selectedRow][selectedCol];
        } else return null;
    }

    public Vector2 getPositionOfRowAndCol(int row, int col) {
        return localToStageCoordinates(getBlockPosition(row, col));
    }

    public Vector2 getAbsolutePosition() {
        return localToStageCoordinates(new Vector2(getX(), getY()));
    }

    public float getAbsX() {
        return getAbsolutePosition().x;
    }

    public float getAbsY() {
        return getAbsolutePosition().y;
    }

    @Override
    public void dispose() {
        boardTexture.dispose();
    }

    private boolean isInputPaused;

    public void pauseInput() {
        isInputPaused = true;
    }

    public void resumeInput() {
        isInputPaused = false;
    }

    public Block[][] getBlocks() {
        return blocks;
    }
}
