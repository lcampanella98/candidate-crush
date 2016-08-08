package com.gmail.enzocampanella98.candidatecrush.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Queue;
import com.gmail.enzocampanella98.candidatecrush.animation.BlockAnimation;
import com.gmail.enzocampanella98.candidatecrush.animation.DisappearBlockAnimation;
import com.gmail.enzocampanella98.candidatecrush.animation.FadeOutBlockAnimation;
import com.gmail.enzocampanella98.candidatecrush.animation.TranslationBlockAnimation;
import com.gmail.enzocampanella98.candidatecrush.sound.MusicHandler;

import java.util.Random;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;

/**
 * Created by Lorenzo Campanella on 6/2/2016.
 */
public class Board {

    private static final float SINGLE_BLOCK_DROP_TIME = 0.20f;
    private static final Vector2 DOWN_DIRECTION = new Vector2(0f, -1f);

    private static Array<BlockType> blockTypes;

    private Block[][] board;

    private Random random;

    static ObjectMap<BlockType, Texture> blockTextures;

    private static Block testBlock;
    private static Block[][] testBoard;

    private Texture boardTexture;
    private Rectangle boardBounds;
    private int boardWidth, boardHeight;
    private int numBlocks;
    private int width, height;

    private MusicHandler musicHandler;

    private Queue<BoardTask> tasks;

    float blockSpacing;

    private BlockAnimation blockAnimation;
    private Array<BlockGroup> blockGroups;
    private BoardHandler boardHandler;


    public Board(int numBlocks, ObjectMap<BlockType, Texture> blockTextures) {
        Board.blockTextures = blockTextures;
        this.numBlocks = numBlocks;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        createBoard();
        tasks = new Queue<BoardTask>();
        musicHandler = new MusicHandler();
    }

