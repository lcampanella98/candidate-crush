package com.gmail.enzocampanella98.candidatecrush.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.VisibleAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;
import com.gmail.enzocampanella98.candidatecrush.action.MyBlockInflaterAction;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.ScoringSystem;
import com.gmail.enzocampanella98.candidatecrush.sound.MusicHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;


public class Board extends Group {

    private static final float SINGLE_BLOCK_DROP_TIME = .1f;

    private Block[][] blocks;

    private Random random;

    private ObjectMap<BlockType, Texture> blockTextures;
    private List<BlockType> blockTypes;

    private Texture boardTexture;
    private Rectangle boardBounds;

    private int numBlocksAcross;

    private MusicHandler musicHandler;


    private float blockSpacing;

    private Array<SimpleBlockGroup> blockGroups; // not set to null because we need it to animate

    private Stack<Array<SimpleBlockGroup>> blockGroupsProcessStack; // push every crush

    private int numTotalCrushes;
    private float boardPad;

    private HashMap<BlockType, Double> blockTypeFrequencies;


    public Board(int numBlocksAcross, List<BlockType> blockTypes, ObjectMap<BlockType, Texture> blockTextures) {
        this.numBlocksAcross = numBlocksAcross;
        this.blockTypes = blockTypes;
        this.blockTextures = blockTextures;

        initBoard();
    }

    private void initBoard() {

        musicHandler = new MusicHandler();

        blocks = new Block[numBlocksAcross][numBlocksAcross];
        random = new Random();

        this.numTotalCrushes = 0; // track number of user-invoked crushes

        int boardWidth = CandidateCrush.V_WIDTH;
        int boardX = (CandidateCrush.V_WIDTH - boardWidth) / 2;

        //noinspection SuspiciousNameCombination
        int boardHeight = boardWidth;
        int boardY = (CandidateCrush.V_HEIGHT - boardHeight) / 2;

        //super.setPosition(boardX, boardY);

        super.setWidth(boardWidth);
        super.setHeight(boardHeight);
        super.setOrigin(0, 0);

        blockGroupsProcessStack = new Stack<Array<SimpleBlockGroup>>();

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


        populateRandomly();
        for (Block[] row : blocks) {
            for (Block b : row) {
                addActor(b);
            }
        }
        shouldAnalyze = true;
        isInputPaused = false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Vector2 mouse = screenToLocalCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        if (time > 0) {
            time -= delta;
            return;
        }
        this.handleInput(mouse);
    }

    public Rectangle getBoardBounds() {
        return boardBounds;
    }

