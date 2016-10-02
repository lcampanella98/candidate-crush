package com.gmail.enzocampanella98.candidatecrush.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Queue;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.sound.MusicHandler;

import java.util.Random;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;

/**
 * Created by Lorenzo Campanella on 6/2/2016.
 */
public class Board extends Group {

    private static final float SINGLE_BLOCK_DROP_TIME = 0.20f;
    private static final Vector2 DOWN_DIRECTION = new Vector2(0f, -1f);

    private static Array<BlockType> blockTypes;

    private Block[][] blocks;

    private Random random;

    static ObjectMap<BlockType, Texture> blockTextures;

    private Texture boardTexture;
    private Rectangle boardBounds;
    private int boardWidth, boardHeight;
    private int numBlocks;
    private int width, height;

    private MusicHandler musicHandler;

    private Queue<BoardTask> tasks;

    private float blockSpacing;

    private Array<BlockGroup> blockGroups;
    private BoardHandler boardHandler;


    public Board(int numBlocksAcross, ObjectMap<BlockType, Texture> blockTextures) {
        super();
        super.debug();
        Board.blockTextures = blockTextures;
        this.numBlocks = numBlocksAcross;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        createBoard();
        setBoardHandler(100, 500, 2000, 1500);
        tasks = new Queue<BoardTask>();
        musicHandler = new MusicHandler();
    }

    private void createBoard() {
        blocks = new Block[numBlocks][numBlocks];
        random = new Random();
        if (blockTypes == null) {
            blockTypes = new Array<BlockType>(BlockType.values());
            blockTypes.removeValue(BlockType.BLANK, true);
        }

        boardWidth = (int) (Math.round(CandidateCrush.V_WIDTH * 0.94));
        int boardx = (CandidateCrush.V_WIDTH - boardWidth) / 2;
        //noinspection SuspiciousNameCombination
        boardHeight = boardWidth;
        int boardy = (CandidateCrush.V_HEIGHT - boardHeight) / 2;
        super.setPosition(boardx, boardy);
        super.setWidth(boardWidth);
        super.setHeight(boardHeight);
        super.setOrigin(0, 0);

        Pixmap boardPixmap = new Pixmap(boardWidth, boardHeight, RGBA8888);
        boardPixmap.setColor(Color.BLUE);
        boardPixmap.fillRectangle(0, 0, boardWidth, boardHeight);
        boardTexture = new Texture(boardPixmap);
        Image bgImage = new Image(boardTexture);
        bgImage.setPosition(0, 0);
        bgImage.setFillParent(true);
        super.addActor(bgImage);
        blockSpacing = (float) boardWidth / numBlocks;

        boardBounds = new Rectangle(boardx, boardy,
                boardWidth, boardHeight);
        populateRandomly();
        for (Block[] row : blocks) {
            for (Block b : row) {
                super.addActor(b);
            }
        }
        shouldAnalyze = true;
        shouldProcessInput = false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        boardHandler.update(delta);
    }

    public void setBoardHandler(int score3, int score4, int score5, int scoreJoined) {
        boardHandler = new BoardHandler(this, score3, score4, score5, scoreJoined);
    }

    public BoardHandler getBoardHandler() {
        return boardHandler;
    }

    public int getScore() {
        return boardHandler.getScore();
    }

    public void setScore(int score) {
        boardHandler.setScore(score);
    }

    public void resetScore() {
        boardHandler.resetScore();
    }

    public Rectangle getBoardBounds() {
        return boardBounds;
    }