    private void createBoard() {
        board = new Block[numBlocks][numBlocks];
        random = new Random();
        if (blockTypes == null) {
            blockTypes = new Array<BlockType>(BlockType.values());
            blockTypes.removeValue(BlockType.BLANK, true);
        }
        if (Gdx.graphics.getWidth() > Gdx.graphics.getHeight()) {

        }
        boardWidth = (int) Math.round(Gdx.graphics.getHeight() * 0.8);
        boardHeight = boardWidth;
        Pixmap boardPixmap = new Pixmap(boardWidth, boardHeight, RGBA8888);
        boardPixmap.setColor(Color.BLUE);
        boardPixmap.fillRectangle(0, 0, boardWidth, boardHeight);
        boardTexture = new Texture(boardPixmap);

        int boardx = width / 2 - boardTexture.getWidth() / 2;
        int boardy = height / 2 - boardTexture.getHeight() / 2;
        blockSpacing = (float) boardWidth / numBlocks;

        boardBounds = new Rectangle(boardx, boardy,
                boardWidth, boardHeight);
        boardPixmap.dispose();
        populateRandomly();
        /*testBlock = board[board.length-1][0];
        animateFillDown(0, 1);
        System.out.println("board created");*/
        /*testBoard = new Block[][]{
                {
                        getNewBlock(0, 0, BlockType.TRUMP), getNewBlock(0, 1, BlockType.TRUMP), getNewBlock(0, 2, BlockType.OBAMA), getNewBlock(0, 3, BlockType.TRUMP)
                },
                {
                        getNewBlock(1, 0, BlockType.CLINTON), getNewBlock(1, 1, BlockType.CLINTON), getNewBlock(1, 2, BlockType.TRUMP), getNewBlock(1, 3, BlockType.CLINTON)
                },
                {
                        getNewBlock(2, 0, BlockType.SANDERS), getNewBlock(2, 1, BlockType.SANDERS), getNewBlock(2, 2, BlockType.TRUMP), getNewBlock(2, 3, BlockType.CLINTON)
                },
                {
                        getNewBlock(3, 0, BlockType.OBAMA), getNewBlock(3, 1, BlockType.OBAMA), getNewBlock(3, 2, BlockType.SANDERS), getNewBlock(3, 3, BlockType.TRUMP)
                }
        };
        board = testBoard;*/
        shouldAnalyze = true;
        shouldProcessInput = false;
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
        for (int i = 0; i < board.length; i++) {
            board[i] = new Block[board.length];
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = getNewRandomBlock(i, j);
            }
        }
    }

    private Block getNewRandomBlock(int row, int col) {
        return getNewBlock(row, col, blockTypes.get(random.nextInt(blockTypes.size)));
    }

    private Block getNewBlock(int row, int col, BlockType blockType) {
        return new Block(blockType,
                new Vector2(boardBounds.x + col * blockSpacing,
                        boardBounds.y + row * blockSpacing),
                blockSpacing, blockSpacing, row, col);
    }

    private Block[][] getBoard() {
        return board;
    }

    private Array<Block> getRowArray(int row) {
        return new Array<Block>(board[row]);
    }

    private Array<Block> getColArray(int col) {
        int numCols = board[0].length;
        Array<Block> colArray = new Array<Block>(numCols);
        for (int row = 0; row < numCols; row++) {
            colArray.add(board[row][col]);
        }
        return colArray;
    }

    private static <T> void flipElements(T[][] a, int i1, int j1, int i2, int j2) {
        T temp = a[i1][j1];
        a[i1][j1] = a[i2][j2];
        a[i2][j2] = temp;
    }

    private void flipBlocks(int i1, int j1, int i2, int j2) {
        flipBlocks(board[i1][j1], board[i2][j2]);
    }

    private void flipBlocks(Block b1, Block b2) {
        flipElements(board, b1.getRow(), b1.getCol(), b2.getRow(), b2.getCol());
        Block.flipRowAndCol(b1, b2);
    }

    private void overwriteBlock(int row, int col) {
        board[row][col] = null;
    }

    private void moveBlock(int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow != fromCol || toRow != toCol) {
            insertBlock(board[fromRow][fromCol], toRow, toCol);
            overwriteBlock(fromRow, fromCol);
        }
    }

    private void insertBlock(Block b, int row, int col) {
        b.setRowAndCol(row, col);
        board[row][col] = b;
    }

    private void insertBlock(Block b) {
        board[b.getRow()][b.getCol()] = b;
    }


    private void animateFillDown(int col, int space) {
        int numRows = board.length;
        Vector2 downDirection = new Vector2(0f, -1f);
        Vector2 initialBlockPosition = new Vector2(boardBounds.x + col * blockSpacing,
                boardBounds.y + boardBounds.getHeight());
        for (int bottomRow = numRows - space, i = 0; i < space; i++) {
            Block b = board[bottomRow + i][col];
            b.setInitialPosition(initialBlockPosition);
            b.resetPosition();
            DisappearBlockAnimation dissapearAnimation = new DisappearBlockAnimation(
                    SINGLE_BLOCK_DROP_TIME * i
            );
            Array<BlockAnimation> moveAnimations = new Array<BlockAnimation>();
            moveAnimations.add(b.getDropDownCropBlockAnimation(SINGLE_BLOCK_DROP_TIME));
            moveAnimations.add(
                    new TranslationBlockAnimation(
                            SINGLE_BLOCK_DROP_TIME * (space - i)
                            , downDirection, (space - i) * blockSpacing)
            );
            b.addAnimation(dissapearAnimation);
            b.addAnimation(moveAnimations);
        }
    }

    private void shiftAndAnimateBlockDown(Block b, int spaces) {
        moveBlock(b.getRow(), b.getCol(), b.getRow() - spaces, b.getCol());
        b.addAnimation(
                new TranslationBlockAnimation(
                        SINGLE_BLOCK_DROP_TIME * spaces, DOWN_DIRECTION, blockSpacing * spaces));
    }

    private boolean analyzeAndRefillBoard(final boolean userInvoked) {

        Array<BlockGroup> analysis = analyzeBoard(this);
        if (analysis.size > 0) { // there was a match
            blockGroups = analysis;
            // add tasks
            // task 1: make blocks fade out
            addTask(new BoardTask() {
                @Override
                public void update(float dt) {
                    if (wasStarted() && !isAnimating()) done();
                }

                @Override
                public void run() {
                    start();
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
                        if (largestGroup.isJoinedGroup()) maxLevel = 't';
                        else maxLevel = String.valueOf(largestGroup.getNumBlocks()).charAt(0);
                        musicHandler.playRandomMusic(largestGroup.getGroupBlockType(), maxLevel);
                    } else if (!musicHandler.isMusicPlaying()) musicHandler.playPopSound();
                }
            });
            // task 2: delete faded blocks and fill in new blocks
            addTask(new BoardTask() {
                @Override
                public void update(float dt) {
                    if (wasStarted() && !isAnimating()) done();
                }

                @Override
                public void run() {
                    start();
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
        float t = SINGLE_BLOCK_DROP_TIME;
        Array<BlockAnimation> list = new Array<BlockAnimation>(2);
        list.add(new TranslationBlockAnimation(t, new Vector2(1f, 1f), (float) (Math.hypot(b.getTextureWidth(), b.getTextureHeight()) / 2)));
        list.add(new FadeOutBlockAnimation(t, b.getTextureWidth(), b.getTextureHeight()));
        b.addAnimation(list);
    }

    private void addTask(BoardTask task) {
        tasks.addLast(task);
    }

    private void refillBoard(Array<BlockGroup> groups) {
        int numCols = board[0].length, numRows = board.length;
        int[] totalSpacesInCols = new int[numCols];
        for (BlockGroup group : groups) {
            for (Block b : group.getGroup()) {
                overwriteBlock(b.getRow(), b.getCol());
                totalSpacesInCols[b.getCol()]++;
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
            int spaces = totalSpacesInCols[col];
            for (int row = numRows - spaces; row < numRows; row++) {
                Block fillBlock = getNewRandomBlock(row, col);
                insertBlock(fillBlock);
            }
            animateFillDown(col, spaces);
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
        Block[][] blocks = board.getBoard();
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

    public void render(SpriteBatch sb) {
        sb.draw(boardTexture, boardBounds.x, boardBounds.y,
                boardBounds.getWidth(), boardBounds.getHeight());

        for (Block[] boardRow : board) {
            for (Block block : boardRow) {
                block.render(sb);
            }
        }
    }

    private boolean hasTask() {
        return tasks.size > 0;
    }

    public void update(float dt) {
        if (hasTask()) {
            if (tasks.first().wasStarted()) {
                tasks.first().update(dt);
                if (tasks.first().isDone()) {
                    tasks.removeFirst();
                    if (hasTask()) tasks.first().run();
                }
            } else tasks.first().run();
        }
        for (Block[] boardRow : board) {
            for (Block block : boardRow) {
                block.update(dt);
            }
        }
        if (boardHandler != null) {
            boardHandler.update(dt);
        }
    }

    public boolean isAnimating() {
        for (Block[] row : board) {
            for (Block b : row) {
                if (b.isAnimating()) return true;
            }
        }
        return false;
    }


    private boolean shouldProcessInput, shouldAnalyze;

    public void handleInput(Vector2 mouse) {

        if (hasTask()) {
            shouldProcessInput = false;
        } else {
            boolean isAnimating = isAnimating();

            // don't process the user's selected blocks if blocks are animating
            if (isAnimating) {
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
                        shouldProcessInput = false;
                        {
                            shouldProcessInput = !gotMatches;
                        }
                    } else
                        shouldAnalyze = gotMatches;
                } else {
                    shouldProcessInput = true;
                }
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
                            blockSelectedNow = board
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
        Vector2 dir = new Vector2(to.getCol() - from.getCol(), to.getRow() - from.getRow());
        from.addAnimation(
                new TranslationBlockAnimation(
                        SINGLE_BLOCK_DROP_TIME, new Vector2(dir), blockSpacing));
        to.addAnimation(
                new TranslationBlockAnimation(
                        SINGLE_BLOCK_DROP_TIME, new Vector2(dir).scl(-1f), blockSpacing));
        flipBlocks(from, to);
    }

    private float getAnimationTimeLeft() {
        float maxTimeLeft = 0f;
        for (Block[] a : board) {
            for (Block b : a) {
                float timeLeft = b.getAnimationTimeLeft();
                if (timeLeft > maxTimeLeft) maxTimeLeft = timeLeft;
            }
        }
        return maxTimeLeft;
    }

    private int getSelectedCol(Vector2 mouse) {
        return (int) Math.floor((mouse.x - boardBounds.x) / blockSpacing);
    }

    private int getSelectedRow(Vector2 mouse) {
        return board.length - 1 - (int) Math.floor((mouse.y - boardBounds.y) / blockSpacing);
    }

    private Block getSelectedBlock(Vector2 mouse) {
        if (boardBounds.contains(mouse)) {
            int selectedRow = getSelectedRow(mouse);
            if (selectedRow >= board.length || selectedRow < 0) return null;
            int selectedCol = getSelectedCol(mouse);
            if (selectedCol >= board[selectedRow].length || selectedCol < 0) return null;
            return board[selectedRow][selectedCol];
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