    private void populateRandomly() {
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = new Block[blocks.length];
            for (int j = 0; j < blocks[i].length; j++) {
                blocks[i][j] = getNewRandomBlock(i, j);
            }
        }
    }

    public void setBlockTypeFrequencies(HashMap<BlockType, Double> frequencies) {
        this.blockTypeFrequencies = frequencies;
    }

    private BlockType getRandomBlockType() {
        if (blockTypeFrequencies != null) {
            double rand = random.nextDouble();

            for (Map.Entry<BlockType, Double> freq : blockTypeFrequencies.entrySet()) {
                if (rand <= freq.getValue()) return freq.getKey();
                rand -= freq.getValue();
            }
        }
        return blockTypes.get(random.nextInt(blockTypes.size()));
    }

    private Block getNewRandomBlock(int row, int col) {
        return getNewBlock(row, col, getRandomBlockType());
    }

    private Block getNewBlock(int row, int col, BlockType blockType) {
        return new Block(blockType, blockTextures.get(blockType), getBlockPosition(row, col),
                blockSpacing, blockSpacing, row, col);
    }

    private Vector2 getBlockPosition(int row, int col) {
        return new Vector2(boardPad + col * blockSpacing, boardPad + row * blockSpacing);
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
            Block b = blocks[bottomRow + i][col];
            b.setPosition(initialBlockPosition.x, initialBlockPosition.y);

            DelayAction da = new DelayAction(SINGLE_BLOCK_DROP_TIME * i);

            MyBlockInflaterAction sizeDownAction = new MyBlockInflaterAction(0f);
            sizeDownAction.setDuration(0f);

            MyBlockInflaterAction sizeUpAction = new MyBlockInflaterAction(b.getHeight());
            sizeUpAction.setDuration(SINGLE_BLOCK_DROP_TIME);

            MoveByAction mba = new MoveByAction();
            mba.setAmount(0, -(crushedBlocksInCol - i) * blockSpacing);
            mba.setDuration(SINGLE_BLOCK_DROP_TIME * (crushedBlocksInCol - i));

            VisibleAction showA = new VisibleAction();
            showA.setVisible(true);

            ParallelAction pa = new ParallelAction(sizeUpAction, mba);

            SequenceAction sa = new SequenceAction(da, sizeDownAction, showA, pa);
            b.addAction(sa);
        }
    }

    private void shiftAndAnimateBlockDown(Block b, int spaces) {
        moveBlock(b.getRow(), b.getCol(), b.getRow() - spaces, b.getCol());
        b.addAction(Actions.moveBy(0, -blockSpacing * spaces, SINGLE_BLOCK_DROP_TIME * spaces));
    }

    private boolean analyzeAndAnimateBoard(final boolean userInvoked) {

        Array<SimpleBlockGroup> analysis = analyzeBoard2(this);
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
            if (userInvoked) {
                assert largestGroup != null;
                musicHandler.playRandomMusic(largestGroup.getType(),
                        ScoringSystem.getCrushType(largestGroup));
            } else if (!musicHandler.isMusicPlaying()) musicHandler.playPopSound();


            return true;
        } else { // there was no match
            blockGroups = null;
            return false;
        }
    }


    private void fadeBlockOut(Block b) {
        b.addAction(Actions.scaleTo(0f, 0f, SINGLE_BLOCK_DROP_TIME));
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
                Block fillBlock = getNewRandomBlock(row, col);
                fillBlock.setVisible(false);
                insertBlock(fillBlock);
            }
            animateFillDown(col, crushedBlocksInCol);
        }
    }

    private static boolean analysisCurrentBlock(
            Block currentBlock, Array<BlockGroup> matches, Array<Block> currentString, int minLen, int numCols, boolean last) {
        boolean added = false;
        if (currentString.size > 0) {
            if (currentBlock.getBlockType() == currentString.get(0).getBlockType()) {
                currentString.add(currentBlock);
                if (last && currentString.size >= minLen) {
                    matches.add(new BlockGroup(currentString, numCols));
                    added = true;
                    currentString.clear();
                }
            } else {
                if (currentString.size >= minLen) {
                    matches.add(new BlockGroup(currentString, numCols));
                    added = true;
                }
                currentString.clear();
                currentString.add(currentBlock);
            }
        } else {
            currentString.add(currentBlock);
        }
        return added;
    }

    private static void doAnalysisMerge(Array<BlockGroup> colGroups, Array<BlockGroup> groups) {
        BlockGroup lastColGroup = colGroups.get(colGroups.size - 1);
        BlockGroup merged = null;
        boolean didMerge = false;
        for (int i = 0; i < groups.size; i++) {
            BlockGroup m;
            if (didMerge) {
                if (merged.getGroupBlockType() != groups.get(i).getGroupBlockType()) continue;
                m = BlockGroup.getMergedGroup(merged, groups.get(i));
            } else {
                if (lastColGroup.getGroupBlockType() != groups.get(i).getGroupBlockType()) continue;
                m = BlockGroup.getMergedGroup(lastColGroup, groups.get(i));
            }
            if (m != null) {
                groups.removeIndex(i);
                i--;
                didMerge = true;
                merged = m;
            }
        }
        if (didMerge) {
            groups.add(merged);
            colGroups.removeValue(lastColGroup, true);
        }
    }

    private static Array<SimpleBlockGroup> analyzeBoard2(Board board) {
        int minCrushLen = 3;
        Array<SimpleBlockGroup> groups = new Array<SimpleBlockGroup>();
        Block[][] blocks = board.blocks;

        for (Block[] row : blocks) {
            for (Block b : row) { // clear
                b.visited = false;
                b.blockGroup = null;
            }
        }

        Block b;
        SimpleBlockGroup curGroup;
        for (int r = 0; r < blocks.length; ++r) {
            curGroup = new SimpleBlockGroup();
            for (int c = 0; c < blocks[r].length; ++c) {
                b = blocks[r][c];
                boolean typeMatch = curGroup.typeMatch(b);
                if (typeMatch) curGroup.addBlock(b);

                if (!typeMatch || c == blocks[r].length - 1) {
                    // terminate block group

                    // 1. determine if it is a crush
                    if (curGroup.size() >= minCrushLen) {
                        // 2. determine whether to look for an l-shape
                        if (curGroup.size() < 5) { // we don't look for l-shape for horizontal sizes >= 5
                            // 3. look for l-shape
                            Block b1, b2;
                            for (int curCol = curGroup.minCol; curCol <= curGroup.maxCol; ++curCol) {
                                if ((r > 1
                                        && curGroup.typeMatch((b1 = blocks[r - 1][curCol]))
                                        && curGroup.typeMatch((b2 = blocks[r - 2][curCol])))
                                        ||
                                        (r < blocks.length - 2
                                                && curGroup.typeMatch((b1 = blocks[r + 1][curCol]))
                                                && curGroup.typeMatch((b2 = blocks[r + 2][curCol])))
                                        ||
                                        (r > 0 && r < blocks.length - 1
                                                && curGroup.typeMatch((b1 = blocks[r + 1][curCol]))
                                                && curGroup.typeMatch((b2 = blocks[r - 1][curCol])))) {
                                    if (curGroup.size() == 4) {
                                        if (curCol == curGroup.minCol || curCol == curGroup.minCol + 1) {
                                            curGroup.remove(blocks[r][curGroup.maxCol]);
                                        } else {
                                            curGroup.remove(blocks[r][curGroup.minCol]);
                                        }
                                    }
                                    curGroup.addBlock(b1);
                                    curGroup.addBlock(b2);
                                    break;
                                }
                            }
                        }
                        for (Block block : curGroup) block.blockGroup = curGroup;
                        groups.add(curGroup);
                    }
                    curGroup = new SimpleBlockGroup();
                    if (!typeMatch) curGroup.addBlock(b);
                }
            }
        }
        for (int c = 0; c < blocks[0].length; ++c) {
            curGroup = new SimpleBlockGroup();
            for (int r = 0; r < blocks.length; ++r) {
                b = blocks[r][c];
                boolean typeMatch = curGroup.typeMatch(b);
                if (typeMatch) {
                    curGroup.addBlock(b);
                }

                if (!typeMatch || r == blocks.length - 1) {
                    // terminate current group

                    // 1. determine if it is a crush
                    if (curGroup.size() >= minCrushLen) {
                        // 2. determine if there are any horizontal groups attached
                        Set<SimpleBlockGroup> attachedGroups = new HashSet<SimpleBlockGroup>();
                        for (Block b1 : curGroup) {
                            if (b1.blockGroup != null) {
                                attachedGroups.add(b1.blockGroup);
                            }
                        }
                        boolean shouldAddVerticalGroup = true;
                        if (attachedGroups.size() > 0) {
                            int fiveHorizontalCount = 0, lShapeCount = 0;
                            for (SimpleBlockGroup g : attachedGroups) {
                                if (g.isLShape()) ++lShapeCount;
                                else ++fiveHorizontalCount;
                            }
                            if (fiveHorizontalCount > 0) {
                                // keep all horizontal fives and remove all l-shapes
                                for (SimpleBlockGroup g : attachedGroups) {
                                    if (g.isLShape()) {
                                        groups.removeValue(g, true);
                                        for (Block block : g) block.blockGroup = null;
                                    }
                                }
                                shouldAddVerticalGroup = false;
                            } else if (curGroup.size() < 5) {
                                // keep first l-shape we see and remove everything else
                                SimpleBlockGroup toKeep = null;
                                for (SimpleBlockGroup g : attachedGroups) {
                                    if (toKeep == null) toKeep = g;
                                    else {
                                        groups.removeValue(g, true);
                                        for (Block block : g) block.blockGroup = null;
                                    }
                                }
                                shouldAddVerticalGroup = false;
                            } else {
                                shouldAddVerticalGroup = true;
                                // remove all attached groups
                                for (SimpleBlockGroup g : attachedGroups) {
                                    groups.removeValue(g, true);
                                    for (Block block : g) block.blockGroup = null;
                                }
                            }
                        }
                        if (shouldAddVerticalGroup) {
                            groups.add(curGroup);
                            for (Block block : curGroup) block.blockGroup = curGroup;
                        }
                    }
                    curGroup = new SimpleBlockGroup();
                    if (!typeMatch) curGroup.addBlock(b);
                }
            }
        }
        return groups;
    }

    private static Array<BlockGroup> analyzeBoard(Board board) {
        int minLen = 3;
        Array<BlockGroup> rowGroups = new Array<BlockGroup>();
        Block[][] blocks = board.getBlocks();
        int numCols = blocks[0].length;

        for (Block[] row : blocks) {
            Array<Block> currentString = new Array<Block>();
            for (int c = 0; c < numCols; c++) {
                Block currentBlock = row[c];
                analysisCurrentBlock(currentBlock, rowGroups, currentString, minLen, numCols, c == numCols - 1);
            }
        }

        Array<BlockGroup> colGroups = new Array<BlockGroup>();
        Array<BlockGroup> groups = new Array<BlockGroup>(rowGroups);

        for (int c = 0; c < numCols; c++) {
            Array<Block> currentString = new Array<Block>();
            for (int r = 0; r < blocks.length; r++) {
                Block currentBlock = blocks[r][c];
                if (analysisCurrentBlock(currentBlock, colGroups, currentString, minLen, numCols, r == blocks.length - 1)) {
                    doAnalysisMerge(colGroups, groups);
                }
            }
        }
        groups.addAll(colGroups);

        return groups;
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

    private float time = 1;

    private void handleInput(Vector2 mouse) {
        if (doChildrenHaveActions()) {
            return;
        }
        if (shouldAnalyze) {
            gotMatches = analyzeAndAnimateBoard(userFlippedBlocks); // crush
            shouldAnalyze = false;
            if (gotMatches) { // user crushed blocks
                blockGroupsProcessStack.push(blockGroups); // enqueue block groups only directly after crush
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

    public Stack<Array<SimpleBlockGroup>> getCrushStack() {
        return blockGroupsProcessStack;
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

    public List<BlockType> getBlockTypes() {
        return this.blockTypes;
    }

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
}