    public void populateRandomly() {
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = new Block[blocks.length];
            for (int j = 0; j < blocks[i].length; j++) {
                blocks[i][j] = getNewRandomBlock(i, j);
            }
        }
    }

    private Block getNewRandomBlock(int row, int col) {
        return getNewBlock(row, col, blockTypes.get(random.nextInt(blockTypes.size)));
    }

    private Block getNewBlock(int row, int col, BlockType blockType) {
        return new Block(blockType,
                new Vector2(
                        boardBounds.x + col * blockSpacing,
                        boardBounds.y + row * blockSpacing),
                blockSpacing, blockSpacing, row, col);
    }

    private Block[][] getBlocks() {
        return blocks;
    }

    private Array<Block> getRowArray(int row) {
        return new Array<Block>(blocks[row]);
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

    private void overwriteBlock(int row, int col) {
        Block toOverwrite = blocks[row][col];
        toOverwrite.clear();
        toOverwrite.remove();
        blocks[row][col] = null;
    }

    private void moveBlock(int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow != fromCol || toRow != toCol) {
            insertBlock(blocks[fromRow][fromCol], toRow, toCol);
            overwriteBlock(fromRow, fromCol);
        }
    }

    private void insertBlock(Block b, int row, int col) {
        b.setRowAndCol(row, col);
        blocks[row][col] = b;
    }

    private void insertBlock(Block b) {
        blocks[b.getRow()][b.getCol()] = b;
    }

    private void animateFillDown(int col, int crushedBlocksInCol) {
        int numRows = blocks.length;
        Vector2 initialBlockPosition = new Vector2(boardBounds.x + col * blockSpacing,
                boardBounds.y + boardBounds.getHeight());
        for (int bottomRow = numRows - crushedBlocksInCol, i = 0; i < crushedBlocksInCol; i++) {
            Block b = blocks[bottomRow + i][col];
            b.setPosition(initialBlockPosition.x, initialBlockPosition.y);
            b.addAction(Actions.delay(SINGLE_BLOCK_DROP_TIME * i));
            b.addAction(Actions.moveBy(
                    0, -(crushedBlocksInCol - i) * blockSpacing, SINGLE_BLOCK_DROP_TIME * (crushedBlocksInCol - i)));
        }
    }

    private void shiftAndAnimateBlockDown(Block b, int spaces) {
        moveBlock(b.getRow(), b.getCol(), b.getRow() - spaces, b.getCol());
        b.addAction(Actions.moveBy(0, -blockSpacing * spaces, SINGLE_BLOCK_DROP_TIME * spaces));
    }

    private boolean analyzeAndRefillBoard(final boolean userInvoked) {

        Array<BlockGroup> analysis = analyzeBoard(this);
        if (analysis.size > 0) { // there was a match
            blockGroups = analysis;
            // add tasks
            // task 1: make blocks fade out
            int largestGroupNumBlocks = 0;
            BlockGroup largestGroup = null;
            for (BlockGroup group : blockGroups) {
                for (Block b : group.getGroup()) {
                    fadeBlockOut(b);
                }
                if (group.getNumBlocks() > largestGroupNumBlocks) {
                    largestGroupNumBlocks = group.getNumBlocks();
                    largestGroup = group;
                }
            }
            if (userInvoked) {
                char maxLevel;
                assert largestGroup != null;
                maxLevel = largestGroup.isJoinedGroup() ? 't'
                        : String.valueOf(largestGroup.getNumBlocks()).charAt(0);
                musicHandler.playRandomMusic(largestGroup.getGroupBlockType(), maxLevel);
            } else if (!musicHandler.isMusicPlaying()) musicHandler.playPopSound();
            // task 2: delete faded blocks and fill in new blocks

            final BlockGroup finalLargestGroup = largestGroup;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //noinspection StatementWithEmptyBody
                    while (blockGroups.first().getGroup().first().hasActions()) {
                    }
                    refillBoard(blockGroups);
                }
            });
            return true;
        } else { // there was no match
            blockGroups = null;
            return false;
        }
    }


    private void fadeBlockOut(Block b) {
        b.addAction(Actions.scaleBy(0f, 0f, SINGLE_BLOCK_DROP_TIME));
    }

    private void shiftBlockOneLeft(Block b) {
        b.addAction(Actions.moveBy(-blockSpacing, 0, SINGLE_BLOCK_DROP_TIME));
    }

    private void shiftBlockOneRight(Block b) {
        b.addAction(Actions.moveBy(blockSpacing, 0, SINGLE_BLOCK_DROP_TIME));
    }

    private void shiftBlockOneUp(Block b) {
        b.addAction(Actions.moveBy(0, blockSpacing, SINGLE_BLOCK_DROP_TIME));
    }

    private void shiftBlockOneDown(Block b) {
        b.addAction(Actions.moveBy(0, -blockSpacing, SINGLE_BLOCK_DROP_TIME));
    }

    private void refillBoard(Array<BlockGroup> groups) {
        int numCols = blocks[0].length, numRows = blocks.length;
        int[] totalCrushedBlocksInCols = new int[numCols];
        for (BlockGroup group : groups) {
            for (Block b : group.getGroup()) {
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
                Block fillBlock = getNewRandomBlock(row, col);
                insertBlock(fillBlock);
            }
            animateFillDown(col, crushedBlocksInCol);
        }
    }

    private static void analysisCurrentBlock(
            Block currentBlock, Array<BlockGroup> matches, Array<Block> currentString, int minLen, int numCols) {
        if (currentString.size > 0) {
            if (currentBlock.getBlockType() == currentString.get(0).getBlockType()) {
                currentString.add(currentBlock);
            } else {
                if (currentString.size >= minLen) {
                    matches.add(new BlockGroup(currentString, numCols));
                }
                currentString.clear();
                currentString.add(currentBlock);
            }
        } else {
            currentString.add(currentBlock);
        }
    }

    private static Array<BlockGroup> analyzeBoard(Board board) {
        int minLen = 3;
        Array<BlockGroup> rowMatches = new Array<BlockGroup>(),
                colMatches = new Array<BlockGroup>();
        Block[][] blocks = board.getBlocks();
        int numCols = blocks[0].length;

        for (Block[] block : blocks) {
            Array<Block> currentString = new Array<Block>();
            for (int j = 0; j < numCols; j++) {
                Block currentBlock = block[j];
                analysisCurrentBlock(currentBlock, rowMatches, currentString, minLen, numCols);
            }
            if (currentString.size >= minLen) {
                rowMatches.add(new BlockGroup(currentString, numCols));
            }
        }

        for (int j = 0; j < numCols; j++) {
            Array<Block> currentString = new Array<Block>();
            for (Block[] rows : blocks) {
                Block currentBlock = rows[j];
                analysisCurrentBlock(currentBlock, colMatches, currentString, minLen, numCols);
            }
            if (currentString.size >= minLen) {
                colMatches.add(new BlockGroup(currentString, numCols));
            }
        }

        Array<BlockGroup> mergedGroups = new Array<BlockGroup>();

        for (int i = 0; i < rowMatches.size; i++) {
            BlockGroup nextRowGroup = rowMatches.get(i);
            for (int j = 0; j < colMatches.size; j++) {
                BlockGroup nextColGroup = colMatches.get(j);
                BlockGroup merged = BlockGroup.getMergedGroup(nextRowGroup, nextColGroup);
                if (merged != null) {
                    mergedGroups.add(merged);
                    rowMatches.removeIndex(i);
                    colMatches.removeIndex(j);
                    i--;
                    break;
                }
            }
        }
        Array<BlockGroup> groups = new Array<BlockGroup>(mergedGroups.size + rowMatches.size + colMatches.size);
        groups.addAll(mergedGroups);
        groups.addAll(rowMatches);
        groups.addAll(colMatches);
        return groups;
    }

    public void update(float dt) {
        if (boardHandler != null) {
        }
        act(dt);
    }

    public boolean doChildrenHaveActions() {
        for (Block[] row : blocks) {
            for (Block b : row) {
                if (b.isPerformingAction()) return true;
            }
        }
        return false;
    }


    private boolean shouldProcessInput, shouldAnalyze;

    public void handleInput(Vector2 mouse) {

        if (doChildrenHaveActions()) {
            shouldProcessInput = false;
        } else {
            // no blocks are currently animating (screen is static)
            // user might be touching the screen
            if (shouldAnalyze) {
                boolean gotMatches = analyzeAndRefillBoard(areBlocksFlipping);
                if (areBlocksFlipping) {
                    // player flipped the blocks
                    if (!gotMatches) {
                        // player got no matches
                        flipBlocksAndAnimate(blockSelectedPreviously, blockSelectedNow);
                    }
                    blockSelectedPreviously = null;
                    blockSelectedNow = null;
                    areBlocksFlipping = false;
                    shouldProcessInput = !gotMatches;
                } else
                    shouldAnalyze = gotMatches;
            } else {
                shouldProcessInput = true;
            }
        }

        if (shouldProcessInput) {
            if (Gdx.input.isTouched()) {
                blockSelectedNow = getSelectedBlock(mouse);

                if (Gdx.input.isTouched(0) && blockSelectedNow != null) {
                    if (blockSelectedPreviously == null) {
                        blockSelectedPreviously = blockSelectedNow;
                        blockSelectedNow = null;
                    } else {
                        if (blockSelectedPreviously.getRow() != blockSelectedNow.getRow()
                                || blockSelectedPreviously.getCol() != blockSelectedNow.getCol()) {
                            // player flipped two blocks

                            // infer blockSelectedNow
                            Vector2 dir = new Vector2(
                                    blockSelectedNow.getCol() - blockSelectedPreviously.getCol(),
                                    blockSelectedNow.getRow() - blockSelectedPreviously.getRow()
                            );
                            if (Math.abs(dir.x) > Math.abs(dir.y)) {
                                dir.set(dir.x, 0);
                            } else {
                                dir.set(0, dir.y);
                            }
                            dir.setLength(1f);
                            blockSelectedNow = blocks
                                    [blockSelectedPreviously.getRow() + Math.round(dir.y)]
                                    [blockSelectedPreviously.getCol() + Math.round(dir.x)];
                            // blockSelectedNow now correct

                            flipBlocksAndAnimate(blockSelectedPreviously, blockSelectedNow);
                            areBlocksFlipping = true;
                            shouldProcessInput = false;
                            shouldAnalyze = true;
                        }
                    }
                }
            } else {
                blockSelectedPreviously = null;
            }
        }
    }

    Array<BlockGroup> getBlockGroups() {
        return blockGroups;
    }

    private Block blockSelectedPreviously, blockSelectedNow;

    private boolean areBlocksFlipping;

    private void flipBlocksAndAnimate(Block from, Block to) {
        if (to.getCol() == from.getCol()) {
            if (to.getRow() > from.getRow()) {
                shiftBlockOneRight(from);
                shiftBlockOneLeft(to);
            } else {
                shiftBlockOneLeft(from);
                shiftBlockOneRight(to);
            }
        } else {
            if (to.getCol() > from.getCol()) {
                shiftBlockOneUp(from);
                shiftBlockOneDown(to);
            } else {
                shiftBlockOneDown(from);
                shiftBlockOneUp(to);
            }
        }
        flipBlocks(from, to);
    }

    private int getSelectedCol(Vector2 mouse) {
        return (int) Math.floor((mouse.x - boardBounds.x) / blockSpacing);
    }

    private int getSelectedRow(Vector2 mouse) {
        return blocks.length - 1 - (int) Math.floor((mouse.y - boardBounds.y) / blockSpacing);
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

    public void dispose() {
        boardTexture.dispose();
        for (ObjectMap.Entry<BlockType, Texture> e : blockTextures.entries()) {
            e.value.dispose();
        }
        blockTextures.clear();
    }
}
