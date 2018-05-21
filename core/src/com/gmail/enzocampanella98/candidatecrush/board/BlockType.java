package com.gmail.enzocampanella98.candidatecrush.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Created by Lorenzo Campanella on 6/2/2016.
 */
public enum BlockType {

    TRUMP("trump", "Donald Trump"),
    CLINTON("clinton", "Hillary Clinton"),
    SANDERS("sanders", "Bernie Sanders"),
    CRUZ("cruz", "Ted Cruz"),
    //MEZA("meza", "Daniel Meza"),
    BLANK();

    private boolean isEmpty;
    private static final String SPRITE_ROOT = "data/img/block_sprites/";
    private String friendlyName, internalPath, lname;

    BlockType(String lname, String friendlyName) {
        this.lname = lname;
        this.internalPath = SPRITE_ROOT + lname + "_sprite.png";
        this.friendlyName = friendlyName;
        this.isEmpty = false;
    }

    BlockType() {
        isEmpty = true;
    }

    @Override
    public String toString() {
        return getFriendlyName();
    }

    public boolean isBlankMaterial() {
        return isEmpty;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getLname() {
        return lname;
    }

    public String getInternalPath() {
        return internalPath;
    }

    public FileHandle getFileHandle() {
        if (isBlankMaterial()) return null;
        return Gdx.files.internal(getInternalPath());
    }

    public static void dispose() {
        disposeOfSounds();
    }

    private static void disposeOfSounds() {
        for (BlockType t : values()) {
//            t.getBlockSounds().dispose();
        }
    }
}
