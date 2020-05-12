package com.gmail.enzocampanella98.candidatecrush;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.gmail.enzocampanella98.candidatecrush.level.LevelFactory;
import com.gmail.enzocampanella98.candidatecrush.screens.MenuScreen;
import com.gmail.enzocampanella98.candidatecrush.sound.CCSoundBank;
import com.gmail.enzocampanella98.candidatecrush.tools.Hasher;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CandidateCrush extends Game {

    public static int V_WIDTH = 1080, V_HEIGHT = 1920;
    public static final String TITLE = "Candidate Crush";

    public static final boolean IS_TESTING_LEVELS = false;
    public static final Integer START_LEVEL_OVERRIDE = null;
    public static final Integer START_LEVEL_HARD_MODE_OVERRIDE = null;
    public static final Boolean IS_OAK_BAES_UNLOCKED_OVERRIDE = null;
    public static final Integer START_LEVEL_OAK_BAES_OVERRIDE = null;
    public static final Integer START_LEVEL_OAK_BAES_HARD_MODE_OVERRIDE = null;

    public CCGameData gameData;
    private Json json = new Json();
    private FileHandle gameDataFileHandle;

    public SpriteBatch batch;

    public static final float iphone11Width = 1242.0f;

    @Override
    public void create() {
        V_HEIGHT = Math.round((int)(V_WIDTH * (((double)Gdx.graphics.getHeight()) / Gdx.graphics.getWidth())));


        CCSoundBank.getInstance();
        initializeGameData();
        batch = new SpriteBatch();
        setScreen(new MenuScreen(this));
        LevelFactory.printLevels();
    }

    public static float scaled(float n) {
        if (Gdx.graphics.getWidth() > iphone11Width) {
            return n * (iphone11Width / Gdx.graphics.getWidth());
        }
        return n;
    }

    public void printScreenInfo() {
        System.out.printf("Width: %d\nHeight: %d\nDensity: %f\n",
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(),
                Gdx.graphics.getDensity());
    }

    public void disposeCurrentScreen() {
        getScreen().dispose();
    }

    public void initializeGameData() {
        gameDataFileHandle = Gdx.files.local("bin/GameData.json");
        if (!gameDataFileHandle.exists()) {
            gameData = new CCGameData();

            gameData.setMaxBeatenLevel(0);
            gameData.setMaxBeatenLevelHardMode(0);
            gameData.setOakBaesUnlocked(false);
            gameData.setMaxBeatenOakBaesLevel(0);
            gameData.setMaxBeatenOakBaesLevelHardMode(0);

            saveData();
        } else {
            loadData();
        }

        // overrides
        if (START_LEVEL_OVERRIDE != null) {
            gameData.setMaxBeatenLevel(START_LEVEL_OVERRIDE - 1);
        }
        if (START_LEVEL_HARD_MODE_OVERRIDE != null) {
            gameData.setMaxBeatenLevelHardMode(START_LEVEL_HARD_MODE_OVERRIDE - 1);
        }
        if (IS_OAK_BAES_UNLOCKED_OVERRIDE != null) {
            gameData.setOakBaesUnlocked(IS_OAK_BAES_UNLOCKED_OVERRIDE);
        }
        if (START_LEVEL_OAK_BAES_OVERRIDE != null) {
            gameData.setMaxBeatenOakBaesLevel(START_LEVEL_OAK_BAES_OVERRIDE - 1);
        }
        if (START_LEVEL_OAK_BAES_HARD_MODE_OVERRIDE != null) {
            gameData.setMaxBeatenOakBaesLevelHardMode(START_LEVEL_OAK_BAES_HARD_MODE_OVERRIDE - 1);
        }

        saveData();
    }

    public void saveData() {
        if (gameData != null) {
            gameDataFileHandle.writeString(Base64Coder.encodeString(json.prettyPrint(gameData)),
                    false);
        }
    }

    public void loadData() {
        gameData = json.fromJson(CCGameData.class,
                Base64Coder.decodeString(gameDataFileHandle.readString()));
    }

    public boolean isHardModeUnlocked() {
        return gameData.getMaxBeatenLevel() >= LevelFactory.NUM_LEVELS;
    }

    public boolean isOakBaesUnlocked() {
        return gameData.isOakBaesUnlocked();
    }

    public boolean tryUnlockOakBaes(String key) {
        try {
            if (Hasher.hash(key).equals("d966c9c066d1631d75efd8c97d7e73ca553d88651bfcc30511bd3e71705dd9b6")) {
                gameData.setOakBaesUnlocked(true);
                saveData();
                return true;
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return false;
    }

    public void beatLevel(int level, boolean inHardMode) {
        if (hasBeatenLevel(level, inHardMode)) return;

        if (inHardMode) {
            gameData.setMaxBeatenLevelHardMode(level);
        } else {
            gameData.setMaxBeatenLevel(level);
        }
        saveData();
    }

    public boolean hasBeatenLevel(int level, boolean inHardMode) {
        return level <=
                (inHardMode ? gameData.getMaxBeatenLevelHardMode() : gameData.getMaxBeatenLevel());
    }

    public Collection<String> getNewlyBeatenLevelUnlocks(int levelBeaten, boolean inHardMode) {
        List<String> lines = new ArrayList<>();
        if (levelBeaten == LevelFactory.NUM_LEVELS) {
            if (inHardMode) {
                lines.addAll(Arrays.asList(
                        "Congratulations!",
                        "You're a career politician!"
                ));
            } else {
                lines.addAll(Arrays.asList(
                        "Congratulations!",
                        "You've beaten the game!",
                        "Hard mode unlocked"
                ));
            }
        } else {
            lines.add("Level " + (levelBeaten+1) + " unlocked!");
            if (LevelFactory.increaseTierLevels.contains(levelBeaten+1)) {
                lines.add("New sound-bytes unlocked!");
            }
        }
        return lines;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
